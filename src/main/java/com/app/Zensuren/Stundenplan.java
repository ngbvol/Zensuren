package com.app.Zensuren;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Html;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 19.08.14.
 */
public class Stundenplan extends Activity implements View.OnClickListener {
    String schuljahr;
    String[] datum;
    int wochentag;
    DataManipulator dm;
    String[] kursids;
    String[] eintragids;
    int modus;
    int tauschen;
    int stundenlink=0;
    int x;
    String[] dialogtermin;
    Button[] buttons;
    View.OnLongClickListener listener;
    Calendar c;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sitzplanlayout);

        modus = 0;
        datum = new String[11];
        String pdatum = getIntent().getStringExtra("pdatum");

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        c = Calendar.getInstance();
        c.setTime(new Date()); // yourdate is a object of type Date
        if(!pdatum.equals("")) {
            if (pdatum.substring(0, 1).equals("s")) {
                stundenlink = Integer.parseInt(pdatum.substring(1, 2));
                pdatum = "";
            }
        }
        if(!pdatum.equals("")){
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            try {
                c.setTime(df.parse(pdatum));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        wochentag = c.get(Calendar.DAY_OF_WEEK); // this will for example return 2 for tuesday
        if (c.SUNDAY == wochentag){
                c.add(Calendar.DAY_OF_MONTH, 1);
        }
        if (c.SATURDAY == wochentag){
            c.add(Calendar.DAY_OF_MONTH, 2);
        }
        wochentag = c.get(Calendar.DAY_OF_WEEK); // this will for example return 2 for tuesday





        listener = new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                int knopfnummer=bestimmeknopf(v);
                int j = 0;
                int i = knopfnummer;
                while(i>11){
                    i = i - 10;
                    j = j + 1;
                }
                String stunde = Integer.toString(j);
                dialogtermin = dm.gettermin(stunde, datum[i]);
                dialogtermin[1]=datum[i];
                dialogtermin[2]=stunde;
                AlertDialog.Builder alert2 = new AlertDialog.Builder(Stundenplan.this);

                alert2.setTitle("Termin eingeben:");
                alert2.setMessage("");

// Set an EditText view to get user input
                final EditText sjn2 = new EditText(Stundenplan.this);
                alert2.setView(sjn2);
                sjn2.setText(dialogtermin[3]);


                alert2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(!dialogtermin[0].equals("")){
                            dm.updatetermin(dialogtermin[0],sjn2.getText().toString(),dialogtermin[4]);
                        }else {
                            dm.settermin(dialogtermin[1], dialogtermin[2], sjn2.getText().toString(), dialogtermin[4]);
                        }
                        Stundenplan.this.finish();
                        startActivity(Stundenplan.this.getIntent());


                    }
                });

                alert2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert2.setNeutralButton("Termin löschen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(!dialogtermin[0].equals("")){
                            dm.loeschetermin(dialogtermin[0]);
                            Stundenplan.this.finish();
                            startActivity(Stundenplan.this.getIntent());
                        }
                    }
                });
                alert2.show();
                return true;
            }
        };


        buttons = new Button[101];
        buttons[1]=(Button) findViewById(R.id.button1);
        buttons[2]=(Button) findViewById(R.id.button2);
        buttons[3]=(Button) findViewById(R.id.button3);
        buttons[4]=(Button) findViewById(R.id.button4);
        buttons[5]=(Button) findViewById(R.id.button5);
        buttons[6]=(Button) findViewById(R.id.button6);
        buttons[7]=(Button) findViewById(R.id.button7);
        buttons[8]=(Button) findViewById(R.id.button8);
        buttons[9]=(Button) findViewById(R.id.button9);
        buttons[10]=(Button) findViewById(R.id.button10);

        buttons[11]=(Button) findViewById(R.id.button11);
        buttons[12]=(Button) findViewById(R.id.button12);
        buttons[13]=(Button) findViewById(R.id.button13);
        buttons[14]=(Button) findViewById(R.id.button14);
        buttons[15]=(Button) findViewById(R.id.button15);
        buttons[16]=(Button) findViewById(R.id.button16);
        buttons[17]=(Button) findViewById(R.id.button17);
        buttons[18]=(Button) findViewById(R.id.button18);
        buttons[19]=(Button) findViewById(R.id.button19);
        buttons[20]=(Button) findViewById(R.id.button20);

        buttons[21]=(Button) findViewById(R.id.button21);
        buttons[22]=(Button) findViewById(R.id.button22);
        buttons[23]=(Button) findViewById(R.id.button23);
        buttons[24]=(Button) findViewById(R.id.button24);
        buttons[25]=(Button) findViewById(R.id.button25);
        buttons[26]=(Button) findViewById(R.id.button26);
        buttons[27]=(Button) findViewById(R.id.button27);
        buttons[28]=(Button) findViewById(R.id.button28);
        buttons[29]=(Button) findViewById(R.id.button29);
        buttons[30]=(Button) findViewById(R.id.button30);

        buttons[31]=(Button) findViewById(R.id.button31);
        buttons[32]=(Button) findViewById(R.id.button32);
        buttons[33]=(Button) findViewById(R.id.button33);
        buttons[34]=(Button) findViewById(R.id.button34);
        buttons[35]=(Button) findViewById(R.id.button35);
        buttons[36]=(Button) findViewById(R.id.button36);
        buttons[37]=(Button) findViewById(R.id.button37);
        buttons[38]=(Button) findViewById(R.id.button38);
        buttons[39]=(Button) findViewById(R.id.button39);
        buttons[40]=(Button) findViewById(R.id.button40);

        buttons[41]=(Button) findViewById(R.id.button41);
        buttons[42]=(Button) findViewById(R.id.button42);
        buttons[43]=(Button) findViewById(R.id.button43);
        buttons[44]=(Button) findViewById(R.id.button44);
        buttons[45]=(Button) findViewById(R.id.button45);
        buttons[46]=(Button) findViewById(R.id.button46);
        buttons[47]=(Button) findViewById(R.id.button47);
        buttons[48]=(Button) findViewById(R.id.button48);
        buttons[49]=(Button) findViewById(R.id.button49);
        buttons[50]=(Button) findViewById(R.id.button50);

        buttons[51]=(Button) findViewById(R.id.button51);
        buttons[52]=(Button) findViewById(R.id.button52);
        buttons[53]=(Button) findViewById(R.id.button53);
        buttons[54]=(Button) findViewById(R.id.button54);
        buttons[55]=(Button) findViewById(R.id.button55);
        buttons[56]=(Button) findViewById(R.id.button56);
        buttons[57]=(Button) findViewById(R.id.button57);
        buttons[58]=(Button) findViewById(R.id.button58);
        buttons[59]=(Button) findViewById(R.id.button59);
        buttons[60]=(Button) findViewById(R.id.button60);

        buttons[61]=(Button) findViewById(R.id.button61);
        buttons[62]=(Button) findViewById(R.id.button62);
        buttons[63]=(Button) findViewById(R.id.button63);
        buttons[64]=(Button) findViewById(R.id.button64);
        buttons[65]=(Button) findViewById(R.id.button65);
        buttons[66]=(Button) findViewById(R.id.button66);
        buttons[67]=(Button) findViewById(R.id.button67);
        buttons[68]=(Button) findViewById(R.id.button68);
        buttons[69]=(Button) findViewById(R.id.button69);
        buttons[70]=(Button) findViewById(R.id.button70);

        buttons[71]=(Button) findViewById(R.id.button71);
        buttons[72]=(Button) findViewById(R.id.button72);
        buttons[73]=(Button) findViewById(R.id.button73);
        buttons[74]=(Button) findViewById(R.id.button74);
        buttons[75]=(Button) findViewById(R.id.button75);
        buttons[76]=(Button) findViewById(R.id.button76);
        buttons[77]=(Button) findViewById(R.id.button77);
        buttons[78]=(Button) findViewById(R.id.button78);
        buttons[79]=(Button) findViewById(R.id.button79);
        buttons[80]=(Button) findViewById(R.id.button80);

        buttons[81]=(Button) findViewById(R.id.button81);
        buttons[82]=(Button) findViewById(R.id.button82);
        buttons[83]=(Button) findViewById(R.id.button83);
        buttons[84]=(Button) findViewById(R.id.button84);
        buttons[85]=(Button) findViewById(R.id.button85);
        buttons[86]=(Button) findViewById(R.id.button86);
        buttons[87]=(Button) findViewById(R.id.button87);
        buttons[88]=(Button) findViewById(R.id.button88);
        buttons[89]=(Button) findViewById(R.id.button89);
        buttons[90]=(Button) findViewById(R.id.button90);

        buttons[91]=(Button) findViewById(R.id.button91);
        buttons[92]=(Button) findViewById(R.id.button92);
        buttons[93]=(Button) findViewById(R.id.button93);
        buttons[94]=(Button) findViewById(R.id.button94);
        buttons[95]=(Button) findViewById(R.id.button95);
        buttons[96]=(Button) findViewById(R.id.button96);
        buttons[97]=(Button) findViewById(R.id.button97);
        buttons[98]=(Button) findViewById(R.id.button98);
        buttons[99]=(Button) findViewById(R.id.button99);
        buttons[100]=(Button) findViewById(R.id.button100);

        kursids = new String[101];
        eintragids = new String[101];

        for(int i=1;i<101;i++){
            buttons[i].setText(" \n ");
            final float scale = this.getResources().getDisplayMetrics().density*110+0.5f;
            buttons[i].setTextSize(12);
            buttons[i].setWidth((int)scale);
            // buttons[i].setHeight(80);
            buttons[i].setIncludeFontPadding(false);
            buttons[i].setPadding(1, 1, 1, 1);
            buttons[i].getBackground().setColorFilter(Color.parseColor("#444444"), PorterDuff.Mode.SCREEN);
            // buttons[i].setBackgroundColor(Color.parseColor("#000000"));
            kursids[i]="";
            eintragids[i]="";
            buttons[i].setOnLongClickListener(listener);

        }
        dm = new DataManipulator(this);

        schuljahr = dm.geteinstellung(1);
        buttons[1].setText("Stunde \n ");
        final float scale = this.getResources().getDisplayMetrics().density*70+0.5f;
        buttons[1].setWidth((int)scale);
        buttons[1].getBackground().setColorFilter(Color.parseColor("#222222"), PorterDuff.Mode.SCREEN);


        String[] week = {"Samstag", "Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"};

        String aktwochentag;
        for(int i = 2;i<11;i++){
            if(c.get(Calendar.DAY_OF_WEEK)==7){
                c.add(Calendar.DAY_OF_MONTH, 2);
            }
            datum[i] = sdf.format(c.getTime());
            aktwochentag = week[c.get(Calendar.DAY_OF_WEEK)];
            buttons[i].setText(aktwochentag + "\n" + datum[i]);
            buttons[i].getBackground().setColorFilter(Color.parseColor("#222222"), PorterDuff.Mode.SCREEN);
            c.add(Calendar.DAY_OF_MONTH, 1);
        }

        for(int i = 1;i<10;i++){
            buttons[i*10+1].setText(Integer.toString(i)+"\n ");
            buttons[i*10+1].setWidth((int)scale);
            buttons[i*10+1].getBackground().setColorFilter(Color.parseColor("#222222"), PorterDuff.Mode.SCREEN);
            for(int j=2;j<11;j++){
                int stunde = (j + wochentag -3)*10 +i;
                while(stunde > 60){
                    stunde = stunde - 50;
                }
                String sstunde = Integer.toString(stunde);

                String[] kurs = dm.getstundenplankursid(schuljahr, sstunde, 1);
                //Gerade - ungerade Wochen
                if(kurs[2].contains(sstunde+"u")) {
                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                    try {
                        c.setTime(df.parse(datum[j]));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(c.get(Calendar.WEEK_OF_YEAR)%2==0){
                        kurs = dm.getstundenplankursid(schuljahr, sstunde, 2);
                    }
                    //  erledigen: datum[j] auswerten
                }
                if(kurs[2].contains(sstunde+"g")) {
                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                    try {
                        c.setTime(df.parse(datum[j]));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(c.get(Calendar.WEEK_OF_YEAR)%2==1){
                        kurs = dm.getstundenplankursid(schuljahr, sstunde, 2);
                    }
                    //  erledigen: datum[j] auswerten
                }
                buttons[i*10+j].setText(kurs[1] + "\n");
                String[] termin = dm.gettermin(Integer.toString(i),datum[j]);
                if(!termin[0].equals("")) {
                    buttons[i * 10 + j].setText(buttons[i * 10 +j].getText() + String.format("%.16s", termin[3]));
                    buttons[i * 10 + j].getBackground().setColorFilter(Color.parseColor("#00AA00"), PorterDuff.Mode.SCREEN);
                }
                if(!kurs[0].equals("")) {
                    buttons[i * 10 + j].getBackground().setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SCREEN);
                    if(!termin[0].equals("")) {
                        buttons[i * 10 + j].getBackground().setColorFilter(Color.parseColor("#AA9900"), PorterDuff.Mode.SCREEN);
                    }
                    kursids[i * 10 + j]=kurs[0];
                    buttons[i * 10 + j].setOnClickListener(this);
                    String[] eintrag = dm.getkursmappeneintragnachdatum(kurs[0],datum[j]);
                    if(!eintrag[0].equals("")){
                        buttons[i*10+j].setText(kurs[1] + "\n" + String.format("%.16s",eintrag[4]));
                        buttons[i * 10 + j].getBackground().setColorFilter(Color.parseColor("#0000FF"), PorterDuff.Mode.SCREEN);
                        if(!termin[0].equals("")) {
                            buttons[i * 10 + j].getBackground().setColorFilter(Color.parseColor("#00AAFF"), PorterDuff.Mode.SCREEN);
                        }

                        eintragids[i * 10 + j]=eintrag[0];
                    }
                }else{
                    kursids[i * 10 + j]="";
                    buttons[i * 10 + j].setOnClickListener(this);
/*                    String[] termin = dm.gettermin(Integer.toString(i),datum[j]);
                    if(!termin[0].equals("")){
                        buttons[i*10+j].setText("Termin:\n" + String.format("%.16s",termin[3]));
                        buttons[i * 10 + j].getBackground().setColorFilter(Color.parseColor("#00AA00"), PorterDuff.Mode.SCREEN);
                    }*/

                }

            }
        }






        x=0;
        String stg;

        if(stundenlink>0){
            String[] kurs = dm.getstundenplankursid(schuljahr, Integer.toString((wochentag - 1)*10+stundenlink), 1);
            if(!kurs[0].equals("")){
                Intent i3 = new Intent(this, Kursliste.class);
                i3.putExtra("pkurs", kurs[0]);
                i3.putExtra("pkursname", kurs[1]);
                startActivity(i3);
            }
        }



    }

    public void onClick(View v){
        int knopfnummer = bestimmeknopf(v);

        if(!eintragids[knopfnummer].equals("")){
            Intent i12 = new Intent(this, kursbuchbearbeiten.class);
            i12.putExtra("peid", eintragids[knopfnummer]);
            startActivityForResult(i12,3);
        }else{
            if(!kursids[knopfnummer].equals("")) {
                int i = knopfnummer;
                while(i>11){
                    i = i - 10;
                }
                Intent i19 = new Intent(this, kursbucheintragen.class);
                i19.putExtra("pkurs", kursids[knopfnummer]);
                i19.putExtra("pkursname", buttons[knopfnummer].getText().toString() + datum[i]);
                i19.putExtra("pdatum", datum[i]);
                startActivityForResult(i19,3);
            }else{
                int j = 0;
                int i = knopfnummer;
                while(i>11){
                    i = i - 10;
                    j = j + 1;
                }
                String stunde = Integer.toString(j);
                dialogtermin = dm.gettermin(stunde, datum[i]);
                dialogtermin[1]=datum[i];
                dialogtermin[2]=stunde;
                AlertDialog.Builder alert2 = new AlertDialog.Builder(this);

                alert2.setTitle("Termin eingeben:");
                alert2.setMessage("");

// Set an EditText view to get user input
                final EditText sjn2 = new EditText(this);
                alert2.setView(sjn2);
                sjn2.setText(dialogtermin[3]);


                alert2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(!dialogtermin[0].equals("")){
                            dm.updatetermin(dialogtermin[0],sjn2.getText().toString(),dialogtermin[4]);
                        }else {
                            dm.settermin(dialogtermin[1], dialogtermin[2], sjn2.getText().toString(), dialogtermin[4]);
                        }
                        Stundenplan.this.finish();
                        startActivity(Stundenplan.this.getIntent());


                    }
                });

                alert2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert2.setNeutralButton("Termin löschen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                       if(!dialogtermin[0].equals("")){
                           dm.loeschetermin(dialogtermin[0]);
                           Stundenplan.this.finish();
                           startActivity(Stundenplan.this.getIntent());
                       }
                    }
                });
                alert2.show();


            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3) {
            finish();
            startActivity(getIntent());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.stundenplanmenue, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String pdatum = getIntent().getStringExtra("pdatum");
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        switch (item.getItemId()) {
            case R.id.datumaendern:
                AlertDialog.Builder alert3 = new AlertDialog.Builder(this);

                alert3.setTitle("Datum eingeben:");
                alert3.setMessage("");

// Set an EditText view to get user input
                // final EditText sjn2 = new EditText(this);
                // alert3.setView(sjn2);


                final DatePicker dp = new DatePicker(this);
                alert3.setView(dp);
                dp.setCalendarViewShown(false);
                Time today = new Time(Time.getCurrentTimezone());
                today.setToNow();

                dp.updateDate(today.year,today.month,today.monthDay);



                alert3.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String meinstartdatum = Integer.toString(dp.getDayOfMonth()) + "." + Integer.toString(dp.getMonth()+1) + "." + Integer.toString(dp.getYear());
                        Stundenplan.this.finish();
                        Intent dieserintent = Stundenplan.this.getIntent();
                        dieserintent.putExtra("pdatum", meinstartdatum);
                        startActivity(dieserintent);

                    }
                });

                alert3.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert3.show();

                break;
            case R.id.wochevor:
                c = Calendar.getInstance();
                c.setTime(new Date()); // yourdate is a object of type Date
                if(!pdatum.equals("")){
                    try {
                        c.setTime(df.parse(pdatum));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                wochentag = c.get(Calendar.DAY_OF_WEEK); // this will for example return 2 for tuesday
                if (c.SUNDAY == wochentag){
                    c.add(Calendar.DAY_OF_MONTH, 1);
                }
                if (c.SATURDAY == wochentag){
                    c.add(Calendar.DAY_OF_MONTH, 2);
                }
                c.add(Calendar.DAY_OF_MONTH, 7);
                String meinstartdatum = df.format(c.getTime());
                Stundenplan.this.finish();
                Intent dieserintent = Stundenplan.this.getIntent();
                dieserintent.putExtra("pdatum", meinstartdatum);
                startActivity(dieserintent);

                break;
            case R.id.wochezurueck:
                c = Calendar.getInstance();
                c.setTime(new Date()); // yourdate is a object of type Date
                if(!pdatum.equals("")){
                    try {
                        c.setTime(df.parse(pdatum));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                wochentag = c.get(Calendar.DAY_OF_WEEK); // this will for example return 2 for tuesday
                if (c.SUNDAY == wochentag){
                    c.add(Calendar.DAY_OF_MONTH, 1);
                }
                if (c.SATURDAY == wochentag){
                    c.add(Calendar.DAY_OF_MONTH, 2);
                }
                c.add(Calendar.DAY_OF_MONTH, -7);
                String meinstartdatum2 = df.format(c.getTime());
                Stundenplan.this.finish();
                Intent dieserintent2 = Stundenplan.this.getIntent();
                dieserintent2.putExtra("pdatum", meinstartdatum2);
                startActivity(dieserintent2);

                break;
            case R.id.zeiten:
                AlertDialog.Builder alert1 = new AlertDialog.Builder(this);

                alert1.setTitle("Stundenzeiten:");
                alert1.setMessage("Bitte Stundenzeiten als Liste eingeben. z.B.: 0815,0900,0905,...");

// Set an EditText view to get user input
                final EditText sjn = new EditText(this);
                String zeiten = dm.geteinstellung(3);
                if (zeiten.isEmpty()) zeiten ="0815,0900,0905,0950,1005,1050,1055,1140,1155,1240,1245,1330,1345,1430,1435,1520,1525,1610";
                sjn.setText(zeiten);
                alert1.setView(sjn);

                alert1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String sname = sjn.getText().toString();
                        dm.seteinstellung(3,sname);
                    }
                });

                alert1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert1.show();
                break;
        }
        return true;
    }

    public int bestimmeknopf(View v){
        int gedrueckt=0;
        switch(v.getId()) {
            case R.id.button1:
                gedrueckt=1;
                break;
            case R.id.button2:
                gedrueckt=2;
                break;
            case R.id.button3:
                gedrueckt=3;
                break;
            case R.id.button4:
                gedrueckt=4;
                break;
            case R.id.button5:
                gedrueckt=5;
                break;
            case R.id.button6:
                gedrueckt=6;
                break;
            case R.id.button7:
                gedrueckt=7;
                break;
            case R.id.button8:
                gedrueckt=8;
                break;
            case R.id.button9:
                gedrueckt=9;
                break;
            case R.id.button10:
                gedrueckt=10;
                break;

            case R.id.button11:
                gedrueckt=11;
                break;
            case R.id.button12:
                gedrueckt=12;
                break;
            case R.id.button13:
                gedrueckt=13;
                break;
            case R.id.button14:
                gedrueckt=14;
                break;
            case R.id.button15:
                gedrueckt=15;
                break;
            case R.id.button16:
                gedrueckt=16;
                break;
            case R.id.button17:
                gedrueckt=17;
                break;
            case R.id.button18:
                gedrueckt=18;
                break;
            case R.id.button19:
                gedrueckt=19;
                break;
            case R.id.button20:
                gedrueckt=20;
                break;

            case R.id.button21:
                gedrueckt=21;
                break;
            case R.id.button22:
                gedrueckt=22;
                break;
            case R.id.button23:
                gedrueckt=23;
                break;
            case R.id.button24:
                gedrueckt=24;
                break;
            case R.id.button25:
                gedrueckt=25;
                break;
            case R.id.button26:
                gedrueckt=26;
                break;
            case R.id.button27:
                gedrueckt=27;
                break;
            case R.id.button28:
                gedrueckt=28;
                break;
            case R.id.button29:
                gedrueckt=29;
                break;
            case R.id.button30:
                gedrueckt=30;
                break;

            case R.id.button31:
                gedrueckt=31;
                break;
            case R.id.button32:
                gedrueckt=32;
                break;
            case R.id.button33:
                gedrueckt=33;
                break;
            case R.id.button34:
                gedrueckt=34;
                break;
            case R.id.button35:
                gedrueckt=35;
                break;
            case R.id.button36:
                gedrueckt=36;
                break;
            case R.id.button37:
                gedrueckt=37;
                break;
            case R.id.button38:
                gedrueckt=38;
                break;
            case R.id.button39:
                gedrueckt=39;
                break;
            case R.id.button40:
                gedrueckt=40;
                break;

            case R.id.button41:
                gedrueckt=41;
                break;
            case R.id.button42:
                gedrueckt=42;
                break;
            case R.id.button43:
                gedrueckt=43;
                break;
            case R.id.button44:
                gedrueckt=44;
                break;
            case R.id.button45:
                gedrueckt=45;
                break;
            case R.id.button46:
                gedrueckt=46;
                break;
            case R.id.button47:
                gedrueckt=47;
                break;
            case R.id.button48:
                gedrueckt=48;
                break;
            case R.id.button49:
                gedrueckt=49;
                break;
            case R.id.button50:
                gedrueckt=50;
                break;

            case R.id.button51:
                gedrueckt=51;
                break;
            case R.id.button52:
                gedrueckt=52;
                break;
            case R.id.button53:
                gedrueckt=53;
                break;
            case R.id.button54:
                gedrueckt=54;
                break;
            case R.id.button55:
                gedrueckt=55;
                break;
            case R.id.button56:
                gedrueckt=56;
                break;
            case R.id.button57:
                gedrueckt=57;
                break;
            case R.id.button58:
                gedrueckt=58;
                break;
            case R.id.button59:
                gedrueckt=59;
                break;
            case R.id.button60:
                gedrueckt=60;
                break;

            case R.id.button61:
                gedrueckt=61;
                break;
            case R.id.button62:
                gedrueckt=62;
                break;
            case R.id.button63:
                gedrueckt=63;
                break;
            case R.id.button64:
                gedrueckt=64;
                break;
            case R.id.button65:
                gedrueckt=65;
                break;
            case R.id.button66:
                gedrueckt=66;
                break;
            case R.id.button67:
                gedrueckt=67;
                break;
            case R.id.button68:
                gedrueckt=68;
                break;
            case R.id.button69:
                gedrueckt=69;
                break;
            case R.id.button70:
                gedrueckt=70;
                break;

            case R.id.button71:
                gedrueckt=71;
                break;
            case R.id.button72:
                gedrueckt=72;
                break;
            case R.id.button73:
                gedrueckt=73;
                break;
            case R.id.button74:
                gedrueckt=74;
                break;
            case R.id.button75:
                gedrueckt=75;
                break;
            case R.id.button76:
                gedrueckt=76;
                break;
            case R.id.button77:
                gedrueckt=77;
                break;
            case R.id.button78:
                gedrueckt=78;
                break;
            case R.id.button79:
                gedrueckt=79;
                break;
            case R.id.button80:
                gedrueckt=80;
                break;

            case R.id.button81:
                gedrueckt=81;
                break;
            case R.id.button82:
                gedrueckt=82;
                break;
            case R.id.button83:
                gedrueckt=83;
                break;
            case R.id.button84:
                gedrueckt=84;
                break;
            case R.id.button85:
                gedrueckt=85;
                break;
            case R.id.button86:
                gedrueckt=86;
                break;
            case R.id.button87:
                gedrueckt=87;
                break;
            case R.id.button88:
                gedrueckt=88;
                break;
            case R.id.button89:
                gedrueckt=89;
                break;
            case R.id.button90:
                gedrueckt=90;
                break;

            case R.id.button91:
                gedrueckt=91;
                break;
            case R.id.button92:
                gedrueckt=92;
                break;
            case R.id.button93:
                gedrueckt=93;
                break;
            case R.id.button94:
                gedrueckt=94;
                break;
            case R.id.button95:
                gedrueckt=95;
                break;
            case R.id.button96:
                gedrueckt=96;
                break;
            case R.id.button97:
                gedrueckt=97;
                break;
            case R.id.button98:
                gedrueckt=98;
                break;
            case R.id.button99:
                gedrueckt=99;
                break;
            case R.id.button100:
                gedrueckt=100;
                break;

        }
        return gedrueckt;
    }
}

