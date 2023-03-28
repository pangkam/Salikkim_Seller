package com.salikkim.seller.Interfaces;

import android.view.MenuItem;

import com.salikkim.seller.Models.Orders;

public interface OrderItemClick {
    void onImageClick(int position, Orders orders);
    void onMenuItemClick(int position, MenuItem menuItem, Orders orders);
}
