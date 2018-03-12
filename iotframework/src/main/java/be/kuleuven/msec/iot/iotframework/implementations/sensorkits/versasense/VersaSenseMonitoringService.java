package be.kuleuven.msec.iot.iotframework.implementations.sensorkits.versasense;

import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import be.kuleuven.msec.iot.iotframework.callbackinterfaces.OnEventOccurred;
import be.kuleuven.msec.iot.iotframework.implementations.sensorkits.versasense.versasensejsonmodel.VersaSenseDataJSONModel;
import be.kuleuven.msec.iot.iotframework.implementations.sensorkits.versasense.versasensejsonmodel.VersaSensePeripheralSampleJSONModel;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static java.lang.Thread.sleep;

/**
 * Created by ilsebohe on 16/10/2017.
 */

public class VersaSenseMonitoringService {
    MqttClient client;
    private Observable<Pair<String, String>> monitor;
    Map<String, OnEventOccurred<String>> subscribers = new HashMap<String, OnEventOccurred<String>>();
    CountDownLatch connectLatch;
    boolean connecting = false;

    public Observable<Pair<String, String>> getMonitor() {
        return monitor;
    }

    public MqttClient getClient() {
        return client;
    }

    public void setClient(MqttClient client) {
        this.client = client;
    }

    public void setMonitor(Observable<Pair<String, String>> monitor) {
        this.monitor = monitor;
    }

    public VersaSenseMonitoringService() {
        connectLatch = new CountDownLatch(1);
        monitor = Observable.create(new ObservableOnSubscribe<Pair<String, String>>() {
            @Override
            public void subscribe(ObservableEmitter<Pair<String, String>> emitter) throws Exception {
                try {
                    client = new MqttClient("tcp://gateway.versasense.com:1883",
                            MqttClient.generateClientId(),
                            new MemoryPersistence()
                    );
                    client.setCallback(new MqttCallback() {
                        @Override
                        public void connectionLost(Throwable cause) {
                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage message) throws Exception {
                            String str = new String(message.getPayload(), "UTF-8");
                            Log.e(topic, str);
                            emitter.onNext(new Pair<>(topic, str));
                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken token) {
                        }
                    });
                    connectClient();
                    connectLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void connectClient() throws MqttException {
        if (!connecting) {
            Log.e("Connecting client", "waiting....");
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("user");
            String password = "password";
            options.setPassword(password.toCharArray());
            client.connect(options);
            connecting = true;
        }
        else {
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (client.isConnected()) {
            connecting=false;
            Log.e("Client connected", "client should be connected");
        }

    }

    public void subscribe(final String macAddress, final String measurement, final OnEventOccurred<String> oeo) {
        System.out.println("subscribing");
        Disposable disposable = monitor
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Pair<String, String>>() {
                    @Override
                    public void accept(@NonNull Pair<String,String> pair) throws Exception {
                        Gson gson = new Gson();
                        VersaSensePeripheralSampleJSONModel sample = gson.fromJson(pair.second, VersaSensePeripheralSampleJSONModel.class);
                        for (VersaSenseDataJSONModel data : sample.getData()) {
                            OnEventOccurred oeo = subscribers.get(pair.first + data.getMeasurement());
                            if (oeo != null) {
                                oeo.onUpdate(data.getValue().toString());
                            }
                        }

                    }
                });
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connectLatch.await();
                    if (!client.isConnected()) connectClient();
                    subscribers.put("vs/" + macAddress + "/sensor-data" + measurement, oeo);
                    client.subscribe("vs/" + macAddress + "/sensor-data", 1);
                    Log.i("Subscribed to device", macAddress);
                } catch (MqttException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void unsubscribe(String macAddress, String measurement) {
        try {
            subscribers.remove("vs/" + macAddress + "/sensor-data" + measurement);
            if (subscribers.isEmpty())
                client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
