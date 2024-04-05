package msu.edu.cse476.hoerdema.cse476projectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartStudyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_study);
    }

    public void onStartStudying(View view) {
        // code to check location may go here !
        // or might be better idea to have that code in the study activity ?
        // ^ if we want to repeatedly check while they are studying that they are still in a building
        Intent intent = new Intent(this, StudyActivity.class);
        startActivity(intent);
    }

    public void onSeeLeaderboard(View view) {
        Intent intent = new Intent(this, LeaderBoardActivity.class);
        startActivity(intent);
    }
}