using System;
using System.Diagnostics;
using Windows.Devices.Bluetooth;
using Windows.Devices.Bluetooth.Rfcomm;
using Windows.Networking.Sockets;
using Windows.Storage.Streams;

namespace RaspberryPi
{
    public class BluetoothController
    {
        private StreamSocket streamSocket;
        private DataWriter dataWriter;
        private RfcommServiceProvider rfcommServiceProvider;
        private StreamSocketListener streamSocketListener;

        /// <summary>
        /// Initializes the server using RfcommServiceProvider to advertise the Chat Service UUID and start listening
        /// for incoming connections.
        /// </summary>
        public async void InitializeRfcommServer()
        {
            try
            {
                rfcommServiceProvider = await RfcommServiceProvider.CreateAsync(RfcommServiceId.FromUuid(Constants.RfcommChatServiceUuid));
            }
            // Catch exception HRESULT_FROM_WIN32(ERROR_DEVICE_NOT_AVAILABLE)
            catch (Exception ex) when ((uint)ex.HResult == 0x800710DF)
            {
                Debug.WriteLine($"Make sure your Bluetooth is on: {ex.Message}");
                return;
            }

            // Create a listener for this service and start listening 
            streamSocketListener = new StreamSocketListener();
            streamSocketListener.ConnectionReceived += SocketListener_ConnectionReceived;
            string rfcomm = rfcommServiceProvider.ServiceId.AsString();

            try
            {
                await streamSocketListener.BindServiceNameAsync(rfcomm, SocketProtectionLevel.BluetoothEncryptionAllowNullAuthentication);
            }
            catch (Exception ex)
            {
                Debug.WriteLine($"Error: {ex.Message}");
                return;
            }

            // Set the SDP attributes and start Bluetooth advertising
            InitializeServiceSdpAttributes(rfcommServiceProvider);

            try
            {
                rfcommServiceProvider.StartAdvertising(streamSocketListener, true);
            }
            catch (Exception ex)
            {
                // If you aren't able to get a reference to an RfcommServiceProvider, tell the user why.
                // Usually throws an exception if user changed their privacy settings to prevent Sync w/ Devices.
                Debug.WriteLine($"Error: {ex.Message}");
                return;
            }

            Debug.WriteLine($"Listening for incoming connections");
        }

        /// <summary>
        /// Creates the SDP record that will be revealed to the Client device when pairing occurs
        /// </summary>
        /// <param name="serviceProvider">The <see cref="RfcommServiceProvider"/> that is being used to initialize the server</param>
        private void InitializeServiceSdpAttributes(RfcommServiceProvider serviceProvider)
        {
            DataWriter sdpWriter = new DataWriter();

            // Write the Service Name Attribute
            sdpWriter.WriteByte(Constants.SdpServiceNameAttributeType);

            // The length of the UTF-8 encoded Service Name SDP Attribute
            sdpWriter.WriteByte((byte)Constants.SdpServiceName.Length);

            // The UTF-8 encoded Service Name value
            sdpWriter.UnicodeEncoding = UnicodeEncoding.Utf8;
            sdpWriter.WriteString(Constants.SdpServiceName);

            // Set the SDP Attribute on the RFCOMM Service Provider
            serviceProvider.SdpRawAttributes.Add(Constants.SdpServiceNameAttributeId, sdpWriter.DetachBuffer());
        }

        public async void SendMessage(string message)
        {
            // There is no need to send a zero length message
            if (message.Length != 0)
            {
                // Make sure that the connection is still up and there is a message to send
                if (streamSocket != null)
                {
                    dataWriter.WriteUInt32((uint)message.Length);
                    dataWriter.WriteString(message);

                    await dataWriter.StoreAsync();
                }
                else
                {
                    Debug.WriteLine($"No clients connected");
                }
            }
        }

        private void Disconnect()
        {
            if (rfcommServiceProvider != null)
            {
                rfcommServiceProvider.StopAdvertising();
                rfcommServiceProvider = null;
            }

            if (streamSocketListener != null)
            {
                streamSocketListener.Dispose();
                streamSocketListener = null;
            }

            if (dataWriter != null)
            {
                dataWriter.DetachStream();
                dataWriter = null;
            }

            if (streamSocket != null)
            {
                streamSocket.Dispose();
                streamSocket = null;
            }
        }

        private async void SocketListener_ConnectionReceived(StreamSocketListener sender, StreamSocketListenerConnectionReceivedEventArgs args)
        {
            // Don't need the listener anymore
            streamSocketListener.Dispose();
            streamSocketListener = null;

            try
            {
                streamSocket = args.Socket;
            }
            catch (Exception ex)
            {
                Debug.WriteLine(ex.Message);
                Disconnect();
                return;
            }

            // Note - this is the supported way to get a Bluetooth device from a given socket
            BluetoothDevice remoteDevice = await BluetoothDevice.FromHostNameAsync(streamSocket.Information.RemoteHostName);

            dataWriter = new DataWriter(streamSocket.OutputStream);
            DataReader reader = new DataReader(streamSocket.InputStream);
            bool remoteDisconnection = false;

            Debug.WriteLine($"Connected to client: {remoteDevice.Name}");

            // Infinite read buffer loop
            while (true)
            {
                try
                {
                    // Based on the protocol we've defined, the first uint is the size of the message
                    uint readLength = await reader.LoadAsync(sizeof(uint));

                    // Check if the size of the data is expected (otherwise the remote has already terminated the connection)
                    if (readLength < sizeof(uint))
                    {
                        remoteDisconnection = true;
                        break;
                    }

                    uint currentLength = reader.ReadUInt32();

                    // Load the rest of the message since you already know the length of the data expected
                    readLength = await reader.LoadAsync(currentLength);

                    // Check if the size of the data is expected (otherwise the remote has already terminated the connection)
                    if (readLength < currentLength)
                    {
                        remoteDisconnection = true;
                        break;
                    }

                    string message = reader.ReadString(currentLength);

                    Debug.WriteLine($"Received: {message}");
                }
                // Catch exception HRESULT_FROM_WIN32(ERROR_OPERATION_ABORTED). 
                catch (Exception ex) when ((uint)ex.HResult == 0x800703E3)
                {
                    Debug.WriteLine("Client Disconnected Successfully");
                    break;
                }
            }

            reader.DetachStream();
            if (remoteDisconnection)
            {
                Disconnect();
                Debug.WriteLine("Client disconnected");
            }
        }
    }
}
