package fmc.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;


import java.util.ArrayList;

import fmc.model.Filters;


public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {


    private ArrayList<FilterChild> mData;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    FilterAdapter(Context context, ArrayList<FilterChild> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }



    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.filter_display, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FilterChild data = mData.get(position);
        holder.filterMain.setText(data.getFilterMain());
        holder.filterSub.setText(data.getFilterSub());
        holder.filterSwitch.setChecked(data.isChecked());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView filterMain;
        TextView filterSub;
        Switch filterSwitch;

        ViewHolder(View itemView) {
            super(itemView);
            filterMain = itemView.findViewById(R.id.filter_main);
            filterSub = itemView.findViewById(R.id.filter_sub);
            filterSwitch = itemView.findViewById(R.id.filter_switch);

            filterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    for (FilterChild filterChild: mData) {
                        if (filterChild.getFilter().getEventType().equals(filterMain.getText())) {
                            filterChild.getFilter().setShow(isChecked);
                            break;
                        }
                    }
                }
            });
        }
    }

    // convenience method for getting data at click position
    FilterChild getItem(int id) {
        return mData.get(id);
    }
}

class FilterChild {
    private String filterMain;
    private String filterSub;
    private boolean checked;
    private Filters filter;

    public FilterChild(String filterMain, String filterSub, boolean checked, Filters filter) {
        this.filterMain = filterMain;
        this.filterSub = filterSub;
        this.checked = checked;
        this.filter = filter;
    }

    public String getFilterMain() {
        return filterMain;
    }

    public String getFilterSub() {
        return filterSub;
    }

    public boolean isChecked() {
        return checked;
    }

    public Filters getFilter() {
        return filter;
    }
}
