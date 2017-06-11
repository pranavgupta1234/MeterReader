package pranav.apps.amazing.meterreader.session;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Pranav Gupta on 6/11/2017.
 */

public class SessionManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    private static final String PREF_NAME = "MeterReader";
    private static final String M_NAME = "NAME";
    private static final String IS_LOGIN = "isLoggedIn";


    public SessionManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
    }

    public void createLoginSession(String name){

        editor = pref.edit();
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(M_NAME,name);
        editor.apply();

    }

    public String getmName(){
        return pref.getString(M_NAME,"");
    }

    public Boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN,false);
    }



}
