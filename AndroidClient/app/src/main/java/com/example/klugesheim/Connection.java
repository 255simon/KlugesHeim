package com.example.klugesheim;

import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection {

    private static String host = "192.168.2.200";
    private static int port = 2048;

    public static void sendMessage(final String msg){
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

    public static Switch receiveMessage(View view) throws InterruptedException {

        TextInputLayout textInput = view.findViewById(R.id.name_input_layout);
        final Switch s = new Switch();
        s.setName(textInput.getEditText().getText().toString());
        textInput.getEditText().getText().clear();

        Thread thread = new Thread(new Runnable(){

            @Override
            public void run() {

                try {
                    Socket sock = new Socket(host, port);
                    OutputStream outStream = sock.getOutputStream();
                    PrintWriter output = new PrintWriter(outStream);
                    output.print("scan");
                    output.flush();
                    BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                    String command = in.readLine();
                    s.setCommand(command);
                    sock.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        thread.start();
        thread.join();

        return s;
    }
}
