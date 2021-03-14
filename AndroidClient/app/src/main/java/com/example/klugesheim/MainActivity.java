package com.example.klugesheim;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        final Button scanButton = findViewById(R.id.scan_button);
        final TextView out = findViewById(R.id.output);

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
        scanButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View buttonView){
                receiveMessage();
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

    private void receiveMessage(){
        final TextView out = findViewById(R.id.output);
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){

               try{
                   Socket sock = new Socket(host, port);
                   OutputStream outStream = sock.getOutputStream();
                   PrintWriter output = new PrintWriter(outStream);
                   output.print("scan");
                   output.flush();
                   in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                   String msg = in.readLine();
                   out.setText(msg);
                   sock.close();
               }catch(IOException e){
                   e.printStackTrace();
               }
           }

        });

        thread.start();
    }
    private String host = "192.168.2.200";    //insert server ip here
    private int port = 2048;
    BufferedReader in;
}