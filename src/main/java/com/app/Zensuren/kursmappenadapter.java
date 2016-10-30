package com.app.Zensuren;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tim on 11.06.14.
 */
public class kursmappenadapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] datum;
    private final String[] stundenart;
    private final String[] thema;
    private final String[] hausaufgabe;
    private final String kursid;
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    SimpleDateFormat sdf2 = new SimpleDateFormat("c, dd.MM.yyyy");

    public kursmappenadapter(Activity context, String kursid, String[] datum, String[] stundenart, String[] thema, String[] hausaufgabe) {
        super(context, R.layout.kursmappenlayout, thema);
        this.context = context;
        this.datum = datum;
        this.thema = thema;
        this.kursid = kursid;
        this.hausaufgabe = hausaufgabe;
        this.stundenart = stundenart;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.kursmappenlayout, null, true);

        TextView themaView = (TextView) rowView.findViewById(R.id.thema);
        String n = thema[position];
        themaView.setText(n);

        TextView haView = (TextView) rowView.findViewById(R.id.hausaufgabe);
        String h = hausaufgabe[position];
        haView.setText(h);

        TextView datumView = (TextView) rowView.findViewById(R.id.datum);
        String d = datum[position];
        Date dd=null;
        try {
            dd = sdf.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        d = sdf2.format(dd);

        datumView.setText(d);

        return rowView;
    }


}

