/*
 * Copyright (c) 2010-2019 Belledonne Communications SARL.
 *
 * This file is part of linphone-android
 * (see https://www.linphone.org).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.linphone.assistant;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.linphone.APICALL;
import org.linphone.LinphoneManager;
import org.linphone.R;
import org.linphone.core.AccountCreator;
import org.linphone.core.AccountCreatorListenerStub;
import org.linphone.core.Core;
import org.linphone.core.DialPlan;
import org.linphone.core.tools.Log;

public class PhoneAccountCreationAssistantActivity extends AssistantActivity {
    private TextView mCountryPicker, mError, mSipUri, mCreate;
    private EditText mPrefix, mPhoneNumber, mUsername;
    private CheckBox mUseUsernameInsteadOfPhoneNumber;

    private AccountCreatorListenerStub mListener;
    String NumberUser = "";
    EditText editTextCode;
    private final String MY_PREFS_NAME = "jirtuu";
    SharedPreferences.Editor editor;
    APICALL api;
    String username = "";
    String password = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assistant_phone_account_creation);
        api = new APICALL();
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        mCountryPicker = findViewById(R.id.select_country);
        mCountryPicker.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCountryPickerDialog();
                    }
                });

        mError = findViewById(R.id.phone_number_error);

        mSipUri = findViewById(R.id.sip_uri);

        mCreate = findViewById(R.id.assistant_create);
        mCreate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //                        AccountCreator accountCreator =
                        // getAccountCreator();
                        //                        enableButtonsAndFields(false);
                        //
                        //                        if (mUseUsernameInsteadOfPhoneNumber.isChecked())
                        // {
                        //
                        // accountCreator.setUsername(mUsername.getText().toString());
                        //                        } else {
                        //
                        // accountCreator.setUsername(accountCreator.getPhoneNumber());
                        //                        }
                        //
                        //                        AccountCreator.Status status =
                        // accountCreator.isAccountExist();
                        //                        if (status != AccountCreator.Status.RequestOk) {
                        //                            Log.e(
                        //                                    "[Phone Account Creation Assistant]
                        // isAccountExists returned "
                        //                                            + status);
                        //                            enableButtonsAndFields(true);
                        //                            showGenericErrorDialog(status);
                        //                        }
                        // todo.. request otp

                        NumberUser =
                                mPrefix.getText().toString() + mPhoneNumber.getText().toString();
                        new ATask4LoginVERIFY().execute();
                    }
                });
        mCreate.setEnabled(false);

        mPrefix = findViewById(R.id.dial_code);
        mPrefix.setText("+");
        mPrefix.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        String prefix = s.toString();
                        if (prefix.startsWith("+")) {
                            prefix = prefix.substring(1);
                        }
                        DialPlan dp = getDialPlanFromPrefix(prefix);
                        if (dp != null) {
                            mCountryPicker.setText(dp.getCountry());
                        }

                        updateCreateButtonAndDisplayError();
                    }
                });

        mPhoneNumber = findViewById(R.id.phone_number);
        mPhoneNumber.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        updateCreateButtonAndDisplayError();
                    }
                });

        ImageView phoneNumberInfos = findViewById(R.id.info_phone_number);
        phoneNumberInfos.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPhoneNumberDialog();
                    }
                });

        mUseUsernameInsteadOfPhoneNumber = findViewById(R.id.use_username);
        mUseUsernameInsteadOfPhoneNumber.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mUsername.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                        updateCreateButtonAndDisplayError();
                    }
                });

        mUsername = findViewById(R.id.username);
        mUsername.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        updateCreateButtonAndDisplayError();
                    }
                });

        mListener =
                new AccountCreatorListenerStub() {
                    public void onIsAccountExist(
                            AccountCreator creator, AccountCreator.Status status, String resp) {
                        Log.i(
                                "[Phone Account Creation Assistant] onIsAccountExist status is "
                                        + status);
                        if (status.equals(AccountCreator.Status.AccountExist)
                                || status.equals(AccountCreator.Status.AccountExistWithAlias)) {
                            showAccountAlreadyExistsDialog();
                            enableButtonsAndFields(true);
                        } else if (status.equals(AccountCreator.Status.AccountNotExist)) {
                            status = getAccountCreator().createAccount();
                            if (status != AccountCreator.Status.RequestOk) {
                                Log.e(
                                        "[Phone Account Creation Assistant] createAccount returned "
                                                + status);
                                enableButtonsAndFields(true);
                                showGenericErrorDialog(status);
                            }
                        } else {
                            enableButtonsAndFields(true);
                            showGenericErrorDialog(status);
                        }
                    }

                    @Override
                    public void onCreateAccount(
                            AccountCreator creator, AccountCreator.Status status, String resp) {
                        Log.i(
                                "[Phone Account Creation Assistant] onCreateAccount status is "
                                        + status);
                        if (status.equals(AccountCreator.Status.AccountCreated)) {
                            startActivity(
                                    new Intent(
                                            PhoneAccountCreationAssistantActivity.this,
                                            PhoneAccountValidationAssistantActivity.class));
                        } else {
                            enableButtonsAndFields(true);
                            showGenericErrorDialog(status);
                        }
                    }
                };
    }

    @Override
    protected void onResume() {
        super.onResume();

        Core core = LinphoneManager.getCore();
        if (core != null) {
            reloadLinphoneAccountCreatorConfig();
        }

        getAccountCreator().addListener(mListener);

        DialPlan dp = getDialPlanForCurrentCountry();
        displayDialPlan(dp);

        String phoneNumber = getDevicePhoneNumber();
        if (phoneNumber != null) {
            mPhoneNumber.setText(phoneNumber);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        getAccountCreator().removeListener(mListener);
    }

    @Override
    public void onCountryClicked(DialPlan dialPlan) {
        super.onCountryClicked(dialPlan);
        displayDialPlan(dialPlan);
    }

    private void enableButtonsAndFields(boolean enable) {
        mPrefix.setEnabled(enable);
        mPhoneNumber.setEnabled(enable);
        mCreate.setEnabled(enable);
    }

    private void updateCreateButtonAndDisplayError() {
        if (mPrefix.getText().toString().isEmpty() || mPhoneNumber.getText().toString().isEmpty())
            return;

        mCreate.setEnabled(true);
        mError.setText("");
        mError.setVisibility(View.INVISIBLE);

        int status = arePhoneNumberAndPrefixOk(mPrefix, mPhoneNumber);
        if (status == AccountCreator.PhoneNumberStatus.Ok.toInt()) {
            if (mUseUsernameInsteadOfPhoneNumber.isChecked()) {
                AccountCreator.UsernameStatus usernameStatus =
                        getAccountCreator().setUsername(mUsername.getText().toString());
                if (usernameStatus != AccountCreator.UsernameStatus.Ok) {
                    mCreate.setEnabled(false);
                    mError.setText(getErrorFromUsernameStatus(usernameStatus));
                    mError.setVisibility(View.VISIBLE);
                }
            }
        } else {
            mCreate.setEnabled(false);
            mError.setText(getErrorFromPhoneNumberStatus(status));
            mError.setVisibility(View.VISIBLE);
        }

        String username;
        if (mUseUsernameInsteadOfPhoneNumber.isChecked()) {
            username = mUsername.getText().toString();
        } else {
            username = getAccountCreator().getPhoneNumber();
        }

        if (username != null) {
            String sip =
                    getString(R.string.assistant_create_account_phone_number_address)
                            + " <sip:"
                            + username
                            + "@"
                            + getResources().getString(R.string.default_domain)
                            + ">";
            mSipUri.setText(sip);
        }
    }

    private void displayDialPlan(DialPlan dp) {
        if (dp != null) {
            mPrefix.setText("+" + dp.getCountryCallingCode());
            mCountryPicker.setText(dp.getCountry());
        }
    }

    ProgressDialog pdia;

    private class ATask4LoginVERIFY extends AsyncTask<String, Void, String> {

        ATask4LoginVERIFY() {
            pdia = new ProgressDialog(PhoneAccountCreationAssistantActivity.this);
            pdia.setMessage("Please Wait...");
            pdia.setCancelable(false);
        }

        String Response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Response = api.postJson(NumberUser);
            return Response;
        }

        protected void onPostExecute(String result) {
            pdia.dismiss();
            try {

                JSONObject obj = new JSONObject(result);
                if (obj.getString("status").equals("pending")) {

                    String id = obj.getString("id");
                    String sid = obj.getString("sid");
                    String to = obj.getString("to");
                    String channel = obj.getString("channel");
                    String status = obj.getString("status");
                    String sendCodeAttempts = obj.getString("sendCodeAttempts");
                    String dateCreated = obj.getString("dateCreated");
                    String verified = obj.getString("verified");
                    editor.putString("id", id);
                    editor.apply();
                    VerifyDialog();
                } else {
                    Toast.makeText(
                                    PhoneAccountCreationAssistantActivity.this,
                                    "" + obj.getString("status"),
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void VerifyDialog() {
        final Dialog dialog =
                new Dialog(PhoneAccountCreationAssistantActivity.this, R.style.Theme_Dialog);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.verifycode);
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //            wlp.gravity = Gravity.BOTTOM;
        //            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        //            window.setAttributes(wlp);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        editTextCode = (EditText) dialog.findViewById(R.id.editTextCode);
        Button Next = (Button) dialog.findViewById(R.id.buttonSignIn);

        Next.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (editTextCode.getText().toString().matches("")) {
                            Toast.makeText(
                                            PhoneAccountCreationAssistantActivity.this,
                                            "Enter Valid Code",
                                            Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }

                        new ATask4VerifyCode().execute();
                    }
                });

        dialog.show();
    }

    private class ATask4VerifyCode extends AsyncTask<String, Void, String> {

        ATask4VerifyCode() {
            pdia = new ProgressDialog(PhoneAccountCreationAssistantActivity.this);
            pdia.setMessage("Please Wait...");
            pdia.setCancelable(false);
        }

        String Response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Response = api.verifyCode(NumberUser, editTextCode.getText().toString());
            return Response;
        }

        protected void onPostExecute(String result) {
            pdia.dismiss();
            try {
                JSONObject obj = new JSONObject(result);
                String user = obj.getString("user");
                String token = obj.getString("token");
                JSONObject obj1 = new JSONObject(user);
                String UserName = obj1.getString("username");
                String endpoint = obj1.getString("endpoint");
                JSONArray obj2 = new JSONArray(endpoint);
                JSONObject jobj2 = obj2.getJSONObject(0);
                String Password = jobj2.getString("password");

                username = UserName;
                password = Password;
                editor.putString("number", NumberUser);
                editor.putString("username", username);
                editor.putString("password", password);
                editor.putString("token", token);
                editor.apply();
                startActivity(
                        new Intent(
                                PhoneAccountCreationAssistantActivity.this,
                                GenericConnectionAssistantActivity.class));

                /** new ATask4AuthLogin().execute(); */
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    /*
       private class ATask4AuthLogin extends AsyncTask<String, Void, String> {

            ATask4AuthLogin() {
               pdia = new ProgressDialog(PhoneAccountCreationAssistantActivity.this);
               pdia.setMessage("Please Wait...");
               pdia.setCancelable(false);
           }

           String Response = "";

           @Override
           protected void onPreExecute() {
               super.onPreExecute();
               pdia.show();
           }

           @Override
           protected String doInBackground(String... params) {
               Response = api.AuthLogin(username, password);
               return Response;
           }

           protected void onPostExecute(String result) {
               pdia.dismiss();
               try {
                   JSONObject obj = new JSONObject(result);
                   String token = obj.getString("token");
                   editor.putString("token", token);
                   editor.apply();

                   startActivity(
                           new Intent(
                                   PhoneAccountCreationAssistantActivity.this,
                                   GenericConnectionAssistantActivity.class));

               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       }

    */
}
