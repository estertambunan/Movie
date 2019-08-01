package com.kosan.ester.tugas.movie.database.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kosan.ester.tugas.movie.App;
import com.kosan.ester.tugas.movie.database.QueryHelper;



public class MovieProvider extends ContentProvider {


    private static final int GET_ALL_FAVORITE = 1;
    private static final int FAVORITE_ID = 2;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        // content://com.kosan.ester.tugas.movie/mst_movie_favorite
        sUriMatcher.addURI(QueryHelper.FavoriteMovie.AUTHORITY, QueryHelper.FavoriteMovie.TABLE_NAME, GET_ALL_FAVORITE);

        // content://com.kosan.ester.tugas.movie/mst_movie_favorite/id
        sUriMatcher.addURI(QueryHelper.FavoriteMovie.AUTHORITY,
                QueryHelper.FavoriteMovie.TABLE_NAME + "/#",
                FAVORITE_ID);
    }


    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)){
            case GET_ALL_FAVORITE:
                cursor = App.getDatabaseOpen().getFavoriteMovie().getQueryProvider();
                break;
            default :
                cursor = null;
                break;

        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        long added;

        switch (sUriMatcher.match(uri)) {
            case GET_ALL_FAVORITE:
                added = App.getDatabaseOpen().getFavoriteMovie().insertProvider(values);
                break;
            default:
                added = 0;
                break;
        }

        if (added > 0) {
            if(getContext() !=null){
                getContext().getContentResolver().notifyChange(uri, null);
            }

        }
        return Uri.parse(QueryHelper.CONTENT_URI_FAVORITE + "/" + added);
    }

    @Override
    public int delete(@NonNull  Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE_ID:
                deleted = App.getDatabaseOpen().getFavoriteMovie().deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }

        if (deleted > 0) {
            if(getContext() !=null)
                getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleted;
    }

    @Override
    public int update(@NonNull  Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable  String[] selectionArgs) {
        int updated;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE_ID:
                updated = App.getDatabaseOpen().getFavoriteMovie().updateProvider(uri.getLastPathSegment(), values);
                break;
            default:
                updated = 0;
                break;
        }

        if (updated > 0) {
            if(getContext() !=null)
                    getContext().getContentResolver().notifyChange(uri, null);
        }
        return updated;
    }
}
