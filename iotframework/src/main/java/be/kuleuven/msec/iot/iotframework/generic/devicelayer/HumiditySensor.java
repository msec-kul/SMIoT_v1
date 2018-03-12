package be.kuleuven.msec.iot.iotframework.generic.devicelayer;

import be.kuleuven.msec.iot.iotframework.callbackinterfaces.OnEventOccurred;
import be.kuleuven.msec.iot.iotframework.callbackinterfaces.OnRequestCompleted;
import be.kuleuven.msec.iot.iotframework.systemmanagement.constants.Device_constants;

/**
 * Created by ilsebohe on 12/10/2017.
 */

public abstract class HumiditySensor extends VirtualIoTDevice {

    double humidity;
    int samplingRate;

    public HumiditySensor(String systemID) {
        super(Device_constants.TYPE_HUMIDITY_SENSOR,systemID);
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public int getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(int samplingRate) {
        this.samplingRate = samplingRate;
    }

    public abstract void requestHumidity(OnRequestCompleted<Double> orc);

    public abstract void monitorHumidity(OnEventOccurred<Double> oeo);

    public abstract void unmonitorHumidity();

    public abstract void changeSamplingRate(int samplingRate, OnRequestCompleted<Boolean> orc);

}
