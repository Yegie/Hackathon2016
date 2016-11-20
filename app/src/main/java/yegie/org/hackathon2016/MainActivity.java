package yegie.org.hackathon2016;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends Activity {

    private float length;
    private boolean sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle settings = SettingsActivity.getGameSettings(getBaseContext());
        length = settings.getFloat(SettingsActivity.GAME_LENGTH);
        sound = settings.getBoolean(SettingsActivity.GAME_SOUND);

        Button mainButton = (Button) findViewById(R.id.main_button);

        Button settingsButton = (Button) findViewById(R.id.settings_button);

        assert mainButton != null;
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMapAct();
            }
        });


        assert settingsButton != null;
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSettingsAct();
            }
        });
    }
    private void startMapAct()
    {
        Intent mapIntent = new Intent(this,MapActivity.class);
        mapIntent.putExtra(SettingsActivity.GAME_LENGTH, length);
        mapIntent.putExtra(SettingsActivity.GAME_SOUND, sound);
        startActivity(mapIntent);
    }

    private void startSettingsAct()
    {
        Intent settingsIntent = new Intent(this,SettingsActivity.class);

        startActivity(settingsIntent);
    }
}

