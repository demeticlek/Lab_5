package com.example.lab_8;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

/**
 * Base activity that provides Toolbar and Navigation Drawer.
 * Other activities extend this to avoid duplicating code.
 */
public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Set up the Toolbar as the ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the Navigation Drawer with hamburger toggle
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set listener for Navigation Drawer items
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Inflate the child activity's content into the FrameLayout
        getLayoutInflater().inflate(getContentLayoutId(),
                findViewById(R.id.content_frame));
    }

    /**
     * Each child activity must return its own content layout ID.
     */
    protected abstract int getContentLayoutId();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate toolbar menu with icons and overflow
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Show Toast for each toolbar item
        if (id == R.id.item_one) {
            Toast.makeText(this, "You clicked on item 1", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.item_two) {
            Toast.makeText(this, "You clicked on item 2", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.item_overflow) {
            Toast.makeText(this, "You clicked on About", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Go to MainActivity if not already there
            if (!(this instanceof MainActivity)) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        } else if (id == R.id.nav_dad_joke) {
            // Go to DadJokeActivity if not already there
            if (!(this instanceof DadJokeActivity)) {
                startActivity(new Intent(this, DadJokeActivity.class));
                finish();
            }
        } else if (id == R.id.nav_exit) {
            // Close all activities and exit
            finishAffinity();
        }

        // Close drawer after selection
        drawerLayout.closeDrawers();
        return true;
    }
}