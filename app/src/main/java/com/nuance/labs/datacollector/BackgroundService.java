package com.nuance.labs.datacollector;

import android.Manifest;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BackgroundService extends Service {
    private static final String TAG = "BackgroundService";
    private static final int LOCATION_INTERVAL = 0;//1000;
    private static final float LOCATION_DISTANCE = 0;//50f;
    private LocationManager mLocationManager = null;


    private class MyPhoneStateListener extends PhoneStateListener {
        private final Location location;
        private final int networkType;
        protected SignalStrength signalStrength;

        public MyPhoneStateListener(Location location, int networkType){
            this.location = location;
            this.networkType = networkType;
        }

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            this.signalStrength = signalStrength;
            Log.i(TAG,NetworkType.fromInt(networkType).name() + " - Signal quality: " + signalStrength.getLevel() + " at location " + location.toString());
            try {
                File storageFile = getStorageFile();
                FileOutputStream fileOutputStream = new FileOutputStream(storageFile,true);
                PrintStream printStream = new PrintStream(fileOutputStream);
                printStream.print(location.getLongitude()+";"+location.getLatitude()+";"+location.getAltitude()+";"+location.getTime()+";"+signalStrength.getLevel() + ";" +NetworkType.fromInt(networkType).name() +"\n");
                fileOutputStream.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }

        }
    }

    public File getStorageFile() throws IOException {
        // Get the directory for the user's public pictures directory.
        DateFormat dateFormat = new SimpleDateFormat("yyyy_mm_dd");

        //to convert Date to String, use format method of SimpleDateFormat class.
        String strDate = dateFormat.format(new Date());
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), strDate + ".cvs");
        if (!file.exists()) {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file,true);
            PrintStream printStream = new PrintStream(fileOutputStream);
            printStream.print("longitude;latitude;altitude;timestamp;signal_strength;network_type\n");
            fileOutputStream.close();

        }
        return file;
    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.i(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            mLastLocation.set(location);
            TelephonyManager mTelephonyManager= (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            MyPhoneStateListener phoneStateListener = new MyPhoneStateListener(location,mTelephonyManager.getNetworkType());
            mTelephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
            mTelephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i(TAG, "onStatusChanged: " + provider);
        }
    }


    private LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER),
            new LocationListener(LocationManager.PASSIVE_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    mLocationListeners[0]
            );
        } catch (java.lang.SecurityException ex) {
            Log.e(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.e(TAG, "network provider does not exist, " + ex.getMessage());
        }

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationManager.removeUpdates(mLocationListeners[0]);
    }

    private void initializeLocationManager() {
        Log.i(TAG, "initializeLocationManager - LOCATION_INTERVAL: "+ LOCATION_INTERVAL + " LOCATION_DISTANCE: " + LOCATION_DISTANCE);
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}
