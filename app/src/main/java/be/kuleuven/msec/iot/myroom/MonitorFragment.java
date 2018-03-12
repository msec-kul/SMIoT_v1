package be.kuleuven.msec.iot.myroom;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import be.kuleuven.msec.iot.iotframework.callbackinterfaces.OnEventOccurred;
import be.kuleuven.msec.iot.myroom.environment.ApplicationEnvironment;

/**
 * Created by ilsebohe on 16/01/2018.
 */

public class MonitorFragment extends Fragment {

    TextView temperatureTextView;
    TextView humidityTextView;
    TextView pressureTextView;



    public static MonitorFragment newInstance() {
        MonitorFragment fragment = new MonitorFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        monitorRoom();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_monitor, container, false);
        temperatureTextView = (TextView) view.findViewById(R.id.textView_temperature);
        humidityTextView = (TextView) view.findViewById(R.id.textView_humidity);
        pressureTextView = (TextView) view.findViewById(R.id.textView_pressure);

        return view;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unmonitorRoom();
    }

    private void unmonitorRoom() {
        ApplicationEnvironment.getRoom().unmonitorTemperature();
        ApplicationEnvironment.getRoom().unmonitorHumidity();
        ApplicationEnvironment.getRoom().unmonitorPressure();
    }

    private void monitorRoom() {
        ApplicationEnvironment.getRoom().monitorTemperature(new OnEventOccurred<Double>() {
            @Override
            public void onUpdate(Double response) {
                temperatureTextView.setText(String.format("%.2f", response) +" °C");
            }
        });
        ApplicationEnvironment.getRoom().monitorHumidity(new OnEventOccurred<Double>() {
            @Override
            public void onUpdate(Double response) {
                humidityTextView.setText(String.format("%.2f", response)+" %");
            }
        });
        ApplicationEnvironment.getRoom().monitorPressure(new OnEventOccurred<Double>() {
            @Override
            public void onUpdate(Double response) {
                pressureTextView.setText(String.format("%.2f", response) + " hPa");
            }
        });
    }




}
