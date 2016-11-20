package yegie.org.hackathon2016;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
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
        //  requestWindowFeature(Window.FEATURE_NO_TITLE);

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


        m1.centerAt(latitude, longitude, true);

        t1.setLayoutParams(new LinearLayout.LayoutParams(width,height));
        t2.setLayoutParams(new LinearLayout.LayoutParams(width,height));
        t3.setLayoutParams(new LinearLayout.LayoutParams(width,height));



        setContentView(activity_map);

    }
}
