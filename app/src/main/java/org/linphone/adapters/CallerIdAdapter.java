package org.linphone.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.linphone.APICALL;
import org.linphone.R;
import org.linphone.activities.Update_Caller_Id;

public class CallerIdAdapter extends RecyclerView.Adapter<CallerIdAdapter.ViewHolder> {
    private ArrayList<String> listdata;
    Update_Caller_Id Act;
    APICALL api;
    CallerIdAdapter.OnItemnumberClickListener listener;

    public CallerIdAdapter(
            ArrayList<String> listdata,
            Update_Caller_Id callid,
            CallerIdAdapter.OnItemnumberClickListener listener) {
        this.listdata = listdata;
        this.Act = callid;
        api = new APICALL();
        this.listener = listener;
    }

    @Override
    public CallerIdAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.calleriditem, parent, false);
        CallerIdAdapter.ViewHolder viewHolder = new CallerIdAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CallerIdAdapter.ViewHolder holder, final int position) {
        holder.PhoneNumber.setText(listdata.get(position));

        holder.root.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(listdata.get(position));
                    }
                });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView PhoneNumber;

        public LinearLayout root;

        public ViewHolder(View itemView) {
            super(itemView);

            this.PhoneNumber = (TextView) itemView.findViewById(R.id.numbern);

            this.root = (LinearLayout) itemView.findViewById(R.id.root);
        }
    }

    public interface OnItemnumberClickListener {
        void onItemClick(String item);
    }
}
