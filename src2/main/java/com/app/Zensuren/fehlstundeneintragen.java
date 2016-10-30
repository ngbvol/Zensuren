package com.app.Zensuren;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 26.06.14.
 */
public class fehlstundeneintragen extends ListActivity {
    public int idToModify;
    DataManipulator dm;

    List<String[]> list = new ArrayList<String[]>();
    List<String[]> names2 = null;
    String kursnummer;
    String kursname;
    String datum;
    String fehlende;
    String[] stg1;
    String[] stg2;
    ArrayAdapter<String> adapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fehlstundeneintraglayout);
        kursnummer = getIntent().getStringExtra("kursnummer");
        kursname = getIntent().getStringExtra("kursname");
        datum = getIntent().getStringExtra("datum");
        fehlende = getIntent().getStringExtra("fehlende");


        dm = new DataManipulator(this);
        names2 = dm.kursliste(kursnummer);

        stg1 = new String[names2.size()];
        stg2 = new String[names2.size()];

        TextView vfehlende = (TextView) findViewById(R.id.fehlende);
        vfehlende.setText(fehlende);
        TextView vkurs = (TextView) findViewById(R.id.tvkurs);
        vkurs.setText(kursname + " am: "+ datum);

        int x = 0;
        String stg;

        for (String[] name : names2) {
            stg = "   " + name[1] + " " + name[0];

            stg1[x] = stg;
            stg2[x] = name[3];

            x++;
        }

        adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1,
                stg1);
        this.setListAdapter(adapter);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int position, long id) {
                dm.noteneintrag(kursnummer,stg2[position],"11","-1","Fehlstunde",datum);
                fehlstundeneintragen.this.finish();


                return true;
            }
        });
    }

    public void onListItemClick(ListView parent, View v, int position, long idl) {
        dm.noteneintrag(kursnummer,stg2[position],"10","-1","Fehlstunde (e)",datum);
        this.finish();

    }



}
