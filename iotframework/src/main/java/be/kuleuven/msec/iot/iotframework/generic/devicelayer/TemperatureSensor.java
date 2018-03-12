package be.kuleuven.msec.iot.iotframework.generic.devicelayer;

import be.kuleuven.msec.iot.iotframework.callbackinterfaces.OnEventOccurred;
import be.kuleuven.msec.iot.iotframework.callbackinterfaces.OnRequestCompleted;
import be.kuleuven.msec.iot.iotframework.systemmanagement.constants.Device_constants;

/**
 * Created by michielwillocx on 11/09/17.
 */

public abstract class TemperatureSensor extends VirtualIoTDevice {

    double temperature;
    int samplingRate;

    public TemperatureSensor(String systemID) {
        super(Device_constants.TYPE_TEMPERATURE_SENSOR,systemID);
    }


    public abstract void requestTemperature(OnRequestCompleted<Double> orc);

    public abstract void monitorTemperature(OnEventOccurred<Double> oeo);

    public abstract void unmonitorTemperature();

    public abstract void changeSamplingRate(int samplingRate, OnRequestCompleted<Boolean> orc);




    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(int samplingRate) {
        this.samplingRate = samplingRate;
    }



}


