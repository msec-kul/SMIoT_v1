package be.kuleuven.msec.iot.myroom.environment;

import java.util.ArrayList;

import be.kuleuven.msec.iot.iotframework.callbackinterfaces.OnEventOccurred;
import be.kuleuven.msec.iot.iotframework.callbackinterfaces.OnRequestCompleted;
import be.kuleuven.msec.iot.iotframework.exceptions.VirtualIoTDeviceNotFoundException;
import be.kuleuven.msec.iot.iotframework.generic.componentlayer.Component;
import be.kuleuven.msec.iot.iotframework.generic.devicelayer.HumiditySensor;
import be.kuleuven.msec.iot.iotframework.generic.devicelayer.Lamp;
import be.kuleuven.msec.iot.iotframework.generic.devicelayer.PressureSensor;
import be.kuleuven.msec.iot.iotframework.generic.devicelayer.TemperatureSensor;

/**
 * Created by ilsebohe on 16/01/2018.
 */

public class Room extends Component {

    ArrayList<Lamp> lamps;
    TemperatureSensor temperatureSensor;
    HumiditySensor humiditySensor;
    PressureSensor pressureSensor;

    String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Room(String componentname) {
        super(componentname);
        lamps = new ArrayList<>();
    }

    public void setTemperatureSensor(TemperatureSensor temperatureSensor) {
        this.temperatureSensor = temperatureSensor;
    }

    public void setHumiditySensor(HumiditySensor humiditySensor) {
        this.humiditySensor = humiditySensor;
    }

    public void setPressureSensor(PressureSensor pressureSensor) {
        this.pressureSensor = pressureSensor;
    }

    public void addLamp(Lamp lamp) {
        this.lamps.add(lamp);
    }

    public void getTemperature(OnRequestCompleted<Double> orc) {
        temperatureSensor.requestTemperature(orc);
    }

    public void monitorTemperature(OnEventOccurred<Double> oeo) {
        temperatureSensor.monitorTemperature(oeo);

    }

    public void unmonitorTemperature() {
        temperatureSensor.unmonitorTemperature();

    }

    public void getHumidity(OnRequestCompleted<Double> orc) {
        humiditySensor.requestHumidity(orc);
    }

    public void monitorHumidity(OnEventOccurred<Double> oeo) {
        humiditySensor.monitorHumidity(oeo);

    }

    public void unmonitorHumidity() {
        humiditySensor.unmonitorHumidity();

    }

    public void getPressure(OnRequestCompleted<Double> orc) {
        pressureSensor.requestPressure(orc);
    }

    public void monitorPressure(OnEventOccurred<Double> oeo) {
        pressureSensor.monitorPressure(oeo);

    }

    public void unmonitorPressure() {
        pressureSensor.unmonitorPressure();

    }

    public void lightsOn(OnRequestCompleted<Boolean> orc) {
        for (Lamp l : lamps) {
            System.out.println(l.toString());
            l.turnOn(orc);
        }
    }

    public void lightsOff(OnRequestCompleted<Boolean> orc) {
        for (Lamp l : lamps) {
            l.turnOff(orc);
        }
    }

    public void changeBrightness(int brightness, OnRequestCompleted orc) {
        for (Lamp l : lamps) {
            l.changeBrightness(brightness, orc);
        }
    }

    public void changeColor(String color, OnRequestCompleted orc) {
        for (Lamp l : lamps) {
            l.changeColor(color, orc);
        }
    }

    public ArrayList<Lamp> getLamps() {
        return lamps;
    }

    public Lamp getLamp(String systemID) {
        for (Lamp lamp :
                lamps) {
            if (lamp.getSystemID().equals(systemID)) return lamp;
        }
        throw new VirtualIoTDeviceNotFoundException(this.getClass().getName(), "", systemID);
    }
}