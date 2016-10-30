package com.app.Zensuren;

/**
 * Created by tim on 13.05.14.
 */
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class LegeKursan extends Activity implements OnClickListener{

    private DataManipulator dh;
    static final int DIALOG_ID = 1;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kursanlegen);
        View add = findViewById(R.id.button1);
        add.setOnClickListener(this);
        View home = findViewById(R.id.button2);
        home.setOnClickListener(this);
    }

    public void onClick(View v){
        switch(v.getId()){

            case R.id.button2:
                Intent i = new Intent(this, Zensuren.class);
                startActivity(i);
                break;

            case R.id.button1:
                View editText1 = (EditText) findViewById(R.id.editText);
                View editText2 = (EditText) findViewById(R.id.editText2);

                String myEditText1=((TextView) editText1).getText().toString();
                String myEditText2=((TextView) editText2).getText().toString();



                this.dh = new DataManipulator(this);
                this.dh.insertkurs(myEditText1,myEditText2);

                showDialog(DIALOG_ID);
                break;

        }
    }
    protected final Dialog onCreateDialog(final int id) {
        Dialog dialog = null;
        switch(id) {
            case DIALOG_ID:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Kurs angelegt! Weiteren Kurs hinzufügen? \n Der neue Kurs wird erst nach einem Neustart der App sichtbar.")
                        .setCancelable(false)
                        .setPositiveButton("Nein", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                LegeKursan.this.finish();

                            }
                        })
                        .setNegativeButton("Ja", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                dialog = alert;
                break;

            default:

        }
        return dialog;
    }



}

