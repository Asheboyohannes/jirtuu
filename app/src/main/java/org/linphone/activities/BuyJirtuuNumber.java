package org.linphone.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.stripe.android.model.Token;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.linphone.APICALL;
import org.linphone.R;
import org.linphone.adapters.AvailableNumberAdapter;
import org.linphone.adapters.CountryAdapter;
import org.linphone.menu.SideMenuFragment;
import org.linphone.models.AvailabeNumberModel;
import org.linphone.models.CountryListModel;
import org.linphone.models.PricesModel;

public class BuyJirtuuNumber extends AppCompatActivity {
    APICALL api;
    ArrayList<CountryListModel> Countries = new ArrayList<>();
    ArrayList<AvailabeNumberModel> AvailableNumberListo = new ArrayList<>();
    private CountryPicker picker;
    ArrayList<String> CountriesForSpinner = new ArrayList<>();
    String[] mStringArrayCountry;
    ArrayList<String> PlansForSpinner = new ArrayList<>();
    String[] mStringArrayplan;
    ArrayList<String> NumberForSpinner = new ArrayList<>();

    String[] mStringArrayNumbers;
    CardView carView;

    ImageView CountryPick;
    LinearLayout exter;
    JSONObject friendlyobject = null;

    String idia = "";
    String Sidia = "";

    boolean ForwardEnabled = false;

    boolean isforwardselected = false;

    TextView select_country;
    AvailableNumberAdapter avmadapter;
    String CountryCode = "";
    CountryAdapter.OnItemClickListener listener;
    AvailableNumberAdapter.OnItemnumberClickListener listenero;
    String AMOUNT = "", PriceUNIT = "";

    JSONObject jObjectType4AvailableNumber = null;
    JSONObject jObjectType4NumberCreate = null;
    String NumberSelected = "", PlanSelected = "";
    String ForwardNumber = "";
    TextView lubal, lubal2, lubal3, mynumber, frwardnote;

    ProgressDialog pdia;
    EditText externalnum;

    TextView YourCellNo, OTherCellNo;

    IndicatorSeekBar Seebar;

    Spinner countriesSpinner, planspin, numberspin, frwardSpin;
    int countryposition = -1;
    private boolean countryisSpinnerInitial = true;
    private boolean planisSpinnerInitial = true;
    private boolean numberisSpinnerInitial = true;
    private boolean frwardisSpinnerInitial = true;
    LinearLayout priceview, numberview, frwardview, othernumview;
    String DESTINATION, RATE;
    TextView dest, rates, monthfee, totalEMP;
    TextView IncomingLabel, ForwardingLAbel, ForwardRate;
    ArrayList<PricesModel> PricesList = new ArrayList<>();

    Button GetThisNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyjirtuunumber);
        Seebar = (IndicatorSeekBar) findViewById(R.id.seekbar);
        Seebar.setIndicatorTextFormat("${PROGRESS} Minutes");
        Seebar.setVisibility(View.GONE);
        lubal = (TextView) findViewById(R.id.lubal);
        lubal.setVisibility(View.GONE);
        lubal2 = (TextView) findViewById(R.id.lubal2);
        lubal2.setVisibility(View.GONE);
        lubal3 = (TextView) findViewById(R.id.lubal3);
        lubal3.setVisibility(View.GONE);

        GetThisNumber = (Button) findViewById(R.id.getthisnumber);

        YourCellNo = (TextView) findViewById(R.id.yourcellno);
        OTherCellNo = (TextView) findViewById(R.id.othercellno);
        mynumber = (TextView) findViewById(R.id.mynumber);

        SharedPreferences prefs = getSharedPreferences("jirtuu", MODE_PRIVATE);
        mynumber.setText(prefs.getString("number", ""));
        mynumber.setVisibility(View.GONE);

        othernumview = (LinearLayout) findViewById(R.id.othernum);
        othernumview.setVisibility(View.GONE);

        externalnum = (EditText) findViewById(R.id.external_number);

        frwardnote = (TextView) findViewById(R.id.frwardnote);
        frwardnote.setVisibility(View.GONE);

        picker = CountryPicker.newInstance("Select Country"); // dialog title
        CountryPick = (ImageView) findViewById(R.id.countrypick);
        exter = (LinearLayout) findViewById(R.id.exter);
        CountryPick.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        picker.setListener(
                                new CountryPickerListener() {
                                    @Override
                                    public void onSelectCountry(
                                            String name,
                                            String code,
                                            String dialCode,
                                            int flagDrawableResID) {
                                        // Implement your code here
                                        externalnum.setText(dialCode);
                                        CountryPick.setImageResource(flagDrawableResID);
                                        picker.dismiss();
                                        externalnum.setSelection(
                                                externalnum.getText().toString().length());
                                    }
                                });
                        picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
                    }
                });

        dest = (TextView) findViewById(R.id.destination);
        rates = (TextView) findViewById(R.id.rate);
        monthfee = (TextView) findViewById(R.id.monthly_fee);
        totalEMP = (TextView) findViewById(R.id.totalemp);
        IncomingLabel = (TextView) findViewById(R.id.incominglabel);
        ForwardingLAbel = (TextView) findViewById(R.id.forwardinglabel);
        ForwardRate = (TextView) findViewById(R.id.forwarding);
        countriesSpinner = (Spinner) findViewById(R.id.countriesspin);
        planspin = (Spinner) findViewById(R.id.planspin);
        numberspin = (Spinner) findViewById(R.id.numberspin);
        carView = (CardView) findViewById(R.id.carView);
        carView.setVisibility(View.GONE);
        api = new APICALL();
        new ATask4CountryListe().execute();
        priceview = (LinearLayout) findViewById(R.id.priceview);
        priceview.setVisibility(View.GONE);
        numberview = (LinearLayout) findViewById(R.id.numberview);
        numberview.setVisibility(View.GONE);
        frwardview = (LinearLayout) findViewById(R.id.forwardview);
        frwardview.setVisibility(View.GONE);
        select_country = (TextView) findViewById(R.id.select_country);

        YourCellNo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        YourCellNo.setBackgroundColor(
                                getResources().getColor(R.color.primary_color));
                        YourCellNo.setTextColor(getResources().getColor(R.color.black_color));

                        OTherCellNo.setBackgroundColor(
                                getResources().getColor(R.color.white_color));
                        OTherCellNo.setTextColor(getResources().getColor(R.color.black_color));
                        mynumber.setVisibility(View.VISIBLE);
                        othernumview.setVisibility(View.GONE);

                        ForwardEnabled = false;
                        isforwardselected = true;

                        ForwardNumber = mynumber.getText().toString();
                        new ATask4FrwardCountryListe().execute();
                    }
                });

        externalnum.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        // TODO Auto-generated method stub
                        ForwardNumber = s.toString();
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        // TODO Auto-generated method stub
                        // ForwardNumber = externalnum.getText().toString();
                        //  new ATask4FrwardCountryListe().execute();
                    }
                });

        externalnum.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH
                                || actionId == EditorInfo.IME_ACTION_DONE
                                || event != null
                                        && event.getAction() == KeyEvent.ACTION_DOWN
                                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (event == null || !event.isShiftPressed()) {
                                // the user is done typing.
                                hideKeyboard();

                                new ATask4FrwardCountryListe().execute();
                                return true; // consume.
                            }
                        }
                        return false; // pass on to other listeners.
                    }
                });

        OTherCellNo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        YourCellNo.setBackgroundColor(getResources().getColor(R.color.white_color));
                        YourCellNo.setTextColor(getResources().getColor(R.color.black_color));

                        OTherCellNo.setBackgroundColor(
                                getResources().getColor(R.color.primary_color));
                        OTherCellNo.setTextColor(getResources().getColor(R.color.black_color));
                        mynumber.setVisibility(View.GONE);
                        othernumview.setVisibility(View.VISIBLE);

                        ForwardEnabled = true;
                        isforwardselected = true;
                    }
                });

        Seebar.setOnSeekChangeListener(
                new OnSeekChangeListener() {
                    @Override
                    public void onSeeking(SeekParams seekParams) {
                        IncomingLabel.setText(
                                "Incoming calls (" + seekParams.progress + " minutes)");
                        ForwardingLAbel.setText(
                                "Call Forwarding (" + seekParams.progress + " minutes)");

                        if (seekParams.progress == 0) {
                            ForwardRate.setText("$0.00");
                            totalEMP.setText(monthfee.getText().toString());
                        } else {
                            Float forwardrate = seekParams.progress * Float.parseFloat(RATE);
                            ForwardRate.setText("$" + forwardrate);

                            Float am =
                                    Float.parseFloat(
                                            monthfee.getText().toString().replace("$", ""));

                            Float tot = forwardrate + am;

                            totalEMP.setText("$" + tot);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(IndicatorSeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(IndicatorSeekBar seekBar) {}
                });

        countriesSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {

                        if (countryisSpinnerInitial) {
                            countryisSpinnerInitial = false;
                        } else {
                            // do your work...

                            if (mStringArrayCountry[position].equals("Select Country")) {

                            } else {
                                countryposition = position;
                                CountryCode =
                                        Countries.get(position - 1)
                                                .getCountryCode(); // item.getCountryCode();
                                PricesList.clear();
                                new ATask4Pricing().execute();
                                planisSpinnerInitial = true;
                                numberisSpinnerInitial = true;

                                NumberForSpinner.clear();

                                NumberForSpinner.add(0, "Select Number");

                                String[] mStringArray = new String[NumberForSpinner.size()];
                                mStringArray = NumberForSpinner.toArray(mStringArray);

                                ArrayAdapter<CharSequence> langAdapter =
                                        new ArrayAdapter<CharSequence>(
                                                BuyJirtuuNumber.this,
                                                R.layout.spinner_text,
                                                mStringArray);
                                langAdapter.setDropDownViewResource(
                                        R.layout.simple_spinner_dropdown);
                                numberspin.setAdapter(langAdapter);
                                numberview.setVisibility(View.GONE);
                                Seebar.setProgress(0);
                                Seebar.getIndicator().getContentView().setVisibility(View.GONE);
                                Seebar.setVisibility(View.GONE);

                                lubal.setVisibility(View.GONE);
                                lubal2.setVisibility(View.GONE);
                                lubal3.setVisibility(View.GONE);
                                carView.setVisibility(View.GONE);
                                frwardview.setVisibility(View.GONE);
                                mynumber.setVisibility(View.GONE);
                                othernumview.setVisibility(View.GONE);
                                frwardnote.setVisibility(View.GONE);
                                YourCellNo.setBackgroundColor(
                                        getResources().getColor(R.color.white_color));
                                YourCellNo.setTextColor(
                                        getResources().getColor(R.color.black_color));

                                OTherCellNo.setBackgroundColor(
                                        getResources().getColor(R.color.white_color));
                                OTherCellNo.setTextColor(
                                        getResources().getColor(R.color.black_color));
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

        planspin.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {

                        if (planisSpinnerInitial) {
                            planisSpinnerInitial = false;
                        } else {
                            // do your work...
                            if (mStringArrayplan[position].equals("Select Plan")) {

                            } else {

                                monthfee.setText("$" + PricesList.get(position - 1).getValue());
                                totalEMP.setText("$" + PricesList.get(position - 1).getValue());

                                PlanSelected = PricesList.get(position - 1).getKey();

                                try {
                                    jObjectType4AvailableNumber = new JSONObject();
                                    // put elements into the object as a key-value pair
                                    jObjectType4AvailableNumber.put("countryCode", CountryCode);
                                    jObjectType4AvailableNumber.put(
                                            "type", PricesList.get(position - 1).getKey());
                                    jObjectType4AvailableNumber.put("inRegion", "");
                                    jObjectType4AvailableNumber.put("areaCode", "");
                                    jObjectType4AvailableNumber.put("contains", "");
                                    jObjectType4AvailableNumber.put(
                                            "smsEnabled", org.json.JSONObject.NULL);
                                    jObjectType4AvailableNumber.put(
                                            "mmsEnabled", org.json.JSONObject.NULL);
                                    jObjectType4AvailableNumber.put("voiceEnabled", true);
                                    jObjectType4AvailableNumber.put(
                                            "faxEnabled", org.json.JSONObject.NULL);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                numberisSpinnerInitial = true;
                                new ATask4ChkAvailableNumbers().execute();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

        numberspin.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        if (numberisSpinnerInitial) {
                            numberisSpinnerInitial = false;
                        } else {
                            // do your work...
                            if (mStringArrayNumbers[position].equals("Select Number")) {

                            } else {
                                NumberSelected = NumberForSpinner.get(position);
                                frwardview.setVisibility(View.VISIBLE);
                                frwardnote.setVisibility(View.VISIBLE);

                                //                                new
                                // ATask4FrwardCountryListe().execute();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

        GetThisNumber.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (isforwardselected) {

                            if (ForwardEnabled) {
                                if (externalnum.getText().toString().matches("")) {
                                    Toast.makeText(
                                                    BuyJirtuuNumber.this,
                                                    "Please write valid external number",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                    return;
                                }
                            }

                            final AlertDialog alertDialog =
                                    new AlertDialog.Builder(BuyJirtuuNumber.this)
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setTitle(
                                                    "Are you sure you want to buy "
                                                            + NumberSelected
                                                            + " number?")
                                            .setMessage("")
                                            .setPositiveButton(
                                                    "Yes",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(
                                                                DialogInterface dialogInterface,
                                                                int i) {
                                                            try {
                                                                jObjectType4NumberCreate =
                                                                        new JSONObject();
                                                                jObjectType4NumberCreate.put(
                                                                        "friendlyName",
                                                                        NumberSelected);
                                                                jObjectType4NumberCreate.put(
                                                                        "phoneNumber",
                                                                        NumberSelected);
                                                                jObjectType4NumberCreate.put(
                                                                        "countryCode", CountryCode);
                                                                jObjectType4NumberCreate.put(
                                                                        "type",
                                                                        PlanSelected.toLowerCase());

                                                                //
                                                                //
                                                                // jObjectType4NumberCreate.put(
                                                                //
                                                                //
                                                                //    "AddressSid", "");
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                            new ATask4NumberCreate().execute();
                                                        }
                                                    })
                                            // set negative button
                                            .setNegativeButton(
                                                    "No",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(
                                                                DialogInterface dialogInterface,
                                                                int i) {}
                                                    })
                                            .show();
                        } else {

                            Toast.makeText(
                                            BuyJirtuuNumber.this,
                                            "Please select number forward type",
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }

    private class ATask4CountryListe extends AsyncTask<String, Void, String> {
        String Token = "";
        private final String MY_PREFS_NAME = "jirtuu";

        ATask4CountryListe() {}

        String Response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(BuyJirtuuNumber.this);
            pdia.setMessage("Please Wait...");
            pdia.setCancelable(false);
            pdia.show();
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            Token = prefs.getString("token", "");
        }

        @Override
        protected String doInBackground(String... params) {
            Response = api.getCountriesResponse(Token);
            return Response;
        }

        protected void onPostExecute(String result) {
            pdia.dismiss();
            try {
                JSONArray jary = new JSONArray(result);
                for (int i = 0; i < jary.length(); i++) {
                    JSONObject obj = jary.getJSONObject(i);
                    CountryListModel cm = new CountryListModel();
                    cm.setCountry(obj.getString("country"));
                    cm.setCountryCode(obj.getString("countryCode"));
                    Countries.add(cm);
                    CountriesForSpinner.add(obj.getString("country"));
                }

                Collections.sort(
                        CountriesForSpinner,
                        new Comparator<String>() {
                            @Override
                            public int compare(String s1, String s2) {
                                return s1.compareToIgnoreCase(s2);
                            }
                        });

                Collections.sort(
                        Countries,
                        new Comparator<CountryListModel>() {
                            @Override
                            public int compare(CountryListModel s1, CountryListModel s2) {
                                return s1.getCountry().compareToIgnoreCase(s2.getCountry());
                            }
                        });

                CountriesForSpinner.add(0, "Select Country");

                mStringArrayCountry = new String[CountriesForSpinner.size()];
                mStringArrayCountry = CountriesForSpinner.toArray(mStringArrayCountry);

                //  String[] years = {"1996", "1997", "1998", "1998"};
                ArrayAdapter<CharSequence> langAdapter =
                        new ArrayAdapter<CharSequence>(
                                BuyJirtuuNumber.this, R.layout.spinner_text, mStringArrayCountry);
                langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                countriesSpinner.setAdapter(langAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //    public void ShowCountryPickerDialog() {
    //        final Dialog dialog = new Dialog(BuyJirtuuNumber.this, R.style.Theme_Dialog);
    //        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    //        dialog.setContentView(R.layout.countrypickerdialog);
    //        Window window = dialog.getWindow();
    //        WindowManager.LayoutParams wlp = window.getAttributes();
    //        //            wlp.gravity = Gravity.BOTTOM;
    //        //            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
    //        //            window.setAttributes(wlp);
    //        dialog.getWindow()
    //                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    //
    //        EditText search = (EditText) dialog.findViewById(R.id.search);
    //        Collections.sort(
    //                Countries,
    //                new Comparator<CountryListModel>() {
    //                    @Override
    //                    public int compare(CountryListModel s1, CountryListModel s2) {
    //                        return s1.getCountry().compareToIgnoreCase(s2.getCountry());
    //                    }
    //                });
    //
    //        listener =
    //                new CountryAdapter.OnItemClickListener() {
    //                    @Override
    //                    public void onItemClick(CountryListModel item) {
    //
    //                        select_country.setText(item.getCountry());
    //                        CountryCode = item.getCountryCode();
    //                        new ATask4Pricing().execute();
    //                        dialog.dismiss();
    //                    }
    //                };
    //        mAdapter = new CountryAdapter(Countries, BuyJirtuuNumber.this, listener);
    //        countries = (RecyclerView) dialog.findViewById(R.id.countries);
    //        countries.setHasFixedSize(true);
    //        countries.setLayoutManager(new LinearLayoutManager(BuyJirtuuNumber.this));
    //        countries.setAdapter(mAdapter);
    //
    //        search.addTextChangedListener(
    //                new TextWatcher() {
    //                    @Override
    //                    public void onTextChanged(CharSequence s, int start, int before, int
    // count) {
    //                        final String query = s.toString().toLowerCase().trim();
    //                        final ArrayList<CountryListModel> filteredList = new ArrayList<>();
    //
    //                        for (int i = 0; i < Countries.size(); i++) {
    //
    //                            final String text =
    //                                    Countries.get(i).getCountry().toLowerCase()
    //                                            + ""
    //                                            + Countries.get(i).getCountryCode().toLowerCase();
    //                            if (text.contains(query)) {
    //                                filteredList.add(Countries.get(i));
    //                            }
    //                        }
    //
    //                        mAdapter = new CountryAdapter(filteredList, BuyJirtuuNumber.this,
    // listener);
    //                        countries.setHasFixedSize(true);
    //                        countries.setAdapter(mAdapter);
    //                        mAdapter.notifyDataSetChanged();
    //                    }
    //
    //                    @Override
    //                    public void beforeTextChanged(
    //                            CharSequence s, int start, int count, int after) {}
    //
    //                    @Override
    //                    public void afterTextChanged(Editable s) {}
    //                });
    //
    //        dialog.show();
    //    }

    private class ATask4Pricing extends AsyncTask<String, Void, String> {
        String Token = "";
        private final String MY_PREFS_NAME = "jirtuu";

        ATask4Pricing() {}

        String Response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(BuyJirtuuNumber.this);
            pdia.setMessage("Please Wait...");
            pdia.setCancelable(false);
            pdia.show();
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            Token = prefs.getString("token", "");
        }

        @Override
        protected String doInBackground(String... params) {
            Response = api.getCountryPricing(CountryCode, Token);
            return Response;
        }

        protected void onPostExecute(String result) {
            pdia.dismiss();
            PlansForSpinner.clear();
            if (!result.equals("")) {
                try {
                    JSONObject jobj = new JSONObject(result);
                    String Country = jobj.getString("country");
                    String isoCountry = jobj.getString("isoCountry");
                    String priceUnit = jobj.getString("priceUnit");
                    String prices = jobj.getString("prices");
                    JSONObject jobj1 = new JSONObject(prices);
                    if (jobj1.has("local")) {
                        String local = jobj1.getString("local");
                        AMOUNT = local;
                        PriceUNIT = priceUnit;
                        PricesModel pm = new PricesModel();
                        pm.setKey("Local");
                        pm.setValue(local);
                        pm.setTvMessage("Local: " + local + " " + priceUnit + "/month");
                        pm.setPriceUnit(priceUnit);
                        PricesList.add(pm);
                    } else {
                        AMOUNT = "";
                    }
                    if (jobj1.has("toll free")) {
                        String toll_free = jobj1.getString("toll free");
                        AMOUNT = toll_free;
                        PriceUNIT = priceUnit;
                        PricesModel pm = new PricesModel();
                        pm.setKey("toll free");
                        pm.setValue(toll_free);
                        pm.setTvMessage("Toll Free: " + toll_free + " " + priceUnit + "/month");
                        pm.setPriceUnit(priceUnit);
                        PricesList.add(pm);
                    } else {
                        AMOUNT = "";
                    }
                    if (jobj1.has("mobile")) {
                        String Mobile = jobj1.getString("mobile");
                        AMOUNT = Mobile;
                        PriceUNIT = priceUnit;
                        PricesModel pm = new PricesModel();
                        pm.setKey("mobile");
                        pm.setValue(Mobile);
                        pm.setTvMessage("Mobile: " + Mobile + " " + priceUnit + "/month");
                        pm.setPriceUnit(priceUnit);
                        PricesList.add(pm);
                    } else {
                        AMOUNT = "";
                    }
                    if (jobj1.has("national")) {
                        String National = jobj1.getString("national");
                        AMOUNT = National;
                        PriceUNIT = priceUnit;
                        PricesModel pm = new PricesModel();
                        pm.setKey("national");
                        pm.setValue(National);
                        pm.setTvMessage("National: " + National + " " + priceUnit + "/month");
                        pm.setPriceUnit(priceUnit);
                        PricesList.add(pm);
                    } else {
                        AMOUNT = "";
                    }

                    for (int i = 0; i < PricesList.size(); i++) {
                        PlansForSpinner.add(PricesList.get(i).getTvMessage());
                    }
                    PlansForSpinner.add(0, "Select Plan");
                    mStringArrayplan = new String[PlansForSpinner.size()];
                    mStringArrayplan = PlansForSpinner.toArray(mStringArrayplan);

                    ArrayAdapter<CharSequence> langAdapter =
                            new ArrayAdapter<CharSequence>(
                                    BuyJirtuuNumber.this, R.layout.spinner_text, mStringArrayplan);
                    langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                    planspin.setAdapter(langAdapter);
                    priceview.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ATask4ChkAvailableNumbers extends AsyncTask<String, Void, String> {
        String Token = "";
        private final String MY_PREFS_NAME = "jirtuu";

        ATask4ChkAvailableNumbers() {}

        String Response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(BuyJirtuuNumber.this);
            pdia.setMessage("Please Wait...");
            pdia.setCancelable(false);
            pdia.show();
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            Token = prefs.getString("token", "");
        }

        @Override
        protected String doInBackground(String... params) {
            Response = api.getAvailableNumber(jObjectType4AvailableNumber, Token);
            return Response;
        }

        protected void onPostExecute(String result) {
            pdia.dismiss();
            if (result.equals("")) {
                Toast.makeText(BuyJirtuuNumber.this, "empty result", Toast.LENGTH_SHORT).show();
            } else if (result.equals("[]")) {
                Toast.makeText(
                                BuyJirtuuNumber.this,
                                "No Number Available! Try Again",
                                Toast.LENGTH_SHORT)
                        .show();
                NumberForSpinner.clear();
                NumberForSpinner.add(0, "Select Number");
                String[] mStringArray = new String[NumberForSpinner.size()];
                mStringArray = NumberForSpinner.toArray(mStringArray);
                ArrayAdapter<CharSequence> langAdapter =
                        new ArrayAdapter<CharSequence>(
                                BuyJirtuuNumber.this, R.layout.spinner_text, mStringArray);
                langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                numberspin.setAdapter(langAdapter);
                numberview.setVisibility(View.GONE);

            } else if (result.startsWith("<!DOCTYPE html>")) {
                Toast.makeText(BuyJirtuuNumber.this, "No Number Available", Toast.LENGTH_SHORT)
                        .show();
                NumberForSpinner.clear();
                NumberForSpinner.add(0, "Select Number");
                String[] mStringArray = new String[NumberForSpinner.size()];
                mStringArray = NumberForSpinner.toArray(mStringArray);
                ArrayAdapter<CharSequence> langAdapter =
                        new ArrayAdapter<CharSequence>(
                                BuyJirtuuNumber.this, R.layout.spinner_text, mStringArray);
                langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                numberspin.setAdapter(langAdapter);
                numberview.setVisibility(View.GONE);
                // Something Went Wrong Try Again Later
                return;
            } else if (result.startsWith("<script> Sfdump = window.Sfdump")) {
                Toast.makeText(BuyJirtuuNumber.this, "No Number Available", Toast.LENGTH_SHORT)
                        .show();
                NumberForSpinner.clear();
                NumberForSpinner.add(0, "Select Number");
                String[] mStringArray = new String[NumberForSpinner.size()];
                mStringArray = NumberForSpinner.toArray(mStringArray);
                ArrayAdapter<CharSequence> langAdapter =
                        new ArrayAdapter<CharSequence>(
                                BuyJirtuuNumber.this, R.layout.spinner_text, mStringArray);
                langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                numberspin.setAdapter(langAdapter);
                numberview.setVisibility(View.GONE);

            } else {
                AvailableNumberListo.clear();
                try {

                    Object json = new JSONTokener(result).nextValue();
                    if (json instanceof JSONObject) {
                        // you have an object
                        Toast.makeText(BuyJirtuuNumber.this, "" + result, Toast.LENGTH_SHORT)
                                .show();
                    } else if (json instanceof JSONArray) {
                        // you have an array
                        JSONArray jary = new JSONArray(result);
                        for (int i = 0; i < jary.length(); i++) {
                            AvailabeNumberModel ANM = new AvailabeNumberModel();
                            JSONObject jobj = jary.getJSONObject(i);
                            String phoneNumber = jobj.getString("phoneNumber");
                            ANM.setPhoneNumber(phoneNumber);
                            String friendlyName = jobj.getString("friendlyName");
                            ANM.setFriendlyName(friendlyName);
                            String isoCountry = jobj.getString("isoCountry");
                            ANM.setIsoCountry(isoCountry);
                            String Capabilities = jobj.getString("capabilities");
                            JSONObject jobj2 = new JSONObject(Capabilities);
                            String voice = jobj2.getString("voice");
                            ANM.setVoice(voice);
                            String SMS = jobj2.getString("SMS");
                            ANM.setSMS(SMS);
                            String MMS = jobj2.getString("MMS");
                            ANM.setMMS(MMS);

                            String latitude = jobj.getString("latitude");
                            ANM.setLatitude(latitude);
                            String longitude = jobj.getString("longitude");
                            ANM.setLongitude(longitude);
                            String locality = jobj.getString("locality");
                            ANM.setLocality(locality);
                            String postalCode = jobj.getString("postalCode");
                            ANM.setPostalCode(postalCode);
                            String region = jobj.getString("region");
                            ANM.setRegion(region);
                            AvailableNumberListo.add(ANM);
                        }

                        NumberForSpinner.clear();
                        for (int i = 0; i < AvailableNumberListo.size(); i++) {
                            NumberForSpinner.add(AvailableNumberListo.get(i).getFriendlyName());
                        }
                        NumberForSpinner.add(0, "Select Number");
                        mStringArrayNumbers = new String[NumberForSpinner.size()];
                        mStringArrayNumbers = NumberForSpinner.toArray(mStringArrayNumbers);

                        //  String[] years = {"1996", "1997", "1998", "1998"};
                        ArrayAdapter<CharSequence> langAdapter =
                                new ArrayAdapter<CharSequence>(
                                        BuyJirtuuNumber.this,
                                        R.layout.spinner_text,
                                        mStringArrayNumbers);
                        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                        numberspin.setAdapter(langAdapter);
                        numberview.setVisibility(View.VISIBLE);

                        // ShowAvailableNumbersDialog();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ATask4NumberCreate extends AsyncTask<String, Void, String> {
        String Token = "";
        private final String MY_PREFS_NAME = "jirtuu";

        ATask4NumberCreate() {}

        String Response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(BuyJirtuuNumber.this);
            pdia.setMessage("Please Wait...");
            pdia.setCancelable(false);
            pdia.show();
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            Token = prefs.getString("token", "");
        }

        @Override
        protected String doInBackground(String... params) {
            Response = api.NumberCreate(jObjectType4NumberCreate, Token);
            return Response;
        }

        protected void onPostExecute(String result) {
            pdia.dismiss();
            String id = "";
            String friendly = "";
            String Phonenumber = "";
            String sid = "";
            String status = "";
            String endpoint_id = "";
            if (result == null || result.equals("")) {
                Toast.makeText(
                                BuyJirtuuNumber.this,
                                "Something went wrong! Please try again",
                                Toast.LENGTH_SHORT)
                        .show();
            } else {
                try {
                    JSONObject jobj = new JSONObject(result);
                    if (jobj.has("status")) {
                        if (jobj.getString("status").equals("in-use")) {

                            id = jobj.getString("id");
                            idia = id;
                            friendly = jobj.getString("friendlyName");
                            Phonenumber = jobj.getString("phoneNumber");
                            sid = jobj.getString("sid");
                            Sidia = sid;
                            status = jobj.getString("status");
                            //  endpoint_id = jobj.getString("endpoint_id");

                            // todo show purchase message .
                            AlertDialog alertDialog =
                                    new AlertDialog.Builder(BuyJirtuuNumber.this).create();
                            alertDialog.setTitle("Success");
                            alertDialog.setMessage(
                                    "You have purchased "
                                            + Phonenumber
                                            + "number. Go to My Numbers to see the numbers you own.\nThank you for using Jirtuu.");
                            alertDialog.setButton(
                                    AlertDialog.BUTTON_NEUTRAL,
                                    "Done",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            //    finish();

                                            try {
                                                friendlyobject = new JSONObject();
                                                friendlyobject.put("friendlyName", NumberSelected);
                                                friendlyobject.put(
                                                        "forward_enabled", ForwardEnabled);
                                                if (ForwardEnabled) {
                                                    friendlyobject.put("forward_typ", "external");
                                                    friendlyobject.put(
                                                            "external_number",
                                                            externalnum.getText().toString());
                                                } else {
                                                    friendlyobject.put("forward_typ", "voicemail");
                                                    friendlyobject.put("external_number", "");
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            new ATask4editFname().execute();
                                        }
                                    });
                            alertDialog.show();
                            new ATask4Balance().execute();

                        } else {
                            Toast.makeText(
                                            BuyJirtuuNumber.this,
                                            "" + jobj.getString("status"),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //    public void ShowAvailableNumbersDialog() {
    //        final Dialog dialog =
    //                new Dialog(BuyJirtuuNumber.this,
    // android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    //        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    //        dialog.setContentView(R.layout.availablenumber);
    //        Window window = dialog.getWindow();
    //        dialog.getWindow()
    //                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    //        ImageView back = (ImageView) dialog.findViewById(R.id.back);
    //        RecyclerView AvailableNumberList =
    //                (RecyclerView) dialog.findViewById(R.id.availablenumbers);
    //        listenero =
    //                new AvailableNumberAdapter.OnItemnumberClickListener() {
    //                    @Override
    //                    public void onItemClick(final AvailabeNumberModel item) {
    //                        dialog.dismiss();
    //                        final AlertDialog alertDialog =
    //                                new AlertDialog.Builder(BuyJirtuuNumber.this)
    //                                        .setIcon(android.R.drawable.ic_dialog_alert)
    //                                        .setTitle(
    //                                                "Are you sure you want to buy "
    //                                                        + item.getPhoneNumber()
    //                                                        + " number?")
    //                                        .setMessage("")
    //                                        .setPositiveButton(
    //                                                "Yes",
    //                                                new DialogInterface.OnClickListener() {
    //                                                    @Override
    //                                                    public void onClick(
    //                                                            DialogInterface dialogInterface,
    //                                                            int i) {
    //                                                        try {
    //                                                            jObjectType4NumberCreate =
    //                                                                    new JSONObject();
    //                                                            jObjectType4NumberCreate.put(
    //                                                                    "friendlyName",
    //                                                                    item.getFriendlyName());
    //                                                            jObjectType4NumberCreate.put(
    //                                                                    "phoneNumber",
    //                                                                    item.getPhoneNumber());
    //                                                            jObjectType4NumberCreate.put(
    //                                                                    "countryCode",
    // CountryCode);
    //                                                            //
    //                                                            //                      if
    //                                                            // (LocalRadio.isChecked()) {
    //                                                            //
    //                                                            //
    //                                                            // jObjectType4NumberCreate.put(
    //                                                            //
    //                                                            //
    //                                                            // "type", "local");
    //                                                            //
    //                                                            //                      } else {
    //                                                            //
    //                                                            //
    //                                                            // jObjectType4NumberCreate.put(
    //                                                            //
    //                                                            //
    //                                                            // "type", "toll free");
    //                                                            //
    //                                                            //                      }
    //                                                            jObjectType4NumberCreate.put(
    //                                                                    "AddressSid", "");
    //                                                        } catch (JSONException e) {
    //                                                            e.printStackTrace();
    //                                                        }
    //                                                        new ATask4NumberCreate().execute();
    //                                                    }
    //                                                })
    //                                        // set negative button
    //                                        .setNegativeButton(
    //                                                "No",
    //                                                new DialogInterface.OnClickListener() {
    //                                                    @Override
    //                                                    public void onClick(
    //                                                            DialogInterface dialogInterface,
    //                                                            int i) {}
    //                                                })
    //                                        .show();
    //                    }
    //                };
    //        avmadapter =
    //                new AvailableNumberAdapter(AvailableNumberListo, BuyJirtuuNumber.this,
    // listenero);
    //        AvailableNumberList.setHasFixedSize(true);
    //        AvailableNumberList.setLayoutManager(new LinearLayoutManager(BuyJirtuuNumber.this));
    //        AvailableNumberList.setAdapter(avmadapter);
    //        back.setOnClickListener(
    //                new View.OnClickListener() {
    //                    @Override
    //                    public void onClick(View v) {
    //                        dialog.dismiss();
    //                    }
    //                });
    //
    //        dialog.show();
    //    }

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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class ATask4FrwardCountryListe extends AsyncTask<String, Void, String> {
        String Token = "";
        private final String MY_PREFS_NAME = "jirtuu";

        ATask4FrwardCountryListe() {}

        String Response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(BuyJirtuuNumber.this);
            pdia.setMessage("Please Wait...");
            pdia.setCancelable(false);
            pdia.show();
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            Token = prefs.getString("token", "");
        }

        @Override
        protected String doInBackground(String... params) {
            Response =
                    api.getForawardCountriesResponse(
                            Token,
                            ForwardNumber.replace("-", "")
                                    .replace(")", "")
                                    .replace("(", "")
                                    .replace("+", "")
                                    .replace(" ", ""));
            return Response;
        }

        protected void onPostExecute(String result) {
            pdia.dismiss();
            try {
                JSONObject obj = new JSONObject(result);
                if (obj.has("errors")) {
                    JSONObject jb = new JSONObject(result);
                    String er = jb.getString("errors");
                    JSONObject jb2 = new JSONObject(er);
                    String erroers = jb2.getString("number");
                    Toast.makeText(BuyJirtuuNumber.this, "" + erroers, Toast.LENGTH_SHORT).show();
                } else {
                    String Destination = obj.getString("destination");
                    String Rate = obj.getString("rate");
                    dest.setText(Destination);
                    rates.setText("$" + Rate);
                    DESTINATION = Destination;
                    RATE = Rate;
                    carView.setVisibility(View.VISIBLE);
                    Seebar.setVisibility(View.VISIBLE);
                    Seebar.getIndicator().getContentView().setVisibility(View.VISIBLE);
                    lubal.setVisibility(View.VISIBLE);
                    lubal2.setVisibility(View.VISIBLE);
                    lubal3.setVisibility(View.VISIBLE);
                    //                    frwardview.setVisibility(View.VISIBLE);
                    // frwardnote.setVisibility(View.VISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class ATask4editFname extends AsyncTask<String, Void, String> {
        String Token = "";
        private final String MY_PREFS_NAME = "jirtuu";

        ATask4editFname() {}

        String Response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(BuyJirtuuNumber.this);
            pdia.setMessage("Please Wait...");
            pdia.setCancelable(false);
            pdia.show();
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            Token = prefs.getString("token", "");
        }

        @Override
        protected String doInBackground(String... params) {
            Response = api.UpdateFriendlyName(idia, friendlyobject, Token);
            return Response;
        }

        protected void onPostExecute(String result) {
            pdia.dismiss();
            if (result.equals("")) {
                Toast.makeText(BuyJirtuuNumber.this, "empty result", Toast.LENGTH_SHORT).show();
            } else {

                finish();
            }
        }
    }

    //    @Override
    //    public void onBackPressed() {
    //        // It's expensive, if running turn it off.
    //        //  DataHelper.cancelSearch();
    //        new ATask4FrwardCountryListe().execute();
    //        hideKeyboard();
    //        super.onBackPressed();
    //    }
    //
    private void hideKeyboard() {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(externalnum.getWindowToken(), 0);
    }
}
