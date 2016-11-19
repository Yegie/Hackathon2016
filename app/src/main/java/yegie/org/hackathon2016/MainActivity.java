package yegie.org.hackathon2016;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mainButton = (Button) findViewById(R.id.main_button);

        Button settingsButton = (Button) findViewById(R.id.settings_button);

        assert mainButton != null;
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //perform some action
            }
        });

        assert settingsButton != null;
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //perform some action
            }
        });
    }


}
