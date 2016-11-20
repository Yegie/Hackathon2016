package yegie.org.hackathon2016;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = (int) ((1.0/10.0) * displaymetrics.heightPixels);
        int width = (int) ((1.0/3.0) * displaymetrics.widthPixels);

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Location location = lm.getLastKnownLocation(Context.LOCATION_SERVICE);

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();



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
