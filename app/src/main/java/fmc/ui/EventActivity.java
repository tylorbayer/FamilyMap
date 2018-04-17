package fmc.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import fmc.model.Model;
import fmshared.model.Events;
import fmshared.model.Persons;

public class EventActivity extends FragmentActivity {

    private Model model = Model.getInstance();

    private MapFragment mapFragment;

//    public static String hostNum;
//    public static int portNum;
//
    private Events[] events;
    private Events currentEvent = null;
    private Persons[] persons;
//    private Persons currentPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        persons = model.getPersons();
        currentEvent = (Events) getIntent().getSerializableExtra("Event");
        events = model.getEvents();

        FragmentManager fm = this.getSupportFragmentManager();
        mapFragment = (MapFragment) fm.findFragmentById(R.id.mapFrameLayout);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance(currentEvent);
            fm.beginTransaction()
                    .add(R.id.mapFrameLayout, mapFragment)
                    .commit();
        }

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}