package com.example.klugesheim;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ToggleButton lightButton = findViewById(R.id.button_light1);
        lightButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked) {
                    sendMessage("pilight-send -p clarus_switch -i A3 -u 20 --on");
                }
                else{
                    sendMessage("pilight-send -p clarus_switch -i A3 -u 20 --off");
                }
            }
        });
    }
    private void sendMessage(final String msg) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Socket sock = new Socket(host, port);
                    OutputStream out = sock.getOutputStream();
                    PrintWriter output = new PrintWriter(out);
                    output.println(msg);
                    output.flush();
                    sock.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
    private String host = "";    //insert server ip here
    private int port = 2048;
}