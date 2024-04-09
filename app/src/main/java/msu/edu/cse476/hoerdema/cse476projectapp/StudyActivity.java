package msu.edu.cse476.hoerdema.cse476projectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;
import java.util.Locale;

public class StudyActivity extends AppCompatActivity {
    // for storing info in shared preferences
    private SharedPreferences settings = null;
    private final static String STARTTIME = "starttime";

    // all three of the following store milliseconds
    // time the studying started
    private long starttime;

    // variables for use when calculating total time -> get used/changed a lot so I made them member variables
    private long currenttime;
    private long timestudying;

    // used to format the time string displayed on screen
    String time = "";
    private String username = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        settings = getSharedPreferences(
                "my.app.packagename_preferences", Context.MODE_PRIVATE);

        starttime = SystemClock.elapsedRealtime();
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(STARTTIME, Long.toString(starttime));
        editor.apply();

        setTimeStudying();
    }

    // called when the 'Stop Studying' button pressed
    public void onStopStudying(View view) {
        // need to store the time studied as well
        // i figure we can store it in seconds maybe?
        // to get seconds its just:
        username = getIntent().getStringExtra("username");
        // currenttime = SystemClock.elapsedRealtime();
        // timestudying = currenttime - starttime;
        // int seconds = (int)timestudying / 1000;

        Intent intent = new Intent(this, LeaderBoardActivity.class);
        startActivity(intent);
    }

    // updates the onscreen timer with how long the student has been studying
    private void setTimeStudying(){

        TextView stopwatchView
                = (TextView)findViewById(
                R.id.textStopwatch);


        // after some research I settled on using a handler to perform the task of updating the time repeatedly
        final Handler handler
                = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                currenttime = SystemClock.elapsedRealtime();
                timestudying = currenttime - starttime;

                // time studying is in milliseconds so have to convert to seconds
                int studyseconds = (int)timestudying / 1000;

                // then use those seconds to calc hour, mins, secs
                int hours = studyseconds / 3600;
                int minutes = (studyseconds % 3600) / 60;
                int seconds = studyseconds % 60;

                // https://www.geeksforgeeks.org/java-string-format-method-with-examples/
                time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);

                stopwatchView.setText(time);

                // run this again in half a second to update the time
                handler.postDelayed(this, 500);
            }
        });
    }

    /**
     * Called when this application becomes foreground again.
     */
    @Override
    protected void onResume() {
        super.onResume();
        // pull the start time back from preferences so the stopwatch can be updated accurately
        starttime = Long.parseLong(settings.getString(STARTTIME, "0"));
        setTimeStudying();
    }

    /**
     * Called when this application is no longer the foreground application.
     */
    @Override
    protected void onPause() {
        // put this start start time in preferences so the stopwatch can be updated when resumed
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(STARTTIME, Long.toString(starttime));
        editor.apply();
        super.onPause();
    }
}