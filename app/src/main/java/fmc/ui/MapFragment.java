package fmc.ui;


import android.content.Intent;
import android.graphics.Color;
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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.net.URL;
import java.util.ArrayList;

import fmc.model.Filters;
import fmc.model.Settings;
import fmc.server.Proxy;
import fmshared.model.Events;
import fmshared.model.Persons;

import static android.app.Activity.RESULT_OK;
import static fmc.ui.MainActivity.authToken;
import static fmc.ui.MainActivity.hostNum;
import static fmc.ui.MainActivity.logReq;
import static fmc.ui.MainActivity.portNum;
import static fmc.ui.MainActivity.gson;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, Proxy.Context {

    private static GoogleMap mMap;
    private Events[] events;
    View v;
    private Marker currentMarker = null;
    private final int REQ_CODE_SETTINGS = 1;
    private final int REQ_CODE_FILTERS = 2;

    private static Settings settings = new Settings();

    private ArrayList<Marker> mMarkerArray;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
                Intent personActivity = new Intent(getContext(), PersonActivity.class);
                startActivity(personActivity);
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
                settingsActivity.putExtra("Settings", settings);
                startActivityForResult(settingsActivity, REQ_CODE_SETTINGS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMarkerArray = new ArrayList<>();

        for (Events event: events) {
            LatLng latLng = new LatLng(Double.parseDouble(event.getLatitude()), Double.parseDouble(event.getLongitude()));

            String eventType = event.getEventType();
            Float color;

            switch (eventType) {
                case "birth":
                    color = BitmapDescriptorFactory.HUE_AZURE;
                    break;
                case "marriage":
                    color = BitmapDescriptorFactory.HUE_ORANGE;
                    break;
                case "death":
                    color = BitmapDescriptorFactory.HUE_VIOLET;
                    break;
                default:
                    color = BitmapDescriptorFactory.HUE_CYAN;
            }

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(event.getEventID())
                    .snippet(event.getPersonID())
                    .icon(BitmapDescriptorFactory.defaultMarker(color)));

            mMarkerArray.add(marker);
        }

        mMap.setOnMarkerClickListener(this);
    }


    @Override
    public void populateMap(Events[] events) {
        this.events = events;
    }

    @Override
    public void rePopulateMap(Events[] events) {
        this.events = events;
        mMarkerArray = new ArrayList<>();

        for (Events event: events) {
            LatLng latLng = new LatLng(Double.parseDouble(event.getLatitude()), Double.parseDouble(event.getLongitude()));

            String eventType = event.getEventType();
            Float color;

            switch (eventType) {
                case "birth":
                    color = BitmapDescriptorFactory.HUE_AZURE;
                    break;
                case "marriage":
                    color = BitmapDescriptorFactory.HUE_ORANGE;
                    break;
                case "death":
                    color = BitmapDescriptorFactory.HUE_VIOLET;
                    break;
                default:
                    color = BitmapDescriptorFactory.HUE_CYAN;
            }

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(event.getEventID())
                    .snippet(event.getPersonID())
                    .icon(BitmapDescriptorFactory.defaultMarker(color)));

            mMarkerArray.add(marker);
        }

        mMap.setOnMarkerClickListener(this);
        drawLines();
    }

    @Override
    public void logRegPassed(boolean passed, String type, String authToken) {}

    @Override
    public void populateEvent(Events event, Persons person) {
        TextView dataMain = v.findViewById(R.id.data_main);
        String mainText = person.getFirstName() + " " + person.getLastName();
        dataMain.setText(mainText);

        TextView dataSub = v.findViewById(R.id.data_sub);
        String subText = event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() +
                " (" + event.getYear() + ")";
        dataSub.setText(subText);

        Drawable dataIcon;

        if (person.getGender().equals("m"))
            dataIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.male_icon).sizeDp(40);
        else
            dataIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).colorRes(R.color.female_icon).sizeDp(40);

        ImageView dataImageView = v.findViewById(R.id.data_image);
        dataImageView.setImageDrawable(dataIcon);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        currentMarker = marker;

        Proxy proxy = new Proxy("", this, "event", hostNum, portNum, authToken);
        try {
            String file = "event/" + marker.getTitle();
            proxy.execute(new URL("http", hostNum, portNum, file));
        }
        catch (Exception e) {
            Log.d("DEBUG", e.getMessage());
        }

        drawLines();

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String result;

        if (requestCode == REQ_CODE_SETTINGS && resultCode == RESULT_OK && data != null) {
            settings = SettingsActivity.getResult(data);

            if (settings.isResync()) {
                mMap.clear();
                Proxy proxy = new Proxy(gson.toJson(logReq), this, "login", hostNum, portNum, authToken);
                try {
                    proxy.execute(new URL("http", hostNum, portNum, "user/login"));
                }
                catch (Exception e) {
                    Log.d("DEBUG", e.getMessage());
                }
            }

            String mapType = settings.getMapType();
            if (mMap != null) {
                switch (mapType) {
                    case "Normal":
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case "Hybrid":
                        Toast.makeText(getContext(), mapType, Toast.LENGTH_SHORT).show();
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    case "Satellite":
                        Toast.makeText(getContext(), mapType, Toast.LENGTH_SHORT).show();
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case "Terrain":
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                }
            }
        }
        else if (requestCode == REQ_CODE_FILTERS && resultCode == RESULT_OK && data != null) {
            result = FiltersActivity.getResult(data);
            Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
        }
    }

    private void drawLines() {
        for (Marker marker: mMarkerArray) {
            if (currentMarker.getSnippet().equals(marker.getSnippet()) && !currentMarker.getTitle().equals(marker.getTitle())) {
                mMap.addPolyline(new PolylineOptions()
                .add(currentMarker.getPosition(), marker.getPosition())
                .color(Color.BLUE));
            }
        }
    }
}