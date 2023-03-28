package com.salikkim.seller.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.salikkim.seller.Interfaces.AddressClick;
import com.salikkim.seller.Models.Address;
import com.salikkim.seller.R;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    private final Context context;
    private List<Address> addresses;
    private final AddressClick addressClick;

    private final boolean touch;


    public AddressAdapter(Context context, List<Address> addresses, AddressClick addressClick, boolean touch) {
        this.context = context;
        this.addresses = addresses;
        this.addressClick = addressClick;
        this.touch = touch;
    }

    public void setAddresses(ArrayList<Address> addresses) {
        this.addresses = addresses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        final int pos = position;
        if (addresses.get(pos).isCheck()) {
            holder.tv_address.setText(Html.fromHtml(addresses.get(pos).getName() + "  <b>" + context.getString(R.string.Rs) + addresses.get(pos).getCharge() + "<b>"));
            holder.check.setVisibility(View.VISIBLE);
        } else {
            holder.tv_address.setText(addresses.get(pos).getName());
            holder.check.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    class AddressViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_address;
        private ImageView check;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_address = itemView.findViewById(R.id.address_name);
            check = itemView.findViewById(R.id.address_check);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (touch)
                        addressClick.onAddressClick(getAdapterPosition(), addresses.get(getAdapterPosition()));
                }
            });
        }
    }
}
