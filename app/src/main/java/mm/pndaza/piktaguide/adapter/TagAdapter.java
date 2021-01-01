package mm.pndaza.piktaguide.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mm.pndaza.piktaguide.R;

public class TagAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<String> list;

    public TagAdapter(Context context, ArrayList<String> list) {
        super(context, 0, list);

        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size(); //returns total of items in the list
    }

    @Override
    public String getItem(int position) {
        return list.get(position); //returns list item at the specified position
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String name = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = convertView.findViewById(R.id.tv_name);
        // Populate the data into the template view using the data object
        tvName.setText(getItem(position));
        tvName.setTextColor(Color.BLACK);
        // Return the completed view to render on screen
        return convertView;
    }



}
