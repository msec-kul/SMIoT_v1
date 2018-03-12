package be.kuleuven.msec.iot.myroom.environment;

import android.util.Log;

import java.util.concurrent.CountDownLatch;

import be.kuleuven.msec.iot.iotframework.callbackinterfaces.OnRequestCompleted;
import be.kuleuven.msec.iot.iotframework.generic.componentlayer.Environment;
import be.kuleuven.msec.iot.iotframework.generic.devicelayer.HumiditySensor;
import be.kuleuven.msec.iot.iotframework.generic.devicelayer.Lamp;
import be.kuleuven.msec.iot.iotframework.generic.devicelayer.PressureSensor;
import be.kuleuven.msec.iot.iotframework.generic.devicelayer.TemperatureSensor;
import be.kuleuven.msec.iot.iotframework.generic.devicelayer.VirtualIoTConnector;
import be.kuleuven.msec.iot.iotframework.implementations.lamps.huelamp.HueGateway;
import be.kuleuven.msec.iot.iotframework.implementations.lamps.lightifylamp.LightifyGateway;
import be.kuleuven.msec.iot.iotframework.implementations.sensorkits.versasense.VersaSenseGateway;
import be.kuleuven.msec.iot.iotframework.systemmanagement.constants.Connector_constants;
import be.kuleuven.msec.iot.iotframework.systemmanagement.constants.Device_constants;
import be.kuleuven.msec.iot.iotframework.systemmanagement.jsonmodel.JSMConnector;
import be.kuleuven.msec.iot.iotframework.systemmanagement.jsonmodel.JSMDevice;

/**
 * Created by ilsebohe on 16/01/2018.
 */

public class ApplicationEnvironment extends Environment {
    final private String TAG = "ApplicationEnvironment";
    private static Room room;

    public ApplicationEnvironment() {
        super();
        room = new Room("Room");
    }

    public static Room getRoom() {
        return room;
    }

    @Override
    public void loadEnvironment(final OnRequestCompleted<Boolean> orc) {
        final CountDownLatch latch = new CountDownLatch(configuration.getConnectors().size());
        new Thread(new Runnable() {
            @Override
            public void run() {
                //initializeConnectors
                for (JSMConnector connector : configuration.getConnectors()) {
                    switch (connector.getType()) {
                        case Connector_constants.CONNECTORTYPE_HUE:
                            virtualIoTConnectors.add(new HueGateway(connector.getSystemID(), "http://" + connector.getInitSettings()[0], connector.getInitSettings()[1]));
                            break;
                        case Connector_constants.CONNECTORTYPE_OSRAM:
                            virtualIoTConnectors.add(new LightifyGateway(connector.getSystemID(), connector.getInitSettings()[0], connector.getInitSettings()[1], connector.getInitSettings()[2]));
                            break;
                        case Connector_constants.CONNECTORTYPE_VERSASENSE:
                            virtualIoTConnectors.add(new VersaSenseGateway(connector.getSystemID(), connector.getInitSettings()[0]));
                            break;
                    }
                }
                for (final VirtualIoTConnector c : virtualIoTConnectors) {
                    c.initialize(new OnRequestCompleted() {
                        @Override
                        public void onSuccess(Object response) {
                            c.updateConnectedDeviceList(new OnRequestCompleted<Boolean>() {
                                @Override
                                public void onSuccess(Boolean response) {
                                    latch.countDown();
                                }
                            });
                        }
                    });
                }
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (JSMDevice d : configuration.getComponents().get(0).getDevices()) {
                    System.out.println("type: " + d.getType());
                    switch (d.getType()) {
                        case Device_constants.TYPE_LAMP:
                            room.addLamp((Lamp) getConnectorBySystemID(d.getConnector()).getConnectedDeviceBasedOnSystemID(Device_constants.TYPE_LAMP, d.getUniqueID()));
                            break;
                        case Device_constants.TYPE_TEMPERATURE_SENSOR:
                            room.setTemperatureSensor((TemperatureSensor) getConnectorBySystemID(d.getConnector()).getConnectedDeviceBasedOnSystemID(Device_constants.TYPE_TEMPERATURE_SENSOR, d.getUniqueID()));
                            break;
                        case Device_constants.TYPE_HUMIDITY_SENSOR:
                            room.setHumiditySensor((HumiditySensor) getConnectorBySystemID(d.getConnector()).getConnectedDeviceBasedOnSystemID(Device_constants.TYPE_HUMIDITY_SENSOR, d.getUniqueID()));
                            break;
                        case Device_constants.TYPE_PRESSURE_SENSOR:
                            room.setPressureSensor((PressureSensor) getConnectorBySystemID(d.getConnector()).getConnectedDeviceBasedOnSystemID(Device_constants.TYPE_PRESSURE_SENSOR, d.getUniqueID()));
                            break;
                        default:
                            Log.w(TAG, "Unknown device type: " + d.getType());
                    }
                }
                orc.onSuccess(true);
            }
        }).start();
    }
}
