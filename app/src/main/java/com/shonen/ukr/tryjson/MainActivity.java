package com.shonen.ukr.tryjson;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    final static String JSON_DATA_URL = "https://jsonplaceholder.typicode.com/posts";
    private TextView txtData;
    private Button btbGetData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtData = findViewById(R.id.txt_Json_data);
        btbGetData = findViewById(R.id.btb_get_data);
        btbGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new GetJsonData().execute(buildUrlFromUri());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public URL buildUrlFromUri() throws MalformedURLException {
        Uri buildUri = Uri.parse(JSON_DATA_URL).buildUpon().build();
        URL url = null;
        url = new URL(buildUri.toString());
        return url;
    }

    public String httpConnectionResponse(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        String data= "";
        try {
            InputStream in = httpURLConnection.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            String line = "";
            
            while (line!=null){
                line = bf.readLine();
                data = data + line;
            }
        } finally {
            httpURLConnection.disconnect();
        }
        return data;
    }

    public class GetJsonData extends AsyncTask<URL,Void,String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String jsonData = "";
            try {
               jsonData = httpConnectionResponse(url);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            txtData.append(jsonData +"\\n");
        }
    }
}