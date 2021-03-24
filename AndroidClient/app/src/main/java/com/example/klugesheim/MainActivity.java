package com.example.klugesheim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.klugesheim.MESSAGE";

    private String listElDataKey = "data_key";
    ArrayList<Switch> switchList;
    SwitchAdapter switchAdapter;
    SharedPreferences sharedPref;
    StorageManager storageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        storageManager = new StorageManager(sharedPref);

        final Button scanButton = findViewById(R.id.scan_button);

        ListView listView = (ListView) findViewById(R.id.listview_activity_main);
        switchList = storageManager.loadData();
        switchAdapter = new SwitchAdapter(getApplicationContext(), switchList, storageManager);
        listView.setAdapter(switchAdapter);

        scanButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View buttonView) {
                try {
                    Switch s = Connection.receiveMessage(findViewById(android.R.id.content).getRootView());
                    if(s instanceof FailureSwitch){
                        //some error handling
                        Intent intent = new Intent(getApplicationContext(), ErrorMessageActivity.class);
                        intent.putExtra(EXTRA_MESSAGE, ((FailureSwitch) s).getErrorMessage());
                        startActivity(intent);
                    }
                    else {
                        storageManager.safeData(s);
                        switchAdapter.add(s);
                        switchAdapter.notifyDataSetChanged();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
