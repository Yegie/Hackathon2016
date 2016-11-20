package yegie.org.hackathon2016;

import android.app.Activity;
import android.content.Context;
import android.location.GpsStatus;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import static yegie.org.hackathon2016.R.layout.activity_map;

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
        int height = (int) ((1.0/10.0) * displaymetrics.heightPixels);
        int width = (int) ((1.0/3.0) * displaymetrics.widthPixels);


        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Log.d("debug", "Provider enabled status is " + lm.isProviderEnabled(LocationManager.GPS_PROVIDER));


        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);


     //   LocationListener ll = new MyLocationListener();





        if (location==null)
        {
       //     lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new android.location.LocationListener());
        }

        Log.d("debug", "Finished with location loop");

        latitude = location.getLatitude();
        longitude = location.getLongitude();


        setContentView(activity_map);

        TextView t1 =(TextView) findViewById(R.id.textView1);
        TextView t2 =(TextView) findViewById(R.id.textView2);
        TextView t3 =(TextView) findViewById(R.id.textView3);
        MapView m1 = (MapView) findViewById(R.id.map);
        GraphicsLayer gl = new GraphicsLayer();

        m1.centerAt(latitude, longitude, true);

        SimpleMarkerSymbol simpleMarker = new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.CIRCLE);

        Point pointGeometry = new Point(latitude,longitude);

        Graphic pointGraphic = new Graphic(pointGeometry, simpleMarker);

        gl.addGraphic(pointGraphic);



        m1.addLayer(gl);






        t1.setLayoutParams(new LinearLayout.LayoutParams(width,height));
        t2.setLayoutParams(new LinearLayout.LayoutParams(width,height));
        t3.setLayoutParams(new LinearLayout.LayoutParams(width,height));



        setContentView(activity_map);

    }
}
