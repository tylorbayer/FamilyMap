package fmc.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.ArrayList;

import fmc.model.Filters;

public class FiltersActivity extends Activity {

    public static String EXTRA_RESULT = "result";

    ArrayList<Filters> filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        filters = (ArrayList<Filters>) getIntent().getSerializableExtra("Filters");

        getActionBar().setDisplayHomeAsUpEnabled(true);

        Switch fatherSwitch = findViewById(R.id.father_filter_switch);
        fatherSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    filters.get(0).setShow(true);
                } else {
                    filters.get(0).setShow(true);
                }
            }
        });

        Switch motherSwitch = findViewById(R.id.mother_filter_switch);
        motherSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    filters.get(1).setShow(true);
                } else {
                    filters.get(1).setShow(true);
                }
            }
        });

        Switch maleSwitch = findViewById(R.id.male_filter_switch);
        maleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    filters.get(2).setShow(true);
                } else {
                    filters.get(2).setShow(true);
                }
            }
        });

        Switch femaleSwitch = findViewById(R.id.female_filter_switch);
        femaleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    filters.get(3).setShow(true);
                } else {
                    filters.get(3).setShow(true);
                }
            }
        });
    }

    public static ArrayList<Filters> getResult(Intent intent) {
        return (ArrayList<Filters>) intent.getSerializableExtra(EXTRA_RESULT);
    }

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

        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT, filters);
        setResult(RESULT_OK, intent);
    }
}