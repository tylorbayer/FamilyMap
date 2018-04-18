package fmc.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import fmc.model.Model;
import fmc.model.Settings;


public class SettingsActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private Model model = Model.getInstance();

    public static String EXTRA_RESULT = "result";

    private Settings settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        try {
            settings = model.getSettings().clone();
        }
        catch (Exception e) {
            settings = model.getSettings();
        }

        settings.setResync(false);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner lifeLinesSpinner = findViewById(R.id.life_lines_spinner);
        lifeLinesSpinner.setOnItemSelectedListener(this);

        Spinner familyLinesSpinner = findViewById(R.id.family_lines_spinner);
        familyLinesSpinner.setOnItemSelectedListener(this);

        Spinner spouseLinesSpinner = findViewById(R.id.spouse_lines_spinner);
        spouseLinesSpinner.setOnItemSelectedListener(this);

        Spinner mapTypeSpinner = findViewById(R.id.map_type_spinner);
        mapTypeSpinner.setOnItemSelectedListener(this);

        Switch lifeSwitch = findViewById(R.id.life_lines_switch);
        lifeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setLifeLines(isChecked);
            }
        });

        Switch familySwitch = findViewById(R.id.family_lines_switch);
        familySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setFamilyLines(isChecked);

            }
        });

        Switch spouseSwitch = findViewById(R.id.spouse_lines_switch);
        spouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setSpouseLines(isChecked);
            }
        });

        setLineSpinner(lifeLinesSpinner, settings.getLifeColor());
        setLineSpinner(familyLinesSpinner, settings.getFamilyColor());
        setLineSpinner(spouseLinesSpinner, settings.getSpouseColor());

        String mapType = settings.getMapType();
        switch (mapType) {
            case "Normal":
                mapTypeSpinner.setSelection(0);
                break;
            case "Hybrid":
                mapTypeSpinner.setSelection(1);
                break;
            case "Satellite":
                mapTypeSpinner.setSelection(2);
                break;
            case "Terrain":
                mapTypeSpinner.setSelection(3);
                break;
        }

        lifeSwitch.setChecked(settings.isLifeLines());
        familySwitch.setChecked(settings.isFamilyLines());
        spouseSwitch.setChecked(settings.isSpouseLines());

        LinearLayout sync = findViewById(R.id.sync);
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setResync(true);
                returnResult();
                finish();
            }
        });

        LinearLayout logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(v.getContext(), MainActivity.class);
                mainActivity.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(mainActivity);
            }
        });
    }

    public static Settings getResult(Intent intent) {
        return (Settings) intent.getSerializableExtra(EXTRA_RESULT);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        switch (parent.getId()) {
            case R.id.life_lines_spinner:
                settings.setLifeColor(parent.getSelectedItem().toString());
                break;
            case R.id.family_lines_spinner:
                settings.setFamilyColor(parent.getSelectedItem().toString());
                break;
            case R.id.spouse_lines_spinner:
                settings.setSpouseColor(parent.getSelectedItem().toString());
                break;
            case R.id.map_type_spinner:
                settings.setMapType(parent.getSelectedItem().toString());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                returnResult();
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void returnResult() {
        model.setSettings(settings);

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
    }

    private void setLineSpinner(Spinner mySpinner, String color) {
        switch (color) {
            case "Red":
                mySpinner.setSelection(0);
                break;
            case "Green":
                mySpinner.setSelection(1);
                break;
            case "Blue":
                mySpinner.setSelection(2);
                break;
            case "Yellow":
                mySpinner.setSelection(3);
                break;
        }
    }
}