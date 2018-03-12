package be.kuleuven.msec.iot.iotframework.implementations.lamps.lightifylamp;

import be.kuleuven.msec.iot.iotframework.callbackinterfaces.OnEventOccurred;
import be.kuleuven.msec.iot.iotframework.callbackinterfaces.OnRequestCompleted;
import be.kuleuven.msec.iot.iotframework.generic.devicelayer.Lamp;
import be.kuleuven.msec.iot.iotframework.implementations.lamps.lightifylamp.lightifyjsonmodel.LightifyJSONModel;
import io.reactivex.Completable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by michielwillocx on 11/09/17.
 */

public class LightifyLamp extends Lamp {

    int id;
    String uniqueID;

    final int delay = 0;

    LightifyGateway gateway;

    public LightifyLamp(String color, int brightness, boolean on, boolean online, double hue, int saturation, int temperature, int id, String uniqueID, LightifyGateway gateway) {
        super(uniqueID, color, brightness, on, online, hue, saturation, temperature);
        this.id = id;
        this.uniqueID = uniqueID;
        this.gateway = gateway;
        super.setSystemID(uniqueID);
    }

    @Override
    public void turnOn(final OnRequestCompleted<Boolean> orc) {
        Completable result = gateway.getRestService().changeOnOffState(id, delay, 1);
        Disposable disposable = result.subscribeOn(Schedulers.io())
                .observeOn(observeScheduler)
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        setOn(true);
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
    public void turnOff(final OnRequestCompleted<Boolean> orc) {
        Completable result = gateway.getRestService().changeOnOffState(id, delay, 0);
        Disposable disposable = result.subscribeOn(Schedulers.io())
                .observeOn(observeScheduler)
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        setOn(false);
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
    public void changeColor(final String rgbColor, final OnRequestCompleted<Boolean> orc) {
        Completable result = gateway.getRestService().changeColor(id, delay, rgbColor);
        Disposable disposable = result.subscribeOn(Schedulers.io())
                .observeOn(observeScheduler)
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        setColor(rgbColor);
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
    public void changeBrightness(final int brightness, final OnRequestCompleted<Boolean> orc) {
        Completable result = gateway.getRestService().changeBrightness(id, delay, ((double) brightness / 100));
        Disposable disposable = result.subscribeOn(Schedulers.io())
                .observeOn(observeScheduler)
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        setBrightness(brightness);
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
    public void changeHue(final double hue, final OnRequestCompleted<Boolean> orc) {
        Completable result = gateway.getRestService().changeHue(id, delay, hue * 3.59 + 1);
        Disposable disposable = result.subscribeOn(Schedulers.io())
                .observeOn(observeScheduler)
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        setHue(hue);
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
    public void changeSaturation(final int saturation, final OnRequestCompleted<Boolean> orc) {
        Completable result = gateway.getRestService().changeSaturation(id, delay, ((double) saturation / 100));
        Disposable disposable = result.subscribeOn(Schedulers.io())
                .observeOn(observeScheduler)
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        setSaturation(saturation);
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
    public void changeTemperature(final int temperature, final OnRequestCompleted<Boolean> orc) {
        Completable result = gateway.getRestService().changeTemperature(id, delay, temperature * 70 + 1000);
        Disposable disposable = result.subscribeOn(Schedulers.io())
                .observeOn(observeScheduler)
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        setTemperature(temperature);
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
    public void monitorLampStatus(OnEventOccurred<Lamp> oeo) {
    }

    @Override
    public String toString() {
        return "Lamp OVERALL{" +
                "color='" + super.getColor() + '\'' +
                ", brightness=" + super.getBrightness() +
                ", on=" + super.isOn() +
                ", online=" + super.isOnline() +
                ", hue=" + super.getHue() +
                ", saturation=" + super.getSaturation() +
                ", temperature=" + super.getTemperature() +
                '}';
    }

    @Override
    public void isReachable(OnRequestCompleted<Boolean> orc) {
        gateway.getRestService().getLamp(id).subscribeOn(Schedulers.io()).observeOn(observeScheduler).subscribe(new Consumer<LightifyJSONModel>() {
            @Override
            public void accept(LightifyJSONModel lightifyJSONModel) throws Exception {
                System.out.println("LIGHTIFY " + lightifyJSONModel);
                orc.onSuccess(true);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                orc.onFailure(new Exception(throwable));
            }
        });

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

    public String getUniqueID() {
        return uniqueID;
    }
}
