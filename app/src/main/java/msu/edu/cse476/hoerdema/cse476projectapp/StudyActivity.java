package msu.edu.cse476.hoerdema.cse476projectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StudyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
    }

    public void onStopStudying(View view) {
        // need to store the time studied as well
        Intent intent = new Intent(this, LeaderBoardActivity.class);
        startActivity(intent);
    }
}