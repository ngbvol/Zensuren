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
 * Created by tim on 26.05.14.
 */
public class MyKurslistenAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] name;
    private final String[] sm1;
    private final String[] sm2;
    private final String[] k1;
    private final String[] k2;
    private final String[] z;
    private final String[] farbe;
    private int listpos;

    public MyKurslistenAdapter(Activity context, String[] name, String[] sm1, String[] k1, String[] sm2, String[] k2, String[] z, String[] farbe) {
        super(context, R.layout.kurslistenlayout, name);
        this.context = context;
        this.name = name;
        this.sm1 = sm1;
        this.k1 = k1;
        this.sm2 = sm2;
        this.k2 = k2;
        this.z = z;
        this.farbe = farbe;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.kurslistenlayout, null, true);

        TextView nameView = (TextView) rowView.findViewById(R.id.name);
        String n = name[position];
        nameView.setText(Html.fromHtml(n));

        TextView nummerView = (TextView) rowView.findViewById(R.id.nummer);
        listpos = position +1;
        nummerView.setText(Integer.toString(listpos));

        TextView sm1View = (TextView) rowView.findViewById(R.id.tsm1);
        sm1View.setTextColor(Color.parseColor(getfarbe(sm1[position])));
        sm1View.setText(sm1[position]);

        TextView sm2View = (TextView) rowView.findViewById(R.id.tsm2);
        sm2View.setTextColor(Color.parseColor(getfarbe(sm2[position])));
        sm2View.setText(sm2[position]);
        TextView k1View = (TextView) rowView.findViewById(R.id.tk1);
        k1View.setTextColor(Color.parseColor(getfarbe(k1[position])));
        k1View.setText(k1[position]);
        TextView k2View = (TextView) rowView.findViewById(R.id.tk2);
        k2View.setTextColor(Color.parseColor(getfarbe(k2[position])));
        k2View.setText(k2[position]);
        TextView zView = (TextView) rowView.findViewById(R.id.tz);
        zView.setTextColor(Color.parseColor(getfarbe(z[position])));
        zView.setText(z[position]);
        TextView untenlinks = (TextView) rowView.findViewById(R.id.untenlinks);
        untenlinks.setBackgroundColor(0xff000000);
        if (farbe[position].equals("gr")){untenlinks.setBackgroundColor(0xff00ff00);}
        if (farbe[position].equals("ge")){untenlinks.setBackgroundColor(0xffffff00);}
        if (farbe[position].equals("ro")){untenlinks.setBackgroundColor(0xffff0000);}

        return rowView;
    }

    public String getfarbe(String note){
        String farbezurueck = "#DDDDDD";
        if(note.endsWith("NN")){
            farbezurueck = "#111111";
        }else{
            if(note.endsWith("4-")|note.endsWith("5+")|note.endsWith("5")|note.endsWith("5-")|note.endsWith("6")){
                farbezurueck = "#FF0000";
            }
        }
        return farbezurueck;
    }

}
