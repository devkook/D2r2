package com.diginori.d2r2;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    ImageView robot_face;
    int[] robots;
    String[] words;

    private TimerTask mTask;
    private Timer mTimer;

    TextToSpeech tts;

    SensorManager sm;
    Sensor s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        robot_face = (ImageView) findViewById(R.id.imageView);

        robots = new int[4];
        robots[0] = R.mipmap.robotface;
        robots[1] = R.mipmap.robotface_redeye;
        robots[2] = R.mipmap.robotface_redenm;
        robots[3] = R.mipmap.robotface_red;

        words = new String[4];
        words[0] = "banana";
        words[1] = "desk";
        words[2] = "Incredibly handsome Tom Cruise";
        words[3] = "Very pretty Julia Roberts";

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                    Log.i("TTS", "SET");
                }else {
                    Log.e("TTS", "ERR");
                }
            }
        });

        say("Oh, well slept. Camera activity is completed. Well look over ~");

        mTask = new TimerTask() {
            @Override
            public void run() {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       changeImg();
                       Log.i("i", "HA");
                   }
               });

            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTask, 0, 5000);

        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        s = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    float x,y,z;

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ORIENTATION:
                    x = event.values[0];
                    y = event.values[1];
                    z = event.values[2];
                    Log.i("SEN X", Float.toString(x));
                    Log.i("SEN Y", Float.toString(y));
                    Log.i("SEN Z", Float.toString(z));
                    break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    protected void onStop() {
        super.onStop();
//        SensorManager.unregisterListener(this, SensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        say("WAY?");

        if(s != null){
            sm.registerListener(this,s,SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        say("GOOD BYE");
    }

    @Override
    protected void onPause() {
        super.onPause();
        say("I am sleepy");
    }

    public void onClick(View v){
        changeImg();
        say(words[ri]);
    }

    private void say(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsGreater21(text);
        } else {
            ttsUnder20(text);
        }
    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId=this.hashCode() + "";
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void changeImg() {
        robot_face.setBackground(getImg());

        if(Math.abs(y) > 60 & Math.abs(y) < 60){
            say("Look good");
        }

        if(Math.abs(x) > 0 & Math.abs(x) < 10){
            say("The sky is always beautiful");
        }
    }

    int ri = 0;
    private Drawable getImg() {
        ri = ri + 1;
        if(ri >= robots.length){
            ri = 0;
        }
        Log.i("RI:",ri + "");

        int id = robots[ri];

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(id, getTheme());
        } else {
            return getResources().getDrawable(id);
        }
    }


}
