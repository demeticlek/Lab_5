package com.example.lab_8;

/**
 * Main screen of the app. Extends BaseActivity to inherit
 * the Toolbar and Navigation Drawer functionality.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected int getContentLayoutId() {
        // Load the main page content layout
        return R.layout.content_main;
    }
}
