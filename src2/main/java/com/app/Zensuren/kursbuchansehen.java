package com.app.Zensuren;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 11.06.14.
 */
public class kursbuchansehen extends ListActivity {
    TextView selection;
    public int idToModify;
    DataManipulator dm;

    List<String[]> list = new ArrayList<String[]>();
    List<String[]> names2 =null ;
    String[] stg1;
    String[] stg2;
    String[] stg3;
    String[] stg4;
    String[] stg5;

    kursmappenadapter adapter;

    String kursnummer;
    String kursname;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abhakliste);
        kursnummer = getIntent().getStringExtra("pkurs");
        kursname = getIntent().getStringExtra("pkursname");
        TextView aufgabentitel = (TextView) findViewById(R.id.textView);
        aufgabentitel.setText(kursname);

        dm = new DataManipulator(this);
        names2 = dm.kursmappenliste(kursnummer);

        stg1=new String[names2.size()];
        stg2=new String[names2.size()];
        stg3=new String[names2.size()];
        stg4=new String[names2.size()];
        stg5=new String[names2.size()];


        int x=0;

        for (String[] name : names2) {
            stg1[x]=name[0];
            stg2[x]=name[1];
            stg3[x]=name[2];
            stg4[x]=name[3];
            stg5[x]=name[4];
            x++;
        }

        adapter = new kursmappenadapter(this, kursnummer, stg2, stg3, stg4, stg5);
        this.setListAdapter(adapter);


        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long id) {

                Intent i12 = new Intent(kursbuchansehen.this, kursbuchbearbeiten.class);
                i12.putExtra("peid", stg1[position]);
                startActivityForResult(i12,2);
                return true;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            startActivity(getIntent());
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.kursmappenmenue, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.export:
                Collections.reverse(names2);
                dm.speicherliste(names2,6,"Eintrag-ID;Datum;Stundenzahl;Thema;Hausaufgabe;Fehlende","kursmappe-"+kursname+".csv");
                Collections.reverse(names2);
                break;
        }
        return true;
    }


    public void onListItemClick(ListView parent, View v, int position, long id) {
        String fehlende=dm.getfehlende(stg2[position],kursnummer);
        Intent i18 = new Intent(this, fehlstundeneintragen.class);
        i18.putExtra("fehlende", fehlende);
        i18.putExtra("kursnummer", kursnummer);
        i18.putExtra("kursname", kursname);
        i18.putExtra("datum", stg2[position]);
        startActivity(i18);
    }






}
