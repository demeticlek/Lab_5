package com.example.lab_6;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<JSONObject> characterList = new ArrayList<>();
    private CharacterAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.characterListView);
        adapter = new CharacterAdapter();
        listView.setAdapter(adapter);

        new FetchCharactersTask().execute();
        listView.setOnItemClickListener((parent, view, position, id) -> {
            try {
                JSONObject character = characterList.get(position);

                Bundle bundle = new Bundle();
                bundle.putString("name", character.getString("name"));
                bundle.putString("height", character.getString("height"));
                bundle.putString("mass", character.getString("mass"));
                bundle.putString("birth_year", character.getString("birth_year"));

                // FrameLayout check
                View frameLayout = findViewById(R.id.fragmentContainer);

                if (frameLayout != null) {
                    // TABLET: Fragment
                    DetailsFragment fragment = new DetailsFragment();
                    fragment.setArguments(bundle);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, fragment)
                            .commit();
                } else {
                    // Phone: EmptyActivity
                    android.content.Intent intent = new android.content.Intent(MainActivity.this, EmptyActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    //  AsyncTask: API
    private class FetchCharactersTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://swapi.dev/api/people/?format=json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                return result.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String responseText) {
            if (responseText != null) {
                try {
                    JSONArray characters = new JSONObject(responseText).getJSONArray("results");
                    for (int i = 0; i < characters.length(); i++) {
                        characterList.add(characters.getJSONObject(i));
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //  BaseAdapter: ListView'
    private class CharacterAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return characterList.size();
        }

        @Override
        public JSONObject getItem(int position) {
            return characterList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView;
            if (convertView == null) {
                textView = new TextView(MainActivity.this);
                textView.setTextSize(20);
                textView.setPadding(16, 16, 16, 16);
            } else {
                textView = (TextView) convertView;
            }

            try {
                String name = getItem(position).getString("name");
                textView.setText(name);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return textView;
        }
    }
}

