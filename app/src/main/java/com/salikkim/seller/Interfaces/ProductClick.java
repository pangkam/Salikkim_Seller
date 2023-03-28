package com.salikkim.seller.Interfaces;

import android.view.MenuItem;
import com.salikkim.seller.Models.Product;

public interface ProductClick {
   // void onPhotoClick(int position,Products products);
    void onSeeAddressClick(int position, Product product);
    void onMenuItemClick(int position, MenuItem menuItem, Product products);
}
