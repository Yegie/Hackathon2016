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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;

/**
 * Created by Kiera on 11/19/2016.
 */

public class MapActivity extends Activity {

    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = (int) ((1.0 / 10.0) * displaymetrics.heightPixels);
        int width = (int) ((1.0 / 3.0) * displaymetrics.widthPixels);

        long numMilliSeconds = (long) getIntent().getExtras().getFloat(SettingsActivity.GAME_LENGTH) * 60000;


        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Log.d("debug", "Provider enabled status is " + lm.isProviderEnabled(LocationManager.GPS_PROVIDER));


        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        LocationListener ll = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();


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


        while (location == null) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
        }

        Log.d("debug", "Finished with location loop");

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        Log.d("debug", "Latitude is " + latitude);
        Log.d("debug", "Longitude is " + longitude);

        setContentView(R.layout.activity_map);


        TextView t1 = (TextView) findViewById(R.id.textView1);
        final TextView t2 = (TextView) findViewById(R.id.textView2);
        TextView t3 = (TextView) findViewById(R.id.textView3);
        MapView m1 = (MapView) findViewById(R.id.map);
        GraphicsLayer gl = new GraphicsLayer();

        //hardcode since GPS is slightly inaccurate for demo purposes
        latitude = 39.998361;
        longitude = -83.00776;

        m1.centerAndZoom(latitude, longitude, .1f);

        t1.setText("Test updating UI");

        SimpleMarkerSymbol simpleMarker = new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.CIRCLE);
        Point pointGeometry = new Point(latitude, longitude);
        Graphic pointGraphic = new Graphic(pointGeometry, simpleMarker);


        int res1 = gl.addGraphic(pointGraphic);

        gl.setGraphicVisible(res1, true);
        gl.bringToFront(res1);
        int userDot = m1.addLayer(gl);


        t1.setLayoutParams(new LinearLayout.LayoutParams(width,height));
        t2.setLayoutParams(new LinearLayout.LayoutParams(width,height));
        t3.setLayoutParams(new LinearLayout.LayoutParams(width,height));


        CountDownTimer ct = new CountDownTimer(numMilliSeconds, 1000l) {

            public void onTick(long millisUntilFinished) {
                millisUntilFinished = millisUntilFinished - 1000l;

                //Add an entra zero so time doesn't look weird if <10 seconds
                StringBuilder str = new StringBuilder();
                str.append(millisUntilFinished/1000/60);
                str.append(":");
                if ((((millisUntilFinished/1000)%60)<10))
                {
                    str.append("0");
                }
                str.append((millisUntilFinished/1000)%60);

                t2.setText(str);
            }

            public void onFinish() {
                t2.setText("Finished!");
            }
        }.start();


    }
}
