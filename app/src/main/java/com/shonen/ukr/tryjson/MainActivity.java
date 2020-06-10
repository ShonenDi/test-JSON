package com.shonen.ukr.tryjson;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    final static String JSON_DATA_URL = "https://jsonplaceholder.typicode.com/posts";
    private TextView txtData;
    private Button btbGetData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public URL buildUrlFromUri(String ureUri) throws MalformedURLException {
        Uri buildUri = Uri.parse(JSON_DATA_URL).buildUpon().build();
        URL url=null;
        url = new URL(buildUri.toString());
        return url;
    }
}