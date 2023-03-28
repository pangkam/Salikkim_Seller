package com.salikkim.seller.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.salikkim.seller.Interfaces.OrderItemClick;
import com.salikkim.seller.Models.Orders;
import com.salikkim.seller.R;


import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private List<Orders> ordersList;
    private OrderItemClick orderItemClick;

    public OrdersAdapter(Context context, List<Orders> ordersList, OrderItemClick orderItemClick) {
        this.context = context;
        this.ordersList = ordersList;
        this.orderItemClick = orderItemClick;
    }

    public void setOrdersList(List<Orders> ordersList) {
        this.ordersList = ordersList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(
                R.layout.card_order, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final int pos = position;
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                .setBaseColor(Color.parseColor("#F3F3F3"))
                .setBaseAlpha(1)
                .setHighlightColor(Color.parseColor("#E7E7E7"))
                .setHighlightAlpha(1)
                .setDropoff(50)
                .build();
        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
        Glide.with(context).
                load(ordersList.get(pos).getThumbnail())
                .placeholder(shimmerDrawable)
                .into(itemViewHolder.thumbnail);

        itemViewHolder.customer.setText(Html.fromHtml("Customer: <b>" + ordersList.get(pos).getCustomer_Name() + "</b>"));
        itemViewHolder.title.setText(Html.fromHtml("Title: <b>" + ordersList.get(pos).getTitle() + "</b>"));
        itemViewHolder.order_date.setText(ordersList.get(pos).getDate());

        itemViewHolder.price.setText(Html.fromHtml(context.getString(R.string.Rs) +
                String.format("%.0f", ordersList.get(pos).getPrice())));
        itemViewHolder.price.setPaintFlags(itemViewHolder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        itemViewHolder.sale_price.setText(Html.fromHtml("<b>" + context.getString(R.string.Rs) +
                String.format("%.0f", ordersList.get(pos).getSale_Price())));


        itemViewHolder.total_price.setText(Html.fromHtml("<b>" + context.getString(R.string.Rs) +
                String.format("%.0f", ordersList.get(pos).getSale_Price())
                +
                " + " + context.getString(R.string.Rs) + String.format("%.0f", ordersList.get(pos).getShipping_Charge()) + "</b>(Shipping Charge) <b>x</b> "+ordersList.get(pos).getQuantity()));


        itemViewHolder.final_price.setText(Html.fromHtml("Total: <b>" + context.getString(R.string.Rs) + String.format("%.0f", ((ordersList.get(pos).getSale_Price())
                + ordersList.get(pos).getShipping_Charge())*ordersList.get(pos).getQuantity()) + "</b>"));

        itemViewHolder.color.setText(Html.fromHtml("Color: <b>" + ordersList.get(pos).getColor() + "</b>"));
        itemViewHolder.size.setText(Html.fromHtml("Size: <b>" + ordersList.get(pos).getSize() + "</b>"));
        itemViewHolder.quantity.setText(Html.fromHtml("Quantity: <b>" + ordersList.get(pos).getQuantity() + "</b>"));
        itemViewHolder.address.setText(Html.fromHtml("To: <b>" + ordersList.get(pos).getAddress() + "</b>"));
        setUpStatus(itemViewHolder, pos);
    }

    private void setUpStatus(ItemViewHolder itemViewHolder, int pos) {
        if(ordersList.get(pos).getScreenshot()==null){
            itemViewHolder.screenshot.setText("Not available");
            itemViewHolder.screenshot.setTextColor((ContextCompat.getColor(context, R.color.red)));
        }else {
            itemViewHolder.screenshot.setText("Available");
            itemViewHolder.screenshot.setTextColor((ContextCompat.getColor(context, R.color.green)));
        }
        if (ordersList.get(pos).getStatus() == 4) {
            itemViewHolder.status.setText(Html.fromHtml("<b>Not delivered</b>(" + ordersList.get(pos).getStatus_Info() + ")"));
            itemViewHolder.status.setTextColor((ContextCompat.getColor(context, R.color.red)));

        } else if (ordersList.get(pos).getStatus() == 3) {
            itemViewHolder.status.setText(Html.fromHtml("<b>Delivered</b>"));
            itemViewHolder.status.setTextColor((ContextCompat.getColor(context, R.color.green)));
        } else if (ordersList.get(pos).getStatus() == 2) {
            itemViewHolder.status.setText(Html.fromHtml("<b>Ordered successful</b>"));
            itemViewHolder.status.setTextColor((ContextCompat.getColor(context, R.color.red)));
            itemViewHolder.pay_mode.setText(Html.fromHtml("Payment mode: <b>Pending payment"));
        } else if (ordersList.get(pos).getStatus() == 1) {
            itemViewHolder.status.setText(Html.fromHtml("<b>Ordered successful</b>"));
            itemViewHolder.status.setTextColor((ContextCompat.getColor(context, R.color.green)));
        } else if (ordersList.get(pos).getStatus() == 0) {
            itemViewHolder.status.setText(Html.fromHtml("<b>Ordered canceled</b>(" + ordersList.get(pos).getStatus_Info() + ")"));
            itemViewHolder.status.setTextColor((ContextCompat.getColor(context, R.color.red)));
        }

    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        private ImageView thumbnail, btn_menu;
        private TextView customer, title, price, sale_price, total_price,final_price, color, size, quantity, status, order_date, pay_mode, address,screenshot;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.order_thumbnail);
            customer = itemView.findViewById(R.id.order_customer_name);
            title = itemView.findViewById(R.id.order_title);
            order_date = itemView.findViewById(R.id.order_date);
            price = itemView.findViewById(R.id.order_price);
            sale_price = itemView.findViewById(R.id.order_sale_price);
            total_price = itemView.findViewById(R.id.order_total_price);
            final_price = itemView.findViewById(R.id.order_final_price);
            color = itemView.findViewById(R.id.order_color);
            size = itemView.findViewById(R.id.order_size);
            quantity = itemView.findViewById(R.id.order_qnty);
            status = itemView.findViewById(R.id.order_status);
            pay_mode = itemView.findViewById(R.id.order_pay_mode);
            address = itemView.findViewById(R.id.order_address);
            btn_menu = itemView.findViewById(R.id.order_btn_action);
            screenshot = itemView.findViewById(R.id.tv_order_screenshot);


            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    orderItemClick.onImageClick(getAdapterPosition(), ordersList.get(getAdapterPosition()));
                }
            });
            btn_menu.setOnClickListener(this);

        }

        @SuppressLint("RestrictedApi")
        @Override
        public void onClick(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            if (popupMenu.getMenu() instanceof MenuBuilder) {
                ((MenuBuilder) popupMenu.getMenu()).setOptionalIconsVisible(true);
            }
            popupMenu.getMenuInflater().inflate(R.menu.order_action_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int position = getAdapterPosition();
            orderItemClick.onMenuItemClick(position, item, ordersList.get(position));
            return false;
        }
    }
}
