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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button scanButton = findViewById(R.id.scan_button);

        scanButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View buttonView){
                receiveMessage();
            }
        });
        ListView listView = (ListView) findViewById(R.id.listview_activity_main);
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("whats");
        arrayList.add("up");
        ArrayAdapter<String> myArrayAdapter =
                new ArrayAdapter<>(
                  this,
                  R.layout.list_row,
                  R.id.textView,
                  arrayList);
        listView.setAdapter(myArrayAdapter);
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

    private String host = "";    //insert server ip here
    private int port = 2048;
    BufferedReader in;
}
