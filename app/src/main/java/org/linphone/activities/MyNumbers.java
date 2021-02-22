package org.linphone.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.linphone.APICALL;
import org.linphone.R;
import org.linphone.adapters.MyNumbersAdapter;
import org.linphone.models.MyNumbersModel;

public class MyNumbers extends AppCompatActivity {
    ProgressDialog pdia;
    APICALL api = new APICALL();
    ArrayList<MyNumbersModel> MyNumberList = new ArrayList<>();
    MyNumbersAdapter madapter;
    RecyclerView RcView;
    TextView Empty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mynumbers);
        Empty = (TextView) findViewById(R.id.empty);
        RcView = (RecyclerView) findViewById(R.id.rcview);

        new ATask4MyNumbers().execute();
    }

    private class ATask4MyNumbers extends AsyncTask<String, Void, String> {
        String Token = "";
        private final String MY_PREFS_NAME = "jirtuu";

        ATask4MyNumbers() {}

        String Response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(MyNumbers.this);
            pdia.setMessage("Please Wait...");
            pdia.setCancelable(false);
            pdia.show();
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            Token = prefs.getString("token", "");
        }

        @Override
        protected String doInBackground(String... params) {
            Response = api.getMyNumbers(Token);
            return Response;
        }

        protected void onPostExecute(String result) {
            pdia.dismiss();
            if (result.equals("")) {
                Toast.makeText(MyNumbers.this, "empty result", Toast.LENGTH_SHORT).show();
            } else {

                try {
                    JSONObject obj = new JSONObject(result);
                    String data = obj.getString("data");
                    JSONArray jary = new JSONArray(data);
                    for (int i = 0; i < jary.length(); i++) {
                        MyNumbersModel MNM = new MyNumbersModel();
                        JSONObject jobj = jary.getJSONObject(i);
                        MNM.setId(jobj.getString("id"));
                        MNM.setFriendlyName(jobj.getString("friendlyName"));
                        MNM.setPhoneNumber(jobj.getString("phoneNumber"));
                        MNM.setSid(jobj.getString("sid"));
                        MNM.setStatus(jobj.getString("status"));
                        //  MNM.setEndpoint_id(jobj.getString("endpoint_id"));
                        MNM.setForward_enabled(jobj.getString("forward_enabled"));
                        MNM.setForward_typ(jobj.getString("forward_type"));
                        MNM.setExternal_number(jobj.getString("external_number"));
                        MyNumberList.add(MNM);
                    }

                    if (MyNumberList.size() == 0) {
                        Empty.setVisibility(View.VISIBLE);
                        RcView.setVisibility(View.GONE);

                    } else {
                        Empty.setVisibility(View.GONE);
                        RcView.setVisibility(View.VISIBLE);
                        madapter = new MyNumbersAdapter(MyNumberList, MyNumbers.this);
                        RcView.setHasFixedSize(true);
                        RcView.setLayoutManager(new LinearLayoutManager(MyNumbers.this));
                        RcView.setAdapter(madapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
