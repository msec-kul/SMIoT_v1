package be.kuleuven.msec.iot.iotframework.systemmanagement.jsonmodel;

/**
 * Created by michielwillocx on 25/09/17.
 */

public class JSMConnector {
    String systemID;
    String type;
    String[] initSettings;

    public String getSystemID() {
        return systemID;
    }

    public void setSystemID(String systemID) {
        this.systemID = systemID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getInitSettings() {
        return initSettings;
    }

    public void setInitSettings(String[] initSettings) {
        this.initSettings = initSettings;
    }
}
