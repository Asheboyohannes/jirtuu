package org.linphone.adapters;

import android.app.ProgressDialog;
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
import org.linphone.models.CountryListModel;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {
    private ArrayList<CountryListModel> listdata;
    BuyJirtuuNumber Act;

    ProgressDialog pdia;
    APICALL api;
    OnItemClickListener listener;
    // RecyclerView recyclerView;
    public CountryAdapter(
            ArrayList<CountryListModel> listdata,
            BuyJirtuuNumber allagents,
            OnItemClickListener listener) {
        this.listdata = listdata;
        this.Act = allagents;

        api = new APICALL();
        this.listener = listener;
    }

    @Override
    public CountryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.countryitem, parent, false);
        CountryAdapter.ViewHolder viewHolder = new CountryAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CountryAdapter.ViewHolder holder, final int position) {

        holder.Country.setText(listdata.get(position).getCountry());
        holder.Countrycode.setText(listdata.get(position).getCountryCode());
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
        public TextView Country;
        public TextView Countrycode;

        public LinearLayout root;

        public ViewHolder(View itemView) {
            super(itemView);

            this.Country = (TextView) itemView.findViewById(R.id.country);
            this.Countrycode = (TextView) itemView.findViewById(R.id.countrycode);
            this.root = (LinearLayout) itemView.findViewById(R.id.root);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(CountryListModel item);
    }
}
