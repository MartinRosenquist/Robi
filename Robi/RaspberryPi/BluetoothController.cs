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
        private StreamSocket socket;
        private DataWriter writer;
        private RfcommServiceProvider serviceProvider;
        private StreamSocketListener socketListener;

        /// <summary>
        /// Initializes the server using RfcommServiceProvider to advertise the Chat Service UUID and start listening
        /// for incoming connections.
        /// </summary>
        public async void InitializeRfcommServer()
        {
            try
            {
                serviceProvider = await RfcommServiceProvider.CreateAsync(RfcommServiceId.FromUuid(Constants.RfcommChatServiceUuid));
            }
            // Catch exception HRESULT_FROM_WIN32(ERROR_DEVICE_NOT_AVAILABLE)
            catch (Exception ex) when ((uint)ex.HResult == 0x800710DF)
            {
                Debug.WriteLine($"Make sure your Bluetooth is on: {ex.Message}");
                return;
            }

            // Create a listener for this service and start listening 
            socketListener = new StreamSocketListener();
            socketListener.ConnectionReceived += SocketListener_ConnectionReceived;
            string rfcomm = serviceProvider.ServiceId.AsString();

            await socketListener.BindServiceNameAsync(rfcomm, SocketProtectionLevel.BluetoothEncryptionAllowNullAuthentication);

            // Set the SDP attributes and start Bluetooth advertising
            InitializeServiceSdpAttributes(serviceProvider);

            try
            {
                serviceProvider.StartAdvertising(socketListener, true);
            }
            catch (Exception ex)
            {
                // If you aren't able to get a reference to an RfcommServiceProvider, tell the user why.
                // Usually throws an exception if user changed their privacy settings to prevent Sync w/ Devices.
                Debug.WriteLine(ex.Message);
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
                if (socket != null)
                {
                    writer.WriteUInt32((uint)message.Length);
                    writer.WriteString(message);

                    await writer.StoreAsync();
                }
                else
                {
                    Debug.WriteLine($"No clients connected");
                }
            }
        }

        private void Disconnect()
        {
            if (serviceProvider != null)
            {
                serviceProvider.StopAdvertising();
                serviceProvider = null;
            }

            if (socketListener != null)
            {
                socketListener.Dispose();
                socketListener = null;
            }

            if (writer != null)
            {
                writer.DetachStream();
                writer = null;
            }

            if (socket != null)
            {
                socket.Dispose();
                socket = null;
            }
        }

        private async void SocketListener_ConnectionReceived(StreamSocketListener sender, StreamSocketListenerConnectionReceivedEventArgs args)
        {
            // Don't need the listener anymore
            socketListener.Dispose();
            socketListener = null;

            try
            {
                socket = args.Socket;
            }
            catch (Exception ex)
            {
                Debug.WriteLine(ex.Message);
                Disconnect();
                return;
            }

            // Note - this is the supported way to get a Bluetooth device from a given socket
            BluetoothDevice remoteDevice = await BluetoothDevice.FromHostNameAsync(socket.Information.RemoteHostName);

            writer = new DataWriter(socket.OutputStream);
            DataReader reader = new DataReader(socket.InputStream);
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
