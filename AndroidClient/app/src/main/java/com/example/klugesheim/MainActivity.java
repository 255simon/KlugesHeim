package com.example.klugesheim;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static private String host = "192.168.2.200";    //insert server ip here
    static private int port = 2048;
    ArrayList<Switch> switchList;
    SwitchAdapter switchAdapter;
    BufferedReader in;

    private class Command{
        private String command;

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button scanButton = findViewById(R.id.scan_button);

        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View buttonView) {
                try {
                    receiveMessage();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        ListView listView = (ListView) findViewById(R.id.listview_activity_main);
        switchList = new ArrayList<Switch>();
        Switch s1 = new Switch("Kühlschrank", "-p clarus_switch -i A3 -u 20");
        switchList.add(s1);
        switchAdapter = new SwitchAdapter(getApplicationContext(), switchList);
        listView.setAdapter(switchAdapter);
    }

    private void receiveMessage() throws InterruptedException {
        //final TextView out = findViewById(R.id.output);
        TextInputLayout textInput = findViewById(R.id.name_input_layout);
        String switchName;
        switchName = textInput.getEditText().getText().toString();

        final Command com = new Command();
        Thread thread = new Thread(new Runnable(){

            @Override
            public void run() {

                try {
                    Socket sock = new Socket(host, port);
                    OutputStream outStream = sock.getOutputStream();
                    PrintWriter output = new PrintWriter(outStream);
                    output.print("scan");
                    output.flush();
                    in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                    com.setCommand(in.readLine());
                    sock.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        thread.start();
        thread.join();
        switchAdapter.add(new Switch(switchName, com.getCommand()));
        switchAdapter.notifyDataSetChanged();
    }

    static public String getHost(){
        return host;
    }
    static public int getPort(){
        return port;
    }
}
