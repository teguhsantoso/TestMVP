package de.example.tsa.testmvp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static de.example.tsa.testmvp.services.Constants.PERMISSION_REQUEST_ACCESS_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private View            rootLayout;
    private TextView        textViewCurrentPosition;
    private GoogleMap       mMap;
    private                 FusedLocationProviderClient mFusedLocationClient;
    private                 Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        this.rootLayout = findViewById(R.id.root_layout);

        this.textViewCurrentPosition = findViewById(R.id.textViewCurrentPosition);

        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Find last device position.
        //getLastLocationAndUpdatePosition();

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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setTiltGesturesEnabled(true);

        // Try finding last position of device.
        getLastLocationAndUpdatePosition();

    }

    private void getLastLocationAndUpdatePosition() {
        findLastDevicePosition();
        //startLocationUpdates();
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
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_ACCESS_LOCATION);
                }
            }).show();
        } else {
            Snackbar.make(rootLayout, "Permission is not available. Requesting access coarse permission.", Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_ACCESS_LOCATION);
        }
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
                textViewCurrentPosition.setText("Last item position: " + mLastLocation.getLatitude() + ", " + mLastLocation.getLongitude());

                // Refresh fragment map and put marker.
                LatLng itemPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(itemPosition, 16);
                mMap.addMarker(new MarkerOptions().position(itemPosition).title("Item Position"));
                mMap.moveCamera(cameraPosition);
                mMap.animateCamera(cameraPosition);

            }
        });
    }

}
