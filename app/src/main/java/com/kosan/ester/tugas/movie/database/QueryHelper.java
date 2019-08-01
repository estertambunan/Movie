package com.kosan.ester.tugas.movie.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.kosan.ester.tugas.movie.App;
import com.kosan.ester.tugas.movie.model.Movie;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class QueryHelper {

    private final FavoriteMovie mstFavoriteMovie;
    private SQLiteDatabase mSQLiteDatabase;


    QueryHelper() {
        mstFavoriteMovie = new FavoriteMovie();
    }

    public QueryHelper(SQLiteDatabase sqLiteDatabase) {
        this();
        this.mSQLiteDatabase = sqLiteDatabase;
    }

    // Base content yang digunakan untuk akses content provider
    public static final Uri CONTENT_URI_FAVORITE = new Uri.Builder().scheme(FavoriteMovie.SCHEME)
            .authority(FavoriteMovie.AUTHORITY)
            .appendPath(FavoriteMovie.TABLE_NAME)
            .build();

    public class FavoriteMovie implements BaseColumns {

        private final String TAG = FavoriteMovie.class.getSimpleName();

        public static final String TABLE_NAME = "mst_movie_favorite";
        static final String VOTE_COUNT = "vote_count";
        static final String VIDEO = "video";
        static final String VOTE_AVERAGE = "vote_average";
        static final String TITLE = "title";
        static final String POPULARITY = "popularity";
        static final String POSTER_PATH = "poster_path";
        static final String ORIGINAL_LANGUAGE = "original_language";
        static final String ORIGINAL_TITLE = "original_title";
        static final String GENRE_IDS = "genre_ids";
        static final String BACKDROP_PATH = "backdrop_path";
        static final String ADULT = "adult";
        static final String OVERVIEW = "overview";
        static final String RELEASE_DATE = "release_date";
        static final String IS_FAVORITE = "is_favorite";
        static final String SCHEME = "content";
        public static final String AUTHORITY = "com.kosan.ester.tugas.movie";

        private final String TABLE_SCHEMA = TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY, " +
                VOTE_COUNT + " INTEGER, " +
                VIDEO + " INTEGER," +
                VOTE_AVERAGE + " REAL," +
                TITLE + " TEXT NOT NULL," +
                POPULARITY + " REAL," +
                POSTER_PATH + " TEXT," +
                ORIGINAL_LANGUAGE + " TEXT," +
                ORIGINAL_TITLE + " TEXT," +
                GENRE_IDS + " TEXT," +
                BACKDROP_PATH + " TEXT," +
                ADULT + " INTEGER," +
                OVERVIEW + " TEXT," +
                RELEASE_DATE + " TEXT," +
                IS_FAVORITE + " INTEGER NOT NULL" +
                ");";

        /**
         * Create Table Data Movie Favorite
         */
        void createTable(SQLiteDatabase db) {
            try {
                db.execSQL("CREATE TABLE " + TABLE_SCHEMA);
            } catch (SQLException ex) {
                Log.e(TAG, "Create Table : " + ex);
            }
        }

        /**
         * Drop Table Data Movie Favorite
         */
        void dropTable(SQLiteDatabase db) {
            try {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            } catch (SQLException ex) {
                Log.e(TAG, "Drop Table : " + ex);
            }
        }

        /**
         * Search Data Movie Favorite
         */
        public Movie getFindFavoriteMove(int id) throws SQLiteException {
            Movie movie = null;
            if (mSQLiteDatabase != null) {

                Cursor cursor = mSQLiteDatabase.query(TABLE_NAME, new String[]{
                                _ID, VOTE_COUNT, VIDEO, VOTE_AVERAGE, TITLE, POPULARITY,
                                POSTER_PATH, ORIGINAL_LANGUAGE, ORIGINAL_TITLE,
                                GENRE_IDS, BACKDROP_PATH, ADULT, OVERVIEW, RELEASE_DATE,
                                IS_FAVORITE}, _ID + "=?",
                        new String[]{String.valueOf(id)}, null, null, null, null);
                cursor.moveToFirst();

                if (cursor.moveToFirst()) {
                    movie = new Movie();
                    movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                    movie.setVoteCount(cursor.getInt(cursor.getColumnIndexOrThrow(VOTE_COUNT)));
                    movie.setVideo(cursor.getInt(cursor.getColumnIndexOrThrow(VIDEO)) == 1);
                    movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(VOTE_AVERAGE)));
                    movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                    movie.setPopularity(cursor.getDouble(cursor.getColumnIndexOrThrow(POPULARITY)));
                    movie.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));
                    movie.setOriginalLanguage(cursor.getString(cursor.getColumnIndexOrThrow(ORIGINAL_LANGUAGE)));
                    movie.setOriginalTitle(cursor.getString(cursor.getColumnIndexOrThrow(ORIGINAL_TITLE)));
                    movie.setGenreIds(new Gson().fromJson(cursor.getString(cursor.getColumnIndexOrThrow(GENRE_IDS)), int[].class));
                    movie.setBackdropPath(cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP_PATH)));
                    movie.setAdult(cursor.getInt(cursor.getColumnIndexOrThrow(ADULT)) == 1);
                    movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                    movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                    movie.setIsFavorite(cursor.getInt(cursor.getColumnIndexOrThrow(IS_FAVORITE)) == 1);
                }
                cursor.close();
            }

            return movie;
        }

        /**
         * Get All Data Movie Favorite
         * Limit for maximum show movie
         * offset for step pagination
         */

        public List<Movie> getAllData(int limit, int offset) throws SQLiteException {
            List<Movie> output;
            if (mSQLiteDatabase != null) {
                /*Cursor cursor = mSQLiteDatabase.query(TABLE_NAME, null, IS_FAVORITE + "=?",
                        new String[]{String.valueOf("1")}, null, null, _ID + " ASC",
                        String.valueOf(limit == 0 ? null : limit));*/
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + IS_FAVORITE + "=? LIMIT ? OFFSET ?";
                Cursor cursor = mSQLiteDatabase.rawQuery(sql,
                        new String[]{String.valueOf("1"), String.valueOf(limit), String.valueOf(offset)});
                cursor.moveToFirst();
                output = new ArrayList<>();
                if (cursor.getCount() > 0) {
                    do {
                        Movie movie = new Movie();
                        movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                        movie.setVoteCount(cursor.getInt(cursor.getColumnIndexOrThrow(VOTE_COUNT)));
                        movie.setVideo(cursor.getInt(cursor.getColumnIndexOrThrow(VIDEO)) == 1);
                        movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(VOTE_AVERAGE)));
                        movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                        movie.setPopularity(cursor.getDouble(cursor.getColumnIndexOrThrow(POPULARITY)));
                        movie.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));
                        movie.setOriginalLanguage(cursor.getString(cursor.getColumnIndexOrThrow(ORIGINAL_LANGUAGE)));
                        movie.setOriginalTitle(cursor.getString(cursor.getColumnIndexOrThrow(ORIGINAL_TITLE)));
                        movie.setGenreIds(new Gson().fromJson(cursor.getString(cursor.getColumnIndexOrThrow(GENRE_IDS)), int[].class));
                        movie.setBackdropPath(cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP_PATH)));
                        movie.setAdult(cursor.getInt(cursor.getColumnIndexOrThrow(ADULT)) == 1);
                        movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                        movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                        movie.setIsFavorite(cursor.getInt(cursor.getColumnIndexOrThrow(IS_FAVORITE)) == 1);

                        output.add(movie);
                        cursor.moveToNext();

                    } while (!cursor.isAfterLast());
                }
                cursor.close();
                App.getDatabaseClose();
                return output;
            }

            return null;
        }

        /**
         * Query Provider
         * Content Provider
         */

        public Cursor getQueryProvider() {
            if (mSQLiteDatabase != null) {
                return mSQLiteDatabase.query(TABLE_NAME
                        , null
                        , IS_FAVORITE+"=?"
                        , new String[]{String.valueOf("1")}
                        , null
                        , null
                        , _ID + " DESC");
            }

            return null;
        }

        /**
         * Insert Provider
         * Content Provider
         */
        public long insertProvider(ContentValues values) {
            if (mSQLiteDatabase != null)
                return mSQLiteDatabase.insert(TABLE_NAME, null, values);

            return 0;
        }

        /**
         * Update Provider
         * Content Provider
         */
        public int updateProvider(String id, ContentValues values) {
            if (mSQLiteDatabase != null)
                return mSQLiteDatabase.update(TABLE_NAME, values, _ID + " = ?", new String[]{id});

            return 0;
        }

        /**
         * Delete Provider
         * Content Provider
         */
        public int deleteProvider(String id) {
            if (mSQLiteDatabase != null)
                return mSQLiteDatabase.delete(TABLE_NAME, _ID + " = ?", new String[]{id});

            return 0;
        }

    }


    /**
     * get Content Values From Movies
     * Content Provider
     */

    public static ContentValues getAllContentValue(Movie movie){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteMovie._ID, movie.getId());
        contentValues.put(FavoriteMovie.VOTE_COUNT, movie.getVoteCount());
        contentValues.put(FavoriteMovie.VIDEO, movie.isVideo());
        contentValues.put(FavoriteMovie.VOTE_AVERAGE, movie.getVoteAverage());
        contentValues.put(FavoriteMovie.TITLE, movie.getTitle());
        contentValues.put(FavoriteMovie.POPULARITY, movie.getPopularity());
        contentValues.put(FavoriteMovie.POSTER_PATH, movie.getPosterPath());
        contentValues.put(FavoriteMovie.ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
        contentValues.put(FavoriteMovie.ORIGINAL_TITLE, movie.getOriginalTitle());
        contentValues.put(FavoriteMovie.GENRE_IDS, new Gson().toJson(movie.getGenreIds()));
        contentValues.put(FavoriteMovie.BACKDROP_PATH, movie.getBackdropPath());
        contentValues.put(FavoriteMovie.ADULT, movie.isAdult());
        contentValues.put(FavoriteMovie.OVERVIEW, movie.getOverview());
        contentValues.put(FavoriteMovie.RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(FavoriteMovie.IS_FAVORITE, 1);

        return  contentValues;
    }

    public static ContentValues getIsFavorite(boolean isFavorite){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteMovie.IS_FAVORITE, String.valueOf(isFavorite ? 1 : 0));

        return  contentValues;
    }


    /**
     * Call Define Database Helper
     */
    public FavoriteMovie getFavoriteMovie() {
        return mstFavoriteMovie;
    }

}
