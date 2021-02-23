package org.linphone.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;
import org.json.JSONException;
import org.json.JSONObject;
import org.linphone.APICALL;
import org.linphone.Constants;
import org.linphone.R;
import org.linphone.menu.SideMenuFragment;

public class TopUpBalance extends AppCompatActivity {
    TextView AmountShow;
    Button plan3sub;
    CardInputWidget mCardInputWidget;
    EditText amountEnter, emailEnter;
    Card cardToSave;
    Stripe stripe;
    APICALL api;
    JSONObject StripeObject = null;
    ProgressDialog pdia;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topupbalance);
        api = new APICALL();
        AmountShow = (TextView) findViewById(R.id.amountvalue);
        plan3sub = (Button) findViewById(R.id.plan3sub);
        mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);
        mCardInputWidget.clearFocus();
        amountEnter = (EditText) findViewById(R.id.amount);
        emailEnter = (EditText) findViewById(R.id.email);

        amountEnter.clearFocus();
        amountEnter.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        // TODO Auto-generated method stub
                        AmountShow.setText(s);
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        // TODO Auto-generated method stub
                    }
                });
        plan3sub.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (amountEnter.getText().toString().matches("")) {
                            Toast.makeText(
                                            TopUpBalance.this,
                                            "Please Enter Valid Amount",
                                            Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }

                        if (emailEnter.getText().toString().matches("")) {
                            Toast.makeText(
                                            TopUpBalance.this,
                                            "Please enter your email address",
                                            Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }

                        cardToSave = mCardInputWidget.getCard();
                        if (cardToSave == null) {
                            Toast.makeText(TopUpBalance.this, "Invalid Card", Toast.LENGTH_SHORT)
                                    .show();
                        } else {

                            pdia = new ProgressDialog(TopUpBalance.this);
                            pdia.setMessage("Please Wait...");
                            pdia.setCancelable(false);
                            pdia.show();
                            stripe =
                                    new Stripe(
                                            TopUpBalance.this,
                                            Constants.STRIPE_PUBLISHABLE_KEY_LIVE);
                            stripe.createToken(
                                    cardToSave,
                                    new TokenCallback() {
                                        public void onSuccess(@NonNull Token token) {

                                            try {
                                                StripeObject = new JSONObject();
                                                StripeObject.put("stripeToken", token.getId());
                                                StripeObject.put(
                                                        "email", emailEnter.getText().toString());
                                                StripeObject.put(
                                                        "amount", AmountShow.getText().toString());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            new ATask4PAyment().execute();
                                        }

                                        public void onError(@NonNull Exception error) {
                                            if (pdia.isShowing()) {
                                                pdia.dismiss();
                                            }
                                            Toast.makeText(
                                                            TopUpBalance.this,
                                                            "" + error,
                                                            Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    });
                        }
                    }
                });
    }

    private class ATask4PAyment extends AsyncTask<String, Void, String> {
        String Token = "";
        private final String MY_PREFS_NAME = "jirtuu";

        ATask4PAyment() {}

        String Response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            Token = prefs.getString("token", "");
        }

        @Override
        protected String doInBackground(String... params) {
            Response = api.Send_Stripe_Payment(StripeObject, Token);
            return Response;
        }

        protected void onPostExecute(String result) {
            if (pdia.isShowing()) {
                pdia.dismiss();
            }
            try {
                JSONObject obj = new JSONObject(result);
                if (obj.getString("status").equals("payment success")) {
                    Toast.makeText(TopUpBalance.this, "Payment is Successfull", Toast.LENGTH_SHORT)
                            .show();
                    new ATask4Balance().execute();
                } else {
                    Toast.makeText(
                                    TopUpBalance.this,
                                    "Something went wrong please try again.",
                                    Toast.LENGTH_SHORT)
                            .show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class ATask4Balance extends AsyncTask<String, Void, String> {
        String Token = "";
        private final String MY_PREFS_NAME = "jirtuu";

        ATask4Balance() {}

        String Response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            Token = prefs.getString("token", "");
        }

        @Override
        protected String doInBackground(String... params) {
            Response = api.getHttpBalanceResponse(Token);
            return Response;
        }

        protected void onPostExecute(String result) {
            try {
                JSONObject obj = new JSONObject(result);
                String balance = obj.getString("balance");
                // Balance.setText("Balance: $" + balance);
                MainActivity.BALANCE = "Balance: $" + balance;
                SideMenuFragment.BalanceTV.setText(MainActivity.BALANCE);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
