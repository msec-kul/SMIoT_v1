package be.kuleuven.msec.iot.iotframework.generic.devicelayer;


import be.kuleuven.msec.iot.iotframework.callbackinterfaces.OnEventOccurred;
import be.kuleuven.msec.iot.iotframework.callbackinterfaces.OnRequestCompleted;
import be.kuleuven.msec.iot.iotframework.systemmanagement.constants.Device_constants;

/**
 * Created by michielwillocx on 11/09/17.
 */

public abstract class Lamp extends VirtualIoTDevice {

    String color;
    int brightness;
    boolean on;
    boolean online;
    double hue;
    int saturation;
    int temperature;



    public Lamp(String systemID) {
        super(Device_constants.TYPE_LAMP,systemID);
    }

    public Lamp(String systemID, String color, int brightness, boolean on, boolean online, double hue, int saturation, int temperature) {
        super(Device_constants.TYPE_LAMP,systemID);
        this.color = color;
        this.brightness = brightness;
        this.on = on;
        this.online = online;
        this.hue = hue;
        this.saturation = saturation;
        this.temperature = temperature;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public double getHue() {
        return hue;
    }

    public void setHue(double hue) {
        this.hue = hue;
    }

    public int getSaturation() {
        return saturation;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public abstract void turnOn(OnRequestCompleted<Boolean> orc);

    public abstract void turnOff(OnRequestCompleted<Boolean> orc);

    public abstract void changeColor(String RGBcolor, OnRequestCompleted<Boolean> orc);

    public abstract void changeBrightness(int brightness, OnRequestCompleted<Boolean> orc);

    public abstract void changeHue(double hue, OnRequestCompleted<Boolean> orc);

    public abstract void changeSaturation(int saturation, OnRequestCompleted<Boolean> orc);

    public abstract void changeTemperature(int temperature, OnRequestCompleted<Boolean> orc);

    public abstract void monitorLampStatus(OnEventOccurred<Lamp> oeo);

}
