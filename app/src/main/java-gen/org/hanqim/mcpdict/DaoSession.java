package org.hanqim.mcpdict;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import org.hanqim.mcpdict.User;
import org.hanqim.mcpdict.Mcp;

import org.hanqim.mcpdict.UserDao;
import org.hanqim.mcpdict.McpDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userDaoConfig;
    private final DaoConfig mcpDaoConfig;

    private final UserDao userDao;
    private final McpDao mcpDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        mcpDaoConfig = daoConfigMap.get(McpDao.class).clone();
        mcpDaoConfig.initIdentityScope(type);

        userDao = new UserDao(userDaoConfig, this);
        mcpDao = new McpDao(mcpDaoConfig, this);

        registerDao(User.class, userDao);
        registerDao(Mcp.class, mcpDao);
    }
    
    public void clear() {
        userDaoConfig.getIdentityScope().clear();
        mcpDaoConfig.getIdentityScope().clear();
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public McpDao getMcpDao() {
        return mcpDao;
    }

}
