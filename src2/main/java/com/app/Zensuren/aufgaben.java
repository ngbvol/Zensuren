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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 07.06.14.
 */
public class aufgaben extends ListActivity {
    DataManipulator dm;

    List<String[]> list = new ArrayList<String[]>();
    List<String[]> aufgaben = null;
    String kursnummer;
    String[] stg1;
    String[] stg2;
    ArrayAdapter<String> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aufgabenwahl);
        kursnummer = getIntent().getStringExtra("pkurs");

        dm = new DataManipulator(this);
        aufgaben = dm.aufgabenliste(kursnummer);
        stg1 = new String[aufgaben.size()];
        stg2 = new String[aufgaben.size()];

        int x = 0;
        String stg;

        for (String[] aufgabe : aufgaben) {
            stg1[x] = aufgabe[1];
            stg2[x] = aufgabe[0];
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

                AlertDialog.Builder builder = new AlertDialog.Builder(aufgaben.this);

                builder.setTitle("Eintrag löschen");
                builder.setMessage("Sind Sie sicher?");

                builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        dm.aufgabeloeschen(stg2[position]);
                        dialog.dismiss();
                        aufgaben.this.finish();
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

                return true;
            }
        });


    }

    public void onListItemClick(ListView parent, View v, int position, long id) {
        // speichern: stg2[position]
        Intent i9 = new Intent(this, abhaken.class);
        i9.putExtra("paufgabennummer", stg2[position]);
        i9.putExtra("pkurs", kursnummer);
        i9.putExtra("paufgabentext", stg1[position]);
        startActivity(i9);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.aufgabenmenue, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.neueaufg:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("Neue Aufgabe");
                alert.setMessage("Bitte Titel eingeben");

// Set an EditText view to get user input
                final EditText name = new EditText(this);
                alert.setView(name);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String sname = name.getText().toString();
                        dm.aufgabeanlegen(sname, kursnummer);
                        aufgaben.this.finish();
                        startActivity(getIntent());
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
                break;
            case R.id.neuschriftlich:
                dm.aufgabeanlegen("schriftlich",kursnummer);
                startActivity(this.getIntent());
                this.finish();
                break;
        }
        return true;
    }

}