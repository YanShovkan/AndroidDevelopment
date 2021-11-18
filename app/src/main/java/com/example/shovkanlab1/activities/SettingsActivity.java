package com.example.shovkanlab1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.shovkanlab1.R;

public class SettingsActivity extends AppCompatActivity {

    Button buttonBD, buttonFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        buttonBD = findViewById(R.id.buttonDB);
        buttonFiles = findViewById(R.id.buttonPreferences);

        buttonBD.setOnClickListener(v -> DBSet());

        buttonFiles.setOnClickListener(v -> PreferencesSet());
    }

    private void DBSet() {
        openMainActivity("DB");
    }

    private void PreferencesSet() {
        openMainActivity("Preferences");
    }

    public void openMainActivity(String props)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("save_props", props);
        startActivity(intent);
    }
}