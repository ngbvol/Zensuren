package com.app.Zensuren;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by tim on 09.06.14.
 */
public class kursbucheintragen extends Activity implements View.OnClickListener {

    TextView vkurs;
    EditText vthema;
    EditText vhausaufgabe;
    Button vdoppelstunde;
    Button vspeichern;
    Button vliste;
    // DatePicker vdatum;
    DataManipulator dm;
    String kursnummer;
    String kursname;
    String pdatum;
    CalendarView calendarView;
    int pjahr;
    int pmonat;
    int ptag;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kursbucheintrag);
        vkurs = (TextView) findViewById(R.id.textViewkurs);
        vthema = (EditText) findViewById(R.id.editTextthema);
        vhausaufgabe = (EditText) findViewById(R.id.editTextha);
        vdoppelstunde = (Button) findViewById(R.id.buttonds);
        vspeichern = (Button) findViewById(R.id.buttonspeichern);
        vliste = (Button) findViewById(R.id.buttonliste);
        // vdatum = (DatePicker) findViewById(R.id.datePicker);
        Calendar myCal2 = new GregorianCalendar();
        pjahr = myCal2.get( Calendar.YEAR  );
        pmonat = myCal2.get( Calendar.MONTH );
        ptag = myCal2.get( Calendar.DATE  );
        CalendarView calendarView=(CalendarView) findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new OnDateChangeListener() {
                                                 @Override
                                                 public void onSelectedDayChange(CalendarView view, int year, int month,
                                                                                 int dayOfMonth) {
                                                     pjahr = year;
                                                     pmonat = month;
                                                     ptag = dayOfMonth;
                                                     // Toast.makeText(getApplicationContext(), ""+dayOfMonth, 0).show();// TODO Auto-generated method stub

                                                 }
                                             });
        kursnummer = getIntent().getStringExtra("pkurs");
        kursname = getIntent().getStringExtra("pkursname");
        pdatum = getIntent().getStringExtra("pdatum");
        dm = new DataManipulator(this);

        if(!pdatum.equals("")){
            calendarView.setVisibility(View.INVISIBLE);
        }

        vkurs.setText("Kurs: " + kursname);
        vspeichern.setOnClickListener(this);
        vdoppelstunde.setOnClickListener(this);
    }


    public void onClick(View v){

        switch(v.getId()){
            case R.id.buttonspeichern:
                String tag = Integer.toString(ptag);
                if(ptag<10){
                    tag = "0"+tag;
                }
                String monat = Integer.toString(pmonat+1);
                if(pmonat<10){
                    monat = "0"+monat;
                }
                String mydatum = tag + "." + monat + "." + Integer.toString(pjahr);
                if(!pdatum.equals("")){
                    mydatum = pdatum;
                }
                String mystundenart;
                if(vdoppelstunde.getText().equals("Doppelstunde")){
                    mystundenart = "2";
                }else{
                    mystundenart = "1";
                }

                dm.setkursmappeneintrag(kursnummer,mydatum,mystundenart,vthema.getText().toString(),vhausaufgabe.getText().toString());
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();
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
