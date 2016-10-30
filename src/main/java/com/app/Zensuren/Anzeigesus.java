package com.app.Zensuren;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Anzeigesus extends ListActivity  implements OnClickListener {
    DataManipulator dm;

    List<String[]> list = new ArrayList<String[]>();
    List<String[]> names2 =null ;
    String[] stg1;
    String[] stg2;
    String[] stg3;
    String[] stg4;
    String[] stg5;
    String kursid;
    String susid;
    String hauptnotenart;
    String notenart;
    Button buttonsm1;
    Button buttonsm2;
    Button buttonk1;
    Button buttonk2;
    Button buttonz;
    MyArrayAdapter adapter;
    MydreizeiligAdapter adapter2;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anzeige);
        kursid = getIntent().getStringExtra("pkursanzeige");
        susid = getIntent().getStringExtra("psusanzeige");

        TextView namensschild = (TextView) findViewById(R.id.textView2);
        namensschild.setText(Html.fromHtml(getIntent().getStringExtra("susname")));

        //String ausgabe = "Kursnummer: " + kursid + ", sus: " + susid + ", name: " ;
        //Toast.makeText(getApplicationContext(), ausgabe, Toast.LENGTH_LONG).show();


        dm = new DataManipulator(this);
        names2 = dm.notenliste(kursid, susid);
        hauptnotenart = dm.geteinstellung(Integer.parseInt(kursid)+1000);

        buttonsm1 = (Button) findViewById(R.id.buttonsm1);
        buttonsm2 = (Button) findViewById(R.id.buttonsm2);
        buttonk1 = (Button) findViewById(R.id.buttonk1);
        buttonk2 = (Button) findViewById(R.id.buttonk2);
        buttonz = (Button) findViewById(R.id.buttonz);
        textaufbuttons();
        buttonsm1.setOnClickListener(this);
        buttonsm2.setOnClickListener(this);
        buttonk1.setOnClickListener(this);
        buttonk2.setOnClickListener(this);
        buttonz.setOnClickListener(this);

        stg1=new String[names2.size()];
        stg2=new String[names2.size()];
        stg3=new String[names2.size()];
        stg4=new String[names2.size()];
        stg5=new String[names2.size()];

        adapter = new MyArrayAdapter(this, stg1, stg2, stg3, stg4);


        int x=0;
        //String stg;

        for (String[] name : names2) {
            //stg = name[1]+" - "+name[2]+" - "+name[3];

            stg1[x]=name[2];
            stg2[x]=name[0];
            stg3[x]=dm.punktezunote(name[3]);
            stg4[x]=name[1];
            x++;
        }

        this.setListAdapter(adapter);



//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                this,android.R.layout.simple_list_item_1,
//                stg1);
//        this.setListAdapter(adapter);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int position, long id) {
                if (stg4[position].equals("10") || stg4[position].equals("11") || stg4[position].equals("12")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Anzeigesus.this);
                    builder.setTitle("Fehlstunde bearbeiten");
                    builder.setItems(new CharSequence[]
                                    {"entschuldigt", "unentschuldigt", "schulisch bedingt", "verspätet", "löschen"},
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // The 'which' argument contains the index position
                                    // of the selected item
                                    switch (which) {
                                        case 0:
                                            dm.entschuldige(stg2[position], susid);
                                            break;
                                        case 1:
                                            dm.unentschuldige(stg2[position], susid);
                                            break;
                                        case 2:
                                            dm.schulischentschuldigt(stg2[position], susid);
                                            break;
                                        case 3:
                                            dm.fehlstundezuverspaetet(stg2[position], susid);
                                            break;
                                        case 4:
                                            dm.loeschenote(stg2[position], susid);
                                            break;
                                    }
                                    dialog.dismiss();
                                    Anzeigesus.this.finish();
                                    startActivity(getIntent());
                                }
                            }
                    );
                    builder.create().show();

                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Anzeigesus.this);

                    builder.setTitle("Eintrag löschen");
                    builder.setMessage("Sind Sie sicher?");

                    builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            DataManipulator dh = new DataManipulator(Anzeigesus.this);
                            dh.loeschenote(stg2[position], susid);
                            dialog.dismiss();
                            Anzeigesus.this.finish();
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
                }


                return true;
            }
        });

    }


    public void textaufbuttons(){
        if(hauptnotenart.equals(""))hauptnotenart = "K1-K2-SM1-SM2-Z";
        String[] hauptnoten = hauptnotenart.split("-");
        buttonsm1.setText(hauptnoten[0] + ":\n" + dm.getsondernote(kursid, susid, "5"));
        buttonk1.setText(hauptnoten[1] + ":\n" + dm.getsondernote(kursid, susid, "6"));
        buttonsm2.setText(hauptnoten[2] + ":\n" + dm.getsondernote(kursid, susid, "7"));
        buttonk2.setText(hauptnoten[3] + ":\n" + dm.getsondernote(kursid, susid, "8"));
        buttonz.setText(hauptnoten[4] + ":\n" + dm.getsondernote(kursid, susid, "9"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.onCreate(null);
    }

    public void onListItemClick(ListView parent, View v, int position, long id) {
        // selection.setText(stg2[position]);
    }


    public void onClick(View v) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH:mm:ss");
        String zeit = sdf.format(new Date());
        // TODO Auto-generated method stub
        switch(v.getId()){

            case R.id.buttonsm1:
                notenart = "5";
                break;
            case R.id.buttonsm2:
                notenart = "7";
                break;
            case R.id.buttonk1:
                notenart = "6";
                break;
            case R.id.buttonk2:
                notenart = "8";
                break;
            case R.id.buttonz:
                notenart = "9";
                break;

        }
        Intent i7 = new Intent(this, setzegesamtnote.class);
        i7.putExtra("pkurs_id", kursid);
        i7.putExtra("psus_id", susid);
        i7.putExtra("pnotenart", notenart);
        startActivity(i7);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.zensurenanzeigenmenue, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.allekurse:
                int x=0;
                //String stg;
                names2 = dm.notenliste("0", susid);
                stg1=new String[names2.size()];
                stg2=new String[names2.size()];
                stg3=new String[names2.size()];
                stg4=new String[names2.size()];
                stg5=new String[names2.size()];
                for (String[] name : names2) {
                    //stg = name[1]+" - "+name[2]+" - "+name[3];

                    stg1[x]=name[2];
                    stg2[x]=name[0];
                    stg3[x]=dm.punktezunote(name[3]);
                    stg4[x]=name[1];
                    stg5[x]=name[4];
                    x++;
                }
                Collections.reverse(Arrays.asList(stg1));
                Collections.reverse(Arrays.asList(stg2));
                Collections.reverse(Arrays.asList(stg3));
                Collections.reverse(Arrays.asList(stg4));
                Collections.reverse(Arrays.asList(stg5));
                adapter2 = new MydreizeiligAdapter(this, stg1, stg2, stg3, stg4, stg5);
                this.setListAdapter(adapter2);
                break;
        }
        return true;
    }


}

