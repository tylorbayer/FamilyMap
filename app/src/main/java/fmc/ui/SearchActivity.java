package fmc.ui;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;

import fmc.model.Model;
import fmshared.model.Events;
import fmshared.model.Persons;

public class SearchActivity extends Activity implements SearchAdapter.ItemClickListener {

    private Model model = Model.getInstance();

    Persons[] persons;
    Events[] events;

    SearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        persons = model.getPersons();
        events = model.getEvents();

        final RecyclerView recyclerView = findViewById(R.id.search_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getParent()));

        final EditText searchBox = findViewById(R.id.search_box);
        searchBox.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String ss = s.toString().toLowerCase();

                ArrayList<DataChild> data = new ArrayList<>();

                for (Persons person: persons) {
                    if (person.getFirstName().toLowerCase().contains(ss) || person.getLastName().toLowerCase().contains(ss)) {
                        Drawable personIcon;

                        if (person.getGender().equals("m"))
                            personIcon = new IconDrawable(getBaseContext(), FontAwesomeIcons.fa_male).colorRes(R.color.male_icon).sizeDp(35);
                        else
                            personIcon = new IconDrawable(getBaseContext(), FontAwesomeIcons.fa_female).colorRes(R.color.female_icon).sizeDp(35);

                        DataChild personDataChild = new DataChild(person.getFirstName() + " " + person.getLastName(), "", personIcon, person);

                        data.add(personDataChild);
                    }
                }

                for (Events event: events) {
                    if (event.getEventType().toLowerCase().contains(ss) || event.getCountry().toLowerCase().contains(ss) ||
                            event.getCity().toLowerCase().contains(ss) || event.getYear().toLowerCase().contains(ss)) {
                        Persons currentPerson = null;

                        for (Persons person: persons) {
                            if (event.getPersonID().equals(person.getPersonID())) {
                                currentPerson = person;
                                break;
                            }
                        }
                        Drawable eventIcon = new IconDrawable(getBaseContext(), FontAwesomeIcons.fa_map_marker).colorRes(R.color.grey).sizeDp(35);
                        DataChild eventsDataChild = new DataChild(event.getEventType() + ": " + event.getCity() +
                                ", " + event.getCountry() + " (" + event.getYear() + ")", currentPerson.getFirstName() +
                                " " + currentPerson.getLastName(), eventIcon, event);

                        data.add(eventsDataChild);
                    }
                }

                adapter = new SearchAdapter(getBaseContext(), data);
                updateOnClick();
                recyclerView.setAdapter(adapter);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        Drawable dataIcon = new IconDrawable(this, FontAwesomeIcons.fa_times).colorRes(R.color.grey).sizeDp(40);
        ImageView clearTextImage = findViewById(R.id.clear_txt);
        clearTextImage.setImageDrawable(dataIcon);
        clearTextImage.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                searchBox.setText("");
            }
        });

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void updateOnClick() {
        adapter.setClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        DataChild dataChild = adapter.getItem(position);

        if (dataChild.getDataSub().equals("")) {
            Intent personActivity = new Intent(this, PersonActivity.class);
            personActivity.putExtra("Person", (Persons) dataChild.getObject());
            startActivity(personActivity);
        }
        else {
            Intent eventActivity = new Intent(this, EventActivity.class);
            eventActivity.putExtra("Event", (Events) dataChild.getObject());
            startActivity(eventActivity);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}