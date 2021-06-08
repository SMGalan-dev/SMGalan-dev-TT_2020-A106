package com.example.tt_a106_v0.bleglucometer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.welie.blessed.BluetoothBytesParser;
import com.welie.blessed.BluetoothCentralManager;
import com.welie.blessed.BluetoothCentralManagerCallback;
import com.welie.blessed.BluetoothPeripheral;
import com.welie.blessed.BluetoothPeripheralCallback;
import com.welie.blessed.ConnectionPriority;
import com.welie.blessed.GattStatus;
import com.welie.blessed.HciStatus;
import com.welie.blessed.ScanFailure;
import com.welie.blessed.WriteType;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteOrder;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import timber.log.Timber;

import static com.welie.blessed.BluetoothBytesParser.FORMAT_SINT16;
import static com.welie.blessed.BluetoothBytesParser.FORMAT_UINT16;
import static com.welie.blessed.BluetoothBytesParser.FORMAT_UINT8;

public class BluetoothHandler {

    // Intent constants
    public static final String MEASUREMENT_GLUCOSE = "blessed.measurement.glucose";
    public static final String MEASUREMENT_GLUCOSE_EXTRA = "blessed.measurement.glucose.extra";
    public static final String MEASUREMENT_EXTRA_PERIPHERAL = "blessed.measurement.peripheral";

    // UUIDs for the Device Information service (DIS)
    private static final UUID DIS_SERVICE_UUID = UUID.fromString("0000180A-0000-1000-8000-00805f9b34fb");
    private static final UUID MANUFACTURER_NAME_CHARACTERISTIC_UUID = UUID.fromString("00002A29-0000-1000-8000-00805f9b34fb");
    private static final UUID MODEL_NUMBER_CHARACTERISTIC_UUID = UUID.fromString("00002A24-0000-1000-8000-00805f9b34fb");

    // UUIDs for the Current Time service (CTS)
    private static final UUID CTS_SERVICE_UUID = UUID.fromString("00001805-0000-1000-8000-00805f9b34fb");
    private static final UUID CURRENT_TIME_CHARACTERISTIC_UUID = UUID.fromString("00002A2B-0000-1000-8000-00805f9b34fb");

    // UUIDs for the Battery Service (BAS)
    private static final UUID BTS_SERVICE_UUID = UUID.fromString("0000180F-0000-1000-8000-00805f9b34fb");
    private static final UUID BATTERY_LEVEL_CHARACTERISTIC_UUID = UUID.fromString("00002A19-0000-1000-8000-00805f9b34fb");

    public static final UUID GLUCOSE_SERVICE_UUID = UUID.fromString("00001808-0000-1000-8000-00805f9b34fb");
    public static final UUID GLUCOSE_MEASUREMENT_CHARACTERISTIC_UUID = UUID.fromString("00002A18-0000-1000-8000-00805f9b34fb");
    public static final UUID GLUCOSE_RECORD_ACCESS_POINT_CHARACTERISTIC_UUID = UUID.fromString("00002A52-0000-1000-8000-00805f9b34fb");
    public static final UUID GLUCOSE_MEASUREMENT_CONTEXT_CHARACTERISTIC_UUID = UUID.fromString("00002A34-0000-1000-8000-00805f9b34fb");

    // Contour Glucose Service
    public static final UUID CONTOUR_SERVICE_UUID = UUID.fromString("00000000-0002-11E2-9E96-0800200C9A66");
    private static final UUID CONTOUR_CLOCK = UUID.fromString("00001026-0002-11E2-9E96-0800200C9A66");

    // Local variables
    public BluetoothCentralManager central;
    private static com.example.tt_a106_v0.bleglucometer.BluetoothHandler instance = null;
    private final Context context;
    private final Handler handler = new Handler();

    // Callback for peripherals
    private final BluetoothPeripheralCallback peripheralCallback = new BluetoothPeripheralCallback() {
        @Override
        public void onServicesDiscovered(@NotNull BluetoothPeripheral peripheral) {
            // Request a higher MTU, iOS always asks for 185
            peripheral.requestMtu(185);

            // Request a new connection priority
            peripheral.requestConnectionPriority(ConnectionPriority.HIGH);

            // Read manufacturer and model number from the Device Information Service
            peripheral.readCharacteristic(DIS_SERVICE_UUID, MANUFACTURER_NAME_CHARACTERISTIC_UUID);
            peripheral.readCharacteristic(DIS_SERVICE_UUID, MODEL_NUMBER_CHARACTERISTIC_UUID);

            // Turn on notifications for Current Time Service and write it if possible
            BluetoothGattCharacteristic currentTimeCharacteristic = peripheral.getCharacteristic(CTS_SERVICE_UUID, CURRENT_TIME_CHARACTERISTIC_UUID);
            if (currentTimeCharacteristic != null) {
                peripheral.setNotify(currentTimeCharacteristic, true);
/*
                // If it has the write property we write the current time
                if ((currentTimeCharacteristic.getProperties() & PROPERTY_WRITE) > 0) {
                    // Write the current time unless it is an Omron device
                    final String name = peripheral.getName();
                    if (!(name.contains("BLEsmart_"))) {
                        BluetoothBytesParser parser = new BluetoothBytesParser();
                        parser.setCurrentTime(Calendar.getInstance());
                        peripheral.writeCharacteristic(currentTimeCharacteristic, parser.getValue(), WriteType.WITH_RESPONSE);
                    }
                }
 */
            }

            // Try to turn on notifications for other characteristics
            peripheral.readCharacteristic(BTS_SERVICE_UUID, BATTERY_LEVEL_CHARACTERISTIC_UUID);
            peripheral.setNotify(GLUCOSE_SERVICE_UUID, GLUCOSE_MEASUREMENT_CHARACTERISTIC_UUID, true);
            peripheral.setNotify(GLUCOSE_SERVICE_UUID, GLUCOSE_MEASUREMENT_CONTEXT_CHARACTERISTIC_UUID, true);
            peripheral.setNotify(GLUCOSE_SERVICE_UUID, GLUCOSE_RECORD_ACCESS_POINT_CHARACTERISTIC_UUID, true);
            peripheral.setNotify(CONTOUR_SERVICE_UUID, CONTOUR_CLOCK, true);
        }

        @Override
        public void onNotificationStateUpdate(@NotNull BluetoothPeripheral peripheral, @NotNull BluetoothGattCharacteristic characteristic, @NotNull GattStatus status) {
            if (status == GattStatus.SUCCESS) {
                final boolean isNotifying = peripheral.isNotifying(characteristic);
                Timber.i("SUCCESS: Notify set to '%s' for %s", isNotifying, characteristic.getUuid());
                if (characteristic.getUuid().equals(CONTOUR_CLOCK)) {
                    writeContourClock(peripheral);
                } else if (characteristic.getUuid().equals(GLUCOSE_RECORD_ACCESS_POINT_CHARACTERISTIC_UUID)) {
                    writeGetAllGlucoseMeasurements(peripheral);
                }
            } else {
                Timber.e("ERROR: Changing notification state failed for %s (%s)", characteristic.getUuid(), status);
            }
        }
        @Override
        public void onCharacteristicUpdate(@NotNull BluetoothPeripheral peripheral, @NotNull byte[] value, @NotNull BluetoothGattCharacteristic characteristic, @NotNull GattStatus status) {
            if (status != GattStatus.SUCCESS) return;

            UUID characteristicUUID = characteristic.getUuid();
            BluetoothBytesParser parser = new BluetoothBytesParser(value);

            if (characteristicUUID.equals((GLUCOSE_MEASUREMENT_CHARACTERISTIC_UUID))) {
                GlucoseMeasurement measurement = new GlucoseMeasurement(value);
                Intent intent = new Intent(MEASUREMENT_GLUCOSE);
                intent.putExtra(MEASUREMENT_GLUCOSE_EXTRA, measurement);
                sendMeasurement(intent, peripheral);
                Timber.d("%s", measurement);
            } else if (characteristicUUID.equals(CURRENT_TIME_CHARACTERISTIC_UUID)) {
                Date currentTime = parser.getDateTime();
                Timber.i("Received device time: %s", currentTime);
            } else if (characteristicUUID.equals(BATTERY_LEVEL_CHARACTERISTIC_UUID)) {
                int batteryLevel = parser.getIntValue(FORMAT_UINT8);
                Timber.i("Received battery level %d%%", batteryLevel);
            } else if (characteristicUUID.equals(MANUFACTURER_NAME_CHARACTERISTIC_UUID)) {
                String manufacturer = parser.getStringValue(0);
                Timber.i("Received manufacturer: %s", manufacturer);
            } else if (characteristicUUID.equals(MODEL_NUMBER_CHARACTERISTIC_UUID)) {
                String modelNumber = parser.getStringValue(0);
                Timber.i("Received modelnumber: %s", modelNumber);
            }
        }

        @Override
        public void onMtuChanged(@NotNull BluetoothPeripheral peripheral, int mtu, @NotNull GattStatus status) {
            Timber.i("new MTU set: %d", mtu);
        }

        private void sendMeasurement(@NotNull Intent intent, @NotNull BluetoothPeripheral peripheral ) {
            intent.putExtra(MEASUREMENT_EXTRA_PERIPHERAL, peripheral.getAddress());
            context.sendBroadcast(intent);
        }

        private void writeContourClock(@NotNull BluetoothPeripheral peripheral) {
            Calendar calendar = Calendar.getInstance();
            int offsetInMinutes = calendar.getTimeZone().getRawOffset() / 60000;
            calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
            BluetoothBytesParser parser = new BluetoothBytesParser(ByteOrder.LITTLE_ENDIAN);
            parser.setIntValue(1, FORMAT_UINT8);
            parser.setIntValue(calendar.get(Calendar.YEAR), FORMAT_UINT16);
            parser.setIntValue(calendar.get(Calendar.MONTH) + 1, FORMAT_UINT8);
            parser.setIntValue(calendar.get(Calendar.DAY_OF_MONTH), FORMAT_UINT8);
            parser.setIntValue(calendar.get(Calendar.HOUR_OF_DAY), FORMAT_UINT8);
            parser.setIntValue(calendar.get(Calendar.MINUTE), FORMAT_UINT8);
            parser.setIntValue(calendar.get(Calendar.SECOND), FORMAT_UINT8);
            parser.setIntValue(offsetInMinutes, FORMAT_SINT16);
            peripheral.writeCharacteristic(CONTOUR_SERVICE_UUID, CONTOUR_CLOCK, parser.getValue(), WriteType.WITH_RESPONSE);
        }

        private void writeGetAllGlucoseMeasurements(@NotNull BluetoothPeripheral peripheral) {
            byte OP_CODE_REPORT_STORED_RECORDS = 1;
            byte OPERATOR_ALL_RECORDS = 1;
            final byte[] command = new byte[] {OP_CODE_REPORT_STORED_RECORDS, OPERATOR_ALL_RECORDS};
            peripheral.writeCharacteristic(GLUCOSE_SERVICE_UUID, GLUCOSE_RECORD_ACCESS_POINT_CHARACTERISTIC_UUID, command, WriteType.WITH_RESPONSE);
        }
    };

    // Callback for central
    private final BluetoothCentralManagerCallback bluetoothCentralManagerCallback = new BluetoothCentralManagerCallback() {

        @Override
        public void onConnectedPeripheral(@NotNull BluetoothPeripheral peripheral) {
            Timber.i("connected to '%s'", peripheral.getName());
        }

        @Override
        public void onConnectionFailed(@NotNull BluetoothPeripheral peripheral, final @NotNull HciStatus status) {
            Timber.e("connection '%s' failed with status %s", peripheral.getName(), status);
        }

        @Override
        public void onDisconnectedPeripheral(@NotNull final BluetoothPeripheral peripheral, final @NotNull HciStatus status) {
            Timber.i("disconnected '%s' with status %s", peripheral.getName(), status);

            // Reconnect to this device when it becomes available again
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    central.autoConnectPeripheral(peripheral, peripheralCallback);
                }
            }, 5000);

        }

        @Override
        public void onDiscoveredPeripheral(@NotNull BluetoothPeripheral peripheral, @NotNull ScanResult scanResult) {
            Timber.i("Found peripheral '%s'", peripheral.getName());
            central.stopScan();
            central.connectPeripheral(peripheral, peripheralCallback);
        }

        @Override
        public void onBluetoothAdapterStateChanged(int state) {
            Timber.i("bluetooth adapter changed state to %d", state);
            if (state == BluetoothAdapter.STATE_ON) {
                // Bluetooth is on now, start scanning again
                // Scan for peripherals with a certain service UUIDs
                central.startPairingPopupHack();
            }
        }

        @Override
        public void onScanFailed(@NotNull ScanFailure scanFailure) {
            Timber.i("scanning failed with error %s", scanFailure);
        }
    };

    public static synchronized com.example.tt_a106_v0.bleglucometer.BluetoothHandler getInstance(Context context) {
        if (instance == null) {
            instance = new com.example.tt_a106_v0.bleglucometer.BluetoothHandler(context.getApplicationContext());
        }
        return instance;
    }

    private BluetoothHandler(Context context) {
        this.context = context;

        // Plant a tree
        Timber.plant(new Timber.DebugTree());

        // Create BluetoothCentral
        central = new BluetoothCentralManager(context, bluetoothCentralManagerCallback, new Handler());

        // Scan for peripherals with a certain service UUIDs
        central.startPairingPopupHack();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                central.scanForPeripheralsWithServices(new UUID[]{GLUCOSE_SERVICE_UUID});
            }
        },1000);
    }
}
