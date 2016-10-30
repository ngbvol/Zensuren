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
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by tim on 15.05.14.
 */

public class Kursliste extends ListActivity {
    TextView selection;
    public int idToModify;
    DataManipulator dm;

    List<String[]> list = new ArrayList<String[]>();
    List<String[]> names2 =null ;
    String[] stg1;
    String[] stg2;
    String[] stg3;
    String[] stsm1;
    String[] stsm2;
    String[] stk1;
    String[] stk2;
    String[] stz;
    String[] stfarbe;
    String[] stnotenliste;
    String[] stexp;
    List<String[]> exportliste;
    MyKurslistenAdapter adapter;
    MyKursnotenlistenadapter adapter3;
    MydreizeiligAdapter adapter4;

    String kursnummer;
    String kursname;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kursliste);
        kursnummer = getIntent().getStringExtra("pkurs");
        kursname = getIntent().getStringExtra("pkursname");

        dm = new DataManipulator(this);
        names2 = dm.kursliste(kursnummer);

        stg1=new String[names2.size()];
        stg2=new String[names2.size()];
        stg3=new String[names2.size()];
        stsm1=new String[names2.size()];
        stsm2=new String[names2.size()];
        stk1=new String[names2.size()];
        stk2=new String[names2.size()];
        stz=new String[names2.size()];
        stfarbe=new String[names2.size()];
        stnotenliste=new String[names2.size()];
        stexp=new String[7];
        exportliste=new ArrayList<String[]>();

        int x=0;
        String stg;

        for (String[] name : names2) {
            stg = name[1]+" "+name[0];
            stg1[x]=stg;
            stg2[x]=name[2];
            stg3[x]=name[3];
            x++;
        }

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this,android.R.layout.simple_list_item_1,
                stg1);
        this.setListAdapter(adapter2);
//        adapter = new MyKurslistenAdapter(this, stg1, stsm1, stk1, stsm2, stk2, stz);
//        this.setListAdapter(adapter);

        getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long id) {

                Intent i55 = new Intent(Kursliste.this, Anzeigesus.class);
                i55.putExtra("pkursanzeige", kursnummer);
                i55.putExtra("psusanzeige", stg3[position]);
                i55.putExtra("susname", stg1[position]);
                startActivity(i55);
                return true;
            }
        });

    }

    public void onListItemClick(ListView parent, View v, int position, long id) {
        Intent i4 = new Intent(this, Noteneingabe.class);
        i4.putExtra("pkurs_id", kursnummer);
        i4.putExtra("psus_id", stg3[position]);
        i4.putExtra("susname", stg1[position]);
        startActivity(i4);
    }

    public void bereitelistenstringsvor(){
        int y=0;
        String stg;
        exportliste.clear();

        for (String[] name : names2) {
            stexp=new String[10];
            stg = name[1]+" "+name[0];
            String hauptnotenart = dm.geteinstellung(Integer.parseInt(kursnummer)+1000);
            if(hauptnotenart.equals(""))hauptnotenart = "K1-K2-SM1-SM2-Z";
            String[] hauptnoten = hauptnotenart.split("-");
            if(dm.hatschriftlich(name[3],kursnummer)){
                stg = "<font color = '#FFFF00'>" + stg + "</font>";
            }
            stg1[y]=stg;
            stg2[y]=name[2];
            stg3[y]=name[3];
            stsm1[y]=hauptnoten[0] + ": " + dm.getsondernote(kursnummer, name[3], "5");
            stk1[y]=hauptnoten[1] + ": " + dm.getsondernote(kursnummer, name[3], "6");
            stsm2[y]=hauptnoten[2] + ": " + dm.getsondernote(kursnummer, name[3], "7");
            stk2[y]=hauptnoten[3] + ": " + dm.getsondernote(kursnummer, name[3], "8");
            stz[y]=hauptnoten[4] + ": " + dm.getsondernote(kursnummer, name[3], "9");
            stfarbe[y]= dm.getsondernote(kursnummer, name[3],"80");
            stexp[0]=Integer.toString(y+1);
            stexp[1]=stg;
            stexp[2]=dm.getsondernote(kursnummer, name[3], "5");
            stexp[3]=dm.getsondernote(kursnummer, name[3], "6");
            stexp[4]=dm.getsondernote(kursnummer, name[3], "7");
            stexp[5]=dm.getsondernote(kursnummer, name[3], "8");
            stexp[6]=dm.getsondernote(kursnummer, name[3], "9");
            stexp[7]=dm.notezupunkte(dm.getsondernote(kursnummer, name[3], "9"));
            stexp[8]=Integer.toString(dm.zaehlefehlstunden(stg3[y],kursnummer,1));
            stexp[9]=Integer.toString(dm.zaehlefehlstunden(stg3[y],kursnummer,0));
            exportliste.add(stexp);
            stnotenliste[y]=dm.getnotenstring(kursnummer, name[3]);
            y++;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.kurslistenmenue, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.zufall:
                java.util.Collections.shuffle(names2);
                bereitelistenstringsvor();
                adapter = new MyKurslistenAdapter(this, stg1, stsm1, stk1, stsm2, stk2, stz, stfarbe);
                this.setListAdapter(adapter);
                break;
/*            case R.id.alphabetisch:
                names2 = dm.kursliste(kursnummer);
                bereitelistenstringsvor();
                adapter = new MyKurslistenAdapter(this, stg1, stsm1, stk1, stsm2, stk2, stz);
                this.setListAdapter(adapter);
                break;*/
            case R.id.zeigenoten:
                names2 = dm.kursliste(kursnummer);
                bereitelistenstringsvor();
                for(int i=0;i<stg1.length;i++){
                    stg1[i]+=" --- ("+dm.zaehlefehlstunden(stg3[i],kursnummer,1)+" / "+dm.zaehlefehlstunden(stg3[i],kursnummer,0)+")";
                }
                adapter3 = new MyKursnotenlistenadapter(this, stg1, stnotenliste, stfarbe);
                this.setListAdapter(adapter3);
                break;
            case R.id.aufgaben:
                Intent i8 = new Intent(this, aufgaben.class);
                i8.putExtra("pkurs", kursnummer);
                startActivity(i8);
                break;
            case R.id.sitzplan:
                Intent i19 = new Intent(this, Sitzplan.class);
                i19.putExtra("pkurs", kursnummer);
                i19.putExtra("pkursname", kursname);
                startActivity(i19);
                break;
            case R.id.kursmappe:
                Intent i9 = new Intent(this, kursbucheintragen.class);
                i9.putExtra("pkurs", kursnummer);
                i9.putExtra("pkursname", kursname);
                i9.putExtra("pdatum", "");
                startActivity(i9);
                break;
            case R.id.kursmappe2:
                Intent i11 = new Intent(this, kursbuchansehen.class);
                i11.putExtra("pkurs", kursnummer);
                i11.putExtra("pkursname", kursname);
                startActivity(i11);
                break;
            case R.id.statistik:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Notenstatistik");

                WebView wv = new WebView(this);
                wv.loadData(dm.statistik(kursnummer),"text/html", null);

                alert.setView(wv);
                alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                alert.show();
                break;
            case R.id.export:
                bereitelistenstringsvor();
                String hauptnotenart = dm.geteinstellung(Integer.parseInt(kursnummer)+1000);
                if(hauptnotenart.equals(""))hauptnotenart = "K1-K2-SM1-SM2-Z";
                String exphead = "Nr;Name;"+hauptnotenart.replace("-",";")+";Punkte;FSG;FSU";
                // dm.speicherliste(exportliste,10,exphead,"notenliste-"+kursname+".csv");
                String dateiname = kursname.substring(0, kursname.indexOf("(")-3);
                dm.speicherliste(dm.exportnotenliste(kursnummer),5,"Vorname;Name;Note;Bemerkung;Datum","gesamtliste-"+kursname+".csv");
                Toast.makeText(this,"Liste gespeichert",Toast.LENGTH_SHORT);
                break;
            case R.id.allenoten:
                // Muss in eigene Activity!
                Intent i2 = new Intent(this, alleeintraegeanzeigen.class);
                i2.putExtra("pkurs", kursnummer);
                i2.putExtra("pkursname", kursname);
                startActivity(i2);
                break;
            case R.id.hauptnoten:
                names2 = dm.kursliste(kursnummer);
                bereitelistenstringsvor();

                for(int i=0;i<stg1.length;i++){
                    stg1[i]+=" --- ("+dm.zaehlefehlstunden(stg3[i],kursnummer,1)+" / "+dm.zaehlefehlstunden(stg3[i],kursnummer,0)+")";
                }
                adapter = new MyKurslistenAdapter(this, stg1, stsm1, stk1, stsm2, stk2, stz, stfarbe);
                this.setListAdapter(adapter);
        }
        return true;
    }




}
