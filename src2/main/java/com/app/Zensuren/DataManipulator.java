package com.app.Zensuren;


import android.content.ContentResolver;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.List;



public class DataManipulator {
	private static final  String DATABASE_NAME = "zensuren.db";
    private static final  String DATABASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"zensuren";
//    private static final  String DATABASE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath()+File.separator+"zensuren";
//    private static final  String DATABASE_PATH = Environment.getDataDirectory().getPath();
	private static final int DATABASE_VERSION = 4;
	static final String TABLE_NAME = "newtable";
	private static Context context;
	static SQLiteDatabase db;

	private SQLiteStatement insertStmt;
    private SQLiteStatement insertkursStmt;
    private SQLiteStatement insertnoteStmt;

    private static final String INSERT = "insert into "
		+ TABLE_NAME + " (name,number,skypeId,address) values (?,?,?,?)";
    private static final String INSERTKURS = "insert into kurse (kurs,schuljahr) values (?,?)";


	public DataManipulator(Context context) {
        File file = new File(DATABASE_PATH, "zensuren");

        DataManipulator.context = context;
		OpenHelper openHelper = new OpenHelper(DataManipulator.context);
		DataManipulator.db = openHelper.getWritableDatabase();
		this.insertStmt = DataManipulator.db.compileStatement(INSERT);
        this.insertkursStmt = DataManipulator.db.compileStatement(INSERTKURS);

	}
	public long insert(String name,String number,String skypeId,String address) {
		this.insertStmt.bindString(1, name);
		this.insertStmt.bindString(2, number);
		this.insertStmt.bindString(3, skypeId);
		this.insertStmt.bindString(4, address);
		return this.insertStmt.executeInsert();
	}

    public long insertkurs(String kurs,String schuljahr) {
        this.insertkursStmt.bindString(1, kurs);
        this.insertkursStmt.bindString(2, schuljahr);
        return this.insertkursStmt.executeInsert();
    }

    public void noteneintrag(String kursid,String susid, String art, String note, String bemerkung, String datum) {
        db.execSQL("insert into noten values("+kursid+","+susid+","+art+","+note+",'"+bemerkung+"','"+datum+"')");
    }

    public int suseintrag(String nachname,String vorname) {
        int id3=-1;
        String aktvorname="";
        String aktnachname="";
        String aktid="";
        Cursor cursor = db.rawQuery("select id, name, vorname from sus", null);
        cursor.moveToFirst();
        do {
            aktid = cursor.getString(0);
            aktnachname = cursor.getString(1);
            aktvorname = cursor.getString(2);
            if(aktnachname.equals(nachname)&&aktvorname.equals(vorname)){
                id3=Integer.parseInt(aktid);
            }
        } while (cursor.moveToNext());
        if(id3==-1){
            id3 = getmaxsusid()+1;
            db.execSQL("insert into sus values("+id3+",'"+nachname+"','"+vorname+"')");
        }
        return id3;
    }

    public void importkursliste(String kursid, String[] vornamen, String[] nachnamen){
        int susid=0;
        for(int i=0;i<vornamen.length;i++){
            susid = suseintrag(nachnamen[i],vornamen[i]);
            weisekurszu(Integer.toString(susid),kursid);
        }

    }

    public void loeschenote(String datum, String susid){
        db.execSQL("delete from noten where datum = '" + datum + "' and sus_id = " + susid );
    }

    public void entschuldige(String datum, String susid){
        db.execSQL("update noten set eintragsart = 10 where datum = '" + datum + "' and sus_id = " + susid + " and eintragsart = 11" );
        db.execSQL("update noten set bemerkung = 'Fehlstunde (e)' where datum = '" + datum + "' and sus_id = " + susid + " and eintragsart = 10");
    }

    public void unentschuldige(String datum, String susid){
        db.execSQL("update noten set eintragsart = 11 where datum = '" + datum + "' and sus_id = " + susid + " and eintragsart = 10");
        db.execSQL("update noten set bemerkung = 'Fehlstunde (u)' where datum = '" + datum + "' and sus_id = " + susid + " and eintragsart = 11");
    }

    public void schulischentschuldigt(String datum, String susid){
        db.execSQL("update noten set eintragsart = 12 where datum = '" + datum + "' and sus_id = " + susid + " and eintragsart > 9" );
        db.execSQL("update noten set bemerkung = 'Fehlstunde (sb)' where datum = '" + datum + "' and sus_id = " + susid + " and eintragsart = 12");
    }

    public void fehlstundezuverspaetet(String datum, String susid){
        db.execSQL("update noten set eintragsart = 13 where datum = '" + datum + "' and sus_id = " + susid + " and eintragsart > 9");
        db.execSQL("update noten set bemerkung = 'verspaetet' where datum = '" + datum + "' and sus_id = " + susid + " and eintragsart = 13");
    }

    public int getstundenart(String datum, String kursid){
        Cursor cursor = db.rawQuery("select datum, stundenart from kursbuch where kurs_id="+kursid, null);
        int stunden = 0;
        try {
            if (cursor.moveToFirst()) {
                do {
                    String[] mappeneintrag =new String[]{cursor.getString(0),cursor.getString(1)};
                    if(datum.substring(0,9).equals(mappeneintrag[0].substring(0,9))){
                        stunden = Integer.parseInt(mappeneintrag[1]);
                    }
                } while (cursor.moveToNext());
            }
        } catch (NumberFormatException e) {
        }
        cursor.close();
        return stunden;
    }

    public int zaehlefehlstunden(String susid, String kursid, int entschuldigt){
        int stunden = 0;
        int vergleichsart =10;
        if(entschuldigt==0){
            vergleichsart = 11;
        }
        Cursor cursor = db.rawQuery("select datum, eintragsart from noten where kurs_id="+kursid+" and sus_id="+susid, null);
        if (cursor.moveToFirst()) {
            do {
                try{
                    if(Integer.parseInt(cursor.getString(1))==11) {
                        stunden += getstundenart(cursor.getString(0),kursid);
                    }
                    if(entschuldigt ==1 && Integer.parseInt(cursor.getString(1))==10) {
                        stunden += getstundenart(cursor.getString(0),kursid);
                    }

                }catch (IndexOutOfBoundsException e){

                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return stunden;
    }

    public String getfehlende(String datum, String kursid){
        Cursor cursor = db.rawQuery("select datum, eintragsart, name from noten, sus where sus_id=id and kurs_id="+kursid, null);
        String fehlende = "";
            if (cursor.moveToFirst()) {
                do {
                    try{
                        if (cursor.getString(0).contains(datum)) {
                            if(cursor.getString(1).equals("10")) {
                                fehlende += cursor.getString(2);
                                fehlende += " (e), ";
                            }
                            if(cursor.getString(1).equals("11")) {
                                fehlende += cursor.getString(2);
                                fehlende += " (u), ";
                            }
                            if(cursor.getString(1).equals("12")) {
                                fehlende += cursor.getString(2);
                                fehlende += " (sb), ";
                            }
                        }}catch (IndexOutOfBoundsException e){

                    }
                } while (cursor.moveToNext());
            }
        cursor.close();
        return fehlende;
    }


    public int getmaxsusid(){
        Cursor cursor = db.rawQuery("select max(id) from sus", null);
        cursor.moveToFirst();
        int maximum = Integer.parseInt(cursor.getString(0));
        cursor.close();
        return maximum;
    }

    public int getmaxaufgabenid(){
        Cursor cursor = db.rawQuery("select max(id) from aufgaben", null);
        int maximum = 0;
        try {
            if(cursor.moveToFirst()) {
                maximum = Integer.parseInt(cursor.getString(0));
            }
        } catch (NumberFormatException e) {
        }
        cursor.close();
        return maximum;
    }


    public List<String[]> listekurse(String sj)
    {
        Cursor cursor;
        List<String[]> list = new ArrayList<String[]>();
        if(sj != null && !sj.isEmpty()) {
            //cursor = db.rawQuery("select id, kurs, schuljahr from kurse where instr(schuljahr, '" + sj + "') > 0 order by kurs asc", null);
            cursor = db.rawQuery("select id, kurs, schuljahr, (select count (*) from kursliste where kursliste.kurs_id = kurse.id), (select count (*) from erledigt,aufgaben where aufgaben.id = erledigt.aufgaben_id and aufgabe = 'schriftlich' and aufgaben.kurs_id=kurse.id) from kurse where schuljahr like '%" + sj + "%' or schuljahr = '*' order by kurs asc", null);
            // cursor = db.query("kurse", new String[]{"id", "kurs", "schuljahr"},
            //        "schuljahr ='" + sj + "'", null, null, null, "kurs asc");
        }else{
            cursor = db.query("kurse", new String[]{"id", "kurs", "schuljahr"},
                    null, null, null, null, "kurs asc");
        }

        int x=0;
        if (cursor.moveToFirst()) {
            do {
                String[] b1=new String[]{cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)};

                list.add(b1);

                x=x+1;
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor.close();

        return list;
    }

    public List<String[]> kursliste(String kursid)
    {

        List<String[]> list = new ArrayList<String[]>();
        Cursor cursor = db.rawQuery("select name, vorname, platz, sus_id from sus, kursliste where id=sus_id and kurs_id=" + kursid + " order by name", null);

        int x=0;
        if (cursor.moveToFirst()) {
            do {
                String[] b1=new String[]{cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3)};

                list.add(b1);

                x=x+1;
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor.close();

        return list;
    }


    public List<String[]> kursmappenliste(String kursid)
    {

        List<String[]> list = new ArrayList<String[]>();
        Cursor cursor = db.rawQuery("select id, datum, stundenart, eintrag, hausaufgabe from kursbuch where kurs_id=" + kursid + " order by id desc", null);

        int x=0;
        if (cursor.moveToFirst()) {
            do {
                String fehlende = getfehlende(cursor.getString(1),kursid);
                String[] b1=new String[]{cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),fehlende};

                list.add(b1);

                x=x+1;
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor.close();

        return list;
    }

    public List<String[]> exportnotenliste(String kursid)
    {

        List<String[]> list = new ArrayList<String[]>();
        Cursor cursor = db.rawQuery("select name,vorname,note,bemerkung,datum from noten,sus where kurs_id=14 and sus.id=sus_id", null);

        int x=0;
        if (cursor.moveToFirst()) {
            do {
                String[] b1=new String[]{cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)};

                list.add(b1);

                x=x+1;
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor.close();

        return list;
    }


    public List<String[]> susliste()
    {

        List<String[]> list = new ArrayList<String[]>();
        Cursor cursor = db.rawQuery("select name, vorname, id from sus order by name", null);

        int x=0;
        if (cursor.moveToFirst()) {
            do {
                String[] b1=new String[]{cursor.getString(0),cursor.getString(1),cursor.getString(2)};

                list.add(b1);

                x=x+1;
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor.close();

        return list;
    }

    public String statistik(String kursid){
        String statistikstring = "<HTML><BODY><table border><tr><th bgcolor=\"#00FF00\">1</th><th bgcolor=\"#88FF88\">2</th><th bgcolor=\"#8888FF\">3</th><th bgcolor=\"#FFFF00\">4</th><th bgcolor=\"#FFAA00\">4-</th><th bgcolor=\"#FFAAAA\">5</th><th bgcolor=\"#FF8888\">6</th></tr>";
        String hauptnotenart = geteinstellung(Integer.parseInt(kursid)+1000);
        if(hauptnotenart.equals(""))hauptnotenart = "K1-K2-SM1-SM2-Z";
        String[] hauptnoten = hauptnotenart.split("-");
        Cursor cursor;
        String[] noten = new String[100];
        for(int i = 5;i<10;i++){
            cursor = db.rawQuery("select note from noten where kurs_id = " + kursid + " and eintragsart = " + Integer.toString(i), null);
            int x=0;
            int eins = 0;
            int zwei = 0;
            int drei = 0;
            int vier = 0;
            int vierminus = 0;
            int fuenf = 0;
            int sechs = 0;
            double schnitt=0;
            if (cursor.moveToFirst()) {
                do {
                    noten[x] = cursor.getString(0);
                    schnitt = schnitt + Double.parseDouble(noten[x]);
                    x=x+1;
                } while (cursor.moveToNext());
                schnitt = 5.6666 - schnitt/(3*x);
            }
            cursor.close();
            for(int y = 0; y<x;y++){
                if(Integer.parseInt(noten[y])>12){eins ++;}else{
                    if(Integer.parseInt(noten[y])>9){zwei ++;}else{
                        if(Integer.parseInt(noten[y])>6){drei ++;}else{
                            if(Integer.parseInt(noten[y])>4){vier ++;}else{
                                if(Integer.parseInt(noten[y])>3){vierminus ++;}else{
                                    if(Integer.parseInt(noten[y])>0){fuenf ++;}else{
                                        sechs ++;}}}}}}
            }
            statistikstring = statistikstring + "<tr><td colspan = 7>" + hauptnoten[i-5] +": Anzahl: " + Integer.toString(x) + ", Durchschnitt: " + String.format("%.2f", schnitt) + "</td></tr>";
            statistikstring = statistikstring + "<tr><td bgcolor=\"#00FF00\">" + Integer.toString(eins) + "</td><td bgcolor=\"#88FF88\">" + Integer.toString(zwei) + "</td><td bgcolor=\"#8888FF\">" + Integer.toString(drei) + "</td><td bgcolor=\"#FFFF00\">" + Integer.toString(vier) + "</td><td bgcolor=\"#FFAA00\">" + Integer.toString(vierminus) + "</td><td bgcolor=\"#FFAAAA\">" + Integer.toString(fuenf) + "</td><td bgcolor=\"#FF8888\">" + Integer.toString(sechs) + "</td></tr>";
        }
        statistikstring = statistikstring + "</table></BODY></HTML>";
        return statistikstring;
    }

    public List<String[]> neuestenoten(String kursid2){
        List<String[]> list = new ArrayList<String[]>();
        Cursor cursor;
        cursor = db.rawQuery("select datum, eintragsart, bemerkung, note, name, vorname from noten, sus where sus_id = id and kurs_id=" + kursid2 , null);
        int x=0;
        if (cursor.moveToFirst()) {
            do {
                String[] b1=new String[]{cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(5) + " " + cursor.getString(4)};

                list.add(b1);

                x=x+1;
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor.close();

        return list;

    }


    public List<String[]> notenliste(String kursid2, String susid2){
        List<String[]> list = new ArrayList<String[]>();
        Cursor cursor;
        if(kursid2.equals("0")){
            cursor = db.rawQuery("select datum, eintragsart, bemerkung, note, kurs, schuljahr from noten, kurse where kurs_id = id and sus_id=" + susid2, null);
        }else{
            cursor = db.rawQuery("select datum, eintragsart, bemerkung, note, note, note from noten where kurs_id=" + kursid2 + " and sus_id=" + susid2, null);
        }
        int x=0;
        if (cursor.moveToFirst()) {
            do {
                String[] b1=new String[]{cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4) + " - " + cursor.getString(5)};

                list.add(b1);

                x=x+1;
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor.close();

        return list;
    }

    public String getnotenstring(String kursid, String susid){
        String noten = new String();
        Cursor cursor = db.rawQuery("select datum, eintragsart, bemerkung, note from noten where kurs_id=" + kursid + " and sus_id=" + susid, null);

        int x=0;
        if (cursor.moveToFirst()) {
            do {
                String[] b1=new String[]{cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3)};
                int eart = Integer.parseInt(b1[1]);
                String formatiertenote = "";
                switch (eart){
                    case 1:
                        formatiertenote = "<font color = '#FF2222'>-</font>";
                        break;
                    case 2:
                        formatiertenote = "<font color = '#2222FF'>o</font>";
                        break;
                    case 3:
                        formatiertenote = "<font color = '#22FF22'>+</font>";
                        break;
                    case 4:
                        formatiertenote = "<font color = '#2222FF'>" + punktezunote(b1[3]) + "</font>";
                        break;
                    case 5:
                        formatiertenote = "<font color = '#FFFF00'>" + punktezunote(b1[3]) + "</font>";
                        break;
                    case 6:
                        formatiertenote = "<font color = '#FFFF00'>" + punktezunote(b1[3]) + "</font>";
                        break;
                    case 7:
                        formatiertenote = "<font color = '#FF9900'>" + punktezunote(b1[3]) + "</font>";
                        break;
                    case 8:
                        formatiertenote = "<font color = '#FF9900'>" + punktezunote(b1[3]) + "</font>";
                        break;
                    case 9:
                        formatiertenote = "<font color = '#FF4444'>" + punktezunote(b1[3]) + "</font>";
                        break;
                    case 10:
                        formatiertenote = "<font color = '#FF22FF'>f</font>";
                        break;
                    case 11:
                        formatiertenote = "<font color = '#FF00FF'>F</font>";
                        break;
                    case 12:
                        formatiertenote = "<font color = '#FF22FF'>fs</font>";
                        break;
                    case 13:
                        formatiertenote = "<font color = '#FF22FF'>v</font>";
                        break;
                    case 80:
                        formatiertenote = "";
                        break;
                    case 99:
                        formatiertenote = "";
                        break;
                    default:
                        formatiertenote = "<font color = '#FFFFFF'>" + punktezunote(b1[3]) + "</font>";
                }

                if(eart != 99){noten = noten + formatiertenote + " ";}

                x=x+1;
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor.close();
        return noten;
    }

    public String getsondernote(String kursid, String susid, String notenart){
        String snzur="";
        try{
        Cursor cursor = db.rawQuery("select note from noten where kurs_id=" + kursid + " and sus_id=" + susid + " and eintragsart=" + notenart, null);
        if(cursor.moveToFirst()){
            snzur=cursor.getString(0);
            snzur = punktezunote(snzur);
        }else{
            snzur="--";
        }
        cursor.close();}catch (Exception e){};

        return snzur;
    }

    public String[] getsondernoten(String kursid, String susid){
        String[] snzur=new String[5];
        for(int i=5;i<10;i++){
            snzur[i-5]="--";
        }
        Cursor cursor = db.rawQuery("select note,eintragsart from noten where kurs_id=" + kursid + " and sus_id=" + susid + " and eintragsart>4 and eintragsart<10 ", null);
        if(cursor.moveToFirst()) {
            do {
                int i = 0;
                i = Integer.parseInt(cursor.getString(1));
                if(i>0){
                    snzur[i-5]= cursor.getString(0);
                    snzur[i - 5] = punktezunote(snzur[i - 5]);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return snzur;
    }

    public void setsondernote(String kursid, String susid, String notenart, String note, String datum){
        int notenint = Integer.parseInt(notenart);
        String textdummy="";
        String hauptnotenart = geteinstellung(Integer.parseInt(kursid) + 1000);
        if(hauptnotenart.equals(""))hauptnotenart = "K1-K2-SM1-SM2-Z";
        String[] hauptnoten = hauptnotenart.split("-");
        switch (notenint){
            case 5:
                textdummy = "Festlegung " + hauptnoten[0];
                break;
            case 6:
                textdummy = "Festlegung " + hauptnoten[1];
                break;
            case 7:
                textdummy = "Festlegung " + hauptnoten[2];
                break;
            case 8:
                textdummy = "Festlegung " + hauptnoten[3];
                break;
            case 9:
                textdummy = "Festlegung " + hauptnoten[4];
                break;
        }
        Cursor cursor = db.rawQuery("select note from noten where kurs_id=" + kursid + " and sus_id=" + susid + " and eintragsart = " + notenart, null);
        if(cursor.moveToFirst()) {
            db.execSQL("delete from noten where kurs_id=" + kursid + " and sus_id=" + susid + " and eintragsart = " + notenart);
        }
        cursor.close();
        noteneintrag(kursid, susid, notenart, note, textdummy, datum);
    }

	public void delete(int rowId) {
		db.delete(TABLE_NAME, null, null); 
	}

    public void weisekurszu(String susid, String kursid){
        db.execSQL("insert into kursliste values("+kursid+","+susid+",0)");
    }

    public void weiseplatzzu(String susid, String kursid, String platz){
        db.execSQL("update kursliste set platz = " + platz + " where kurs_id = " + kursid + " and sus_id = " + susid);
    }

    public void entferneauskurs(String susid, String kursid){
        db.execSQL("delete from kursliste where kurs_id = " + kursid + " and sus_id = " + susid);
    }

    public void loeschesus(String susid){
        db.execSQL("delete from sus where id = " + susid);
        db.execSQL("delete from noten where sus_id = " + susid);
        db.execSQL("delete from kursliste where sus_id = " + susid);
    }

    public void updatesus(String susid, String name, String vorname){
        db.execSQL("update sus set name = '" + name + "' where id = " + susid);
        db.execSQL("update sus set vorname = '" + vorname + "' where id = " + susid);
    }

    public void loeschekurs(String kursid){
        db.execSQL("delete from kurse where id = " + kursid);
        db.execSQL("delete from kursliste where kurs_id = " + kursid);
        db.execSQL("delete from noten where kurs_id = " + kursid);
        Cursor cursor = db.rawQuery("select id from aufgaben where kurs_id = " + kursid, null);
        if (cursor.moveToFirst()) {
            do {
                db.execSQL("delete from erledigt where aufgaben_id = " + cursor.getString(0));
            } while (cursor.moveToNext());
        }
        db.execSQL("delete from aufgaben where kurs_id = " + kursid);
        db.execSQL("delete from kursbuch where kurs_id = " + kursid);
    }


    public boolean istinkurs(String susid, String kursid){
        Cursor cursor = db.rawQuery("select * from kursliste where kurs_id = " + kursid + " and sus_id = " + susid, null);
        boolean zurueck;
        if (cursor.moveToFirst()){
            zurueck = true;
        } else {
            zurueck  = false;
        }
        cursor.close();
        return zurueck;
    }

    public void setkursdaten(String kursid, String kursname, String halbjahr){
        db.execSQL("update kurse set kurs = '" + kursname + "' where id = " + kursid);
        db.execSQL("update kurse set schuljahr = '" + halbjahr + "' where id = " + kursid);
    }

    public void uebertragekurs(String kursid, String kursname, String halbjahr){
        Cursor cursor = db.rawQuery("select sus_id from kursliste where kurs_id = " + kursid , null);
        long dummy = insertkurs(kursname, halbjahr);
        Cursor neucurser = db.rawQuery("select id from kurse where kurs = '" + kursname + "' and schuljahr = '" + halbjahr + "'", null);
        String neuid = "";
        if(neucurser.moveToFirst()) {
            neuid = neucurser.getString(0);
        }
        if (cursor.moveToFirst()) {
            do {
                weisekurszu(cursor.getString(0),neuid);
            } while (cursor.moveToNext());
        }
        cursor.close();

    }

    public List<String[]> aufgabenliste (String kursid){
        List<String[]> list = new ArrayList<String[]>();
        Cursor cursor = db.rawQuery("select id, aufgabe from aufgaben where kurs_id= " + kursid , null);

        int x=0;
        if (cursor.moveToFirst()) {
            do {
                String[] b1=new String[]{cursor.getString(0),cursor.getString(1)};

                list.add(b1);

                x=x+1;
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor.close();

        return list;
    }

    public void aufgabeanlegen (String aufgabentext, String kursid){
        int neueaufgid = getmaxaufgabenid() +1;
        String aufgabennummer = Integer.toString(neueaufgid);
        db.execSQL("insert into aufgaben values(" + aufgabennummer + "," + kursid + ",'" + aufgabentext + "')");
    }

    public void aufgabeloeschen (String aufgid){
        db.execSQL("delete from aufgaben where id =" + aufgid);
        db.execSQL("delete from erledigt where aufgaben_id = " + aufgid);
    }

    public String haterledigt (String sus, String aufg){
        Cursor cursor = db.rawQuery("select datum from erledigt where aufgaben_id = " + aufg + " and sus_id = " + sus , null);
        String zurueck = "";
        if (cursor.moveToFirst()){
            zurueck = cursor.getString(0);
        }
        cursor.close();
        return zurueck;
    }

    public boolean hatschriftlich (String susid, String kursid){
        Cursor cursor = db.rawQuery("select sus_id from erledigt, aufgaben where aufgaben_id = id and aufgabe = 'schriftlich' and kurs_id = " + kursid + " and sus_id = " + susid , null);
        Boolean zurueck = false;
        if (cursor.moveToFirst()){
            zurueck = true;
        }
        cursor.close();
        return zurueck;
    }

    public void erledigen (String sus, String aufg, String datum){
        db.execSQL("insert into erledigt values(" + aufg + "," + sus + ",'" + datum + "')");
    }

    public void entledigen (String sus, String aufg){
        db.execSQL("delete from erledigt where sus_id = " + sus + " and aufgaben_id = " + aufg);
    }

    public void setkursmappeneintrag(String kursid, String datum, String stundenart, String eintrag, String hausaufgabe){
        Cursor cursor = db.rawQuery("select max(id) from kursbuch", null);
        int maximum = 0;
        try {
            if(cursor.moveToFirst()) {
                maximum = Integer.parseInt(cursor.getString(0));
            }
        } catch (NumberFormatException e) {
        }
        cursor.close();
        maximum = maximum + 1;
        db.execSQL("insert into kursbuch values( " + maximum + "," + kursid + ",'" + datum + "'," + stundenart + ",'" + eintrag + "','" + hausaufgabe + "')");
    }

    public void updatekursmappeneintrag(String eintragid, String datum, String stundenart, String eintrag, String hausaufgabe){
        db.execSQL("update kursbuch set datum = '" + datum + "' where id = " + eintragid);
        db.execSQL("update kursbuch set stundenart = " + stundenart + " where id = " + eintragid);
        db.execSQL("update kursbuch set eintrag = '" + eintrag + "' where id = " + eintragid);
        db.execSQL("update kursbuch set hausaufgabe = '" + hausaufgabe + "' where id = " + eintragid);
    }

    public void loeschekursmappeneintrag(String eintragid){
        db.execSQL("delete from kursbuch where id = " + eintragid);
    }

    public String[] getkursmappeneintrag(String eintragid){
        Cursor cursor = db.rawQuery("select id, kurs_id, datum, stundenart, eintrag, hausaufgabe from kursbuch where id = " + eintragid, null);
        String[] zurueck = new String[6];
        cursor.moveToFirst();
        zurueck[0] = cursor.getString(0);
        zurueck[1] = cursor.getString(1);
        zurueck[2] = cursor.getString(2);
        zurueck[3] = cursor.getString(3);
        zurueck[4] = cursor.getString(4);
        zurueck[5] = cursor.getString(5);
        cursor.close();
        return zurueck;
    }

    public String[] getstundenplankursid(String schuljahr, String stunde, int eintragsnummer){
        String[] zurueck= new String[3];
        zurueck[0] = "";
        zurueck[1] = "";
        zurueck[2] = "";
        stunde = "%"+stunde+"%";

            Cursor cursor = db.rawQuery("select kurse.id, kurse.kurs, newtable.number from newtable inner join kurse on kurse.id = newtable.id - 1000 where newtable.number like '" + stunde + "' and (kurse.schuljahr = '" + schuljahr + "' or kurse.schuljahr = '*')", null);
            if (cursor.moveToFirst()) {
                if(eintragsnummer == 1){
                    zurueck[0] = cursor.getString(0);
                    zurueck[1] = cursor.getString(1);
                    zurueck[2] = cursor.getString(2);
                }
                if (eintragsnummer>1 && cursor.moveToNext()) {
                    zurueck[0] = cursor.getString(0);
                    zurueck[1] = cursor.getString(1);
                    zurueck[2] = cursor.getString(2);
                }
            }
        cursor.close();
        return zurueck;
    }

    public String[] getkursmappeneintragnachdatum(String kursid, String datum){
        Cursor cursor = db.rawQuery("select id, kurs_id, datum, stundenart, eintrag, hausaufgabe from kursbuch where kurs_id = " + kursid + " and datum like '%" + datum + "%'", null);
        String[] zurueck = new String[6];
        for(int i =0; i<6;i++){
            zurueck[i]="";
        }
        if(cursor.moveToFirst()) {
            zurueck[0] = cursor.getString(0);
            zurueck[1] = cursor.getString(1);
            zurueck[2] = cursor.getString(2);
            zurueck[3] = cursor.getString(3);
            zurueck[4] = cursor.getString(4);
            zurueck[5] = cursor.getString(5);
        }
        cursor.close();
        return zurueck;
        }

    public String[] gettermin(String stunde, String datum){
        Cursor cursor = db.rawQuery("select id, datum, stunde, termin, bemerkung from termine where datum = '" + datum + "' and stunde = " + stunde , null);
        String[] zurueck = new String[5];
        for(int i =0; i<5;i++){
            zurueck[i]="";
        }
        if(cursor.moveToFirst()) {
            zurueck[0] = cursor.getString(0);
            zurueck[1] = cursor.getString(1);
            zurueck[2] = cursor.getString(2);
            zurueck[3] = cursor.getString(3);
            zurueck[4] = cursor.getString(4);
        }
        cursor.close();
        return zurueck;
    }

    public void settermin(String datum, String stunde, String termin, String bemerkung){
        Cursor cursor = db.rawQuery("select max(id) from termine", null);
        int maximum = 0;
        try {
            if(cursor.moveToFirst()) {
                maximum = Integer.parseInt(cursor.getString(0));
            }
        } catch (NumberFormatException e) {
        }
        cursor.close();
        maximum = maximum + 1;
        db.execSQL("insert into termine values( " + maximum +  ",'" + datum + "'," + stunde + ",'" + termin + "','" + bemerkung + "')");
    }

    public void updatetermin(String terminid, String termin, String bemerkung){
        db.execSQL("update termine set termin = '" + termin + "' where id = " + terminid);
        db.execSQL("update termine set bemerkung = '" + bemerkung + "' where id = " + terminid);
    }

    public void loeschetermin(String terminid){
        db.execSQL("delete from termine where id = " + terminid);
    }


    public String geteinstellung (int eid){
        String zurueck;
        Cursor cursor = db.rawQuery("select name from newtable where id = " + eid , null);
        if (cursor.moveToFirst()){
            zurueck = cursor.getString(0);
        } else {
            zurueck  = "";
        }
        cursor.close();
        return zurueck;
    }

    public void seteinstellung (int eid, String wert){
        Cursor cursor = db.rawQuery("select name from newtable where id = " + eid , null);
        if(cursor.moveToFirst()){
            db.execSQL("update newtable set name = '" + wert + "' where id = " + eid);
        }else {
            db.execSQL("insert into newtable values(" + eid + ",'" + wert + "', '', '', '')");
        }
        cursor.close();
    }

    public String geteinstellung2 (int eid){
        String zurueck;
        Cursor cursor = db.rawQuery("select number from newtable where id = " + eid , null);
        if (cursor.moveToFirst()){
            zurueck = cursor.getString(0);
        } else {
            zurueck  = "";
        }
        cursor.close();
        return zurueck;
    }

    public void seteinstellung2 (int eid, String wert){
        Cursor cursor = db.rawQuery("select id from newtable where id = " + eid , null);
        if(cursor.moveToFirst()){
            db.execSQL("update newtable set number = '" + wert + "' where id = " + eid);
        }else {
            db.execSQL("insert into newtable values(" + eid + ", '','" + wert + "', '', '')");
        }
        cursor.close();
    }

    public String[] gettextbeispiele(){
        String[] rueckgabe = new String[10];
        for(int i=0;i<10;i++){
            rueckgabe[i]=geteinstellung(i+10);
        }
        return rueckgabe;
    }

    public String punktezunote(String punkte){
        String ausgabe = "";
        if(punkte.equals("0"))ausgabe = "6";
        if(punkte.equals("1"))ausgabe = "5-";
        if(punkte.equals("2"))ausgabe = "5";
        if(punkte.equals("3"))ausgabe = "5+";
        if(punkte.equals("4"))ausgabe = "4-";
        if(punkte.equals("5"))ausgabe = "4";
        if(punkte.equals("6"))ausgabe = "4+";
        if(punkte.equals("7"))ausgabe = "3-";
        if(punkte.equals("8"))ausgabe = "3";
        if(punkte.equals("9"))ausgabe = "3+";
        if(punkte.equals("10"))ausgabe = "2-";
        if(punkte.equals("11"))ausgabe = "2";
        if(punkte.equals("12"))ausgabe = "2+";
        if(punkte.equals("13"))ausgabe = "1-";
        if(punkte.equals("14"))ausgabe = "1";
        if(punkte.equals("15"))ausgabe = "1+";
        if(punkte.equals("-1"))ausgabe = "F";
        if(punkte.equals("-2"))ausgabe = " ";
        if(punkte.equals("-11"))ausgabe = "gr";
        if(punkte.equals("-12"))ausgabe = "ge";
        if(punkte.equals("-13"))ausgabe = "ro";
        return ausgabe;
    }

    public String notezupunkte(String note){
        String ausgabe ="";
        if(note.equals("6"))ausgabe = "0";
        if(note.equals("5-"))ausgabe = "1";
        if(note.equals("5"))ausgabe = "2";
        if(note.equals("5+"))ausgabe = "3";
        if(note.equals("4-"))ausgabe = "4";
        if(note.equals("4"))ausgabe = "5";
        if(note.equals("4+"))ausgabe = "6";
        if(note.equals("3-"))ausgabe = "7";
        if(note.equals("3"))ausgabe = "8";
        if(note.equals("3+"))ausgabe = "9";
        if(note.equals("2-"))ausgabe = "10";
        if(note.equals("2"))ausgabe = "11";
        if(note.equals("2+"))ausgabe = "12";
        if(note.equals("1-"))ausgabe = "13";
        if(note.equals("1"))ausgabe = "14";
        if(note.equals("1+"))ausgabe = "15";
        if(note.equals("--"))ausgabe = "90";
        return ausgabe;
    }

    public void speichercursor(Cursor cursor, int felderzahl, String csvHeader, String dateiname){
        try {
            File outfile = new File(DATABASE_PATH, dateiname.replace("*","a"));
            FileWriter fileWriter = new FileWriter(outfile);
            String data;
            BufferedWriter out = new BufferedWriter(fileWriter);
            if (cursor != null) {
                out.write(csvHeader);
                while (cursor.moveToNext()) {
                    data="";
                    for(int i=0;i<felderzahl-1;i++){
                        data += cursor.getString(i);
                        data += ";";
                    }
                    data += cursor.getString(felderzahl-1);
                    out.write(data);
                }
                cursor.close();
                out.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void speicherliste(List<String[]> liste, int felderzahl, String csvHeader, String dateiname){
        try {
            String dateiname2=dateiname.replace(" ", "-");
            File outfile = new File(DATABASE_PATH, dateiname2.replace("*","-"));
//            File outfile = new File(Environment.DIRECTORY_DOCUMENTS, dateiname.replace("*","-"));
            Log.d("XX",DATABASE_PATH + dateiname2);
            outfile.createNewFile();

            FileWriter fileWriter = new FileWriter(outfile);
            String[] data=null;
            String data2="";

            BufferedWriter out = new BufferedWriter(fileWriter);
            out.write(csvHeader+"\n");
            while(!liste.isEmpty()){
                data = liste.get(0);
                liste.remove(0);
                data2="";
                for(int i=0;i<felderzahl;i++){
                    data2 += data[i];
                    if(i<felderzahl-1){data2 +=";";}
                }
                data2+="\n";
                out.write(data2);
            }
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public  List<String[]> liescsv(String dateiname){
        List<String[]> list = new ArrayList<String[]>();
        File infile = new File(DATABASE_PATH, dateiname);
        try {
            BufferedReader br = new BufferedReader(new FileReader(infile));
            String line;
            String[] zerteilt;
            while ((line = br.readLine()) != null) {
                zerteilt = line.split(";");
                list.add(zerteilt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

	private static class OpenHelper extends SQLiteOpenHelper {

		OpenHelper(Context context) {
			super(context, DATABASE_PATH + File.separator + DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE kurse (id INTEGER PRIMARY KEY, kurs TEXT, schuljahr TEXT)");
            db.execSQL("CREATE TABLE sus (id INTEGER PRIMARY KEY, name TEXT, vorname TEXT)");
            db.execSQL("CREATE TABLE kursliste (kurs_id INTEGER, sus_id INTEGER, platz INTEGER)");
            db.execSQL("CREATE TABLE noten (kurs_id INTEGER, sus_id INTEGER, eintragsart INTEGER, note INTEGER, bemerkung TEXT, datum DATE)");
            db.execSQL("CREATE TABLE newtable (id INTEGER PRIMARY KEY, name TEXT, number TEXT, skypeId TEXT, address TEXT)");
            db.execSQL("insert into sus values(1, 'Dummy', 'Detlef')" );
            db.execSQL("CREATE TABLE kursbuch (id INTEGER PRIMARY KEY, kurs_id INTEGER, datum DATE, stundenart INTEGER, eintrag TEXT, hausaufgabe TEXT)");
            db.execSQL("CREATE TABLE aufgaben (id INTEGER PRIMARY KEY, kurs_id INTEGER, aufgabe TEXT)");
            db.execSQL("CREATE TABLE erledigt (aufgaben_id INTEGER, sus_id INTEGER, datum DATE)");
            db.execSQL("CREATE TABLE termine (id INTEGER, datum DATE, stunde INTEGER, termin TEXT, bemerkung TEXT)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			/*db.execSQL("DROP TABLE IF EXISTS kurse");
            db.execSQL("DROP TABLE IF EXISTS sus");
            db.execSQL("DROP TABLE IF EXISTS kursliste");
            db.execSQL("DROP TABLE IF EXISTS noten");
            db.execSQL("DROP TABLE IF EXISTS newtable");
			onCreate(db);*/
            if(oldVersion==1) {
                db.execSQL("CREATE TABLE kursbuch (id INTEGER PRIMARY KEY, kurs_id INTEGER, datum DATE, stundenart INTEGER, eintrag TEXT, hausaufgabe TEXT)");
                db.execSQL("CREATE TABLE aufgaben (id INTEGER PRIMARY KEY, kurs_id INTEGER, aufgabe TEXT)");
                db.execSQL("CREATE TABLE erledigt (aufgaben_id INTEGER, sus_id INTEGER, datum DATE)");
                db.execSQL("CREATE TABLE termine (id INTEGER, datum DATE, stunde INTEGER, termin TEXT, bemerkung TEXT)");
            }
            if(oldVersion==2){
                db.execSQL("DROP TABLE IF EXISTS kursbuch");
                db.execSQL("CREATE TABLE kursbuch (id INTEGER PRIMARY KEY, kurs_id INTEGER, datum DATE, stundenart INTEGER, eintrag TEXT, hausaufgabe TEXT)");
                db.execSQL("CREATE TABLE termine (id INTEGER, datum DATE, stunde INTEGER, termin TEXT, bemerkung TEXT)");
            }
            if(oldVersion==3){
                db.execSQL("CREATE TABLE termine (id INTEGER, datum DATE, stunde INTEGER, termin TEXT, bemerkung TEXT)");
            }
        }
	}



}
