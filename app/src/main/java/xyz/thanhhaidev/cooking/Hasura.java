package xyz.thanhhaidev.cooking;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ThanhHaiDev on 25/5/17.
 */

public class Hasura {

    public class Config {
        //Replace the following with your cluster name
        private static final String CLUSTER_NAME = "aimlessness17";
        public static final String AUTH_URL = "https://auth." + CLUSTER_NAME + ".hasura-app.io/v1/";
        public static final String DATA_URL = "https://data." + CLUSTER_NAME + ".hasura-app.io/v1/";
        public static final String FILESTORE_URL = "https://filestore." + CLUSTER_NAME + ".hasura-app.io/v1/";
        public static final String CUSTOM = "https://api." + CLUSTER_NAME + ".hasura-app.io";
    }

    private static final String PREF_NAME = "SharedPrefName";
    private static final String AUTHTOKEN_KEY = "AuthToken";

    public static void saveSessionToken(String authToken, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(AUTHTOKEN_KEY, authToken);
        editor.apply();
    }

    public static String getSessionToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString(AUTHTOKEN_KEY, null);
    }
}
