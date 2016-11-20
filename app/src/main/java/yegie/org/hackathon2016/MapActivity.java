package yegie.org.hackathon2016;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;

/**
 * Created by Kiera on 11/19/2016.
 */

public class MapActivity extends Activity {
    private GraphicsLayer gl=null;
    private MapView m1=null;

    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        DisplayMetrics displaymetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height=(int) ((1.0 / 10.0) * displaymetrics.heightPixels);
        int width=(int) ((1.0 / 3.0) * displaymetrics.widthPixels);

        long numMilliSeconds=(long) getIntent().getExtras().getFloat(SettingsActivity.GAME_LENGTH) * 60000;


        LocationManager lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Log.d("debug", "Provider enabled status is " + lm.isProviderEnabled(LocationManager.GPS_PROVIDER));


        Location location=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        LocationListener ll=new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                m1.centerAt(latitude,longitude,true);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

//        while (location==null)
//        {
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
//        }

        Log.d("debug", "Finished with location loop");

        latitude=location.getLatitude();
        longitude=location.getLongitude();

        Log.d("debug", "Latitude is " + latitude);
        Log.d("debug", "Longitude is " + longitude);

        setContentView(R.layout.activity_map);


        TextView t1=(TextView) findViewById(R.id.textView1);
        final TextView t2=(TextView) findViewById(R.id.textView2);
        TextView t3=(TextView) findViewById(R.id.textView3);

        t1.setLayoutParams(new LinearLayout.LayoutParams(width,height));
        t2.setLayoutParams(new LinearLayout.LayoutParams(width,height));
        t3.setLayoutParams(new LinearLayout.LayoutParams(width,height));

//        t1.setClickable(true);
//        t1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                populateMarkers();
//            }
//        });

        m1=(MapView) findViewById(R.id.map);

        gl=new GraphicsLayer();

        m1.addLayer(gl);

        //hardcode since GPS is slightly inaccurate for demo purposes
//        latitude=39.998361;
//        longitude=-83.00776;

        m1.centerAndZoom(latitude, longitude, .1f);
        m1.zoomin();

        t1.setText("Test updating UI");

        // For whatever reason the points can only be added to the map
        // after it's already initialized. Doing this
        // half a second later.
        //
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                populateMarkers();
            }
        },500);
    }

    private void populateMarkers() {
        Log.d("GEOGAME","ShowUserDot()");

        SimpleMarkerSymbol simpleMarker = new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.CIRCLE);

//        Point pointGeometry = new Point(longitude,latitude);

        Point pointGeometry = (Point) GeometryEngine.project(
                new Point(longitude,latitude),
                SpatialReference.create(SpatialReference.WKID_WGS84),
                m1.getSpatialReference());

        Graphic pointGraphic = new Graphic(pointGeometry, simpleMarker);
        gl.addGraphic(pointGraphic);

//        gl.setGraphicVisible(userDot,true);

        gl.addGraphic(pointGraphic);

//        CountDownTimer ct = new CountDownTimer(numMilliSeconds, 1000l) {
//
//            public void onTick(long millisUntilFinished) {
//                millisUntilFinished = millisUntilFinished - 1000l;
//
//                //Add an entra zero so time doesn't look weird if <10 seconds
//                StringBuilder str = new StringBuilder();
//                str.append(millisUntilFinished/1000/60);
//                str.append(":");
//                if ((((millisUntilFinished/1000)%60)<10))
//                {
//                    str.append("0");
//                }
//                str.append((millisUntilFinished/1000)%60);
//
//                t2.setText(str);
//            }
//
//            public void onFinish() {
//                t2.setText("Finished!");
//            }
//        }.start();
    }
}
