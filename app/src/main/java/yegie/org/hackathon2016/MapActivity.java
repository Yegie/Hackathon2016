package yegie.org.hackathon2016;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
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
    public static double MAP_LAT_START=39.998361;
    public static double MAP_LNG_START=-83.00776;
    public static float CONST_OF_RAND=0.000987f;

    private GraphicsLayer gl=null;
    private MapView m1=null;
    int[] idsOfCoins;
    boolean[] coinReal;
    int numOfCoins = 30;

    double latitude, longitude;
    private int userUid, coinsCollected = 0;
    boolean ditributedPoints = false;

    Handler mHandler=new Handler();
    private Runnable walker;
    int stepCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Create displaymetrics to find screen width and height
        DisplayMetrics displaymetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        final int height=(int) ((1.0 / 10.0) * displaymetrics.heightPixels);
        final int width=(int) ((1.0 / 3.0) * displaymetrics.widthPixels);

        long numMilliSeconds=(long) getIntent().getExtras().getFloat(SettingsActivity.GAME_LENGTH) * 60000;

        //Make location manager to figure out location
        LocationManager lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Create listener for location
        final LocationListener ll = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                TextView t3=(TextView) findViewById(R.id.textView3);
                t3.setLayoutParams(new LinearLayout.LayoutParams(width,height));
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                if (m1 != null) {
                    m1.centerAt(latitude, longitude, true);

                    Point point = (Point) GeometryEngine.project(
                            new Point(longitude,latitude),
                            SpatialReference.create(SpatialReference.WKID_WGS84),
                            m1.getSpatialReference());

                    gl.updateGraphic(userUid, point);
                    if(!ditributedPoints){
                        ditributedPoints = true;
                        distributePoints(numOfCoins);
                    } else {
                        for(int i = 0; i < numOfCoins; ++i){
                            if(coinReal[i]) {
                                Point cur = (Point) gl.getGraphic(idsOfCoins[i]).getGeometry();
                                final float closeConst = 0.000009f;
                                if ((cur.getY() - latitude) * (cur.getY() - latitude) < closeConst
                                        && (cur.getX() - longitude) * (cur.getX() - longitude) < closeConst) {
                                    coinReal[i] = false;
                                    coinsCollected++;
                                    t3.setText("Coins: " + coinsCollected);
                                    gl.removeGraphic(idsOfCoins[i]);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                //Do nothing
            }

            @Override
            public void onProviderEnabled(String s) {
                //Do nothing
            }

            @Override
            public void onProviderDisabled(String s) {
                //Do nothing
            }
        };

        if(true) {//this is a debug thing the can simulate movement
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
        }
        else {
            walker=new Runnable() {
            final static double RADIUS=0.0002;
            final static double STEP=Math.PI/100;


            double angle=0;

            double clat=MAP_LAT_START;
            double clng=MAP_LNG_START;

            @Override
            public void run() {
                double lat=clat + RADIUS * Math.sin(angle);
                double lng=clng + RADIUS * Math.cos(angle);

                angle+=STEP;

                Location location=new Location("fake");
                location.setLatitude(lat);
                location.setLongitude(lng);

                ll.onLocationChanged(location);

                mHandler.postDelayed(walker,200);
            }
        };
        mHandler.postDelayed(walker,200);
    }

        //Set the screen to the layout we made
        setContentView(R.layout.activity_map);

        //Assign text fields and tell them where to go, map view, graphics layer for dots
        TextView t1=(TextView) findViewById(R.id.textView1);
        final TextView t2=(TextView) findViewById(R.id.textView2);

        t1.setLayoutParams(new LinearLayout.LayoutParams(width,height));
        t2.setLayoutParams(new LinearLayout.LayoutParams(width,height));

        m1=(MapView) findViewById(R.id.map);

        gl=new GraphicsLayer();

        m1.addLayer(gl);

        //Doesn't work correctly but hypothetically should zoom
        m1.centerAndZoom(latitude, longitude, .1f);
        m1.zoomin();


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

        //Create timer object and update text field 2 with time left
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

        steps(t1);
    }

    //Method to put 30 dots within ~1 Mile radius
    private void distributePoints(int n){

        SimpleMarkerSymbol coinMarker = new SimpleMarkerSymbol(Color.MAGENTA, 8, SimpleMarkerSymbol.STYLE.CIRCLE);

        idsOfCoins = new int[n];
        coinReal = new boolean[n];

        for(int i = 0; i < n; ++i)
        {
            float xCord = (float)longitude + (float)(Math.random()*2-1)*CONST_OF_RAND;
            float yCord = (float)latitude + (float)(Math.random()*2-1)*CONST_OF_RAND;

            Point pointGeometry = (Point) GeometryEngine.project(
                    new Point(xCord,yCord),
                    SpatialReference.create(SpatialReference.WKID_WGS84),
                    m1.getSpatialReference());

            Graphic pointGraphic = new Graphic(pointGeometry, coinMarker);
            idsOfCoins[i] = gl.addGraphic(pointGraphic);
            coinReal[i] = true;

        }
    }

    //Puts markers on map
    private void populateMarkers() {
        Log.d("GEOGAME","ShowUserDot()");

        SimpleMarkerSymbol simpleMarker = new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.CIRCLE);

        Point pointGeometry = (Point) GeometryEngine.project(
                new Point(longitude,latitude),
                SpatialReference.create(SpatialReference.WKID_WGS84),
                m1.getSpatialReference());

        Graphic pointGraphic = new Graphic(pointGeometry, simpleMarker);
        userUid = gl.addGraphic(pointGraphic);
    }

    //Counts steps and updates t1 with step count
    public void steps(final TextView t1) {

        SensorManager sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Step Counter
        sManager.registerListener(new SensorEventListener() {

                                      @Override
                                      public void onSensorChanged(SensorEvent event) {
                                          float steps = event.values[0];
                                          stepCount++;
                                          t1.setText("Steps: " + stepCount);
                                      }

                                      @Override
                                      public void onAccuracyChanged(Sensor sensor, int accuracy) {
                                          //Do nothing
                                      }
                                  }, sManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                SensorManager.SENSOR_DELAY_UI);

        // Step Detector
        sManager.registerListener(new SensorEventListener() {

                                      @Override
                                      public void onSensorChanged(SensorEvent event) {
                                          //Do nothing
                                      }

                                      @Override
                                      public void onAccuracyChanged(Sensor sensor, int accuracy) {
                                          //Do nothing
                                      }
                                  }, sManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),
                SensorManager.SENSOR_DELAY_UI);
    }
}
