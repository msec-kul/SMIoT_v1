package be.kuleuven.msec.iot.iotframework.generic.devicelayer;

import be.kuleuven.msec.iot.iotframework.callbackinterfaces.OnEventOccurred;
import be.kuleuven.msec.iot.iotframework.callbackinterfaces.OnRequestCompleted;
import be.kuleuven.msec.iot.iotframework.systemmanagement.constants.Device_constants;

/**
 * Created by ilsebohe on 10/10/2017.
 */

public abstract class PressureSensor extends VirtualIoTDevice {
    double pressure;
    int samplingRate;

    public PressureSensor(String systemID) {
        super(Device_constants.TYPE_PRESSURE_SENSOR,systemID);
    }

    public double getPressure() {
        return pressure;
    }

    public int getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(int samplingRate) {
        this.samplingRate = samplingRate;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }


    public abstract void requestPressure(OnRequestCompleted<Double> orc);

    public abstract void monitorPressure(OnEventOccurred<Double> oeo);

    public abstract void unmonitorPressure();

    public abstract void changeSamplingRate(int samplingRate, OnRequestCompleted<Boolean> orc);


}
