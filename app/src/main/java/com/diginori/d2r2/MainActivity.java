package com.diginori.d2r2;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ImageView robot_face;
    int[] robots;

    private Handler mHandler;
    private Runnable mRunnable;

    private TimerTask mTask;
    private Timer mTimer;

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
        mTimer.schedule(mTask, 0, 777);
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


    public void onClick(View v){
        changeImg();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void changeImg() {
        robot_face.setBackground(getImg());
    }

    int ri = 0;
    private Drawable getImg() {
        Log.i("RI:",ri + "");
        ri = ri + 1;
        if(ri >= robots.length){
            ri = 0;
        }
        int id = robots[ri];

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(id, getTheme());
        } else {
            return getResources().getDrawable(id);
        }
    }
}
