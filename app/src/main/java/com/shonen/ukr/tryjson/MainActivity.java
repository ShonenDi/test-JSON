package com.shonen.ukr.tryjson;

import android.app.ListActivity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ListActivity {

    final static String JSON_DATA_URL = "https://jsonplaceholder.typicode.com/posts";
    private final String USER_ID_TAG = "id";
    private final String USER_POST_TITLE_TAG = "title";
    private final String USER_POST_BODY_TAG = "body";
    private ListView userPostList;

    private JSONArray jsonArray = null;
    ArrayList<HashMap<String, String>> usersPostsList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            new GetJsonData().execute(buildUrlFromUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

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
        String userPost = "";

        try {
            InputStream in = httpURLConnection.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while (line != null) {
                line = bf.readLine();
                result = result + line;
            }

        } finally {
            httpURLConnection.disconnect();

        }
        return result;
    }

    public class GetJsonData extends AsyncTask<URL, Void, Void> {

        @Override
        protected Void doInBackground(URL... urls) {
            URL url = urls[0];
            String jsonData = "";
            try {
                jsonData = httpConnectionResponse(url);
                getArrOfJsonObject(jsonData);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ListAdapter listAdapter = new SimpleAdapter(MainActivity.this, usersPostsList, R.layout.list_item,
                    new String[]{USER_ID_TAG, USER_POST_TITLE_TAG, USER_POST_BODY_TAG},
                    new int[]{R.id.txt_user_id, R.id.txt_user_post_title, R.id.txt_user_post_body});
            setListAdapter(listAdapter);
        }

    }

    public void getArrOfJsonObject(String result) throws JSONException {
        jsonArray = new JSONArray(result);
        int length = jsonArray.length();
        for (int i = 0; i < length; i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            String userId = jsonObject.getString(USER_ID_TAG);
            String userPostTitle = jsonObject.getString(USER_POST_TITLE_TAG);
            String userPostBody = jsonObject.getString(USER_POST_BODY_TAG);
            HashMap<String, String> userPosts = new HashMap<String, String>();
            userPosts.put(USER_ID_TAG, userId);
            userPosts.put(USER_POST_TITLE_TAG, userPostTitle);
            userPosts.put(USER_POST_BODY_TAG, userPostBody);
            usersPostsList.add(userPosts);

        }
    }
}