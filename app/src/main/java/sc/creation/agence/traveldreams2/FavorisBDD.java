package sc.creation.agence.traveldreams2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Stef on 30/07/2017.
 */

public class FavorisBDD {

    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "favoris.db";
    private static final String TABLE_FAVORIS = "table_favoris";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_TITRE = "TITRE";
    private static final int NUM_COL_TITRE = 1;
    private static final String COL_URLIMAGE= "URLIMAGE";
    private static final int NUM_COL_URLIMAGE = 2;
    private static final String COL_URLRESA = "URLRESA";
    private static final int NUM_COL_URLRESA = 3;
    private static final String COL_RESUME = "RESUME";
    private static final int NUM_COL_RESUME = 4;
    private static  final String COL_PAYS ="PAYS";
    private static final int NUM_COL_PAYS = 5;
    private static final String COL_VILLE = "VILLE";
    private static final int NUM_COL_VILLE = 6;

    private SQLiteDatabase bdd;

    private MySQLiteDatabase mySQLiteDatabase;

    public FavorisBDD(Context context){

        mySQLiteDatabase = new MySQLiteDatabase(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open() {
        bdd = mySQLiteDatabase.getWritableDatabase();
    }
    public void close() {
        bdd.close();
    }

    public SQLiteDatabase getBDD() {
        return bdd;
    }

    /**
     * @param favoris
     */

    public long insertFavori(Favoris favoris){
        ContentValues values = new ContentValues();

        values.put(COL_TITRE,favoris.getTitre());
        values.put(COL_RESUME,favoris.getResume());
        values.put(COL_URLIMAGE,favoris.getUrlim());
        values.put(COL_URLRESA,favoris.getUrlresa());
        values.put(COL_PAYS,favoris.getPays());
        values.put(COL_VILLE,favoris.getVille());

        return bdd.insert(TABLE_FAVORIS,null,values);


    }

    /**
     *  @param id
     */

    public int removeFavoriWithID(int id) {
        return bdd.delete(TABLE_FAVORIS, COL_ID + " = " + id, null);
    }

    public List<Map<String, ?>> getAllPersonnes() {
        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();



        String selectQuery = "SELECT * FROM " + TABLE_FAVORIS + " ORDER BY ID ASC";
        Cursor cursor = bdd.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                Map<String, Object> map =new HashMap();
                map.put("id", cursor.getInt(0));
                map.put("titre", cursor.getString(1));
                map.put("resume", cursor.getString(4));
                map.put("pays", cursor.getString(5));
                map.put("ville", cursor.getString(6));
                items.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;



    }
    private Favoris cursorToFavoris(Cursor c) {
        // si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        // Sinon on se place sur le premier élément
        c.moveToFirst();

        Favoris favori = new Favoris();
        favori.setId(c.getInt(NUM_COL_ID));
        favori.setTitre(c.getString(NUM_COL_TITRE));
        favori.setPays(c.getString(NUM_COL_PAYS));
        favori.setVille(c.getString(NUM_COL_VILLE));
        favori.setUrlim(c.getString(NUM_COL_URLIMAGE));
        favori.setResume(c.getString(NUM_COL_RESUME));


        c.close();

        return favori;
    }


}
