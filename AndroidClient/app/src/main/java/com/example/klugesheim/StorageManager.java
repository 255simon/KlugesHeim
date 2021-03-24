package com.example.klugesheim;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class StorageManager {

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private HashSet<String> listElData;
    private String listElDataKey = "data_key";

    public StorageManager(SharedPreferences sharedPref){
        this.sharedPref = sharedPref;
        this.editor = sharedPref.edit();
        this.listElData = new HashSet<String>();
    }

    public void safeData(Switch s){
        listElData.add(s.getName() + "/" + s.getCommand());
        editor.putStringSet(listElDataKey, listElData);
        editor.apply();
    }

    public ArrayList<Switch> loadData(){
        ArrayList<Switch> switchList = new ArrayList<Switch>();
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
        return switchList;
    }

    public void removeData(Switch s){
        listElData.remove(s.getName() + "/" + s.getCommand());
        editor.putStringSet(listElDataKey, listElData);
        editor.apply();
    }
}
