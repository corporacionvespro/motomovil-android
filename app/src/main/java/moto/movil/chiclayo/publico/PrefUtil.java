package moto.movil.chiclayo.publico;

import android.content.Context;
import android.content.SharedPreferences;
import static android.provider.ContactsContract.Directory.PACKAGE_NAME;

/**
 * By: El Bryant
 */

public class PrefUtil {
    private static final String NAME_REFERENCE = "prefMotomovil";
    private Context context;
    public static final int FAILURE_RESULT = 1;
    public static final int SUCCESS_RESULT = 0;
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
    public static final String LOGIN_STATUS = "login_status";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";

    public PrefUtil(Context context) {
        this.context = context;
    }

    public void saveGenericValue(String campo, String valor) {
        SharedPreferences prefs = context.getSharedPreferences(NAME_REFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(campo, valor);
        editor.apply();
    }

    public void clearAll() {
        SharedPreferences prefs = context.getSharedPreferences(NAME_REFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        editor.commit();
    }

    public String getStringValue(String campo) {
        SharedPreferences prefs = context.getSharedPreferences(NAME_REFERENCE, Context.MODE_PRIVATE);
        return prefs.getString(campo, "");
    }
}
