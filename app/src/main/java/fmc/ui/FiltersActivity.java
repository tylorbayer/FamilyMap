package fmc.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;

import fmc.model.Filters;
import fmc.model.Model;

public class FiltersActivity extends Activity {

    private Model model = Model.getInstance();

    public static String EXTRA_RESULT = "result";

    ArrayList<Filters> filters;

    ArrayList<FilterChild> data;

    FilterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        final RecyclerView recyclerView = findViewById(R.id.filter_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getParent()));

        filters = model.getFilters();

        data = new ArrayList<>();

        for (Filters originFilter: filters) {
            Filters filter;
            try {
                filter = originFilter.clone();
            }
            catch (Exception e) {
                filter = originFilter;
            }
            String eventType = filter.getEventType();
            String filterSub;

            if (eventType.equals("Father's Side") || eventType.equals("Mother's Side"))
                filterSub = "FILTER BY " + eventType.toUpperCase() + " SIDE OF FAMILY";
            else if (eventType.equals("Male Events") || eventType.equals("Female Events"))
                filterSub = "FILTER EVENTS BASED ON GENDER";
            else
                filterSub = "FILTER BY " + eventType.toUpperCase();

            FilterChild filterChild = new FilterChild(eventType, filterSub, filter.isShow(), filter);

            data.add(filterChild);
        }

        getActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new FilterAdapter(getBaseContext(), data);
        recyclerView.setAdapter(adapter);
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
        ArrayList<Filters> newFilters = new ArrayList<>();
        for (FilterChild dataChild: data) {
            newFilters.add(dataChild.getFilter());

        }
        model.setFilters(newFilters);

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
    }
}
