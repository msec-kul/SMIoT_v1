package be.kuleuven.msec.iot.iotframework.generic.devicelayer;

import be.kuleuven.msec.iot.iotframework.callbackinterfaces.OnEventOccurred;
import be.kuleuven.msec.iot.iotframework.callbackinterfaces.OnRequestCompleted;
import be.kuleuven.msec.iot.iotframework.systemmanagement.constants.Device_constants;

/**
 * Created by ilsebohe on 31/10/2017.
 */

public abstract class PresenceSensor extends VirtualIoTDevice {
    boolean presence;
    int samplingRate;

    public PresenceSensor(String systemID) {
        super(Device_constants.TYPE_PRESENCE_SENSOR,systemID);
    }

    public boolean isPresence() {
        return presence;
    }

    public void setPresence(boolean presence) {
        this.presence = presence;
    }

    public int getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(int samplingRate) {
        this.samplingRate = samplingRate;
    }

    public abstract void requestPresence(OnRequestCompleted<Boolean> orc);

    public abstract void monitorPresence(OnEventOccurred<Boolean> oeo);

    public abstract void unmonitorPresence();

    public abstract void changeSamplingRate(int samplingRate, OnRequestCompleted<Boolean> orc);
}
