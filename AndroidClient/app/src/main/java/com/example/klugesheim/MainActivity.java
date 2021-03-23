package com.example.klugesheim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private String listElDataKey = "data_key";
    ArrayList<Switch> switchList;
    SwitchAdapter switchAdapter;
    SharedPreferences sharedPref;
    HashSet<String> listElData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        listElData = new HashSet<String>();

        final Button scanButton = findViewById(R.id.scan_button);

        ListView listView = (ListView) findViewById(R.id.listview_activity_main);
        switchList = new ArrayList<Switch>();

        if (sharedPref.contains(listElDataKey)) {
            Iterator<String> it = sharedPref.getStringSet(listElDataKey, null).iterator();
            while (it.hasNext()) {
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

        scanButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View buttonView) {
                try {
                    Switch s = Connection.receiveMessage(findViewById(android.R.id.content).getRootView());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    listElData.add(s.getName() + "/" + s.getCommand());
                    editor.putStringSet(listElDataKey, listElData);
                    editor.apply();
                    switchAdapter.add(s);
                    switchAdapter.notifyDataSetChanged();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
