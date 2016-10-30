package com.app.Zensuren;

/**
 * Created by tim on 20.05.14.
 */
import android.app.Activity;
import android.graphics.Color;
import android.widget.ArrayAdapter;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

public class MyArrayAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] names;
    private final String[] descriptions;
    private final String[] noten;
    private final String[] notenart;

    public MyArrayAdapter(Activity context, String[] oben, String[] unten, String[] noten, String[] notenart) {
        super(context, R.layout.rowlayout, oben);
        this.context = context;
        this.names = oben;
        this.noten = noten;
        this.notenart = notenart;
        this.descriptions = unten;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.rowlayout, null, true);

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
            case 6:
                imageView.setImageResource(R.drawable.klausur);
                nameView.setTextColor(Color.parseColor("#CCCC00"));
                break;
            case 8:
                imageView.setImageResource(R.drawable.klausur);
                nameView.setTextColor(Color.parseColor("#CCCC00"));
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