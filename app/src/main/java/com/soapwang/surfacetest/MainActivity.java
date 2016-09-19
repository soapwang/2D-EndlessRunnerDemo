package com.soapwang.surfacetest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private GameView gameView;
    private FrameLayout mFrame;
    private View ui;
    private Button pauseButton;
    private TextView resumeHint;
    private BroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        mFrame = new FrameLayout(this);
        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        ui = inflater.inflate(R.layout.ui_layout,null);
        pauseButton = (Button) ui.findViewById(R.id.pause);
        resumeHint = (TextView) ui.findViewById(R.id.textView1);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseGame();
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(GameView.RESUMED);
        receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction() == GameView.RESUMED) {
                    resumeGame();
                }
            }
        };
        registerReceiver(receiver, filter);

        mFrame.addView(gameView);
        mFrame.addView(ui);
        setContentView(mFrame);
    }

    public void pauseGame() {
        gameView.pause();
        resumeHint.setVisibility(View.VISIBLE);
        ui.setBackgroundColor(ContextCompat.getColor(
                MainActivity.this,R.color.lightGray));
        pauseButton.setVisibility(View.INVISIBLE);
    }

    public void resumeGame() {
        //gameView.resume();
        resumeHint.setVisibility(View.INVISIBLE);
        ui.setBackgroundColor(Color.TRANSPARENT);
        pauseButton.setVisibility(View.VISIBLE);
    }

    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }
}
