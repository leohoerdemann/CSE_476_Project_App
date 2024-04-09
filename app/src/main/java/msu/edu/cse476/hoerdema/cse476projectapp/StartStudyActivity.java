package msu.edu.cse476.hoerdema.cse476projectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import android.location.Location;
import android.widget.Toast;

public class StartStudyActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_study);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        username = getIntent().getStringExtra("username");
    }

    public void onStartStudying(View view) {
        // code to check location may go here !
        // or might be better idea to have that code in the study activity ?
        // ^ if we want to repeatedly check while they are studying that they are still in a building
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location.
                    if (location != null) {
                        // Check if the location is within the required radius of any of the given coordinates.
                        if (isValidLocation(location)) {
                            Intent intent = new Intent(StartStudyActivity.this, StudyActivity.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                        } else {
                            Toast.makeText(StartStudyActivity.this, "You are not in a valid location to start studying.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        } else {
            Toast.makeText(this, "Location permission is required to use this feature.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSeeLeaderboard(View view) {
        Intent intent = new Intent(this, LeaderBoardActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
            } else {
                // Permission denied.
            }
        }
    }

    private boolean isValidLocation(Location currentLocation) {
        float[] results = new float[1];
        // Check each valid location.
        Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), 42.731209, -84.482338, results);
        if (results[0] < 1000)
            return true; // 1000 is a test distance in meters. This can be changed if it doesn't work.

        Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), 42.729070, -84.480390, results);
        if (results[0] < 1000) return true; // Check for Computer Center.

        Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), 42.727930, -84.482770, results);
        if (results[0] < 1000) return true; // Check for STEM Building.

        return false;
    }
}