package citrusbits.com.customcamera.Service.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Touseef on 5/10/2016.
 */
public class MockCamDbOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mockcam.db";
    private static final int DATABASE_VERSION = 1;
    /*******************************************************
     *
     * Country
     *
     */
    public static final String TABLE_COMMENTS = "comments";
    public static final String COLUMN_COMMENT_ID= "comment_id";
    public static final String COLUMN_TAG = "comment_tag";
    public static final String COULUMN_COMMENT="comment_comemnt";

    private static final String CREATE_TABLE_COMMENTS =
            "CREATE TABLE " + TABLE_COMMENTS + " (" +
                    COLUMN_COMMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TAG + " TEXT, " +
                    COULUMN_COMMENT + " TEXT" +

                    ")";





    /************************************************************
     * Funding
     */










    public MockCamDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COMMENTS);



        Log.e("MOCKCAM DataBase", "Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);


        Log.e("MOCKCAM DataBase", "Upgrade");
        onCreate(db);
    }
}
