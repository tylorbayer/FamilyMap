package fmc.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.Comparator;
import java.util.List;

import fmshared.model.Events;
import fmshared.model.Persons;

public class DataDisplayAdapter extends ExpandableRecyclerAdapter<DataParentViewHolder, DataChildViewHolder> {
    LayoutInflater mInflater;
    private Context context;

    public DataDisplayAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public DataParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.data_display_parent, viewGroup, false);
        return new DataParentViewHolder(view);
    }

    @Override
    public DataChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.data_sub_display, viewGroup, false);
        return new DataChildViewHolder(view, context);
    }

    @Override
    public void onBindParentViewHolder(DataParentViewHolder dataParentViewHolder, int i, Object o) {
        Data data = (Data) o;
        dataParentViewHolder.mCrimeTitleTextView.setText(data.getType());
    }

    @Override
    public void onBindChildViewHolder(DataChildViewHolder dataChildViewHolder, int i, Object o) {
        final DataChild dataChild = (DataChild) o;
        dataChildViewHolder.dataMain.setText(dataChild.getDataMain());
        dataChildViewHolder.dataSub.setText(dataChild.getDataSub());
        dataChildViewHolder.dataImage.setImageDrawable(dataChild.getImage());
        dataChildViewHolder.dataSubDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (dataChild.getObject() instanceof Persons) {
                    intent = new Intent(context, PersonActivity.class);
                    intent.putExtra("Person", (Persons) dataChild.getObject());
                }
                else {
                    intent = new Intent(context, EventActivity.class);
                    intent.putExtra("Event", (Events) dataChild.getObject());
                }
                context.startActivity(intent);
            }
        });
    }
}

class DataParentViewHolder extends ParentViewHolder {

    public TextView mCrimeTitleTextView;
    public ImageButton mParentDropDownArrow;

    public DataParentViewHolder(View itemView) {
        super(itemView);

        mCrimeTitleTextView = itemView.findViewById(R.id.parent_list_item_crime_title_text_view);
        mParentDropDownArrow = itemView.findViewById(R.id.parent_list_item_expand_arrow);
    }
}

class DataChildViewHolder extends ChildViewHolder {
    public LinearLayout dataSubDisplay;
    public TextView dataMain;
    public TextView dataSub;
    public ImageView dataImage;

    public DataChildViewHolder(View itemView, Context context) {
        super(itemView);

        dataSubDisplay = itemView.findViewById(R.id.data_sub_display);
        dataMain = itemView.findViewById(R.id.data_main);
        dataSub = itemView.findViewById(R.id.data_sub);
        dataImage = itemView.findViewById(R.id.data_image);
    }
}

class Data implements ParentObject {

    /* Create an instance variable for your list of children */
    String type;
    private List<Object> mChildrenList;

    public Data(String type, List<Object> mChildrenList) {
        this.type = type;
        this.mChildrenList = mChildrenList;
    }

    public String getType() {
        return type;
    }

    @Override
    public List<Object> getChildObjectList() {
        return mChildrenList;
    }

    @Override
    public void setChildObjectList(List<Object> list) {
        mChildrenList = list;
    }
}

class DataChild {
    private String dataMain;
    private String dataSub;
    private Drawable image;
    private Object personOrEvent;

    public DataChild(String dataMain, String dataSub, Drawable image, Object personOrEvent) {
        this.dataMain = dataMain;
        this.dataSub = dataSub;
        this.image = image;
        this.personOrEvent = personOrEvent;
    }

    public String getDataMain() {
        return dataMain;
    }

    public String getDataSub() {
        return dataSub;
    }

    public Drawable getImage() {
        return image;
    }

    public Object getObject() {
        return personOrEvent;
    }
}
