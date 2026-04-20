package com.example.lab_6;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();

        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(extras);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}