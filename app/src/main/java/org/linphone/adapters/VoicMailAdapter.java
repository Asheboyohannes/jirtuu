package org.linphone.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.linphone.APICALL;
import org.linphone.R;
import org.linphone.activities.MyVoiceMail;
import org.linphone.models.VoiceMail_MessagesModel;

public class VoicMailAdapter extends RecyclerView.Adapter<VoicMailAdapter.ViewHolder> {
    private ArrayList<VoiceMail_MessagesModel> listdata;
    MyVoiceMail Act;
    int currentPosition;
    ProgressDialog pdia;

    APICALL api;
    VoicMailAdapter.OnItemClickListener listener;

    VoiceMail_MessagesModel mnm;

    public VoicMailAdapter(
            ArrayList<VoiceMail_MessagesModel> listdata,
            MyVoiceMail allagents,
            VoicMailAdapter.OnItemClickListener listener) {
        this.listdata = listdata;
        this.Act = allagents;
        this.mnm = new VoiceMail_MessagesModel();

        api = new APICALL();
        this.listener = listener;
    }

    @Override
    public VoicMailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.voicemailitem, parent, false);
        VoicMailAdapter.ViewHolder viewHolder = new VoicMailAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(
            @NonNull final VoicMailAdapter.ViewHolder holder, final int position) {
        currentPosition = position;
        holder.mailboxuser.setText(listdata.get(position).getMailboxuser());
        holder.msg_id.setText(listdata.get(position).getMsg_id());
        holder.callerid.setText(listdata.get(position).getCallerid());
        holder.idios.setText(listdata.get(position).getId());
        //        long Mills = Long.parseLong(listdata.get(position).getOrigtime());

        // holder.origtime.setText(getIntervalTime(Mills));
        holder.origtime.setText(listdata.get(position).getOrigtime());
        holder.duration.setText(listdata.get(position).getDuration());

        holder.play.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mnm = listdata.get(position);
                        listener.onItemClick(mnm, holder.play, position);
                    }
                });

        holder.del.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new ATask4DeleteVoiceMails(listdata.get(position).getId()).execute();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mailboxuser;
        public TextView msg_id;
        public TextView callerid;
        public TextView idios;
        public TextView origtime;
        public TextView duration;

        public LinearLayout root;
        public ImageView play;
        public ImageView del;

        public ViewHolder(View itemView) {
            super(itemView);

            this.mailboxuser = (TextView) itemView.findViewById(R.id.mailboxuser);
            this.msg_id = (TextView) itemView.findViewById(R.id.msg_id);
            this.callerid = (TextView) itemView.findViewById(R.id.callerid);
            this.idios = (TextView) itemView.findViewById(R.id.idios);
            this.origtime = (TextView) itemView.findViewById(R.id.origtime);
            this.duration = (TextView) itemView.findViewById(R.id.duration);
            this.root = (LinearLayout) itemView.findViewById(R.id.root);
            this.play = (ImageView) itemView.findViewById(R.id.play);
            this.del = (ImageView) itemView.findViewById(R.id.del);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(VoiceMail_MessagesModel item, ImageView holderx, int position);
    }

    public static String getIntervalTime(long longInterval) {

        long intMillis = longInterval;
        long dd = TimeUnit.MILLISECONDS.toDays(intMillis);
        long daysMillis = TimeUnit.DAYS.toMillis(dd);
        intMillis -= daysMillis;
        long hh = TimeUnit.MILLISECONDS.toHours(intMillis);
        long hoursMillis = TimeUnit.HOURS.toMillis(hh);
        intMillis -= hoursMillis;
        long mm = TimeUnit.MILLISECONDS.toMinutes(intMillis);
        long minutesMillis = TimeUnit.MINUTES.toMillis(mm);
        intMillis -= minutesMillis;
        long ss = TimeUnit.MILLISECONDS.toSeconds(intMillis);
        long secondsMillis = TimeUnit.SECONDS.toMillis(ss);
        intMillis -= secondsMillis;

        //        String stringInterval = "%02d:%02d:%02d";
        String stringInterval = "%02d:%02d";
        return String.format(stringInterval, mm, ss);
    }

    private class ATask4DeleteVoiceMails extends AsyncTask<String, Void, String> {
        String Token = "";
        private final String MY_PREFS_NAME = "jirtuu";
        String ID = "";

        ATask4DeleteVoiceMails(String id) {
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
            Response = api.DeleteVoiceMail(ID, Token);
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
