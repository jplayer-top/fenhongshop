package com.fanglin.dutyfree;

import android.app.Application;
import com.fanglin.fhlib.other.FHLog;
import org.xutils.DbManager;
import org.xutils.x;
/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/12/1-下午3:58.
 * 功能描述: FHApp
 */
public class FHApp extends Application implements DbManager.DbOpenListener, DbManager.DbUpgradeListener {
    DbManager.DaoConfig config;
    int DBVER = 5;
    private static FHApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        x.Ext.init(this);
        x.Ext.setDebug(true);
        config = new DbManager.DaoConfig();
        config.setDbVersion(DBVER).setDbOpenListener(this).setDbUpgradeListener(this);
    }



    @Override
    public void onDbOpened(DbManager db) {
        db.getDatabase().enableWriteAheadLogging();
    }

    @Override
    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
        try {
            db.dropTable(People.class);
        } catch (Exception e) {
            FHLog.d("Plucky", "drop:" + e.getMessage());
        }
    }

    public DbManager getDb() {
        return x.getDb(config);
    }

    public static FHApp getApp() {
        return app;
    }
}
