package org.linphone.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.suke.widget.SwitchButton;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import org.linphone.APICALL;
import org.linphone.R;
import org.linphone.activities.MyNumbers;
import org.linphone.models.MyNumbersModel;

public class MyNumbersAdapter extends RecyclerView.Adapter<MyNumbersAdapter.ViewHolder> {
    private ArrayList<MyNumbersModel> listdata;
    MyNumbers Act;
    ProgressDialog pdia;
    APICALL api;
    JSONObject friendlyobject = null;
    String id = "";
    String forward_typ = "";
    private CountryPicker picker;

    public MyNumbersAdapter(ArrayList<MyNumbersModel> listdata, MyNumbers allagents) {
        this.listdata = listdata;
        this.Act = allagents;
        api = new APICALL();
        picker = CountryPicker.newInstance("Select Country"); // dialog title
    }

    @Override
    public MyNumbersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.mynumberitem, parent, false);
        MyNumbersAdapter.ViewHolder viewHolder = new MyNumbersAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyNumbersAdapter.ViewHolder holder, final int position) {

        holder.PhoneNumber.setText(listdata.get(position).getPhoneNumber());
        holder.Friendlyname.setText(listdata.get(position).getFriendlyName());
        holder.Status.setText(listdata.get(position).getStatus());
        holder.idios.setText(listdata.get(position).getId());
        holder.editfriendlyname.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        id = listdata.get(position).getId();

                        EditFriendlyNameDialog(listdata.get(position).getFriendlyName(), position);
                    }
                });

        if (listdata.get(position).getExternal_number() == null
                || listdata.get(position).getExternal_number().equals("null")) {
            holder.external_number.setVisibility(View.GONE);
            holder.ext.setVisibility(View.GONE);

        } else {
            holder.external_number.setText(listdata.get(position).getExternal_number());
            holder.external_number.setVisibility(View.VISIBLE);
            holder.ext.setVisibility(View.VISIBLE);
        }

        holder.del.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        id = listdata.get(position).getId();
                        new ATask4DeleteNumber(id).execute();
                    }
                });

        holder.FRTYPE.setText("" + listdata.get(position).getForward_typ());
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView PhoneNumber;
        public TextView Friendlyname;
        public TextView Status;
        public TextView idios;
        public TextView external_number;
        public ImageView editfriendlyname;
        public Button FRTYPE;
        public TextView ext;
        public ImageView del;

        public ViewHolder(View itemView) {
            super(itemView);

            this.PhoneNumber = (TextView) itemView.findViewById(R.id.phonenumber);
            this.Friendlyname = (TextView) itemView.findViewById(R.id.friendlyname);
            this.Status = (TextView) itemView.findViewById(R.id.status);
            this.idios = (TextView) itemView.findViewById(R.id.idios);
            this.editfriendlyname = (ImageView) itemView.findViewById(R.id.editfriendlyname);
            this.external_number = (TextView) itemView.findViewById(R.id.external_number);
            this.FRTYPE = (Button) itemView.findViewById(R.id.frtype);
            this.ext = (TextView) itemView.findViewById(R.id.ext);
            this.del = (ImageView) itemView.findViewById(R.id.del);
        }
    }

    boolean ForwardEnabled = false;

    public void EditFriendlyNameDialog(String friendlyName, int position) {
        final Dialog dialog = new Dialog(Act, R.style.Theme_Dialog);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editfriendlynamedialog);
        Window window = dialog.getWindow();
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final EditText editfname = (EditText) dialog.findViewById(R.id.efnam);
        final EditText externalnum = (EditText) dialog.findViewById(R.id.external_number);
        final TextView label = (TextView) dialog.findViewById(R.id.labeltext);

        final RadioButton voicemail = (RadioButton) dialog.findViewById(R.id.rvoicemail);
        final RadioButton External = (RadioButton) dialog.findViewById(R.id.rexternal);
        final SwitchButton frwrdEnabled = (SwitchButton) dialog.findViewById(R.id.frwardenabled);
        final ImageView CountryPick = (ImageView) dialog.findViewById(R.id.countrypick);
        final LinearLayout exter = (LinearLayout) dialog.findViewById(R.id.exter);

        if (listdata.get(position).getExternal_number().equals("null")) {

        } else {
            externalnum.setText(listdata.get(position).getExternal_number());
        }

        if (listdata.get(position).getForward_enabled().equals("1")) {
            frwrdEnabled.setChecked(true);
            ForwardEnabled = true;
        } else {
            frwrdEnabled.setChecked(false);
            ForwardEnabled = false;
        }

        if (listdata.get(position).getForward_typ().equals("external")) {
            External.setChecked(true);
            voicemail.setChecked(false);
            forward_typ = "external";
        } else {
            External.setChecked(false);
            voicemail.setChecked(true);
            forward_typ = "voicemail";
        }

        //        if (listdata.get(position).getForward_typ().equals("voicemail")) {
        //            voicemail.setChecked(true);
        //            External.setChecked(false);
        //            externalnum.setVisibility(View.GONE);
        //            label.setVisibility(View.GONE);
        //            exter.setVisibility(View.GONE);
        //        } else {
        //            voicemail.setChecked(false);
        //            External.setChecked(true);
        //            externalnum.setVisibility(View.VISIBLE);
        //            label.setVisibility(View.VISIBLE);
        //            exter.setVisibility(View.VISIBLE);
        //        }

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
                        picker.show(Act.getSupportFragmentManager(), "COUNTRY_PICKER");
                    }
                });

        frwrdEnabled.setOnCheckedChangeListener(
                new SwitchButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                        if (isChecked) {
                            ForwardEnabled = true;

                        } else {

                            ForwardEnabled = false;

                            forward_typ = "voicemail";
                        }
                    }
                });

        External.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            externalnum.setVisibility(View.VISIBLE);
                            label.setVisibility(View.VISIBLE);
                            exter.setVisibility(View.VISIBLE);
                            forward_typ = "external";
                        } else {

                            externalnum.setVisibility(View.GONE);
                            label.setVisibility(View.GONE);
                            exter.setVisibility(View.GONE);
                            forward_typ = "voicemail";
                        }
                    }
                });

        editfname.setText(friendlyName);

        Button Update = (Button) dialog.findViewById(R.id.updateenam);

        Update.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (editfname.getText().toString().matches("")) {
                            Toast.makeText(
                                            Act,
                                            "Please write new Friendly Name",
                                            Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }

                        dialog.dismiss();
                        try {
                            friendlyobject = new JSONObject();
                            friendlyobject.put("friendlyName", editfname.getText().toString());
                            friendlyobject.put("forward_enabled", ForwardEnabled);
                            if (voicemail.isChecked()) {
                                friendlyobject.put("forward_type", forward_typ);
                                externalnum.setText("");
                            } else {
                                friendlyobject.put("forward_type", forward_typ);
                            }

                            friendlyobject.put("external_number", externalnum.getText().toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        new ATask4editFname().execute();
                    }
                });

        dialog.show();
    }

    private class ATask4editFname extends AsyncTask<String, Void, String> {
        String Token = "";
        private final String MY_PREFS_NAME = "jirtuu";

        ATask4editFname() {}

        String Response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(Act);
            pdia.setMessage("Please Wait...");
            pdia.setCancelable(false);
            pdia.show();
            SharedPreferences prefs = Act.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            Token = prefs.getString("token", "");
        }

        @Override
        protected String doInBackground(String... params) {
            Response = api.UpdateFriendlyName(id, friendlyobject, Token);
            return Response;
        }

        protected void onPostExecute(String result) {
            pdia.dismiss();
            if (result.equals("")) {
                Toast.makeText(Act, "empty result", Toast.LENGTH_SHORT).show();
            } else {

                if (Build.VERSION.SDK_INT >= 11) {
                    Act.recreate();
                } else {
                    Act.finish();
                    Act.startActivity(Act.getIntent());
                }
            }
        }
    }

    private class ATask4DeleteNumber extends AsyncTask<String, Void, String> {
        String Token = "";
        private final String MY_PREFS_NAME = "jirtuu";
        String ID = "";

        ATask4DeleteNumber(String id) {
            ID = id;
        }

        String Response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(Act);
            pdia.setMessage("Please Wait...");
            pdia.setCancelable(false);
            pdia.show();
            SharedPreferences prefs = Act.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            Token = prefs.getString("token", "");
        }

        @Override
        protected String doInBackground(String... params) {
            Response = api.DeleteNumber(ID, Token);
            return Response;
        }

        protected void onPostExecute(String result) {
            pdia.dismiss();
            if (result.equals("")) {
                //  Toast.makeText(Act, "empty result", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= 11) {
                    Act.recreate();
                } else {
                    Act.finish();
                    Act.startActivity(Act.getIntent());
                }
            } else {

                //  Toast.makeText(Act, "" + result, Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= 11) {
                    Act.recreate();
                } else {
                    Act.finish();
                    Act.startActivity(Act.getIntent());
                }
            }
        }
    }
}
