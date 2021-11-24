package com.example.shovkanlab1.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.shovkanlab1.R;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SplashActivity extends AppCompatActivity {
    List<ToggleButton> button_list;
    ToggleButton toggleButtonOne, toggleButtonTwo, toggleButtonThree, toggleButtonFour, toggleButtonFive,toggleButtonSix,toggleButtonSeven,toggleButtonEight;
    TextView textViewTask;
    int first_button, second_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        first_button = (int)(Math.random() * 7) + 1;
        second_button = (int)(Math.random() * 8) + 1;

        while(first_button == second_button){
            second_button = (int)(Math.random() * 8)+ 1;
        }

        if(first_button > second_button){
            int temp = second_button;
            second_button = first_button;
            first_button = temp;
        }
        toggleButtonOne = findViewById(R.id.toggleButtonOne);
        toggleButtonTwo = findViewById(R.id.toggleButtonTwo);
        toggleButtonThree = findViewById(R.id.toggleButtonThree);
        toggleButtonFour = findViewById(R.id.toggleButtonFour);
        toggleButtonFive = findViewById(R.id.toggleButtonFive);
        toggleButtonSix = findViewById(R.id.toggleButtoSix);
        toggleButtonSeven = findViewById(R.id.toggleButtonSeven);
        toggleButtonEight = findViewById(R.id.toggleButtonEight);

        button_list = new LinkedList<>();

        button_list.add(toggleButtonOne);
        button_list.add(toggleButtonTwo);
        button_list.add(toggleButtonThree);
        button_list.add(toggleButtonFour);
        button_list.add(toggleButtonFive);
        button_list.add(toggleButtonSix);
        button_list.add(toggleButtonSeven);
        button_list.add(toggleButtonEight);

        resetValue();

        textViewTask = findViewById(R.id.textViewTask);
        textViewTask.setText("Нажмите " + first_button + " и " + second_button + " кнопки");
    }

    private void resetValue() {

        int index = 1;
        for(ToggleButton button : button_list){
            button.setTextOff(" "+index);
            if(index == first_button || index == second_button){
                button.setTextOn("V");
            }
            else{
                button.setTextOn("X");
            }
            button.setText(" "+index);
            button.setOnClickListener(v -> check());
            index++;
        }
    }

    private void check() {
        int index = 1;
        for(ToggleButton button : button_list){
            if(index == second_button || index == first_button){
                if(!button.isChecked()){
                    return;
                }
            }
            index++;
        }

        index = 1;
        for(ToggleButton button : button_list){
            if(index != second_button && index != first_button){
                if(button.isChecked()){
                    return;
                }
            }
            index++;
        }

        openSettingsActivity();

    }


    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}