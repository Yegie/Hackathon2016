package yegie.org.hackathon2016;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.ArrayAdapter;


public class SettingsActivity extends AppCompatActivity {
    private boolean gameSound;
    private double gameLength;
    private int spinnerIndex;
    public final String SPINNER_TIME_INDEX = "spinner_time_index";
    private SharedPreferences sharedPref;
    public final String GAME_LENGTH = "game_length";
    public final String GAME_SOUND = "game_sound";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        Spinner timeSpinner = (Spinner) findViewById(R.id.time_spinner);

        CheckBox soundCheck = (CheckBox) findViewById(R.id.sound_checkbox);

        ArrayAdapter<CharSequence> timeOptions = ArrayAdapter.createFromResource(this, R.array.time_options, android.R.layout.simple_spinner_item);
        timeOptions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeOptions);

        if(sharedPref != null){
            Log.d("DEBUG","we have a saved instance state");
            timeSpinner.setSelection(sharedPref.getInt(SPINNER_TIME_INDEX,0));
            soundCheck.setChecked(sharedPref.getBoolean(GAME_SOUND,false));
        }


        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String time = parent.getItemAtPosition(pos).toString();
                String gameTimes[] = getResources().getStringArray(R.array.time_options);
                int i;
                for (i = 0; i < gameTimes.length; i++) {
                    if(time.equals(gameTimes[i]))
                    {
                        break;
                    }
                }
                switch (i) {
                    case (0): {
                        gameLength = 3;
                    }
                    case (1): {
                        gameLength = 5;
                    }
                    case (2): {
                        gameLength = 7;
                    }
                    case (3): {
                        gameLength = 10;
                    }
                    case (4): {
                        gameLength = 15;
                    }
                    case (5): {
                        gameLength = 20;
                    }
                }
                spinnerIndex = i;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Nothing
            }
        });
    }

    private void saveData(){

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(GAME_SOUND, gameSound);
        editor.putFloat(GAME_LENGTH, (float)gameLength);
        editor.putInt(SPINNER_TIME_INDEX, spinnerIndex);
        editor.commit();

    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        if(checked && view.getId() == R.id.sound_checkbox){
            gameSound = true;
        }else if(!checked && view.getId() == R.id.sound_checkbox) {
            gameSound = false;
        }
    }

    @Override
    public void onBackPressed(){
        saveData();
        super.onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        Log.d("DEBUG","Reached onSaveINatabfhaef");
        saveData();
    }

}





