using System;
using System.Diagnostics;
using Windows.Devices.Enumeration;
using Windows.Devices.Spi;
using Windows.UI.Xaml;
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

        public MainPage()
        {
            this.InitializeComponent();
            BluetoothController bluetooth = new BluetoothController();
            bluetooth.InitializeRfcommServer();
            //InitializeMatrix();
        }

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
    }
}
