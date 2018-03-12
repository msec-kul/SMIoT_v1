package be.kuleuven.msec.iot.myroom;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.concurrent.CountDownLatch;

import be.kuleuven.msec.iot.iotframework.callbackinterfaces.OnRequestCompleted;
import be.kuleuven.msec.iot.myroom.environment.ApplicationEnvironment;

public class MainActivity extends AppCompatActivity {

    String filename= "config.json";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = ActuateFragment.newInstance();
                    break;
                case R.id.navigation_dashboard:
                    selectedFragment = MonitorFragment.newInstance();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        loadView();


    }

    private void loadView() {
        final CountDownLatch latch = new CountDownLatch(1);
        final ApplicationEnvironment e = new ApplicationEnvironment();
        e.getConfigurationFromServer(this, filename, new OnRequestCompleted<Boolean>() {
            @Override
            public void onSuccess(Boolean response) {
                e.loadEnvironment(new OnRequestCompleted<Boolean>() {
                    @Override
                    public void onSuccess(Boolean response) {
                        latch.countDown();
                    }
                });
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, ActuateFragment.newInstance());
        transaction.commitAllowingStateLoss();    }

}
