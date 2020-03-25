using System;
using System.Collections.ObjectModel;
using System.Diagnostics;
using Windows.Devices.Bluetooth.Advertisement;
using Windows.Devices.Enumeration;
using Windows.Storage.Streams;
using Windows.UI.Core;

namespace RaspberryPi
{
    class BleManager
    {
        BluetoothLEAdvertisementWatcher watcher = new BluetoothLEAdvertisementWatcher();

        public void StartWatchingAdvertisement()
        {
            BluetoothLEManufacturerData manufacturerData = new BluetoothLEManufacturerData();
            manufacturerData.CompanyId = 0xFFFF;

            watcher.AdvertisementFilter.Advertisement.ManufacturerData.Add(manufacturerData);

            watcher.Received += Watcher_Received;
            watcher.Start();
        }

        public void StopWatchingAdvertisement()
        {
            watcher.Received -= Watcher_Received;
            watcher.Stop();
        }

        private void Watcher_Received(BluetoothLEAdvertisementWatcher sender, BluetoothLEAdvertisementReceivedEventArgs args)
        {
            if ((args.Advertisement.ManufacturerData.Count >= 1))
            {
                using (var dataReader = DataReader.FromBuffer(args.Advertisement.ManufacturerData[0].Data))
                {
                    dataReader.UnicodeEncoding = UnicodeEncoding.Utf8;
                    Debug.WriteLine($"Advertisement received {args.Timestamp} Data: {dataReader.ReadString(args.Advertisement.ManufacturerData[0].Data.Length)}");
                }
            }
        }
    }
}
