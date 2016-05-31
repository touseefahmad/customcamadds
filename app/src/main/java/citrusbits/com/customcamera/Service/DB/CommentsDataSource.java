package citrusbits.com.customcamera.Service.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Touseef on 5/10/2016.
 */
public class CommentsDataSource {


    public String LOGTAG="FC DATABASE";

    SQLiteOpenHelper helper;
    SQLiteDatabase database;
    Context context;

    private static final String[] allColumns = {
            MockCamDbOpenHelper.COLUMN_COMMENT_ID,
            MockCamDbOpenHelper.COLUMN_TAG,
            MockCamDbOpenHelper.COULUMN_COMMENT

    };

    public CommentsDataSource(Context context){
        helper = new MockCamDbOpenHelper(context);
        this.context = context;
    }

    public void open(){
        database = helper.getWritableDatabase();
        Log.d(LOGTAG, "Database open...!");
    }

    public void close(){
        helper.close();
        Log.d(LOGTAG, "Database close...!");
    }

    public ModelClass add(ModelClass modelClass){

        ContentValues values = new ContentValues();
       /* values.put(FCDbOpenHelper.COLUMN_COUNTRY_ID, chemical.getChemicalId());*/
        values.put(MockCamDbOpenHelper.COLUMN_TAG, modelClass.getTag());
        values.put(MockCamDbOpenHelper.COULUMN_COMMENT, modelClass.getComment());
        long id = database.insert(MockCamDbOpenHelper.TABLE_COMMENTS, null, values);
        //		user.setId(id);
        Log.e("id",String .valueOf(id));
        return modelClass;
    }
    public List<ModelClass> findAll(){
        Log.i("MockDB", "inside find all method");

        List<ModelClass> CommentsList = new ArrayList<ModelClass>();
        Log.i("MockDB", "before query");

        Cursor c = database.query(MockCamDbOpenHelper.TABLE_COMMENTS, allColumns, null, null, null, null, null);

        Log.i("MockDB", "after query");

        if(c.getCount() > 0){
            Log.i("MockDB", "Inside if");
            while(c.moveToNext()){
                ModelClass comment = new ModelClass();

                comment.setTag(c.getString(c.getColumnIndex(MockCamDbOpenHelper.COLUMN_TAG)));
                comment.setComment(c.getString(c.getColumnIndex(MockCamDbOpenHelper.COULUMN_COMMENT)));


                CommentsList.add(comment);
            }
        }

        return CommentsList;
    }
}
