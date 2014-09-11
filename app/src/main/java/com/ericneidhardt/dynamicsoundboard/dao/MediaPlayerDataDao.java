package com.ericneidhardt.dynamicsoundboard.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.ericneidhardt.dynamicsoundboard.dao.MediaPlayerData;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table MEDIA_PLAYER_DATA.
*/
public class MediaPlayerDataDao extends AbstractDao<MediaPlayerData, Long> {

    public static final String TABLENAME = "MEDIA_PLAYER_DATA";

    /**
     * Properties of entity MediaPlayerData.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PlayerId = new Property(1, String.class, "playerId", false, "PLAYER_ID");
        public final static Property FragmentTag = new Property(2, String.class, "fragmentTag", false, "FRAGMENT_TAG");
        public final static Property Label = new Property(3, String.class, "label", false, "LABEL");
        public final static Property Uri = new Property(4, String.class, "uri", false, "URI");
    };


    public MediaPlayerDataDao(DaoConfig config) {
        super(config);
    }
    
    public MediaPlayerDataDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'MEDIA_PLAYER_DATA' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'PLAYER_ID' TEXT UNIQUE ," + // 1: playerId
                "'FRAGMENT_TAG' TEXT," + // 2: fragmentTag
                "'LABEL' TEXT," + // 3: label
                "'URI' TEXT);"); // 4: uri
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'MEDIA_PLAYER_DATA'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, MediaPlayerData entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String playerId = entity.getPlayerId();
        if (playerId != null) {
            stmt.bindString(2, playerId);
        }
 
        String fragmentTag = entity.getFragmentTag();
        if (fragmentTag != null) {
            stmt.bindString(3, fragmentTag);
        }
 
        String label = entity.getLabel();
        if (label != null) {
            stmt.bindString(4, label);
        }
 
        String uri = entity.getUri();
        if (uri != null) {
            stmt.bindString(5, uri);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public MediaPlayerData readEntity(Cursor cursor, int offset) {
        MediaPlayerData entity = new MediaPlayerData( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // playerId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // fragmentTag
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // label
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // uri
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, MediaPlayerData entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPlayerId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setFragmentTag(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setLabel(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUri(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(MediaPlayerData entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(MediaPlayerData entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
