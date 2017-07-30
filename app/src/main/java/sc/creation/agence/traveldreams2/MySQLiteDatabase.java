package sc.creation.agence.traveldreams2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Stef on 30/07/2017.
 */
public class MySQLiteDatabase extends SQLiteOpenHelper {


    private static final String TABLE_FAVORIS = "table_favoris";
    private static final String COL_ID = "ID";
    private static final String COL_TITRE = "TITRE";
    private static final String COL_URLIMAGE= "URLIMAGE";
    private static final String COL_URLRESA = "URLRESA";
    private static final String COL_RESUME = "RESUME";


    private static final String CREATE_TABLE_FAVORIS = "CREATE TABLE "
            + TABLE_FAVORIS + " (" + COL_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_TITRE
            + " TEXT NOT NULL, " + COL_URLIMAGE + " TEXT NOT NULL, " + COL_URLRESA + " TEXT NOT NULL, "
            + COL_RESUME + " TEXT NOT NULL);";

    public MySQLiteDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory,
                            int version) {
        super(context, name, factory, version);
    }

    /**
     * Cette méthode est appelée lors de la toute première création de la base
     * de données. Ici, on doit créer les tables et éventuellement les populer.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on crée la table table_contacts dans la BDD
        db.execSQL(CREATE_TABLE_FAVORIS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on supprime la table table_contacts de la BDD et on recrée la BDD
        db.execSQL("DROP TABLE " + TABLE_FAVORIS + ";");
        onCreate(db);
    }
}

