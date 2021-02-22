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
import org.linphone.activities.BuyJirtuuNumber;
import org.linphone.models.AvailabeNumberModel;

public class AvailableNumberAdapter
        extends RecyclerView.Adapter<AvailableNumberAdapter.ViewHolder> {
    private ArrayList<AvailabeNumberModel> listdata;
    BuyJirtuuNumber Act;
    APICALL api;
    AvailableNumberAdapter.OnItemnumberClickListener listener;

    public AvailableNumberAdapter(
            ArrayList<AvailabeNumberModel> listdata,
            BuyJirtuuNumber allagents,
            AvailableNumberAdapter.OnItemnumberClickListener listener) {
        this.listdata = listdata;
        this.Act = allagents;
        api = new APICALL();
        this.listener = listener;
    }

    @Override
    public AvailableNumberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.countryitem, parent, false);
        AvailableNumberAdapter.ViewHolder viewHolder =
                new AvailableNumberAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(
            @NonNull AvailableNumberAdapter.ViewHolder holder, final int position) {
        holder.PhoneNumber.setText(listdata.get(position).getPhoneNumber());
        holder.Friendly.setText(listdata.get(position).getFriendlyName());
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
        public TextView Friendly;

        public LinearLayout root;

        public ViewHolder(View itemView) {
            super(itemView);

            this.PhoneNumber = (TextView) itemView.findViewById(R.id.country);
            this.Friendly = (TextView) itemView.findViewById(R.id.countrycode);
            this.root = (LinearLayout) itemView.findViewById(R.id.root);
        }
    }

    public interface OnItemnumberClickListener {
        void onItemClick(AvailabeNumberModel item);
    }
}
