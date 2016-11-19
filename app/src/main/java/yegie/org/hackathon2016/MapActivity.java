package yegie.org.hackathon2016;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.app.Activity;
import android.os.Bundle;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esri.android.map.MapView;

import static yegie.org.hackathon2016.R.layout.activity_map;

/**
 * Created by Kiera on 11/19/2016.
 */

public class MapActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = (int) ((1.0/10.0) * displaymetrics.heightPixels);
        int width = (int) ((1.0/3.0) * displaymetrics.widthPixels);





//if not equal, permission has been granted
        boolean permissionCheck = ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION")!= PackageManager.PERMISSION_GRANTED;
        int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 0;

        Log.d("degug", "trying to get permission");
        if (!permissionCheck) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }

        Log.d("debug", "ACCESS_FINE_LOCATION is " +  MY_PERMISSIONS_REQUEST_FINE_LOCATION);


        while (!permissionCheck){

            Log.d("debug", "checking permission" );
            permissionCheck = (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION")!= PackageManager.PERMISSION_GRANTED);
        }

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Location location = lm.getLastKnownLocation(Context.LOCATION_SERVICE);

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();





            setContentView(activity_map);

            TextView t1 =(TextView) findViewById(R.id.textView1);
            TextView t2 =(TextView) findViewById(R.id.textView2);
            TextView t3 =(TextView) findViewById(R.id.textView3);
            MapView m1 = (MapView) findViewById(R.id.map);


            m1.centerAt(latitude, longitude, true);

            t1.setLayoutParams(new LinearLayout.LayoutParams(width,height));
            t2.setLayoutParams(new LinearLayout.LayoutParams(width,height));
            t3.setLayoutParams(new LinearLayout.LayoutParams(width,height));

        }




    }

//    private final LocationListener locationListener = new LocationListener() {
//        public void onLocationChanged(Location location) {
//            longitude = location.getLongitude();
//            latitude = location.getLatitude();
//        }
//    }



