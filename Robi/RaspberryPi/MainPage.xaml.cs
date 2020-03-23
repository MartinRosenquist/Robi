using System;
using System.Collections.ObjectModel;
using Windows.Devices.Bluetooth;
using Windows.Devices.Bluetooth.Rfcomm;
using Windows.Devices.Enumeration;
using Windows.Devices.Spi;
using Windows.Networking.Sockets;
using Windows.UI.Xaml.Controls;

// The Blank Page item template is documented at https://go.microsoft.com/fwlink/?LinkId=402352&clcid=0x409

namespace RaspberryPi
{
    /// <summary>
    /// An empty page that can be used on its own or navigated to within a Frame.
    /// </summary>
    public sealed partial class MainPage : Page
    {
        private SpiDevice ledMatrix;
        private string[] requestedProperties = { "System.Devices.Aep.DeviceAddress", "System.Devices.Aep.IsConnected" };
        private DeviceWatcher deviceWatcher;

        public MainPage()
        {
            this.InitializeComponent();
        }

        /// <summary>
        /// 
        /// </summary>
        private async void InitializeMatrix()
        {
            // Get a device selector query that will select buses with SPI0
            // property set on them (we expect 1 SPI0 bus at the end)
            string spi0Args = SpiDevice.GetDeviceSelector("SPI0");

            // Find all buses using AQS query formed above 
            DeviceInformationCollection deviceInfo = await DeviceInformation.FindAllAsync(spi0Args);

            // Construct settings beforehand which can't be changed 
            // once a SPI device is created
            SpiConnectionSettings settings = new SpiConnectionSettings(0);
            settings.ClockFrequency = 10000000;
            settings.Mode = SpiMode.Mode0;
            settings.DataBitLength = 8;

            // Ask the SPI bus to open a device with the connection settings provided.
            ledMatrix = await SpiDevice.FromIdAsync(deviceInfo[0].Id, settings);
        }

        /// <summary>
        /// 
        /// </summary>
        private void BleWatch()
        {
            deviceWatcher = DeviceInformation.CreateWatcher(BluetoothLEDevice.GetDeviceSelectorFromPairingState(false), requestedProperties, DeviceInformationKind.AssociationEndpoint);

            // Register event handlers before starting the watcher.
            // Added, Updated and Removed are required to get all nearby devices.
            deviceWatcher.Added += DeviceWatcher_Added;
            deviceWatcher.Updated += DeviceWatcher_Updated;
            deviceWatcher.Removed += DeviceWatcher_Removed;

            // Start the watcher
            deviceWatcher.Start();
        }

        private void DeviceWatcher_Removed(DeviceWatcher sender, DeviceInformationUpdate args)
        {
            throw new NotImplementedException();
        }

        private void DeviceWatcher_Updated(DeviceWatcher sender, DeviceInformationUpdate args)
        {
            throw new NotImplementedException();
        }

        private void DeviceWatcher_Added(DeviceWatcher sender, DeviceInformation args)
        {
            throw new NotImplementedException();
        }
    }
}
