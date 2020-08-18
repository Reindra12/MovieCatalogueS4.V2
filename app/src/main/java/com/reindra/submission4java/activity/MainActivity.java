package com.reindra.submission4java.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.reindra.submission4java.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView title = findViewById(R.id.tv_titlebar);
        title.setText(R.string.title_movie);
        ImageView language = findViewById(R.id.imgsetting);
        language.setOnClickListener(this);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgsetting:
//                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                Intent intent = new Intent(this, NotificationActivity.class);
                startActivity(intent);
                break;
        }
    }
}