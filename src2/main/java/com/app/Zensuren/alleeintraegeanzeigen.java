package com.app.Zensuren;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by tim on 02.03.15.
 */
public class alleeintraegeanzeigen extends ListActivity {
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
    MydreizeiligAdapter adapter4;

    String kursnummer;
    String kursname;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kursliste);
        kursnummer = getIntent().getStringExtra("pkurs");
        kursname = getIntent().getStringExtra("pkursname");

        dm = new DataManipulator(this);

        names2 = dm.neuestenoten(kursnummer);
        int x=0;
        stg1=new String[names2.size()];
        stg2=new String[names2.size()];
        stg3=new String[names2.size()];
        stg4=new String[names2.size()];
        stg5=new String[names2.size()];
        for (String[] name : names2) {
            //stg = name[1]+" - "+name[2]+" - "+name[3];
            stg1[x]=name[2];
            stg2[x]=name[0];
            stg3[x]=dm.punktezunote(name[3]);
            stg4[x]=name[1];
            stg5[x]=name[4];
            x++;
        }
        Collections.reverse(Arrays.asList(stg1));
        Collections.reverse(Arrays.asList(stg2));
        Collections.reverse(Arrays.asList(stg3));
        Collections.reverse(Arrays.asList(stg4));
        Collections.reverse(Arrays.asList(stg5));
        adapter4 = new MydreizeiligAdapter(this, stg1, stg2, stg3, stg4, stg5);
        this.setListAdapter(adapter4);


//        adapter = new MyKurslistenAdapter(this, stg1, stsm1, stk1, stsm2, stk2, stz);
//        this.setListAdapter(adapter);


    }

    public void onListItemClick(ListView parent, View v, int position, long id) {
    }


}
