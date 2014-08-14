package io.ganguo.app.basic;

import android.content.SharedPreferences;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;

import io.ganguo.app.basic.exception.NotRegistionContextException;

/**
 * 程序配置文件（读取和写入）
 * <p/>
 * Created by zhihui_chen on 14-8-4.
 */
public class Config {
    private static final String TAG = Config.class.getName();

    /**
     * 考试宝典数据目录
     */
    public final static String APP_DATA_PATH = "app";
    /**
     * 临时目录名称
     */
    public final static String APP_TEMP_PATH = "temp";

    private static AppContext context = null;

    public static void register(AppContext context) {
        Config.context = context;
    }

    /**
     * 是否存在SD卡
     *
     * @return
     */
    public static boolean isExsitSDCard() {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 取得空闲SD卡空间大小
     *
     * @return MB
     */
    public static long getAvailaleSize() {
        File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
        StatFs stat = new StatFs(path.getPath());
        /* 获取block的SIZE */
        long blockSize = stat.getBlockSize();
        /* 空闲的Block的数量 */
        long availableBlocks = stat.getAvailableBlocks();
        /* 返回bit大小值 */
        return availableBlocks * blockSize / 1024 / 1024;
    }

    /**
     * 获取 ks_data全路径目录
     *
     * @return
     */
    public static String getAppDataPath() {
        // 判断是否挂载了SD卡
        String dataPath = null;
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            dataPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath()
                    + File.separator
                    + APP_DATA_PATH
                    + File.separator;
            File file = new File(dataPath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return dataPath;
    }

    /**
     * 获取程序临时目录
     *
     * @return
     */
    public static String getAppTempPath() {
        return getAppDataPath() + File.separator + APP_TEMP_PATH
                + File.separator;
    }

    /**
     * 获取Preference设置
     */
    public static SharedPreferences getSharedPreferences() {
        if (Config.context == null) {
            try {
                throw new NotRegistionContextException("Config error!");
            } catch (NotRegistionContextException e) {
                Log.e(TAG, "配置类没有注册上AppContext(下文环境)", e);
            }
        }
        return PreferenceManager.getDefaultSharedPreferences(Config.context);
    }

}
