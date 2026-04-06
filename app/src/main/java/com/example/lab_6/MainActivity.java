package com.example.lab_6;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);

        new CatImages().execute();
    }

    class CatImages extends AsyncTask<String, Integer, String> {

        Bitmap currentBitmap = null;

        @Override
        protected String doInBackground(String... params) {
            while (true) {
                try {
                    // Step 1: Fetch JSON from API
                    String jsonResponse = fetchUrl("https://cataas.com/cat?json=true");
                    Log.d("CatAPI", "Response: " + jsonResponse);

                    if (jsonResponse == null) {
                        Thread.sleep(2000);
                        continue;
                    }

                    // Step 2: Parse JSON — try both "id" and "_id"
                    JSONObject json = new JSONObject(jsonResponse);
                    Log.d("CatAPI", "Keys: " + json.toString());

                    String id = null;
                    if (json.has("_id")) {
                        id = json.getString("_id");
                    } else if (json.has("id")) {
                        id = json.getString("id");
                    }

                    if (id == null) {
                        Log.e("CatAPI", "No id found in JSON: " + jsonResponse);
                        Thread.sleep(2000);
                        continue;
                    }

                    Log.d("CatAPI", "Cat ID: " + id);

                    // Step 3: Check if file already exists locally
                    File file = new File(getFilesDir(), id + ".jpg");

                    if (file.exists()) {
                        // Load from local storage
                        Log.d("CatAPI", "Loading from local file: " + file.getAbsolutePath());
                        currentBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    } else {
                        // Download and save image
                        String imageUrl = "https://cataas.com/cat/" + id;
                        Log.d("CatAPI", "Downloading: " + imageUrl);

                        URL imgUrl = new URL(imageUrl);
                        HttpURLConnection imgConn = (HttpURLConnection) imgUrl.openConnection();
                        imgConn.setConnectTimeout(8000);
                        imgConn.setReadTimeout(15000);
                        imgConn.setInstanceFollowRedirects(true);

                        InputStream imgStream = imgConn.getInputStream();
                        FileOutputStream fos = new FileOutputStream(file);
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = imgStream.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                        fos.close();
                        imgStream.close();

                        currentBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        Log.d("CatAPI", "Download complete, bitmap: " + currentBitmap);
                    }

                    // Step 4: Progress bar countdown (~3 seconds)
                    for (int i = 0; i < 100; i++) {
                        publishProgress(i);
                        Thread.sleep(30);
                    }

                } catch (Exception e) {
                    Log.e("CatAPI", "Error: " + e.getMessage());
                    try { Thread.sleep(2000); } catch (Exception ignored) {}
                }
            }
        }

        // Helper method to fetch a URL as a String
        private String fetchUrl(String urlString) {
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(8000);

                InputStream stream = conn.getInputStream();
                StringBuilder sb = new StringBuilder();
                int ch;
                while ((ch = stream.read()) != -1) sb.append((char) ch);
                stream.close();
                return sb.toString();
            } catch (Exception e) {
                Log.e("CatAPI", "fetchUrl error: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);

            // Show the new cat image at the START of each cycle
            if (values[0] == 1 && currentBitmap != null) {
                imageView.setImageBitmap(currentBitmap);
            }
        }
    }
}