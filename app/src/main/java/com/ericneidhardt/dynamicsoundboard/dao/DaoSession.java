package com.ericneidhardt.dynamicsoundboard.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.ericneidhardt.dynamicsoundboard.dao.MediaPlayerData;

import com.ericneidhardt.dynamicsoundboard.dao.MediaPlayerDataDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig mediaPlayerDataDaoConfig;

    private final MediaPlayerDataDao mediaPlayerDataDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        mediaPlayerDataDaoConfig = daoConfigMap.get(MediaPlayerDataDao.class).clone();
        mediaPlayerDataDaoConfig.initIdentityScope(type);

        mediaPlayerDataDao = new MediaPlayerDataDao(mediaPlayerDataDaoConfig, this);

        registerDao(MediaPlayerData.class, mediaPlayerDataDao);
    }
    
    public void clear() {
        mediaPlayerDataDaoConfig.getIdentityScope().clear();
    }

    public MediaPlayerDataDao getMediaPlayerDataDao() {
        return mediaPlayerDataDao;
    }

}
