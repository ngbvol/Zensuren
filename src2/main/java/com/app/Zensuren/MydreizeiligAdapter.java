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
 * Created by tim on 01.03.15.
 */
public class MydreizeiligAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] names;
    private final String[] descriptions;
    private final String[] noten;
    private final String[] notenart;
    private final String[] zeile0;

    public MydreizeiligAdapter(Activity context, String[] oben, String[] unten, String[] noten, String[] notenart, String[] zeile00) {
        super(context, R.layout.dreizeilig, oben);
        this.context = context;
        this.names = oben;
        this.noten = noten;
        this.notenart = notenart;
        this.descriptions = unten;
        this.zeile0 = zeile00;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.dreizeilig, null, true);

        TextView zeile0view = (TextView) rowView.findViewById(R.id.zeile0);
        String z = zeile0[position];
        zeile0view.setText(z);

        TextView noteView = (TextView) rowView.findViewById(R.id.note);
        String n = noten[position];
//        if(n.equals("5")){
//            noteView.setTextColor(Color.parseColor("#FF00FF"));
//        }else{
//            noteView.setTextColor(Color.parseColor("#EEEEEE"));
//        }
        noteView.setText(n);


        TextView descriptionView = (TextView) rowView.findViewById(R.id.description);
        String t = descriptions[position];
        descriptionView.setText(t);

        TextView nameView = (TextView) rowView.findViewById(R.id.name);

        int i = Integer.parseInt(notenart[position]);
        // Wenn die Icons abhängig vom darzustellenden Objekt angepasst werden sollen
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        switch (i) {
            case 0:
                imageView.setImageResource(R.drawable.stift);
                nameView.setTextColor(Color.parseColor("#EEEEEE"));
                break;
            case 1:
                imageView.setImageResource(R.drawable.negativ);
                nameView.setTextColor(Color.parseColor("#FF0000"));
                break;
            case 2:
                imageView.setImageResource(R.drawable.note);
                nameView.setTextColor(Color.parseColor("#FFFF00"));
                break;
            case 3:
                imageView.setImageResource(R.drawable.positiv);
                nameView.setTextColor(Color.parseColor("#00FF00"));
                break;
            case 4:
                imageView.setImageResource(R.drawable.wichtig);
                nameView.setTextColor(Color.parseColor("#0000FF"));
                break;
            case 10:
                imageView.setImageResource(R.drawable.klausur);
                nameView.setTextColor(Color.parseColor("#222266"));
                break;
            case 11:
                imageView.setImageResource(R.drawable.klausur);
                nameView.setTextColor(Color.parseColor("#882222"));
                break;
            case 13:
                imageView.setImageResource(R.drawable.klausur);
                nameView.setTextColor(Color.parseColor("#FF8800"));
                break;
            case 99:
                imageView.setImageResource(R.drawable.stift);
                nameView.setTextColor(Color.parseColor("#ffbdd5ff"));
                break;
            default:
                imageView.setImageResource(R.drawable.klausur);
                nameView.setTextColor(Color.parseColor("#999999"));
                noteView.setTextColor(Color.parseColor("#999999"));

        }

        String s = names[position];
        nameView.setText(s);



        return rowView;
    }

}
