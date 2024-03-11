package msu.edu.cse476.hoerdema.cse476projectapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

public class LeaderBoardActivity extends AppCompatActivity {
    private SwitchCompat weeklySwitch;
    private RecyclerView leaderBoardEntries;
    private RecyclerView leaderBoardWeeklyEntries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);


        weeklySwitch = findViewById(R.id.leaderBoardWeeklyToggle);
        leaderBoardEntries = findViewById(R.id.leaderBoardEntries);
        leaderBoardWeeklyEntries = findViewById(R.id.leaderBoardWeeklyEntries);


        weeklySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (weeklySwitch.isChecked()){
                leaderBoardEntries.setVisibility(View.GONE);
                leaderBoardWeeklyEntries.setVisibility(View.VISIBLE);
            } else {
                leaderBoardEntries.setVisibility(View.VISIBLE);
                leaderBoardWeeklyEntries.setVisibility(View.GONE);
            }
        });

    }





}