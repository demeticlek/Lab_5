package com.example.lab_8;
/**
 * Displays a dad joke in a TextView. Extends BaseActivity
 * to inherit the Toolbar and Navigation Drawer.
 */
public class DadJokeActivity extends BaseActivity {

    @Override
    protected int getContentLayoutId() {
        // Load the dad joke content layout
        return R.layout.content_dad_joke;
    }
}