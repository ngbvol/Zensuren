package com.app.Zensuren;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.List;

/**
 * Created by tim on 10.08.14.
 */
public class Sitzplan extends Activity implements View.OnClickListener {
    String kursnummer;
    String kursname;
    DataManipulator dm;
    List<String[]> names2 =null ;
    String[] stg1;
    String[] stg2;
    String[] stg3;
    int[] plaetze;
    int[] verteilung;
    int modus;
    int tauschen;
    int x;
    Button[] buttons;
    View.OnLongClickListener listener;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sitzplanlayout);

        kursnummer = getIntent().getStringExtra("pkurs");
        kursname = getIntent().getStringExtra("pkursname");
        modus = 0;

        listener = new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                int gedrueckt = bestimmeknopf(v);
                Intent i15 = new Intent(Sitzplan.this, Anzeigesus.class);
                i15.putExtra("pkursanzeige", kursnummer);
                i15.putExtra("psusanzeige", stg3[verteilung[gedrueckt]]);
                i15.putExtra("susname", stg1[verteilung[gedrueckt]]);
                startActivity(i15);
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

        for(int i=1;i<101;i++){
            buttons[i].setOnClickListener(this);
            buttons[i].setOnLongClickListener(listener);
            buttons[i].setText(" \n ");
            final float scale = this.getResources().getDisplayMetrics().density*100+0.5f;
            buttons[i].setTextSize(12);
            buttons[i].setWidth((int)scale);
            // buttons[i].setHeight(80);
            buttons[i].setIncludeFontPadding(false);
            buttons[i].setPadding(1, 1, 1, 1);
            buttons[i].getBackground().setColorFilter(Color.parseColor("#444444"), PorterDuff.Mode.SCREEN);
            // buttons[i].setBackgroundColor(Color.parseColor("#000000"));
        }



        dm = new DataManipulator(this);
        names2 = dm.kursliste(kursnummer);

        stg1=new String[names2.size()];  //Namen
        stg2=new String[names2.size()];  //platz
        stg3=new String[names2.size()];  //susid
        plaetze = new int[names2.size()];
        verteilung = new int[101];
        x=0;
        String stg;
        for (String[] name : names2) {
            stg = name[1]+" "+name[0];
            stg1[x]=stg;
            stg2[x]=name[2]; //platz
            plaetze[x]=Integer.parseInt(name[2]);
            stg3[x]=name[3]; //id
            x++;
        }
        for (int i=0;i<x;i++){
            if(plaetze[i]>0){
                String aufbutton = String.format("%.16s",stg1[i]);
                buttons[plaetze[i]].setText(aufbutton + "\n ");
                buttons[plaetze[i]].getBackground().setColorFilter(Color.parseColor("#444444"), PorterDuff.Mode.SCREEN);
                //buttons[plaetze[i]].setBackgroundResource(R.drawable.rahmenschwarz);
                verteilung[plaetze[i]]=i;
            }
        }
        int j = 1;
        for (int i=0;i<x;i++){
            if(plaetze[i]==0){
                while(!buttons[j].getText().equals(" \n ")){
                    j++;
                }
                String aufbutton = String.format("%.16s",stg1[i]);
                buttons[j].setText(aufbutton + "\n ");
                verteilung[j]=i;
                buttons[j].getBackground().setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SCREEN);
                //buttons[j].setBackgroundResource(R.drawable.rahmenrot);
            }
        }





    }

    public void onClick(View v){
        int gedrueckt = bestimmeknopf(v);

        switch (modus) {
            case 1:
                tauschen = gedrueckt;
                modus = 2;
                buttons[gedrueckt].getBackground().setColorFilter(Color.parseColor("#0000FF"), PorterDuff.Mode.SCREEN);
                //buttons[gedrueckt].setBackgroundResource(R.drawable.rahmenblau);
                break;
            case 2:
                modus = 0;
                if(!buttons[gedrueckt].getText().equals(" \n ")){
                    dm.weiseplatzzu(stg3[verteilung[gedrueckt]],kursnummer,Integer.toString(tauschen));
                }
                if(!buttons[tauschen].getText().equals(" \n ")){
                    dm.weiseplatzzu(stg3[verteilung[tauschen]],kursnummer,Integer.toString(gedrueckt));
                }
                startActivity(getIntent());
                finish();
                break;
            default:
                Intent i14 = new Intent(this, Noteneingabe.class);
                i14.putExtra("pkurs_id", kursnummer);
                i14.putExtra("psus_id", stg3[verteilung[gedrueckt]]);
                i14.putExtra("susname", stg1[verteilung[gedrueckt]]);
                startActivity(i14);
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.sitzplanmenue, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.atauschen:
                modus = 1;
                for (int i = 1; i < 101; i++) {
                    buttons[i].getBackground().setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SCREEN);
//                    buttons[i].setBackgroundResource(R.drawable.rahmenrot);
                }
                break;
            case R.id.azeigen:
                if(modus == 0){
                    modus = 3;
                    /*for(int i=1;i<81;i++){
                        buttons[i].setHeight(130);
                    }*/

                    for (int i=0;i<x;i++){
                        if(plaetze[i]>0){
                            String aufbutton = String.format("%.16s",stg1[i])+ "<br>" + dm.getnotenstring(kursnummer, stg3[i]);
                            buttons[plaetze[i]].setText(Html.fromHtml(aufbutton));
                            String farbnote = dm.getsondernote(kursnummer,stg3[i],"80");
                            String farbe="#444444";
                            if (farbnote.equals("gr")){farbe = "#00FF00";}
                            if (farbnote.equals("ge")){farbe = "#888800";}
                            if (farbnote.equals("ro")){farbe = "#FF0000";}
                            buttons[plaetze[i]].getBackground().setColorFilter(Color.parseColor(farbe), PorterDuff.Mode.SCREEN);
                            // buttons[plaetze[i]].setBackgroundResource(R.drawable.rahmenschwarz);
                            verteilung[plaetze[i]]=i;
                        }
                    }
                }else {
                    if (modus == 3) {
                        modus = 0;
                        /*for(int i=1;i<81;i++){
                            buttons[i].setHeight(80);
                        }*/
                        for (int i = 0; i < x; i++) {
                            if (plaetze[i] > 0) {
                                String aufbutton = String.format("%.16s", stg1[i]);
                                buttons[plaetze[i]].setText(Html.fromHtml(aufbutton) + "\n ");
                                buttons[plaetze[i]].getBackground().setColorFilter(Color.parseColor("#444444"), PorterDuff.Mode.SCREEN);
                                //buttons[plaetze[i]].setBackgroundResource(R.drawable.rahmenschwarz);
                                verteilung[plaetze[i]] = i;
                            }
                        }
                    }
                }
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
