package com.example.hacker_machine.navigationdrawer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomList extends ArrayAdapter<Integer> {
    private final Activity context;
    private final String[] Services;
    private final Integer[] imageId;
    //Constructor
    public CustomList(Activity context,
                      String[] Services, Integer[] imageId) {
        super(context, R.layout.list_single,imageId);
        this.context = context;
        this.Services = Services;
        this.imageId = imageId;
    }
    //provide Customized List View
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);

        txtTitle.setText(Services[position]);
        imageView.setImageResource(imageId[position]);

        return rowView;
    }
}

