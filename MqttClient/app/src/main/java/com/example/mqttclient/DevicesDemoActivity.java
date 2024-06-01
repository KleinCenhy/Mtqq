package com.example.mqttclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.mqttclient.mqtt.MqttService;
import com.example.mqttclient.protocol.AirConditioningMessage;
import com.example.mqttclient.protocol.BoolMessage;
import com.example.mqttclient.protocol.FloatMessage;
import com.example.mqttclient.protocol.IntMessage;
import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.HashMap;
import java.util.Map;

public class DevicesDemoActivity extends AppCompatActivity implements MqttService.MqttEventCallBack, CompoundButton.OnCheckedChangeListener {

    private TextView connectState, temperatureValue, humidityValue, pmValue, gasValue,co2Value,wtValue,lightingValue,doorStatus,peopleStatus;
    private EditText airConditioningValue;
    private MqttService.MqttBinder mqttBinder;
    private String TAG = "MainActivity";
    private Switch parlourLightSwitch, curtain_switch, air_conditioning_switch,pump_switch;
    private SeekBar fan_socket_seekbar,parlour_light_seekbar;
    private Map<String, Integer> subscribeTopics = new HashMap<>();

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mqttBinder = (MqttService.MqttBinder) iBinder;
            mqttBinder.setMqttEventCallback(DevicesDemoActivity.this);
            if (mqttBinder.isConnected()) {
                connectState.setText("已连接");
                subscribeTopics();
            } else {
                connectState.setText("未连接");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_demo);

        connectState = findViewById(R.id.dev_connect_state);

        Intent mqttServiceIntent = new Intent(this, MqttService.class);
        bindService(mqttServiceIntent, connection, Context.BIND_AUTO_CREATE);

        temperatureValue = findViewById(R.id.temperature_value);
        humidityValue = findViewById(R.id.humidity_value);
        pmValue = findViewById(R.id.pm_value);
        co2Value=findViewById(R.id.co2_value);
        gasValue = findViewById(R.id.gas_value);
        wtValue=findViewById(R.id.wt_value);
        lightingValue=findViewById(R.id.lighting_value);
        doorStatus = findViewById(R.id.door_status);
        peopleStatus=findViewById(R.id.people_status);


        airConditioningValue = findViewById(R.id.air_conditioning_value);
        parlourLightSwitch = findViewById(R.id.parlour_light_switch);
        parlourLightSwitch.setOnCheckedChangeListener(this);
        curtain_switch = findViewById(R.id.curtain_switch);
        curtain_switch.setOnCheckedChangeListener(this);
        pump_switch=findViewById(R.id.pump_switch);
        pump_switch.setOnCheckedChangeListener(this);
        parlour_light_seekbar = findViewById(R.id.parlour_light_seekbar);
        parlour_light_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        fan_socket_seekbar = findViewById(R.id.fan_socket_seekbar);
        fan_socket_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        air_conditioning_switch = findViewById(R.id.air_conditioning_switch);
        air_conditioning_switch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.parlour_light_switch:
                try {
                    if (compoundButton.isChecked()) {
                        mqttBinder.publishMessage("/test/light1",
                                new Gson().toJson(new BoolMessage(true)));
                    } else {
                        mqttBinder.publishMessage("/test/light1",
                                new Gson().toJson(new BoolMessage(false)));
                    }
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.curtain_switch:
                try {
                    if (compoundButton.isChecked()) {
                        mqttBinder.publishMessage("/test/curtain1",
                                new Gson().toJson(new BoolMessage(true)));
                    } else {
                        mqttBinder.publishMessage("/test/curtain1",
                                new Gson().toJson(new BoolMessage(false)));
                    }
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.fan_socket_seekbar:
                try {
                    if (compoundButton.isChecked()) {
                        mqttBinder.publishMessage("/test/fan1",
                                new Gson().toJson(new BoolMessage(true)));
                    } else {
                        mqttBinder.publishMessage("/test/fan1",
                                new Gson().toJson(new BoolMessage(false)));
                    }
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.air_conditioning_switch:
                try {
                    if (compoundButton.isChecked()) {
                        String json = new Gson().toJson(new AirConditioningMessage(true,
                                Float.parseFloat(airConditioningValue.getText().toString())));
                        Log.d("json",json);
                        mqttBinder.publishMessage("/test/airConditioning",json);
                    } else {
                        String json = new Gson().toJson(new AirConditioningMessage(false,
                                Float.parseFloat(airConditioningValue.getText().toString())));
                        Log.d("json",json);
                        mqttBinder.publishMessage("/test/airConditioning",json);
                    }
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.pump_switch:
                try {
                    if (compoundButton.isChecked()) {
                        mqttBinder.publishMessage("/test/waterPump",
                                new Gson().toJson(new BoolMessage(true)));
                    } else {
                        mqttBinder.publishMessage("/test/waterPump",
                                new Gson().toJson(new BoolMessage(false)));
                    }
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    void subscribeTopics() {
        try {
            subscribeTopics.put("/test/temp",1);
            subscribeTopics.put("/test/hum", 2);
            subscribeTopics.put("/test/pm",3);
            subscribeTopics.put("/test/co2",4);
            subscribeTopics.put("/test/waterTower",5);
            subscribeTopics.put("/test/gas",6);
            subscribeTopics.put("/test/illuminance",7);
            subscribeTopics.put("/test/door",8);
            subscribeTopics.put("/test/human",9);
            for(Map.Entry<String, Integer> entry : subscribeTopics.entrySet()){
                mqttBinder.subscribe(entry.getKey());
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    void unSubscribeTopics() {
        try {
            for(Map.Entry<String, Integer> entry : subscribeTopics.entrySet()){
                mqttBinder.unSubscribe(entry.getKey());
            }
            subscribeTopics.clear();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectSuccess() {
        subscribeTopics();
        connectState.setText("已连接");
    }

    @Override
    public void onConnectError(String error) {
        Log.d(TAG, "onConnectError: " + error);
        connectState.setText("未连接");
        subscribeTopics.clear();
    }

    @Override
    public void onDeliveryComplete() {
        Log.d(TAG, "publish ok");
    }

    @Override
    public void onMqttMessage(String topic, String message) {
        Log.d("onMqttMessage", "topic:"+topic+ "message length:"+ message.length() + ", message:"+message);
        Gson gson = new Gson();
        switch (subscribeTopics.get(topic)){
            case 1:
                temperatureValue.setText(String.valueOf(gson.fromJson(message.trim(), FloatMessage.class).value));
                break;

            case 2:
                humidityValue.setText(String.valueOf(gson.fromJson(message.trim(), IntMessage.class).value));
                break;

            case 3:
                pmValue.setText(String.valueOf(gson.fromJson(message.trim(), IntMessage.class).value));
                break;

            case 4:
                co2Value.setText(String.valueOf(gson.fromJson(message.trim(), IntMessage.class).value));
                break;
            case 5:
                wtValue.setText(String.valueOf(gson.fromJson(message.trim(), IntMessage.class).value));
                break;
            case 6:
                gasValue.setText(String.valueOf(gson.fromJson(message.trim(), IntMessage.class).value));
                break;
            case 7:
                lightingValue.setText(String.valueOf(gson.fromJson(message.trim(), IntMessage.class).value));
                break;
            case 8:
                String Status1 = gson.fromJson(message.trim(), BoolMessage.class).value ?"开":"关";
                doorStatus.setText(Status1);
                break;
            case 9:
                String Status2 = gson.fromJson(message.trim(), BoolMessage.class).value ?"有":"无";
                peopleStatus.setText(Status2);
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mqttBinder.isConnected()) {
            connectState.setText("已连接");
            subscribeTopics();
        } else {
            connectState.setText("未连接");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unSubscribeTopics();
    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }
    public class GasConcentrationService {

        private static final String TAG = "GasConcentrationService";
        private Handler handler = new Handler();
        private final Runnable checkGasConcentrationRunnable = new Runnable() {
            @Override
            public void run() {
                // 这里调用你的方法来读取气体浓度
                float concentration = readGasConcentration();

                // 检查浓度是否超过阈值
                if (concentration > THRESHOLD) {
                    // 发送通知
                    sendNotification("可燃气体报警", "浓度过高，请立即处理！");
                }

                // 根据需要重复检查
                handler.postDelayed(this, CHECK_INTERVAL);
            }
        };

        // 气体浓度检查的间隔（毫秒）
        private static final long CHECK_INTERVAL = 10000; // 例如，每10秒检查一次

        // 气体浓度的阈值
        private static final float THRESHOLD = 50.0f; // 根据你的需求设置

        // 假设这是一个方法来模拟从传感器读取气体浓度
        private float readGasConcentration() {
            // 这里应该是你的代码来从传感器读取浓度值
            // 为了示例，我们返回一个模拟值
            return 40.0f; // 示例值
        }

        // 发送通知的方法
        private void sendNotification(String title, String message) {
            // 这里是你的代码来创建并发送通知
            // ...
        }

        @Override
        public void onCreate() {
            super.onCreate();
            // 在这里可以初始化一些必要的资源或连接
            // 例如，连接传感器等
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            // 开始检查气体浓度
            handler.post(checkGasConcentrationRunnable);

            // 如果服务被杀死后需要重启，则返回START_STICKY
            // 否则，返回START_NOT_STICKY或START_REDELIVER_INTENT
            return START_STICKY;
        }

        @Override
        public IBinder onBind(Intent intent) {
            // 因为这个服务不需要绑定到Activity，所以返回null
            return null;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            // 在这里释放资源或断开连接
            // 例如，断开与传感器的连接等
            handler.removeCallbacks(checkGasConcentrationRunnable);
        }
    }
}
