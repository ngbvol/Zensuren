package com.app.Zensuren;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by tim on 11.06.14.
 */
public class kursbuchbearbeiten extends Activity implements View.OnClickListener {

    TextView vkurs;
    EditText vthema;
    EditText vhausaufgabe;
    Button vdoppelstunde;
    Button vspeichern;
    Button vliste;
    DatePicker vdatum;
    DataManipulator dm;
    String eintragnummer;
    int pjahr;
    int pmonat;
    int ptag;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kursbucheintrag);
        eintragnummer = getIntent().getStringExtra("peid");
        vkurs = (TextView) findViewById(R.id.textViewkurs);
        vthema = (EditText) findViewById(R.id.editTextthema);
        vhausaufgabe = (EditText) findViewById(R.id.editTextha);
        vdoppelstunde = (Button) findViewById(R.id.buttonds);
        vspeichern = (Button) findViewById(R.id.buttonspeichern);
        vliste = (Button) findViewById(R.id.buttonliste);
        //vdatum = (DatePicker) findViewById(R.id.datePicker);
        CalendarView calendarView=(CalendarView) findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                pjahr = year;
                pmonat = month;
                ptag = dayOfMonth;
                // Toast.makeText(getApplicationContext(), ""+dayOfMonth, 0).show();// TODO Auto-generated method stub

            }
        });
        dm = new DataManipulator(this);

        String[] datensatz = dm.getkursmappeneintrag(eintragnummer);

        vthema.setText(datensatz[4]);
        vhausaufgabe.setText(datensatz[5]);
        if(datensatz[3].equals("1")){
            vdoppelstunde.setText("Einzelstunde");
        }else{
            vdoppelstunde.setText("Doppelstunde");
        }


        String[]datumzerteilt = new String[4];
        datumzerteilt = datensatz[2].split("\\.");
        Calendar myCal2 = new GregorianCalendar();
        pjahr = Integer.parseInt(datumzerteilt[2]);
        pmonat = Integer.parseInt(datumzerteilt[1])-1;
        ptag = Integer.parseInt(datumzerteilt[0]);
        myCal2.set(pjahr, pmonat, ptag);
        calendarView.setDate(myCal2.getTimeInMillis());
        //vdatum.updateDate(Integer.parseInt(datumzerteilt[2]), Integer.parseInt(datumzerteilt[1])-1, Integer.parseInt(datumzerteilt[0]));


        vkurs.setText("Eintrag ändern / löschen");
        vspeichern.setOnClickListener(this);
        vdoppelstunde.setOnClickListener(this);
        vliste.setText("Eintrag löschen");
        vliste.setOnClickListener(this);

    }


    public void onClick(View v){

        switch(v.getId()){
            case R.id.buttonspeichern:
                String tag = Integer.toString(ptag);
                if(ptag<10){
                    tag = "0"+tag;
                }
                String monat = Integer.toString(pmonat+1);
                if(pmonat<9){
                    monat = "0"+monat;
                }
                String mydatum = tag + "." + monat + "." + Integer.toString(pjahr);
                String mystundenart;
                if(vdoppelstunde.getText().equals("Doppelstunde")){
                    mystundenart = "2";
                }else{
                    mystundenart = "1";
                }

                dm.updatekursmappeneintrag(eintragnummer,mydatum,mystundenart,vthema.getText().toString(),vhausaufgabe.getText().toString());

                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();

                this.finish();
                break;
            case R.id.buttonliste:
                dm.loeschekursmappeneintrag(eintragnummer);
                this.finish();
                break;
            case R.id.buttonds:
                if(vdoppelstunde.getText().equals("Doppelstunde")){
                    vdoppelstunde.setText("Einzelstunde");
                }else{
                    vdoppelstunde.setText("Doppelstunde");
                }

        }

    }


}
