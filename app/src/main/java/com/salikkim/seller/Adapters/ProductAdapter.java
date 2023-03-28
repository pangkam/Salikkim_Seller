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
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.salikkim.seller.Interfaces.ProductClick;
import com.salikkim.seller.Models.Product;
import com.salikkim.seller.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Product> productList;
    private ProductClick productClick;

    public ProductAdapter(Context context, List<Product> productList, ProductClick productClick) {
        this.context = context;
        this.productList = productList;
        this.productClick = productClick;
    }

    public void setProductsList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(
                R.layout.card_product, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final int pos = position;
        final String thumbnail;
        if (productList.get(pos).getThumbnail() != null) {
            thumbnail = productList.get(pos).getThumbnail().replace("\"", "");
        } else {
            thumbnail = "";
        }
        ProductViewHolder productViewHolder = (ProductViewHolder) holder;
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
                load(thumbnail)
                .placeholder(shimmerDrawable)
                .into(productViewHolder.thumbnail);

        productViewHolder.title.setText(productList.get(pos).getTitle());
        productViewHolder.price.setText(context.getString(R.string.Rs) + String.format("%.0f", productList.get(pos).getPrice()));
        productViewHolder.price.setPaintFlags(productViewHolder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        productViewHolder.discount.setText(String.format("%.0f", productList.get(pos).getDiscount()) + "% Offer");
        productViewHolder.sale_Price.setText(context.getString(R.string.Rs) + String.format("%.0f", productList.get(pos).getSale_Price()));
        productViewHolder.color.setText(Html.fromHtml("Color: <b>" + productList.get(pos).getColor() + "</b>"));
        productViewHolder.size.setText(Html.fromHtml("Size: <b>" + productList.get(pos).getSize() + "</b>"));
        productViewHolder.quantity.setText(Html.fromHtml("Quantity: <b>" + productList.get(pos).getQuantity() + "</b>"));
        productViewHolder.desc.setText(Html.fromHtml("Description: <b>" + productList.get(pos).getP_Desc() + "</b>"));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        private ImageView thumbnail,btn_more;
        private TextView title, price, sale_Price, discount,size,color,quantity,desc,btn_see_address;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.product_thumbnail);
            title = itemView.findViewById(R.id.product_title);
            price = itemView.findViewById(R.id.product_price);
            sale_Price = itemView.findViewById(R.id.product_sale_price);
            discount = itemView.findViewById(R.id.product_discount);
            size  = itemView.findViewById(R.id.product_size);
            color = itemView.findViewById(R.id.product_color);
            quantity = itemView.findViewById(R.id.product_qnty);
            desc = itemView.findViewById(R.id.product_desc);
            btn_more = itemView.findViewById(R.id.product_btn_action);
            btn_see_address = itemView.findViewById(R.id.product_btn_available_addresses);

            btn_see_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    productClick.onSeeAddressClick(getAdapterPosition(),productList.get(getAdapterPosition()));
                }
            });

            btn_more.setOnClickListener(this);


        }

        @SuppressLint("RestrictedApi")
        @Override
        public void onClick(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            if (popupMenu.getMenu() instanceof MenuBuilder) {
                ((MenuBuilder) popupMenu.getMenu()).setOptionalIconsVisible(true);
            }
            popupMenu.getMenuInflater().inflate(R.menu.product_action_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            int position = getAdapterPosition();
            productClick.onMenuItemClick(position, menuItem, productList.get(position));
            return false;
        }
    }
}
