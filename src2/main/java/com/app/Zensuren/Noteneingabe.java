package com.app.Zensuren;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Noteneingabe extends Activity implements OnClickListener {
    final Context context = this;
    private DataManipulator dh;
    static final int DIALOG_ID = 0;
    private String zeit;
    Spinner spinner;
    String myart;
    boolean mittext;
    ImageButton wichtigbutton;
    ImageButton buttonmittext;

    protected void onCreate(Bundle savedInstanceState){
        String susname = getIntent().getStringExtra("susname");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noteneingabeneu);
        View einsp = findViewById(R.id.button1);
        einsp.setOnClickListener(this);
        View eins = findViewById(R.id.button2);
        eins.setOnClickListener(this);
        View einsm = findViewById(R.id.button3);
        einsm.setOnClickListener(this);
        View zweip = findViewById(R.id.button4);
        zweip.setOnClickListener(this);
        View zwei = findViewById(R.id.button5);
        zwei.setOnClickListener(this);
        View zweim = findViewById(R.id.button6);
        zweim.setOnClickListener(this);
        View dreip = findViewById(R.id.button7);
        dreip.setOnClickListener(this);
        View drei = findViewById(R.id.button8);
        drei.setOnClickListener(this);
        View dreim = findViewById(R.id.button9);
        dreim.setOnClickListener(this);
        View vierp = findViewById(R.id.button10);
        vierp.setOnClickListener(this);
        View vier = findViewById(R.id.button11);
        vier.setOnClickListener(this);
        View vierm = findViewById(R.id.button12);
        vierm.setOnClickListener(this);
        View fuenfp = findViewById(R.id.button13);
        fuenfp.setOnClickListener(this);
        View fuenf = findViewById(R.id.button14);
        fuenf.setOnClickListener(this);
        View fuenfm = findViewById(R.id.button15);
        fuenfm.setOnClickListener(this);
        View sechs = findViewById(R.id.button16);
        sechs.setOnClickListener(this);
        View oha = findViewById(R.id.button17);
        oha.setOnClickListener(this);
        View versp = findViewById(R.id.button18);
        versp.setOnClickListener(this);
        View fehlt = findViewById(R.id.buttonfehlt);
        fehlt.setOnClickListener(this);
        View neutral = findViewById(R.id.button19);
        neutral.setOnClickListener(this);
        View negativ = findViewById(R.id.button20);
        negativ.setOnClickListener(this);
        View positiv = findViewById(R.id.button);
        positiv.setOnClickListener(this);
        View farbbutton = findViewById(R.id.buttonfarbe);
        farbbutton.setOnClickListener(this);
        View freiebem = findViewById(R.id.buttonfreiebemerkung);
        freiebem.setOnClickListener(this);
        TextView tvname = (TextView) findViewById(R.id.textView);
        tvname.setText(Html.fromHtml("Eintrag für " + susname));
        wichtigbutton = (ImageButton) findViewById(R.id.buttonwichtig);
        wichtigbutton.setOnClickListener(this);
        buttonmittext = (ImageButton) findViewById(R.id.buttonmittext);
        buttonmittext.setOnClickListener(this);

        spinner = (Spinner) findViewById(R.id.spinner);

        this.dh = new DataManipulator(this);
        myart = "0";
        mittext = false;
        wichtigbutton.setBackgroundColor(Color.parseColor("#000000"));
        buttonmittext.setBackgroundColor(Color.parseColor("#000000"));
        if(dh.geteinstellung(21).equals("1")){
            mittext = true;
            buttonmittext.setBackgroundColor(Color.parseColor("#22FF22"));
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.myspinner, dh.gettextbeispiele());
        spinner.setAdapter(adapter);
        String spinpos = dh.geteinstellung(20);
        if(spinpos.equals(""))spinpos="0";
        spinner.setSelection(Integer.parseInt(spinpos));
        spinner.setLongClickable(true);
        spinner.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alert3 = new AlertDialog.Builder(context);

                alert3.setTitle("Eintrag ändern:");
                alert3.setMessage("Bitte neuen Eintrag eingeben:");

// Set an EditText view to get user input
                final EditText sjn = new EditText(context);
                sjn.setText(spinner.getSelectedItem().toString());
                alert3.setView(sjn);

                alert3.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String neintrag = sjn.getText().toString();
                        int spinid = spinner.getSelectedItemPosition() + 10;
                        dh.seteinstellung(spinid ,neintrag);
                        adapter.notifyDataSetChanged();
                    }
                });

                alert3.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert3.show();
                return true;
            }

        });

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH:mm:ss");
        zeit = sdf.format(new Date());
    }

    public void onClick(View v){
        //String mybemerkung=((TextView) findViewById(R.id.editText)).getText().toString();
        String mybemerkung = "";
        if(mittext){
            mybemerkung = spinner.getSelectedItem().toString();
        }

        dh.seteinstellung(20,Integer.toString(spinner.getSelectedItemPosition()));
        switch(v.getId()){
            case R.id.button1:
                this.dh.noteneintrag(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"15",mybemerkung,zeit);
                Noteneingabe.this.finish();
                break;
            case R.id.button2:
                this.dh.noteneintrag(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"14",mybemerkung,zeit);
                Noteneingabe.this.finish();
                break;
            case R.id.button3:
                this.dh.noteneintrag(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"13",mybemerkung,zeit);
                Noteneingabe.this.finish();
                break;
            case R.id.button4:
                this.dh.noteneintrag(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"12",mybemerkung,zeit);
                Noteneingabe.this.finish();
                break;
            case R.id.button5:
                this.dh.noteneintrag(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"11",mybemerkung,zeit);
                Noteneingabe.this.finish();
                break;
            case R.id.button6:
                this.dh.noteneintrag(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"10",mybemerkung,zeit);
                Noteneingabe.this.finish();
                break;
            case R.id.button7:
                this.dh.noteneintrag(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"9",mybemerkung,zeit);
                Noteneingabe.this.finish();
                break;
            case R.id.button8:
                this.dh.noteneintrag(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"8",mybemerkung,zeit);
                Noteneingabe.this.finish();
                break;
            case R.id.button9:
                this.dh.noteneintrag(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"7",mybemerkung,zeit);
                Noteneingabe.this.finish();
                break;
            case R.id.button10:
                this.dh.noteneintrag(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"6",mybemerkung,zeit);
                Noteneingabe.this.finish();
                break;
            case R.id.button11:
                this.dh.noteneintrag(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"5",mybemerkung,zeit);
                Noteneingabe.this.finish();
                break;
            case R.id.button12:
                this.dh.noteneintrag(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"4",mybemerkung,zeit);
                Noteneingabe.this.finish();
                break;
            case R.id.button13:
                this.dh.noteneintrag(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"3",mybemerkung,zeit);
                Noteneingabe.this.finish();
                break;
            case R.id.button14:
                this.dh.noteneintrag(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"2",mybemerkung,zeit);
                Noteneingabe.this.finish();
                break;
            case R.id.button15:
                this.dh.noteneintrag(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"1",mybemerkung,zeit);
                Noteneingabe.this.finish();
                break;
            case R.id.button16:
                this.dh.noteneintrag(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"0",mybemerkung,zeit);
                Noteneingabe.this.finish();
                break;
            case R.id.button17:
                mybemerkung = "- ohne HA " + mybemerkung;
                this.dh.noteneintrag(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),"1","-2",mybemerkung,zeit);
                Noteneingabe.this.finish();
                break;
            case R.id.button18:
                mybemerkung = "- verspaetet ";
                this.dh.noteneintrag(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),"13","-3",mybemerkung,zeit);
                Noteneingabe.this.finish();
                break;
            case R.id.button19:
                mybemerkung = "0 " + mybemerkung;
                this.dh.noteneintrag(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),"2","-2",mybemerkung,zeit);
                Noteneingabe.this.finish();
                break;
            case R.id.button20:
                mybemerkung = "- " + mybemerkung;
                this.dh.noteneintrag(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),"1","-2",mybemerkung,zeit);
                Noteneingabe.this.finish();
                break;
            case R.id.button:
                mybemerkung = "+ " + mybemerkung;
                this.dh.noteneintrag(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),"3","-2",mybemerkung,zeit);
                Noteneingabe.this.finish();
                break;
            case R.id.buttonfehlt:
                this.dh.noteneintrag(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),"11","-1","Fehlstunde (u)",zeit);
                Noteneingabe.this.finish();
                break;
            case R.id.buttonfreiebemerkung:
                AlertDialog.Builder alert2 = new AlertDialog.Builder(this);

                alert2.setTitle("Bemerkung eingeben:");
                alert2.setMessage("");

// Set an EditText view to get user input
                final EditText sjn2 = new EditText(this);
                alert2.setView(sjn2);

                alert2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String bemtext = sjn2.getText().toString();
                        if(!bemtext.equals("")){
                            Noteneingabe.this.dh.noteneintrag(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),"99","-3",bemtext,zeit);
                            Noteneingabe.this.finish();
                        }
                    }
                });
                alert2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                ;
                alert2.show();
                break;
            case R.id.buttonfarbe:
                AlertDialog.Builder builder = new AlertDialog.Builder(Noteneingabe.this);
                builder.setTitle("Farbe wählen");
                builder.setItems(new CharSequence[]
                                {"weiß", "grün", "gelb", "rot"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                switch (which) {
                                    case 0:
                                        dh.setsondernote(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),"80","-10",zeit);
                                        Noteneingabe.this.finish();
                                        break;
                                    case 1:
                                        dh.setsondernote(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),"80","-11",zeit);
                                        Noteneingabe.this.finish();
                                        break;
                                    case 2:
                                        dh.setsondernote(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),"80","-12",zeit);
                                        Noteneingabe.this.finish();
                                        break;
                                    case 3:
                                        dh.setsondernote(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),"80","-13",zeit);
                                        Noteneingabe.this.finish();
                                        break;
                                }
                                dialog.dismiss();
                            }
                        }
                );
                builder.create().show();
                break;
            case R.id.buttonwichtig:
                if(myart.equals("0")){
                    myart = "4";
                    wichtigbutton.setBackgroundColor(Color.parseColor("#FF44FF"));
                }else{
                    myart ="0";
                    wichtigbutton.setBackgroundColor(Color.parseColor("#000000"));
                }
                break;
            case R.id.buttonmittext:
                if(mittext){
                    mittext = false;
                    dh.seteinstellung(21,"0");
                    buttonmittext.setBackgroundColor(Color.parseColor("#000000"));
                }else{
                    mittext = true;
                    dh.seteinstellung(21,"1");
                    buttonmittext.setBackgroundColor(Color.parseColor("#22FF22"));
                }

                break;
        }
    }



}

