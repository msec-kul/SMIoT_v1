package be.kuleuven.msec.iot.iotframework.implementations.sensorkits.versasense;

import java.util.ArrayList;
import java.util.Iterator;

import be.kuleuven.msec.iot.iotframework.callbackinterfaces.OnEventOccurred;
import be.kuleuven.msec.iot.iotframework.callbackinterfaces.OnRequestCompleted;
import be.kuleuven.msec.iot.iotframework.generic.devicelayer.VirtualIoTConnector;
import be.kuleuven.msec.iot.iotframework.implementations.humiditysensors.VersaSenseHumiditySensor;
import be.kuleuven.msec.iot.iotframework.implementations.lightsensors.VersaSenseLightSensor;
import be.kuleuven.msec.iot.iotframework.implementations.presencesensors.VersaSensePresenceSensor;
import be.kuleuven.msec.iot.iotframework.implementations.pressuresensors.VersaSensePressureSensor;
import be.kuleuven.msec.iot.iotframework.implementations.sensorkits.versasense.versasensejsonmodel.VersaSenseDeviceJSONModel;
import be.kuleuven.msec.iot.iotframework.implementations.sensorkits.versasense.versasensejsonmodel.VersaSenseMeasurementJSONModel;
import be.kuleuven.msec.iot.iotframework.implementations.sensorkits.versasense.versasensejsonmodel.VersaSensePeripheralJSONModel;
import be.kuleuven.msec.iot.iotframework.implementations.temperaturesensors.VersaSenseTemperatureSensor;
import be.kuleuven.msec.iot.iotframework.systemmanagement.constants.VersaSense_constants;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ilsebohe on 13/10/2017.
 */

public class VersaSenseGateway extends VirtualIoTConnector {

    String address;

    VersaSenseGateway thisGateway;
    VersaSenseRestService restService;

    //TODO monitoring of sensors
    VersaSenseMonitoringService monitoringService;

    public VersaSenseGateway(String systemID, String address) {
        super(systemID);
        this.address = address;
        thisGateway = this;
    }

    @Override
    public void updateConnectedDeviceList(final OnRequestCompleted<Boolean> orc) {
        restService.getAllDevices()
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<ArrayList<VersaSenseDeviceJSONModel>>() {

                    @Override
                    public void accept(@NonNull ArrayList<VersaSenseDeviceJSONModel> versaSenseDeviceJSONModels) throws Exception {
                        final Iterator it = versaSenseDeviceJSONModels.iterator();
                        while (it.hasNext()) {
                            final VersaSenseDeviceJSONModel device = (VersaSenseDeviceJSONModel) it.next();

                            for (VersaSensePeripheralJSONModel peripheral : device.getPeripherals()) {
                                final Iterator it2 = peripheral.getMeasurements().iterator();
                                while (it2.hasNext()) {
                                    final VersaSenseMeasurementJSONModel measurement = (VersaSenseMeasurementJSONModel) it2.next();

                                    if (measurement.getName().equals(VersaSense_constants.TEMPERATURE)) {
                                        VersaSenseTemperatureSensor temp = new VersaSenseTemperatureSensor(device.getMac(),peripheral.getIdentifier(), thisGateway);
                                        connectedDevices.add(temp);
                                    }
                                    if (measurement.getName().equals(VersaSense_constants.PRESSURE)) {
                                        VersaSensePressureSensor temp = new VersaSensePressureSensor(device.getMac(),peripheral.getIdentifier(), thisGateway);
                                        connectedDevices.add(temp);

                                    }
                                    if (measurement.getName().equals(VersaSense_constants.HUMIDITY)) {
                                        VersaSenseHumiditySensor temp = new VersaSenseHumiditySensor(device.getMac(),peripheral.getIdentifier(),  thisGateway);
                                        connectedDevices.add(temp);

                                    }
                                    if (measurement.getName().equals(VersaSense_constants.LIGHT)) {
                                        VersaSenseLightSensor temp = new VersaSenseLightSensor(device.getMac(),peripheral.getIdentifier(),  thisGateway);
                                        connectedDevices.add(temp);

                                    }
                                    if (measurement.getName().equals(VersaSense_constants.MOTION)) {
                                        VersaSensePresenceSensor temp = new VersaSensePresenceSensor(device.getMac(),peripheral.getIdentifier(), thisGateway);
                                        connectedDevices.add(temp);

                                    }
                                    it2.remove();
                                }
                            }
                            it.remove();
                        }
                        orc.onSuccess(true);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        orc.onFailure(new Exception(throwable));
                    }
                });

    }

    @Override
    public void initialize(OnRequestCompleted orc) {

        restService = new Retrofit.Builder()
                .baseUrl(address)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                // uncommment .client(...) to log
                //.client(new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)).build())
                .build()
                .create(VersaSenseRestService.class);

        orc.onSuccess(true);
    }

    @Override
    public void isReachable(OnRequestCompleted<Boolean> orc) {

    }

    @Override
    public void monitorReachability(OnEventOccurred<Boolean> oeo) {

    }

    @Override
    public void connect(OnRequestCompleted<Boolean> orc) {

    }

    @Override
    public void disconnect(OnRequestCompleted<Boolean> orc) {

    }

    public VersaSenseMonitoringService getMonitoringService() {
        if (monitoringService == null) {
            monitoringService = new VersaSenseMonitoringService();
        }
        return monitoringService;
    }

    public VersaSenseRestService getRestService() {
        return restService;
    }


}
