package com.younger.younger.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Younger on 2019-12-08.
 */
public class BookProvider extends ContentProvider {

    public static final String AUTHORITY = "com.younger.younger.contentprovider.provider";
    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://"+AUTHORITY +"/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://"+AUTHORITY +"/user");

    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;


    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        mUriMatcher.addURI(AUTHORITY,"book",BOOK_URI_CODE);
        mUriMatcher.addURI(AUTHORITY,"user",BOOK_URI_CODE);
    }



    private Context context;
    private SQLiteDatabase mDb;
    private String getTableName(Uri uri){

        String tableName = null;
        switch (mUriMatcher.match(uri)){
            case BOOK_URI_CODE:

                tableName = DbOpenHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:

                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
                default:
                    break;

        }
        return tableName;

    }
    @Override
    public boolean onCreate() {

        context = getContext();
        initProviderData();
        Log.e("=====","=======onCreate,current thread:"+Thread.currentThread().getName());
        return true;
    }

    private void initProviderData() {
        mDb = new DbOpenHelper(context).getWritableDatabase();
        mDb.execSQL("delete from "+ DbOpenHelper.BOOK_TABLE_NAME);
        mDb.execSQL("delete from "+ DbOpenHelper.USER_TABLE_NAME);
        mDb.execSQL("insert into book values(3,'Android')");
        mDb.execSQL("insert into book values(4,'iOS')");
        mDb.execSQL("insert into book values(5,'html5')");
        mDb.execSQL("insert into user values(1,'jake',1)");
        mDb.execSQL("insert into user values(2,'jasmine',0)");

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Log.e("=====","=======onCreate,current thread:"+Thread.currentThread().getName());

        String table = getTableName(uri);

        if (table ==null){
            throw new IllegalArgumentException("Unsupported Uri"+uri);
        }

        return mDb.query(table,strings,s,strings1,null,null,s1,null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        String table = getTableName(uri);
        if (null ==table){
            throw new IllegalArgumentException("Unsupported Uri"+uri);
        }
        mDb.insert(table,null,contentValues);
        context.getContentResolver().notifyChange(uri,null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        String table = getTableName(uri);
        if (null ==table){
            throw new IllegalArgumentException("Unsupported Uri"+uri);
        }
        int count = mDb.delete(table,s,strings);
        if (count>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {

        String table = getTableName(uri);
        if (null ==table){
            throw new IllegalArgumentException("Unsupported Uri"+uri);
        }

        int row = mDb.update(table,contentValues,s,strings);
        if (row>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return row;
    }
}
