package com.example.klugesheim;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.klugesheim.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static private String host = "";    //insert server ip here
    static private int port = 2048;
    BufferedReader in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button scanButton = findViewById(R.id.scan_button);

        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View buttonView) {
                receiveMessage();
            }
        });
        ListView listView = (ListView) findViewById(R.id.listview_activity_main);
        ArrayList<Switch> switchList = new ArrayList<Switch>();
        Switch s1 = new Switch("Kühlschrank", "-p clarus_switch -i A3 -u 20");
        switchList.add(s1);
        SwitchAdapter switchAdapter = new SwitchAdapter(getApplicationContext(), switchList);
        listView.setAdapter(switchAdapter);
    }

    private void receiveMessage(){
        //final TextView out = findViewById(R.id.output);
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
                    //out.setText(msg);
                    sock.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

        });

        thread.start();
    }

    static public String getHost(){
        return host;
    }
    static public int getPort(){
        return port;
    }
}
