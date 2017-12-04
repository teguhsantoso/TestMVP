package de.example.tsa.testmvp.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import de.example.tsa.testmvp.R;
import de.example.tsa.testmvp.services.Constants;

import static de.example.tsa.testmvp.services.Constants.PERMISSION_REQUEST_ACCESS_LOCATION;

public class ItemLocationActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     *
     * Permission for finding device location.
     * Location of any device can be determined by two ways:
     * 1. Using NETWORK_PROVIDER (provides better and accurate locations)
     *    Android permissions required for using this provider are either ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION.
     * 2. Using GPS_PROVIDER (less accurate locations)
     *    Android permissions required for using this provider is only ACCESS_FINE_LOCATION.
     *
     */

    private FusedLocationProviderClient mFusedLocationClient;
    private Location                    mLastLocation;
    private LocationRequest             mLocationRequest;
    private View                        rootLayout;
    private TextView                    textViewCurrentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_location);
        this.rootLayout = findViewById(R.id.root_layout);
        this.textViewCurrentPosition = findViewById(R.id.textViewCurrentPosition);
        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Find last device position.
        getLastLocationAndUpdatePosition();

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

    private void getLastLocationAndUpdatePosition() {
        findLastDevicePosition();
        startLocationUpdates();
    }

    private void findLastDevicePosition() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is missing and must be requested.
            requestAccessLocationPermissions();
            return;
        }

        mFusedLocationClient.getLastLocation().addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() == null) {
                            Snackbar.make(rootLayout, "No location detected.", Snackbar.LENGTH_SHORT).show();
                            return;
                        }

                        // Retrieve the last device position.
                        mLastLocation = task.getResult();
                        Snackbar.make(rootLayout, "Last location: " + mLastLocation.getLatitude() + ", " + mLastLocation.getLongitude(), Snackbar.LENGTH_SHORT).show();

                    }
                });
    }

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is missing and must be requested.
            requestAccessLocationPermissions();
            return;
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(Constants.UPDATE_INTERVAL_MILLIS);
        mLocationRequest.setFastestInterval(Constants.FASTEST_INTERVAL_MILLIS);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        }, Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        if (location == null) {
            return;
        }
        Log.d(Constants.LOGGER, ">>> Update listener device position: " + location.getLatitude() + ", " + location.getLongitude());
        this.textViewCurrentPosition.setText("Current position: " + location.getLatitude() + ", " + location.getLongitude());
    }

    private void requestAccessLocationPermissions() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
            Snackbar.make(rootLayout, "Coarse access is required to display the item position.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(ItemLocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_ACCESS_LOCATION);
                }
            }).show();
        } else {
            Snackbar.make(rootLayout, "Permission is not available. Requesting access coarse permission.", Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_ACCESS_LOCATION);
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
