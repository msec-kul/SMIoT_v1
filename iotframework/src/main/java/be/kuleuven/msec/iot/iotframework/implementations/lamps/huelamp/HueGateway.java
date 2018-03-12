package be.kuleuven.msec.iot.iotframework.implementations.lamps.huelamp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import be.kuleuven.msec.iot.iotframework.callbackinterfaces.OnEventOccurred;
import be.kuleuven.msec.iot.iotframework.callbackinterfaces.OnRequestCompleted;
import be.kuleuven.msec.iot.iotframework.generic.devicelayer.VirtualIoTConnector;
import be.kuleuven.msec.iot.iotframework.implementations.lamps.huelamp.huejsonmodel.HueLampJSONModel;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by michielwillocx on 18/09/17.
 */

public class HueGateway extends VirtualIoTConnector {

    ArrayList<HueLamp> lamps;
    HueGateway thisGateway;

    String address;
    String AuthID;

    HueLampRestService restService;


    public HueGateway(String systemID, String address, String authID) {

        super(systemID);

        this.address = address;
        this.AuthID = authID;
        lamps = new ArrayList<>();
        thisGateway = this;

    }

    @Override
    public void updateConnectedDeviceList(final OnRequestCompleted<Boolean> orc) {

        Single<Map<String, HueLampJSONModel>> result = restService.getAllLamps(AuthID);

        Disposable disposable = result.subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Map<String, HueLampJSONModel>>() {
                    @Override
                    public void accept(@NonNull Map<String, HueLampJSONModel> lampMap) {
                        Iterator it = lampMap.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            System.out.println(pair.getKey() + " = " + pair.getValue());

                            HueLampJSONModel dev = (HueLampJSONModel) pair.getValue();

                          HueLamp temp = new HueLamp(HueLamp.convertXYtoRGB(new float[]{(float) dev.getState().getXy()[0], (float) dev.getState().getXy()[1]}), (int) (((double) dev.getState().getBri() / 254) * 100), dev.getState().isOn(), dev.getState().isReachable(), dev.getState().getHue(), (int) (((double) dev.getState().getSat() / 254) * 100), dev.getState().getCt(), Integer.valueOf((String) pair.getKey()), dev.getUniqueid(), thisGateway);
                            lamps.add(temp);
                            connectedDevices.add(temp);
                            it.remove();
                        }
                        orc.onSuccess(true);
                    }
                });
    }

    @Override
    public void initialize(OnRequestCompleted orc) {

        restService = new Retrofit.Builder()
                .baseUrl(address)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(HueLampRestService.class);

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

    public HueLampRestService getRestService() {
        return restService;
    }

    public ArrayList<HueLamp> getLamps() {
        return lamps;
    }

    public void setLamps(ArrayList<HueLamp> lamps) {
        this.lamps = lamps;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAuthID() {
        return AuthID;
    }

    public void setAuthID(String authID) {
        this.AuthID = authID;
    }
}
