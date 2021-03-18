package com.example.klugesheim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.Edits;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    static private String host = "192.168.2.200";    //insert server ip here
    static private int port = 2048;
    private String listElDataKey = "data_key";
    ArrayList<Switch> switchList;
    SwitchAdapter switchAdapter;
    SharedPreferences sharedPref;
    HashSet<String> listElData;
    BufferedReader in;

    private class Command{              //this class is used to modify command string in receive thread
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

        sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        listElData = new HashSet<String>();

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

        if(sharedPref.contains(listElDataKey)){
            Iterator<String> it = sharedPref.getStringSet(listElDataKey, null).iterator();
            while(it.hasNext()){
                String[] stringArray = it.next().split("/");
                String switchName = stringArray[0];
                String command = stringArray[1];
                Switch s = new Switch(switchName, command);
                switchList.add(s);
                listElData.add(switchName + "/" + command);
            }
        }

        switchAdapter = new SwitchAdapter(getApplicationContext(), switchList);
        listView.setAdapter(switchAdapter);
    }

    private void receiveMessage() throws InterruptedException {
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
        SharedPreferences.Editor editor = sharedPref.edit();
        listElData.add(switchName + "/" + com.getCommand());
        editor.putStringSet(listElDataKey, listElData);
        editor.apply();
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
