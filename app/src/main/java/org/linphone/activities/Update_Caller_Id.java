package org.linphone.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.linphone.APICALL;
import org.linphone.R;
import org.linphone.adapters.CallerIdAdapter;

public class Update_Caller_Id extends AppCompatActivity {
    APICALL api;
    ProgressDialog pdia;
    ArrayList<String> Numberz = new ArrayList<>();
    RecyclerView NumberList;
    CallerIdAdapter mAdapter;
    CallerIdAdapter.OnItemnumberClickListener listener;
    JSONObject objnumb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callerid);
        api = new APICALL();
        new ATask4Callerids().execute();
        NumberList = (RecyclerView) findViewById(R.id.numberlist);
    }

    private class ATask4Callerids extends AsyncTask<String, Void, String> {
        String Token = "";
        private final String MY_PREFS_NAME = "jirtuu";

        ATask4Callerids() {}

        String Response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(Update_Caller_Id.this);
            pdia.setMessage("Please Wait...");
            pdia.setCancelable(false);
            pdia.show();
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            Token = prefs.getString("token", "");
        }

        @Override
        protected String doInBackground(String... params) {
            Response = api.getCallerIds(Token);
            return Response;
        }

        protected void onPostExecute(String result) {
            pdia.dismiss();
            if (result.equals("")) {
                Toast.makeText(Update_Caller_Id.this, "empty result", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    String username = obj.getString("username");
                    String Numbers = obj.getString("numbers");
                    Numberz.add(username);
                    JSONArray jarry = new JSONArray(Numbers);
                    for (int i = 0; i < jarry.length(); i++) {
                        String str = jarry.getString(i);
                        Numberz.add(str);
                    }
                    listener =
                            new CallerIdAdapter.OnItemnumberClickListener() {
                                @Override
                                public void onItemClick(String item) {
                                    showconfirmupdatenumber(item);
                                }
                            };
                    mAdapter = new CallerIdAdapter(Numberz, Update_Caller_Id.this, listener);
                    NumberList.setHasFixedSize(true);
                    NumberList.setLayoutManager(new LinearLayoutManager(Update_Caller_Id.this));
                    NumberList.setAdapter(mAdapter);
                } catch (JSONException e) {
                }
            }
        }
    }

    public void showconfirmupdatenumber(String item) {
        final AlertDialog alertDialog =
                new AlertDialog.Builder(Update_Caller_Id.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure you want to Update " + item + " as your caller id?")
                        .setMessage("")
                        .setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        try {
                                            objnumb = new JSONObject();
                                            objnumb.put("callerID", item);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        new ATask4SetCallerids().execute();
                                    }
                                })
                        // set negative button
                        .setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {}
                                })
                        .show();
    }

    private class ATask4SetCallerids extends AsyncTask<String, Void, String> {
        String Token = "";
        private final String MY_PREFS_NAME = "jirtuu";

        ATask4SetCallerids() {}

        String Response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(Update_Caller_Id.this);
            pdia.setMessage("Please Wait...");
            pdia.setCancelable(false);
            pdia.show();
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            Token = prefs.getString("token", "");
        }

        @Override
        protected String doInBackground(String... params) {
            Response = api.SetCallerID(objnumb, Token);
            return Response;
        }

        protected void onPostExecute(String result) {
            pdia.dismiss();
            if (result.equals("")) {
                Toast.makeText(Update_Caller_Id.this, "empty result", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Update_Caller_Id.this, "Caller-Id Updated", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
