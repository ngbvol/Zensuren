package com.app.Zensuren;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tim on 24.05.14.
 */
public class setzegesamtnote extends Activity implements View.OnClickListener {
    final Context context = this;
    private DataManipulator dh;
    static final int DIALOG_ID = 0;
    private String zeit;
    private String myart;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notenwahl);
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
        myart = getIntent().getStringExtra("pnotenart");

        this.dh = new DataManipulator(this);


        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH:mm:ss");
        zeit = sdf.format(new Date());
    }

    public void onClick(View v){
        //String mybemerkung=((TextView) findViewById(R.id.editText)).getText().toString();
        switch(v.getId()){
            case R.id.button1:
                this.dh.setsondernote(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"15",zeit);
                break;
            case R.id.button2:
                this.dh.setsondernote(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"14",zeit);
                break;
            case R.id.button3:
                this.dh.setsondernote(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"13",zeit);
                break;
            case R.id.button4:
                this.dh.setsondernote(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"12",zeit);
                break;
            case R.id.button5:
                this.dh.setsondernote(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"11",zeit);
                break;
            case R.id.button6:
                this.dh.setsondernote(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"10",zeit);
                break;
            case R.id.button7:
                this.dh.setsondernote(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"9",zeit);
                setzegesamtnote.this.finish();
                break;
            case R.id.button8:
                this.dh.setsondernote(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"8",zeit);
                break;
            case R.id.button9:
                this.dh.setsondernote(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"7",zeit);
                break;
            case R.id.button10:
                this.dh.setsondernote(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"6",zeit);
                break;
            case R.id.button11:
                this.dh.setsondernote(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"5",zeit);
                break;
            case R.id.button12:
                this.dh.setsondernote(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"4",zeit);
                break;
            case R.id.button13:
                this.dh.setsondernote(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"3",zeit);
                break;
            case R.id.button14:
                this.dh.setsondernote(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"2",zeit);
                break;
            case R.id.button15:
                this.dh.setsondernote(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"1",zeit);
                break;
            case R.id.button16:
                this.dh.setsondernote(getIntent().getStringExtra("pkurs_id"), getIntent().getStringExtra("psus_id"),myart,"0",zeit);
                break;

        }
        setzegesamtnote.this.finish();

    }



}

