package org.linphone.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.linphone.APICALL;
import org.linphone.R;
import org.linphone.adapters.VoicMailAdapter;
import org.linphone.models.VoiceMail_MessagesModel;

public class MyVoiceMail extends AppCompatActivity {
    RecyclerView RCVIEW;
    ProgressDialog pdia;
    APICALL api;
    ArrayList<VoiceMail_MessagesModel> MyVoiceMailList = new ArrayList<>();
    VoicMailAdapter mAdapter;
    VoicMailAdapter.OnItemClickListener listener;
    boolean isplaying = false;
    TextView Empty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myvoicemail);
        RCVIEW = (RecyclerView) findViewById(R.id.rcview);
        Empty = (TextView) findViewById(R.id.empty);

        api = new APICALL();

        new ATask4MyVoiceMails().execute();
    }

    private class ATask4MyVoiceMails extends AsyncTask<String, Void, String> {
        String Token = "";
        private final String MY_PREFS_NAME = "jirtuu";

        ATask4MyVoiceMails() {}

        String Response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(MyVoiceMail.this);
            pdia.setMessage("Please Wait...");
            pdia.setCancelable(false);
            pdia.show();
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            Token = prefs.getString("token", "");
        }

        @Override
        protected String doInBackground(String... params) {
            Response = api.getEndPoint(Token);
            return Response;
        }

        protected void onPostExecute(String result) {
            pdia.dismiss();
            if (result == null || result.equals("")) {
                Toast.makeText(MyVoiceMail.this, "empty result", Toast.LENGTH_SHORT).show();
            } else {

                try {
                    JSONObject obje = new JSONObject(result);
                    String data = obje.getString("data");

                    JSONArray jir = new JSONArray(data);

                    JSONObject obj = jir.getJSONObject(0);

                    // JSONObject obj = new JSONObject(data);
                    String voiceobj = obj.getString("voicemail");
                    JSONObject obj2 = new JSONObject(voiceobj);
                    String id = obj2.getString("id");
                    String mailbox = obj2.getString("mailbox");
                    String pin = obj2.getString("pin");
                    String Messagesarray = obj2.getString("voicemail_messages");

                    JSONArray jary = new JSONArray(Messagesarray);
                    for (int i = 0; i < jary.length(); i++) {
                        VoiceMail_MessagesModel MNM = new VoiceMail_MessagesModel();
                        JSONObject jobj = jary.getJSONObject(i);
                        MNM.setId(jobj.getString("id"));
                        MNM.setMsgnum(jobj.getString("msgnum"));
                        MNM.setCallerid(jobj.getString("callerid"));
                        MNM.setOrigtime(jobj.getString("origtime"));
                        MNM.setDuration(jobj.getString("duration"));
                        MNM.setMailboxuser(jobj.getString("mailboxuser"));
                        MNM.setMsg_id(jobj.getString("msg_id"));
                        MNM.setMessage_recording(jobj.getString("message_recording"));
                        MyVoiceMailList.add(MNM);
                    }
                    if (MyVoiceMailList.size() == 0) {
                        Empty.setVisibility(View.VISIBLE);
                        RCVIEW.setVisibility(View.GONE);

                    } else {
                        Empty.setVisibility(View.GONE);
                        RCVIEW.setVisibility(View.VISIBLE);

                        listener =
                                new VoicMailAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(
                                            VoiceMail_MessagesModel item,
                                            final ImageView holderx,
                                            final int position) {
                                        try {
                                            if (isplaying) {

                                                return;
                                            } else {

                                                isplaying = true;
                                                //  //  holderx.play.setEnabled(false);
                                                holderx.setImageResource(R.drawable.pause);

                                                MediaPlayer mediaPlayer = new MediaPlayer();
                                                mediaPlayer.setAudioStreamType(
                                                        AudioManager.STREAM_MUSIC);
                                                mediaPlayer.setOnCompletionListener(
                                                        new MediaPlayer.OnCompletionListener() {
                                                            @Override
                                                            public void onCompletion(
                                                                    MediaPlayer mp) {
                                                                isplaying = false;
                                                                //
                                                                // holderx.play.setEnabled(true);
                                                                holderx.setImageResource(
                                                                        R.drawable.record_play);
                                                            }
                                                        });

                                                mediaPlayer.setDataSource(
                                                        item.getMessage_recording());
                                                mediaPlayer.prepare();
                                                mediaPlayer.start();
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };

                        mAdapter = new VoicMailAdapter(MyVoiceMailList, MyVoiceMail.this, listener);
                        RCVIEW.setLayoutManager(new LinearLayoutManager(MyVoiceMail.this));
                        RCVIEW.setAdapter(mAdapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
