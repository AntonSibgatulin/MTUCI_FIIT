package ru.mtucifiit.mtucifiit.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.service.controls.Control;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import ru.mtucifiit.mtucifiit.CheckTypeUserActivity;
import ru.mtucifiit.mtucifiit.config.Config;
import ru.mtucifiit.mtucifiit.config.NetConfig;
import ru.mtucifiit.mtucifiit.model.group.Group;
import ru.mtucifiit.mtucifiit.model.project.HistoryModel;

public class RequestService {

    private String username = null;
    private Integer codeFast = null;
    private Activity context;
    private RequestQueue requestQueue;

    private SharedPreferences sharedPreferences = null;

    public NetConfig netConfig = new NetConfig();


    public RequestService(Activity context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Config.name_app_shared_preferences, Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(context);
    }


    public void getRequest(String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener, errorListener) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json;charset=UTF-8");
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

    public String getStringSh(String key, String def) {
        return sharedPreferences.getString(key, def);
    }

    public String beautGroup(String group) {
        return group.replaceAll("B", "Б").replaceAll("F", "Ф").replaceAll("I", "И").replaceAll("V", "В").replaceAll("T", "Т").replaceAll("S", "С").replaceAll("E", "Э");

    }


    public void saveGroup(Group group) {
        saveString(Config.my_group, group.id);

    }

    public boolean checkAuth() {
        username = getStringSh("username", null);
        codeFast = sharedPreferences.getInt("codeFast", -1);
        return username != null && codeFast != -1;
    }

    public void saveMe(String toString, String username, Integer code_fast) {
        saveString(Config.huser_me_data, toString);
        saveString("username", username);
        saveInteger("codeFast", code_fast);
        this.username = username;
        this.codeFast = code_fast;
    }

    public void saveString(String key, String word) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, word);
        editor.commit();

    }

    public void saveInteger(String key, Integer word) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, word);
        editor.commit();
        ;
    }

    public void post(String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener, String... args) {

        JSONObject jsonObject = new JSONObject();
        try {

            for (int i = 0; i < args.length; i++) {
                jsonObject.put(args[i].split("=")[0], args[i].split("=")[1]);

            }

            if (checkAuth()) {
                jsonObject.put("username", username);
                jsonObject.put("code_fast", codeFast);
            }


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json;charset=UTF-8");
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return jsonObject.toString().getBytes(StandardCharsets.UTF_8);
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };

        requestQueue.add(stringRequest);

    }


    public void like(HistoryModel projectModel, int type, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("history_id",projectModel.id);
            jsonObject.put("type",type);

            if (checkAuth()) {
                jsonObject.put("username", username);
                jsonObject.put("code_fast", codeFast);
            }else{
                return;
            }


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, netConfig.like,listener,errorListener) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json;charset=UTF-8");
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return jsonObject.toString().getBytes(StandardCharsets.UTF_8);
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };
        requestQueue.add(stringRequest);

    }

    public void checkAuthByJSON(JSONObject jsonObject) throws JSONException {
        if(jsonObject.has("message")){
            if(jsonObject.getString("message").equals("AUTH_FALSE")){
                sharedPreferences.edit().clear().commit();
                context.startActivity(new Intent(context, CheckTypeUserActivity.class));
                context.finish();
            }
        }
    }
}
