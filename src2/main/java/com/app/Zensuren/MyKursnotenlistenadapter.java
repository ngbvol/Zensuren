package com.app.Zensuren;



import android.app.Activity;
import android.graphics.Color;
import android.text.Html;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by tim on 12.08.14.
 */
public class MyKursnotenlistenadapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] name;
    private final String[] noten;
    private final String[] farbe;
    private int listpos;

    public MyKursnotenlistenadapter(Activity context, String[] name, String[] noten, String[] farbe) {
        super(context, R.layout.kursnotenlistenlayout, name);
        this.context = context;
        this.name = name;
        this.noten = noten;
        this.farbe = farbe;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.kursnotenlistenlayout, null, true);

        TextView nameView = (TextView) rowView.findViewById(R.id.name);
        String n = name[position];
        nameView.setText(Html.fromHtml(n));

        TextView nummerView = (TextView) rowView.findViewById(R.id.nummer);
        listpos = position +1;
        nummerView.setText(Integer.toString(listpos));

        TextView notenView = (TextView) rowView.findViewById(R.id.untenrechts);
        notenView.setText(Html.fromHtml(noten[position]));

        TextView untenlinks = (TextView) rowView.findViewById(R.id.untenlinks);
        untenlinks.setBackgroundColor(0xff000000);
        if (farbe[position].equals("gr")){untenlinks.setBackgroundColor(0xff00ff00);}
        if (farbe[position].equals("ge")){untenlinks.setBackgroundColor(0xffffff00);}
        if (farbe[position].equals("ro")){untenlinks.setBackgroundColor(0xffff0000);}


        return rowView;
    }



}
