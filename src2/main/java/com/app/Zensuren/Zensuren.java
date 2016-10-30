package com.app.Zensuren;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.app.ListActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import android.app.ActionBar;
import android.widget.Toast;

public class Zensuren extends ListActivity implements OnClickListener {
    DataManipulator dm;

    List<String[]> list = new ArrayList<String[]>();
    List<String[]> names2 =null ;
    String[] stg1;
    String[] stg2;
    String schuljahr;
    Button modbutton;
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		View button1Click = findViewById(R.id.button1);
		button1Click.setOnClickListener(this);
        View buttonmodus = findViewById(R.id.button101);
        buttonmodus.setOnClickListener(this);


        Button sjbutton = (Button) findViewById(R.id.button1);
        modbutton = (Button) findViewById(R.id.button101);


        dm = new DataManipulator(this);

        schuljahr = dm.geteinstellung(1);
        if(schuljahr.equals("")){
            schuljahr = "*";
        }
        names2 = dm.listekurse(schuljahr);
        stg1=new String[names2.size()];
        stg2=new String[names2.size()];

        sjbutton.setText("Schuljahr: " + schuljahr);
        if(dm.geteinstellung(2).equals("1")){
            modbutton.setText("auto");
        }else{
            modbutton.setText("normal");
        }

        int x=0;
        String stg;

        for (String[] name : names2) {
            stg = name[1]+" - "+name[2]+" - ("+name[3]+"/"+name[4]+")";

            stg1[x]=stg;
            stg2[x]=name[0];
            x++;
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_list_item_1,
                stg1);
        this.setListAdapter(adapter);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long id) {
                // Toast.makeText(getApplicationContext(), "Long Clicked : ", Toast.LENGTH_LONG).show();

                Intent i6 = new Intent(Zensuren.this, SusZuteilen.class);
                i6.putExtra("pkurs", stg2[position]);
                i6.putExtra("pkursdaten", stg1[position]);
                startActivity(i6);
                return true;
            }
        });
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int wochentag = c.get(Calendar.DAY_OF_WEEK);
        if(modbutton.getText().equals("auto")&&c.SATURDAY != wochentag && c.SUNDAY != wochentag){
            String pdatum = "";
            if(getstunde()>0) {
                pdatum = "s" + Integer.toString(getstunde());
            }
            Intent i22 = new Intent(this, Stundenplan.class);
            i22.putExtra("pdatum", pdatum);
            startActivity(i22);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionzensuren, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.aaddperson:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("Neuer Schüler");
                alert.setMessage("Bitte Name, Vorname eingeben");

// Set an EditText view to get user input
                final EditText name = new EditText(this);
                alert.setView(name);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String sname = name.getText().toString();
                        String[] snamen = sname.split(",");
                        dm.suseintrag(snamen[0], snamen[1]);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();

                return true;

            case R.id.aaddgroup:
                Intent i2 = new Intent(this, LegeKursan.class);
                startActivity(i2);

                return true;
            case R.id.astundenplan:
                Intent i22 = new Intent(this, Stundenplan.class);
                i22.putExtra("pdatum", "");
                startActivity(i22);
                return true;
            case R.id.aexportnoten:
                for (int x=0;x<names2.size();x++) {
                    String kursnummer = stg2[x];
                    List<String[]> names3 = dm.kursliste(kursnummer);
                    int y=0;
                    List<String[]> exportliste=new ArrayList<String[]>();
                    exportliste.clear();
                    for (String[] namea : names3) {
                        String[] stexp = new String[13];
                        String stg = namea[1] + " " + namea[0];
                        String hauptnotenart = dm.geteinstellung(Integer.parseInt(kursnummer) + 1000);
                        if (hauptnotenart.equals("")) hauptnotenart = "K1-K2-SM1-SM2-Z";
                        String[] sondernoten = dm.getsondernoten(kursnummer,namea[3]);
                        for(int i=0; i<5; i++){
                            if(sondernoten[i].equals(""))sondernoten[i]="20";
                        }

                        int kges = Integer.parseInt(dm.notezupunkte(sondernoten[0]))+Integer.parseInt(dm.notezupunkte(sondernoten[1]));
                        int smges = Integer.parseInt(dm.notezupunkte(sondernoten[2]))+Integer.parseInt(dm.notezupunkte(sondernoten[3]));
                        int zges = Integer.parseInt(dm.notezupunkte(sondernoten[4]));
                        if((kges+smges)<(zges*4)){
                            kges = (int) Math.ceil((double) kges/2);
                        }else{
                            kges = (int) Math.floor(kges/2);
                        }
                        if(kges>15){
                            smges = zges;
                        }else {
                            if (smges + kges * 2 < zges * 4) {
                                smges = (int) Math.ceil(smges / 2);
                            } else {
                                smges = (int) Math.floor(smges / 2);
                            }
                        }

                        stexp[0] = Integer.toString(y + 1);
                        stexp[1] = stg;
                        stexp[2] = "";
                        if(dm.hatschriftlich(namea[3],kursnummer)){
                            stexp[2]="X";
                        }
                        stexp[3] = sondernoten[0];
                        stexp[4] = sondernoten[1];
                        stexp[5] = dm.punktezunote(Integer.toString(kges));
                        stexp[6] = sondernoten[2];
                        stexp[7] = sondernoten[3];
                        stexp[8] = dm.punktezunote(Integer.toString(smges));
                        stexp[9] = sondernoten[4];
                        stexp[10] = dm.notezupunkte(sondernoten[4]);
                        stexp[11]=Integer.toString(dm.zaehlefehlstunden(namea[3],kursnummer,1));
                        stexp[12]=Integer.toString(dm.zaehlefehlstunden(namea[3],kursnummer,0));
                        exportliste.add(stexp);
                        y++;
                    }
                    String hauptnotenart = dm.geteinstellung(Integer.parseInt(kursnummer)+1000);
                    if(hauptnotenart.equals(""))hauptnotenart = "K1-K2-SM1-SM2-Z";
//                    String exphead = "Nr;Name;schriftlich;"+hauptnotenart.replace("-",";")+";Punkte;FSG;FSU";
                    String exphead = "Nr;Name;schriftlich;K1;K2;K gesamt;SM1;SM2;SM gesamt;Endnote;Punkte;FSG;FSU";
                    String dateiname = stg1[x].substring(0, stg1[x].indexOf("(")-3);
                        dm.speicherliste(exportliste, 13, exphead, "notenliste-" + dateiname + ".csv");

                }

                return true;
            case R.id.punkteliste:
                AlertDialog.Builder alert1 = new AlertDialog.Builder(this);

                alert1.setTitle("Punktegrenzen");
                alert1.setMessage("Bitte Maximalpunktzahl eingeben:");

// Set an EditText view to get user input
                final NumberPicker np = new NumberPicker(this);
                alert1.setView(np);
                np.setMinValue(1);
                np.setMaxValue(200);
                np.setValue(100);
                //final EditText name1 = new EditText(this);
                //alert1.setView(name1);

                alert1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //int maxpunkte = Integer.parseInt(name1.getText().toString());
                        int maxpunkte = np.getValue();
                        String ausgabe="<html><body><table><tr><th>Note</th><th>ab Punktzahl</th></tr>";
                        ausgabe = ausgabe + "<tr bgcolor=\"#00FF00\"><td>1+</td><td>"+ String.format("%.0f", maxpunkte*0.95) + "</td></tr>";
                        ausgabe = ausgabe + "<tr bgcolor=\"#00FF00\"><td>1</td><td>"+ String.format("%.0f", maxpunkte*0.9) + "</td></tr>";
                        ausgabe = ausgabe + "<tr bgcolor=\"#00FF00\"><td>1-</td><td>"+ String.format("%.0f", maxpunkte*0.85) + "</td></tr>";
                        ausgabe = ausgabe + "<tr bgcolor=\"#99FF99\"><td>2+</td><td>"+ String.format("%.0f", maxpunkte*0.8) + "</td></tr>";
                        ausgabe = ausgabe + "<tr bgcolor=\"#99FF99\"><td>2</td><td>"+ String.format("%.0f", maxpunkte*0.75) + "</td></tr>";
                        ausgabe = ausgabe + "<tr bgcolor=\"#99FF99\"><td>2-</td><td>"+ String.format("%.0f", maxpunkte*0.7) + "</td></tr>";
                        ausgabe = ausgabe + "<tr bgcolor=\"#9999FF\"><td>3+</td><td>"+ String.format("%.0f", maxpunkte*0.65) + "</td></tr>";
                        ausgabe = ausgabe + "<tr bgcolor=\"#9999FF\"><td>3</td><td>"+ String.format("%.0f", maxpunkte*0.6) + "</td></tr>";
                        ausgabe = ausgabe + "<tr bgcolor=\"#9999FF\"><td>3-</td><td>"+ String.format("%.0f", maxpunkte*0.55) + "</td></tr>";
                        ausgabe = ausgabe + "<tr bgcolor=\"#FFFF00\"><td>4+</td><td>"+ String.format("%.0f", maxpunkte*0.5) + "</td></tr>";
                        ausgabe = ausgabe + "<tr bgcolor=\"#FFFF00\"><td>4</td><td>"+ String.format("%.0f", maxpunkte*0.45) + "</td></tr>";
                        ausgabe = ausgabe + "<tr bgcolor=\"#FF9900\"><td>4-</td><td>"+ String.format("%.0f", maxpunkte*0.4) + "</td></tr>";
                        ausgabe = ausgabe + "<tr bgcolor=\"#FF8888\"><td>5+</td><td>"+ String.format("%.0f", maxpunkte*0.333) + "</td></tr>";
                        ausgabe = ausgabe + "<tr bgcolor=\"#FF8888\"><td>5</td><td>"+ String.format("%.0f", maxpunkte*0.266) + "</td></tr>";
                        ausgabe = ausgabe + "<tr bgcolor=\"#FF8888\"><td>5-</td><td>"+ String.format("%.0f", maxpunkte*0.2) + "</td></tr>";
                        ausgabe = ausgabe + "</table></body></html>";
                        AlertDialog.Builder alert = new AlertDialog.Builder(Zensuren.this);
                        alert.setTitle("Punktegrenzen");

                        WebView wv = new WebView(Zensuren.this);
                        wv.loadData(ausgabe,"text/html", null);

                        alert.setView(wv);
                        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        alert.show();




                    }
                });

                alert1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert1.show();


                return true;
            case R.id.aexportmappe:
                for (int x=0;x<names2.size();x++) {
                    List<String[]> kursmappenliste=new ArrayList<String[]>();
                    kursmappenliste=dm.kursmappenliste(stg2[x]);
                    Collections.reverse(kursmappenliste);
                    String dateiname = stg1[x].substring(0, stg1[x].indexOf("(")-3);
                    dm.speicherliste(kursmappenliste, 6, "Eintrag-ID;Datum;Stundenzahl;Thema;Hausaufgabe;Fehlende", "kursmappe-" + dateiname + ".csv");
                }
                return true;
            case R.id.aexportnotizen:
                for (int x=0;x<names2.size();x++) {
                    String kursnummer = stg2[x];
                    List<String[]> names3 = dm.kursliste(kursnummer);
                    int y=0;
                    List<String[]> exportliste=new ArrayList<String[]>();
                    exportliste.clear();

                    for (String[] namea : names3) {
                        String[] stexp = new String[3];
                        String stg = namea[1] + " " + namea[0];
                        stexp[0] = Integer.toString(y + 1);
                        stexp[1] = stg;
                        stexp[2] = dm.getnotenstring(kursnummer, namea[3]).replaceAll("<[^>]*>", "");
                        exportliste.add(stexp);
                        y++;
                    }
                    String exphead = "Nr;Name;Notennotizen";
                    String dateiname = stg1[x].substring(0, stg1[x].indexOf("(")-3);
                    dm.speicherliste(exportliste,3,exphead, "notizen-"+dateiname+".csv");
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onListItemClick(ListView parent, View v, int position, long id) {
        // speichern: stg2[position]
        Intent i3 = new Intent(this, Kursliste.class);
        i3.putExtra("pkurs", stg2[position]);
        i3.putExtra("pkursname", stg1[position]);
        startActivity(i3);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){

		case R.id.button1:
            AlertDialog.Builder alert1 = new AlertDialog.Builder(this);

            alert1.setTitle("Aktuelles Schuljahr:");
            alert1.setMessage("Bitte Schuljahr eingeben:");

// Set an EditText view to get user input
            final EditText sjn = new EditText(this);
            sjn.setText(schuljahr);
            alert1.setView(sjn);

            alert1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String sname = sjn.getText().toString();
                    dm.seteinstellung(1,sname);
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });

            alert1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert1.show();
			break;
        case R.id.button101:
            if(dm.geteinstellung(2).equals("1")){
                dm.seteinstellung(2,"0");
                modbutton.setText("normal");
            }else{
                dm.seteinstellung(2,"1");
                modbutton.setText("auto");
            }
        break;


		}
	}
    public int getstunde(){
        int stunde =0;
        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int aktuellezeit = hours*60+minutes;
        String[] zeiten = dm.geteinstellung(3).split(",");
        int[] minutenzeiten = new int[20];
        for(int i=0;i<20;i++) {
            if (zeiten.length > i && !zeiten[i].equals("")) {
                minutenzeiten[i] = Integer.parseInt(zeiten[i].substring(0, 2)) * 60 + Integer.parseInt(zeiten[i].substring(2, 4));
            }else{
                minutenzeiten[i] = 0;
            }
        }
        for(int i=0;i<10;i++){
            if(aktuellezeit >=minutenzeiten[i*2] && aktuellezeit <= minutenzeiten[i*2 +1]) stunde = i+1;
        }
        return stunde;
    }
}