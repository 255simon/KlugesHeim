package com.example.klugesheim;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SwitchAdapter extends ArrayAdapter<Switch> {
    private ArrayList<Switch> switches;
    private Context context;
    private StorageManager storageManager;

    public SwitchAdapter(Context context, ArrayList<Switch> switches, StorageManager storageManager) {
        super(context, 0, switches);
        this.switches = switches;
        this.context = context;
        this.storageManager = storageManager;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row,
                    parent, false);
        }

        TextView switchName = convertView.findViewById((R.id.switchName));
        switchName.setText(switches.get(position).getName());

        final Button onButton = convertView.findViewById(R.id.on_button);
        final Button offButton = convertView.findViewById(R.id.off_button);

        onButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection.sendMessage(switches.get(position).getOnCommand());
            }
        });

        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection.sendMessage(switches.get(position).getOffCommand());
            }
        });

        convertView.setOnTouchListener(new OnSwipeTouchListener(this.context){
            public void onSwipeLeft(){
                Switch removeSwitch = switches.get(position);
                storageManager.removeData(removeSwitch);
                switches.remove(removeSwitch);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
}
