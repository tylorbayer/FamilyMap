package fmc.ui;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fmc.model.Model;
import fmshared.model.Events;
import fmshared.model.Persons;

public class PersonActivity extends Activity {
    private Model model = Model.getInstance();

    Persons[] persons;
    Persons currentPerson;
    Events[] events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        persons = model.getPersons();
        currentPerson = (Persons) getIntent().getSerializableExtra("Person");
        events = model.getEvents();

        TextView firstName = findViewById(R.id.data_first);
        firstName.setText(currentPerson.getFirstName());
        TextView lastName = findViewById(R.id.data_last);
        lastName.setText(currentPerson.getLastName());
        TextView gender = findViewById(R.id.data_gender);
        gender.setText(currentPerson.getGender());

        ArrayList<ParentObject> parentObjects = new ArrayList<>();

        ArrayList<Object> eventsChildList = new ArrayList<>();
        for (Events event: events) {
            if (event.getPersonID().equals(currentPerson.getPersonID())) {
                Drawable eventIcon = new IconDrawable(this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.grey).sizeDp(25);
                DataChild eventsDataChild = new DataChild(event.getEventType() + ": " + event.getCity() +
                        ", " + event.getCountry() + " (" + event.getYear() + ")", currentPerson.getFirstName() +
                " " + currentPerson.getLastName(), eventIcon, event);

                eventsChildList.add(eventsDataChild);
            }
        }
        Data eventsData = new Data("Life Events", eventsChildList);
        parentObjects.add(eventsData);

        ArrayList<Object> personsChildList = new ArrayList<>();
        for (Persons person: persons) {
            Drawable personIcon;
            if (person.getPersonID().equals(currentPerson.getSpouseID())) {
                personIcon = getDrawable(person.getGender());
                DataChild personDataChild = new DataChild(person.getFirstName() + " " + person.getLastName(), "Spouse", personIcon, person);
                personsChildList.add(personDataChild);
            }
            else if (person.getPersonID().equals(currentPerson.getFatherID())) {
                personIcon = getDrawable(person.getGender());
                DataChild personDataChild = new DataChild(person.getFirstName() + " " + person.getLastName(), "Father", personIcon, person);
                personsChildList.add(personDataChild);
            }
            else if (person.getPersonID().equals(currentPerson.getMotherID())) {
                personIcon = getDrawable(person.getGender());
                DataChild personDataChild = new DataChild(person.getFirstName() + " " + person.getLastName(), "Mother", personIcon, person);
                personsChildList.add(personDataChild);
            }
            if (person.getFatherID().equals(currentPerson.getPersonID()) || person.getMotherID().equals(currentPerson.getPersonID())) {
                personIcon = getDrawable(person.getGender());
                DataChild personDataChild = new DataChild(person.getFirstName() + " " + person.getLastName(), "Child", personIcon, person);
                personsChildList.add(personDataChild);
            }
        }

        Data personsData = new Data("Family", personsChildList);
        parentObjects.add(personsData);

        DataDisplayAdapter personsDisplayAdapter = new DataDisplayAdapter(this, parentObjects);
        personsDisplayAdapter.setCustomParentAnimationViewId(R.id.parent_list_item_expand_arrow);
        personsDisplayAdapter.setParentClickableViewAnimationDefaultDuration();
        personsDisplayAdapter.setParentAndIconExpandOnClick(true);

        RecyclerView dataRecycler = findViewById(R.id.data_recycler);
        dataRecycler.setLayoutManager(new LinearLayoutManager(this));

        dataRecycler.setAdapter(personsDisplayAdapter);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private Drawable getDrawable(String gender) {
        Drawable personIcon;

        if (gender.equals("m"))
            personIcon = new IconDrawable(this, FontAwesomeIcons.fa_male).colorRes(R.color.male_icon).sizeDp(25);
        else
            personIcon = new IconDrawable(this, FontAwesomeIcons.fa_female).colorRes(R.color.female_icon).sizeDp(25);

        return personIcon;
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