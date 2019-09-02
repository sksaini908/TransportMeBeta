package com.example.hacker_machine.navigationdrawer;
/**
 * Created by hacker-machine on 21/3/16.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CabsList extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> CabName;
    private final ArrayList<String> ETA;
    private final ArrayList<String> Price;
    private final ArrayList<String> ProductId;
    //Constructor
    public CabsList(Activity context, ArrayList<String> CabName, ArrayList<String> ETA,ArrayList<String> Price,
                    ArrayList<String> ProductID) {
        super(context, R.layout.ubercabslist,ProductID );
        this.context = context;
        this.CabName = CabName;
        this.ETA = ETA;
        this.Price = Price;
        this.ProductId=ProductID;
    }
    //provide Customized List View
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.ubercabslist, null, true);

        TextView textViewCabs = (TextView) rowView.findViewById(R.id.textViewCabs);
        TextView textViewEta = (TextView) rowView.findViewById(R.id.textViewETA);
        TextView textViewPrice = (TextView) rowView.findViewById(R.id.textViewPrice);
        try {

            textViewCabs.setText(CabName.get(position));
            textViewEta.setText(ETA.get(position));
            textViewPrice.setText(Price.get(position));

        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return rowView;
    }
}

