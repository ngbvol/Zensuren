package com.app.Zensuren;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 08.06.14.
 */
public class abhaken extends ListActivity {
    TextView selection;
    public int idToModify;
    DataManipulator dm;

    List<String[]> list = new ArrayList<String[]>();
    List<String[]> names2 =null ;
    String[] stg1;
    String[] stg2;
    String[] stg3;

    abhaklistenadapter adapter;

    String kursnummer;
    String aufgabennummer;
    String aufgabentext;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abhakliste);
        kursnummer = getIntent().getStringExtra("pkurs");
        aufgabennummer = getIntent().getStringExtra("paufgabennummer");
        aufgabentext = getIntent().getStringExtra("paufgabentext");
        TextView aufgabentitel = (TextView) findViewById(R.id.textView);
        aufgabentitel.setText(aufgabentext);

        dm = new DataManipulator(this);
        names2 = dm.kursliste(kursnummer);

        stg1=new String[names2.size()];
        stg2=new String[names2.size()];
        stg3=new String[names2.size()];


        int x=0;
        String stg;

        for (String[] name : names2) {
            stg = name[1]+" "+name[0];
            stg1[x]=stg;
            stg2[x]=name[2];
            stg3[x]=name[3];
            x++;
        }

        adapter = new abhaklistenadapter(this, dm, stg1, stg3, kursnummer,aufgabennummer);
        this.setListAdapter(adapter);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long id) {


                return true;
            }
        });

    }

    public void onListItemClick(ListView parent, View v, int position, long id) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH:mm:ss");
        String zeit = sdf.format(new Date());
        if(dm.haterledigt(stg3[position],aufgabennummer).equals("")){
            dm.erledigen(stg3[position],aufgabennummer, zeit);
        }else{
            dm.entledigen(stg3[position],aufgabennummer);
        }
        adapter.notifyDataSetChanged();
    }






}
