package com.example.klugesheim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class SwitchAdapter extends ArrayAdapter<Switch> {
    private ArrayList<Switch> switches;


    public SwitchAdapter(Context context, ArrayList<Switch> switches) {
        super(context, 0, switches);
        this.switches = switches;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        int phaseIndex = position;
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
                sendMessage(switches.get(position).getOnCommand());
            }
        });

        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(switches.get(position).getOffCommand());
            }
        });
        return convertView;
    }

    private void sendMessage(final String msg) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Socket sock = new Socket(MainActivity.getHost(), MainActivity.getPort());
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
}
