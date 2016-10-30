package com.app.Zensuren;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by tim on 08.06.14.
 */
public class abhaklistenadapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] name;
    private final String[] sus;
    private final String kursid;
    private final String aufgid;
    private DataManipulator dm;

    public abhaklistenadapter(Activity context, DataManipulator dm, String[] name, String[] susid, String kursid, String aufgid) {
        super(context, R.layout.abhaklistenlayout, name);
        this.context = context;
        this.name = name;
        this.sus = susid;
        this.kursid = kursid;
        this.dm = dm;
        this.aufgid = aufgid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.abhaklistenlayout, null, true);

        TextView nameView = (TextView) rowView.findViewById(R.id.name);
        String n = name[position];
        nameView.setText(n);

        TextView datumView = (TextView) rowView.findViewById(R.id.datum);
        String d = "";
        datumView.setText(d);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        if(dm.haterledigt(sus[position],aufgid).equals("")){
            imageView.setImageResource(R.drawable.negativ);
            datumView.setText("");
        }else{
            imageView.setImageResource(R.drawable.positiv);
            datumView.setText(dm.haterledigt(sus[position],aufgid));
        }

            return rowView;
    }


}

