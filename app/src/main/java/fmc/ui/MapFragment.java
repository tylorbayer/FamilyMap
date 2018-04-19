package fmc.ui;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import fmc.model.Filters;
import fmc.model.Model;
import fmc.model.Settings;
import fmc.server.Proxy;
import fmshared.fmrequest.LoginRequest;
import fmshared.model.Events;
import fmshared.model.Persons;

import static android.app.Activity.RESULT_OK;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, Proxy.Context {

    private Model model = Model.getInstance();

    public String hostNum;
    public int portNum;

    private LoginRequest logReq;
    private String authToken;

    private Gson gson = new Gson();

    private GoogleMap mMap;
    private Events[] events;
    private Events currentEvent = null;
    private Persons[] persons;
    private Persons currentPerson;
    private View v;
    private Marker currentMarker = null;
    private HashSet<String> eventTypes = new HashSet<>();
    private HashMap<String, Float> eventColorMap = new HashMap<>();
    private float color = -25.0f;
    private final int REQ_CODE_SETTINGS = 1;
    private final int REQ_CODE_FILTERS = 2;

    private static Settings settings = new Settings();
    private static ArrayList<Filters> filters;

    private ArrayList<Polyline> polylines = new ArrayList<>();

    private ArrayList<Marker> mMarkerArray;

    public MapFragment() {}

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    public static MapFragment newInstance(Events currentEvent) {
        MapFragment mapFrag = new MapFragment();

        Bundle args = new Bundle();
        args.putSerializable("Event", currentEvent);
        mapFrag.setArguments(args);

        return mapFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        hostNum = model.getHostNum();
        portNum = model.getPortNum();
        logReq = model.getLogReq();
        authToken = model.getAuthToken();
        events = model.getEvents();
        persons = model.getPersons();
        filters = model.getFilters();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Drawable dataIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_android).colorRes(R.color.android_icon).sizeDp(40);
        ImageView dataImageView = v.findViewById(R.id.data_image);
        dataImageView.setImageDrawable(dataIcon);

        LinearLayout eventInfo = v.findViewById(R.id.event_info);
        eventInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentEvent != null) {
                    Intent personActivity = new Intent(getContext(), PersonActivity.class);
                    personActivity.putExtra("Person", currentPerson);
                    startActivity(personActivity);
                }
            }
        });

        Bundle args = getArguments();
        if (args != null) {
            currentEvent = (Events) args.getSerializable("Event");

            for (Persons person: persons) {
                if (currentEvent.getPersonID().equals(person.getPersonID())) {
                    currentPerson = person;
                }
            }

            TextView dataMain = v.findViewById(R.id.data_main);
            String mainText = currentPerson.getFirstName() + " " + currentPerson.getLastName();
            dataMain.setText(mainText);

            TextView dataSub = v.findViewById(R.id.data_sub);
            String subText = currentEvent.getEventType() + ": " + currentEvent.getCity() + ", " + currentEvent.getCountry() +
                    " (" + currentEvent.getYear() + ")";
            dataSub.setText(subText);

            if (currentPerson.getGender().equals("m"))
                dataIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.male_icon).sizeDp(40);
            else
                dataIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).colorRes(R.color.female_icon).sizeDp(40);

            dataImageView = v.findViewById(R.id.data_image);
            dataImageView.setImageDrawable(dataIcon);
        }

        return v;
    }

    @Override
    public void onResume() {
        drawLines();
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (currentEvent == null) {
            inflater.inflate(R.menu.map_frag_menu, menu);

            MenuItem searchItem = menu.findItem(R.id.search_item);
            Drawable searchIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_search).colorRes(R.color.white).sizeDp(25);
            searchItem.setIcon(searchIcon);

            MenuItem filterItem = menu.findItem(R.id.filters_item);
            Drawable filterIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_filter).colorRes(R.color.white).sizeDp(25);
            filterItem.setIcon(filterIcon);

            MenuItem settingsItem = menu.findItem(R.id.settings_item);
            Drawable settingsIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_gear).colorRes(R.color.white).sizeDp(25);
            settingsItem.setIcon(settingsIcon);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_item:
                Intent searchActivity = new Intent(getContext(), SearchActivity.class);
                startActivity(searchActivity);
                return true;
            case R.id.filters_item:
                Intent filtersActivity = new Intent(getContext(), FiltersActivity.class);
                startActivityForResult(filtersActivity, REQ_CODE_FILTERS);
                return true;
            case R.id.settings_item:
                Intent settingsActivity = new Intent(getContext(), SettingsActivity.class);
                startActivityForResult(settingsActivity, REQ_CODE_SETTINGS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (Events event: events) {
            eventTypes.add(event.getEventType());
        }

        for (String eventType: eventTypes) {
            color += 40.0f;
            eventColorMap.put(eventType, color);
        }

        popMap();

        if (filters.size() == 0) {
            for (String eventType : eventTypes) {
                Filters filter = new Filters(eventType.substring(0, 1).toUpperCase() +
                        eventType.substring(1).toLowerCase() + " Events");

                filters.add(filter);
            }

            filters.add(new Filters("Father's Side"));
            filters.add(new Filters("Mother's Side"));
            filters.add(new Filters("Male Events"));
            filters.add(new Filters("Female Events"));
        }

        if (currentEvent!= null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(currentEvent.getLatitude()),
                    Double.parseDouble(currentEvent.getLongitude())), 5.0f));

            for (Marker marker: mMarkerArray) {
                if (marker.getTitle().equals(currentEvent.getEventID())) {
                    currentMarker = marker;
                }
            }
        }
        drawLines();
    }

    private void popMap() {
        mMarkerArray = new ArrayList<>();

        ArrayList<Events> mEvents = new ArrayList<>(Arrays.asList(events));
        Collections.sort(mEvents);
        for (Events event: mEvents) {
            LatLng latLng = new LatLng(Double.parseDouble(event.getLatitude()), Double.parseDouble(event.getLongitude()));

            String eventType = event.getEventType();
            Float mColor = eventColorMap.get(eventType);
            if (mColor >= 360.0f)
                mColor = 359.9f;

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(event.getEventID())
                    .snippet(event.getPersonID())
                    .icon(BitmapDescriptorFactory.defaultMarker(mColor)));

            mMarkerArray.add(marker);
        }

        mMap.setOnMarkerClickListener(this);

        drawLines();
    }


    @Override
    public void populateMap(Events[] mEvents, Persons[] persons) {}

    @Override
    public void rePopulateMap(Events[] mEvents, Persons[] persons) {
        for (Events event: mEvents) {
            event.setEventType(event.getEventType().toLowerCase());
        }

        model.setEvents(mEvents);
        model.setPersons(persons);

        popMap();
    }

    @Override
    public void logRegPassed(boolean passed, String type, String authToken, String personID) {
        if (!passed) {
            Toast.makeText(getContext(), "Re-sync failed. Try logging in again.", Toast.LENGTH_SHORT).show();
            Intent intent = getActivity().getIntent();
            getActivity().finish();
            startActivity(intent);
        }
        model.setPersonID(personID);
        model.setAuthToken(authToken);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        currentMarker = marker;

        removeLines();

        for (Persons person: persons) {
            if (currentMarker.getSnippet().equals(person.getPersonID())) {
                currentPerson = person;
            }
        }

        for (Events event: events) {
            if (currentMarker.getTitle().equals(event.getEventID())) {
                currentEvent = event;
            }
        }

        TextView dataMain = v.findViewById(R.id.data_main);
        String mainText = currentPerson.getFirstName() + " " + currentPerson.getLastName();
        dataMain.setText(mainText);

        TextView dataSub = v.findViewById(R.id.data_sub);
        String subText = currentEvent.getEventType() + ": " + currentEvent.getCity() + ", " + currentEvent.getCountry() +
                " (" + currentEvent.getYear() + ")";
        dataSub.setText(subText);

        Drawable dataIcon;

        if (currentPerson.getGender().equals("m"))
            dataIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.male_icon).sizeDp(40);
        else
            dataIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).colorRes(R.color.female_icon).sizeDp(40);

        ImageView dataImageView = v.findViewById(R.id.data_image);
        dataImageView.setImageDrawable(dataIcon);

        drawLines();

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        removeLines();

        if (requestCode == REQ_CODE_SETTINGS && resultCode == RESULT_OK && data != null) {
            settings = model.getSettings();

            if (settings.isResync()) {
                mMap.clear();
                filters = new ArrayList<>();
                model.setEvents(null);
                Proxy proxy = new Proxy(gson.toJson(logReq), this, "login", hostNum, portNum);
                try {
                    proxy.execute(new URL("http", hostNum, portNum, "user/login"));
                }
                catch (Exception e) {
                    Toast.makeText(getContext(), "Re-sync failed. Try logging in again.", Toast.LENGTH_SHORT).show();
                    Intent mainActivity = new Intent(getContext(), MainActivity.class);
                    mainActivity.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(mainActivity);
                }
            }

            drawLines();
        }
        else if (requestCode == REQ_CODE_FILTERS && resultCode == RESULT_OK && data != null) {
            filters = model.getFilters();
            events = model.getEvents();
            persons = model.getPersons();
            mMap.clear();
            popMap();
        }
    }

    private void drawLines() {
        ArrayList<Marker> newMarkers;
        Marker newMarker;

        setMapType();
        settings = model.getSettings();

        if (currentMarker != null) {
            newMarkers = new ArrayList<>();
            if (settings.isLifeLines()) {
                for (Marker marker : mMarkerArray) {
                    if (currentMarker.getSnippet().equals(marker.getSnippet())) {
                        newMarkers.add(marker);
                    }
                }

                newMarker = newMarkers.get(0);

                for (Marker marker: newMarkers) {
                    if (newMarker == marker)
                        continue;

                    Polyline polyline = mMap.addPolyline(new PolylineOptions()
                            .add(newMarker.getPosition(), marker.getPosition())
                            .color(settings.getLifeLineColor()));

                    polylines.add(polyline);
                    newMarker = marker;
                }
            }

            if (settings.isFamilyLines()) {
                addFLHelper();
            }

            newMarkers = new ArrayList<>();
            if (settings.isSpouseLines()) {
                for (Marker marker : mMarkerArray) {
                    if (currentPerson.getSpouseID().equals(marker.getSnippet())) {
                        newMarkers.add(marker);
                    }
                }
                if (newMarkers.size() > 0) {
                    Polyline polyline = mMap.addPolyline(new PolylineOptions()
                            .add(currentMarker.getPosition(), newMarkers.get(0).getPosition())
                            .color(settings.getSpouseLineColor()));

                    polylines.add(polyline);
                }
            }
        }
    }

    private void addFLHelper() {
        Persons father;
        Persons mother;

        float width = 10.0f;
        boolean hasNext = true;
        Persons mPerson = currentPerson;
        while(hasNext) {
            father = null;
            width -= 2.0f;
            if (width < 2.0f)
                width = 2.0f;
            for (Persons person: persons) {
                if (mPerson.getFatherID().equals(person.getPersonID())) {
                    father = person;
                    break;
                }
            }

            if (father == null || father.getFatherID().equals("") || father.getMotherID().equals("")) {
                hasNext = false;
            }
            else if (!father.getFatherID().equals("") && !father.getMotherID().equals("")) {
                addFamilyLines(father, width);
                mPerson = father;
            }
        }

        width = 10.0f;
        hasNext = true;
        mPerson = currentPerson;
        while(hasNext) {
            mother = null;
            width -= 2.0f;
            if (width < 2.0f)
                width = 2.0f;
            for (Persons person: persons) {
                if (mPerson.getMotherID().equals(person.getPersonID())) {
                    mother = person;
                    break;
                }
            }

            if (mother == null || mother.getMotherID().equals("") || mother.getMotherID().equals("")) {
                hasNext = false;
            }
            else if (!mother.getFatherID().equals("") && !mother.getMotherID().equals("")) {
                addFamilyLines(mother, width);
                mPerson = mother;
            }
        }
    }

    private void addFamilyLines(Persons mPerson, float width) {
        ArrayList<Marker> mMarkers = new ArrayList<>();

        for (Marker marker : mMarkerArray) {
            if (mPerson.getPersonID().equals(marker.getSnippet())) {
                mMarkers.add(marker);
            }
        }

        if (mMarkers.size() > 0) {
            Polyline polyline = mMap.addPolyline(new PolylineOptions()
                    .add(currentMarker.getPosition(), mMarkers.get(0).getPosition())
                    .color(settings.getSpouseLineColor())
                    .width(width));

            polylines.add(polyline);
        }
    }

    private void removeLines() {
        for (Polyline polyline: polylines) {
            polyline.remove();
        }
        polylines = new ArrayList<>();
    }

    private void setMapType() {
        String mapType = settings.getMapType();
        int gMapType = 0;

        if (mMap != null) {
            switch (mapType) {
                case "Normal":
                    gMapType = GoogleMap.MAP_TYPE_NORMAL;
                    break;
                case "Hybrid":
                    gMapType = GoogleMap.MAP_TYPE_HYBRID;
                    break;
                case "Satellite":
                    gMapType = GoogleMap.MAP_TYPE_SATELLITE;
                    break;
                case "Terrain":
                    gMapType = GoogleMap.MAP_TYPE_TERRAIN;
                    break;
            }
            if (mMap.getMapType() != gMapType)
                mMap.setMapType(gMapType);
        }
    }
}