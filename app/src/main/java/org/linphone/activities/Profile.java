package org.linphone.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import org.linphone.APICALL;
import org.linphone.R;

public class Profile extends AppCompatActivity {
    APICALL api;
    ImageView editname, editemail;
    TextView tv_name, tv_email, tv_balance, tv_username;
    JSONObject pobj = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofile);
        api = new APICALL();
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_balance = (TextView) findViewById(R.id.tv_balance);
        tv_username = (TextView) findViewById(R.id.tv_username);
        editname = (ImageView) findViewById(R.id.editname);
        editemail = (ImageView) findViewById(R.id.editemail);
        editname.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowEditNameEmail();
                    }
                });
        editemail.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowEditNameEmail();
                    }
                });
        new ATask4Profile().execute();
    }

    public class ATask4Profile extends AsyncTask<String, Void, String> {
        String Token = "";
        ProgressDialog pdia;
        private final String MY_PREFS_NAME = "jirtuu";

        ATask4Profile() {}

        String Response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(Profile.this);
            pdia.setMessage("Please Wait...");
            pdia.setCancelable(false);
            pdia.show();
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            Token = prefs.getString("token", "");
        }

        @Override
        protected String doInBackground(String... params) {
            Response = api.getProfile(Token);
            return Response;
        }

        protected void onPostExecute(String result) {
            pdia.dismiss();
            try {
                JSONObject obj = new JSONObject(result);
                String Name = obj.getString("name");
                String email = obj.getString("email");
                String balance = obj.getString("balance");
                String username = obj.getString("username");
                if (Name.equals("null")) {
                    tv_name.setText("");
                } else {
                    tv_name.setText(Name);
                }
                if (email.equals("null")) {
                    tv_email.setText("");
                } else {
                    tv_email.setText(email);
                }
                tv_balance.setText("$" + balance);
                tv_username.setText(username);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void ShowEditNameEmail() {
        final Dialog dialog = new Dialog(Profile.this, R.style.Theme_Dialog);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editname);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //            wlp.gravity = Gravity.BOTTOM;
        //            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        //            window.setAttributes(wlp);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pobj = new JSONObject();
        EditText editname = (EditText) dialog.findViewById(R.id.editname);
        editname.setText(tv_name.getText().toString());
        EditText editemail = (EditText) dialog.findViewById(R.id.editemail);
        editemail.setText(tv_email.getText().toString());
        Button Submit = (Button) dialog.findViewById(R.id.submit);
        Submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editname.getText().toString().matches("")) {
                            Toast.makeText(
                                            Profile.this,
                                            "Please enter valid name",
                                            Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                        if (editemail.getText().toString().matches("")) {
                            Toast.makeText(
                                            Profile.this,
                                            "Please enter valid Email Address",
                                            Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }

                        try {
                            pobj.put("name", editname.getText().toString());
                            pobj.put("email", editemail.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        new ATask4ProfileUpdate().execute();
                        dialog.dismiss();
                    }
                });

        dialog.show();
    }

    public class ATask4ProfileUpdate extends AsyncTask<String, Void, String> {
        String Token = "";
        ProgressDialog pdia;
        private final String MY_PREFS_NAME = "jirtuu";

        ATask4ProfileUpdate() {}

        String Response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(Profile.this);
            pdia.setMessage("Please Wait...");
            pdia.setCancelable(false);
            pdia.show();
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            Token = prefs.getString("token", "");
        }

        @Override
        protected String doInBackground(String... params) {
            Response = api.UpdateProfile(pobj, Token);
            return Response;
        }

        protected void onPostExecute(String result) {
            pdia.dismiss();
            try {
                JSONObject obj = new JSONObject(result);
                if (Build.VERSION.SDK_INT >= 11) {
                    recreate();
                } else {
                    finish();
                    startActivity(Profile.this.getIntent());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
