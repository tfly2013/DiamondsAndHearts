package com.diamondshearts;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Turn {

    public static final String TAG = "EBTurn";

    public String data = "";
    public int turnCounter;

    public Turn() {
    }

    // The byte array that write out to the TBMP API.
    public byte[] persist() {
        JSONObject retVal = new JSONObject();

        try {
            retVal.put("data", data);
            retVal.put("turnCounter", turnCounter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String st = retVal.toString();

        Log.d(TAG, "==== PERSISTING\n" + st);

        return st.getBytes(Charset.forName("UTF-16"));
    }

    // Creates a new instance of Turn.
    static public Turn unpersist(byte[] byteArray) {

        if (byteArray == null) {
            Log.d(TAG, "Empty array---possible bug.");
            return new Turn();
        }

        String st = null;
        try {
            st = new String(byteArray, "UTF-16");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return null;
        }

        Log.d(TAG, "====UNPERSIST \n" + st);

        Turn retVal = new Turn();

        try {
            JSONObject obj = new JSONObject(st);

            if (obj.has("data")) {
                retVal.data = obj.getString("data");
            }
            if (obj.has("turnCounter")) {
                retVal.turnCounter = obj.getInt("turnCounter");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retVal;
    }
}
