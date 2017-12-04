package de.example.tsa.testmvp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import de.example.tsa.testmvp.R;

import static de.example.tsa.testmvp.services.Constants.PERMISSION_REQUEST_ACCESS_LOCATION;

public class ItemLocationActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private FusedLocationProviderClient mFusedLocationClient;
    private Location                    mLastLocation;
    private View                        rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_location);
        this.rootLayout = findViewById(R.id.root_layout);
        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLastLocation();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            findLastDevicePosition();
        } else {
            // Permission is missing and must be requested.
            requestAccessLocationPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void findLastDevicePosition() {
        mFusedLocationClient.getLastLocation().addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            Snackbar.make(rootLayout, "Last location: " + mLastLocation.getLongitude() + ", " + mLastLocation.getLatitude(), Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(rootLayout, "No location detected.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void requestAccessLocationPermissions() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
            Snackbar.make(rootLayout, "Coarse access is required to display the item position.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(ItemLocationActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_ACCESS_LOCATION);
                }
            }).show();
        } else {
            Snackbar.make(rootLayout, "Permission is not available. Requesting access coarse permission.", Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_ACCESS_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // Permission request was denied.
                Snackbar.make(rootLayout, "Coarse access permission request was denied.", Snackbar.LENGTH_SHORT).show();
            } else {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(rootLayout, "Coarse access permission was granted. Starting find last device position.", Snackbar.LENGTH_SHORT).show();
                findLastDevicePosition();
            }
        }
    }
}
