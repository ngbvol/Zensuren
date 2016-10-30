package com.app.Zensuren;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 21.05.14.
 */
public class SusZuteilen extends ListActivity implements View.OnClickListener {
    public int idToModify;
    DataManipulator dm;

    List<String[]> list = new ArrayList<String[]>();
    List<String[]> names2 =null ;
    String kursnummer;
    String[] kursdaten;
    String[] stg1;
    String[] stg2;
    String[] stg3;
    String[] stg4;
    ArrayAdapter<String> adapter;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.susliste);
        kursnummer = getIntent().getStringExtra("pkurs");
        kursdaten = getIntent().getStringExtra("pkursdaten").split(" - ");

        dm = new DataManipulator(this);
        names2 = dm.susliste();

        stg1=new String[names2.size()];
        stg2=new String[names2.size()];
        stg3=new String[names2.size()];
        stg4=new String[names2.size()];


        View button1Click = findViewById(R.id.button1);
        button1Click.setOnClickListener(this);

        int x=0;
        String stg;

        for (String[] name : names2) {
            if (dm.istinkurs(name[2],kursnummer)) {
                stg = "X  " + name[1] + " " + name[0];
            }else{
                stg = "   " + name[1] + " " + name[0];
            }

            stg1[x]=stg;
            stg2[x]=name[2];
            stg3[x]=name[0];
            stg4[x]=name[1];

            x++;
        }

        adapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_list_item_1,
                stg1);
        this.setListAdapter(adapter);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int position, long id) {

/*                AlertDialog.Builder builder = new AlertDialog.Builder(SusZuteilen.this);

                builder.setTitle("Schüler löschen");
                builder.setMessage("Sind Sie sicher? \n Damit werden auch alle Noten der Schülerin oder des Schülers gelöscht!");

                builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        DataManipulator dh = new DataManipulator(SusZuteilen.this);
                        dh.loeschesus(stg2[position]);
                        dialog.dismiss();
                        SusZuteilen.this.finish();
                        startActivity(getIntent());
                    }

                });

                builder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

                return true;*/
                AlertDialog.Builder alert = new AlertDialog.Builder(SusZuteilen.this);

                alert.setTitle("Schüler bearbeiten / löschen");
                alert.setMessage("Bitte Name, Vorname korrigieren");

// Set an EditText view to get user input
                final EditText name = new EditText(SusZuteilen.this);
                alert.setView(name);
                name.setText(stg3[position]+","+stg4[position]);

                alert.setPositiveButton("ändern", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String sname = name.getText().toString();
                        String[] snamen = sname.split(",");
                        dm.updatesus(stg2[position],snamen[0], snamen[1]);
                    }
                });

                alert.setNegativeButton("löschen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dm.loeschesus(stg2[position]);
                        dialog.dismiss();
                        SusZuteilen.this.finish();
                        startActivity(getIntent());
                    }
                });

                alert.show();
                return true;
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.kursbearbeitenmenu, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.halbjahr:
                AlertDialog.Builder alert1 = new AlertDialog.Builder(this);

                alert1.setTitle("neues Schuljahr:");
                alert1.setMessage("Bitte neues Schuljahr, neue Bezeichnung eingeben:");

// Set an EditText view to get user input
                final EditText sjn = new EditText(this);
                alert1.setView(sjn);
                String aufschrift = kursdaten[0] + "," + kursdaten[1];
                sjn.setText(aufschrift);


                alert1.setPositiveButton("kopieren", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        kursdaten = sjn.getText().toString().split(",");
                        dm.uebertragekurs(kursnummer, kursdaten[0], kursdaten[1]);

                    }
                });
                alert1.setNeutralButton("aendern", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        kursdaten = sjn.getText().toString().split(",");
                        dm.setkursdaten(kursnummer, kursdaten[0], kursdaten[1]);

                    }
                });

                alert1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert1.show();
                break;
            case R.id.stundenplan:
                AlertDialog.Builder alert2 = new AlertDialog.Builder(this);

                alert2.setTitle("Stunden eingeben:");
                alert2.setMessage("z.B.: 12,53 - Montag 2. und Freitag 3.");

// Set an EditText view to get user input
                final EditText sjn2 = new EditText(this);
                alert2.setView(sjn2);


                alert2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int eintragsnummer = Integer.parseInt(kursnummer)+1000;
                        dm.seteinstellung2(eintragsnummer,sjn2.getText().toString());

                    }
                });

                alert2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert2.show();
                break;
            case R.id.hauptnoten:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Wie sollen die 5 Hauptnoten benannt werden?");
                builder.setItems(new CharSequence[]
                                {"K1-K2-SM1-SM2-Z", "KA1-KA2-KA3-KA4-Z", "Q1-Q2- - -Z", " - - - - "},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                int einstellungsnummer = Integer.parseInt(kursnummer);
                                einstellungsnummer = einstellungsnummer + 1000;
                                switch (which) {
                                    case 0:
                                        dm.seteinstellung(einstellungsnummer, "K1-K2-SM1-SM2-Z");
                                        break;
                                    case 1:
                                        dm.seteinstellung(einstellungsnummer, "KA1-KA2-KA3-KA4-Z");
                                        break;
                                    case 2:
                                        dm.seteinstellung(einstellungsnummer, "Q1-Q2- - -Z");
                                        break;
                                    case 3:
                                        dm.seteinstellung(einstellungsnummer, " - - - - ");
                                        break;
                                }
                            }
                        });
                builder.create().show();
                break;
            case R.id.loeschekurs:
                AlertDialog.Builder alert3 = new AlertDialog.Builder(this);

                alert3.setTitle("Achtung Kurs wird gelöscht!");
                alert3.setMessage("Auch alle Noten sind dann weg. Sind Sie sicher?");

// Set an EditText view to get user input

                alert3.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dm.loeschekurs(kursnummer);

                    }
                });

                alert3.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert3.show();
                break;
            case R.id.importliste:
                Intent i17 = new Intent(this, FileChooser.class);
                i17.putExtra("pkurs", kursnummer);
                startActivityForResult(i17, 1);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // code for result
                String pfad = data.getStringExtra("pfad");
                Toast.makeText(SusZuteilen.this,"!!"+pfad,Toast.LENGTH_SHORT);
                File infile = new File(pfad);
                try {
                    BufferedReader br = new BufferedReader(new FileReader(infile));
                    String line;
                    String[] zerteilt;
                    int susid;
                    while ((line = br.readLine()) != null) {
                        zerteilt = line.split(",");
                        susid = dm.suseintrag(zerteilt[0],zerteilt[1]);
                        dm.weisekurszu(Integer.toString(susid),kursnummer);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this,"Fahler",Toast.LENGTH_SHORT);
                // Write your code on no result return
            }
        }
    }


    public void onListItemClick(ListView parent, View v, int position, long idl) {
        int id = (int) idl;
        if (dm.istinkurs(stg2[id],kursnummer)) {
            dm.entferneauskurs(stg2[id],kursnummer);
        }else{
            dm.weisekurszu(stg2[id],kursnummer);
        }
        adapter.notifyDataSetChanged();
        this.recreate();

    }

    public void startanzeige(int position){

    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.button1:
                this.finish();
                break;
        }
    }


}
