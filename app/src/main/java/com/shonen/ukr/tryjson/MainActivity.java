package com.shonen.ukr.tryjson;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

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
        String result = "";
        String data = "";
        String parseData = "";
        try {
            InputStream in = httpURLConnection.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = bf.readLine())!=null){
                sb.append(line + "\n");
                data = sb.toString();
                result = getSingJsonObject(data);
                parseData = parseData + result +"\n";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();

        }
        return parseData;
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
            txtData.append(jsonData +"\n");
        }
    }
    public static String getSingJsonObject(String result) throws JSONException {
        String jsonObj = "";
        JSONObject userPost = new JSONObject(result);
        int useNumberId = userPost.getInt("id");
        String userPostTitle = userPost.getString("title");
        String userPostBody = userPost.getString("body");

        jsonObj = "User Id: " + useNumberId + "\n"
                + "Title: " + userPostTitle + "\n"
                + "Post body: " + userPostBody + "\n";
        return jsonObj;
    }
}