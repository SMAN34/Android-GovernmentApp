package com.manderfield.knowyourgovernment;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import static android.content.Context.LOCATION_SERVICE;
public class LocateActivity {
    private MainActivity main;
    private LocationManager LM;
    private LocationListener LL;

    public LocateActivity(MainActivity main) {
        this.main = main;

        if (checkPermission()) {
            SetUp();
            determineLocation();
        }
    }

    public void determineLocation() {
        if (!checkPermission())
            return;

        if (LM == null)
            SetUp();

        if (LM != null) {
            Location loc = LM.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (loc != null) {
                main.setMainLocation(loc.getLatitude(), loc.getLongitude());
                return;
            }
        }

        if (LM != null) {
            Location loc = LM.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (loc != null) {
                main.setMainLocation(loc.getLatitude(), loc.getLongitude());
                return;
            }
        }

        if (LM != null) {
            Location loc = LM.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null) {
                main.setMainLocation(loc.getLatitude(), loc.getLongitude());
                return;
            }
        }

        return;
    }

    private void SetUp() {
        if (LM != null)
            return;

        if (!checkPermission())
            return;

        LM = (LocationManager) main.getSystemService(LOCATION_SERVICE);

        LL = new LocationListener() {
            public void onLocationChanged(Location location) {
             //  Toast.makeText(main, "Update from " + location.getProvider(), Toast.LENGTH_SHORT).show();
                Log.d("get position", ""+location.getLatitude()+location.getLongitude());
                main.setMainLocation(location.getLatitude(), location.getLongitude());
                main.networkwarningClose();

            }
        };

        LM.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000, 0, LL);
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(main, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(main,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
            return false;
        }
        return true;
    }
}