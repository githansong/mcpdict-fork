package org.hanqim.mcpdict;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.inputmethod.InputMethodManager;

public class MCPDictApplication extends Application {

    private static Context context;
    private static Resources resources;
    private static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        resources = context.getResources();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // close db etc.
    }

    public static Context getContext() {
        return context;
    }
    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
    public static Resources getResource() {
        return resources;
    }
    public static String getStr(int id) {
        return resources.getString(id);
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            IBinder ib = activity.getCurrentFocus().getWindowToken();
            inputManager.hideSoftInputFromWindow(ib, InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
