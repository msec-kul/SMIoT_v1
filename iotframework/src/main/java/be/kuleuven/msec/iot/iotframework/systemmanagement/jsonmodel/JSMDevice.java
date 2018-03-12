package be.kuleuven.msec.iot.iotframework.systemmanagement.jsonmodel;

/**
 * Created by michielwillocx on 25/09/17.
 */

public class JSMDevice {
    String type;
    String model;
    String uniqueID;
    String[] settings;
String connector;



    public String[] getSettings() {
        return settings;
    }

    public void setSettings(String[] settings) {
        this.settings = settings;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getConnector() {
        return connector;
    }

    public void setConnector(String connector) {
        this.connector = connector;
    }
}
