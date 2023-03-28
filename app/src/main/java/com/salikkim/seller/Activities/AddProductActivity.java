package com.salikkim.seller.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.salikkim.seller.Fragments.AddressFragment;
import com.salikkim.seller.Fragments.ImagesFragment;
import com.salikkim.seller.Fragments.ItemDetailsFragment;
import com.salikkim.seller.Models.Product;
import com.salikkim.seller.R;
import com.salikkim.seller.databinding.ActivityAddProductBinding;

public class AddProductActivity extends AppCompatActivity {
    private ActivityAddProductBinding addProductBinding;
    private Product product;
    private String seller_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addProductBinding = ActivityAddProductBinding.inflate(getLayoutInflater());
        View view = addProductBinding.getRoot();
        setContentView(view);
        addProductBinding.toolbaraddProductActivity.setTitle("Add item");
        setSupportActionBar(addProductBinding.toolbaraddProductActivity);
        addProductBinding.toolbaraddProductActivity.setNavigationIcon(R.drawable.baseline_arrow_back);
        addProductBinding.toolbaraddProductActivity.setNavigationOnClickListener(v -> finish());
        String edit = getIntent().getExtras().getString("edit");
        seller_id = getIntent().getExtras().getString("seller_id");
        product = (Product) getIntent().getParcelableExtra("product");
        product.setS_id(seller_id);

        switch (edit) {
            case "item":
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_main, new ItemDetailsFragment(product, true))
                        .commitNow();
                break;
            case "address":
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_main, new AddressFragment(product, true))
                        .commitNow();
                break;
            case "image":
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_main, new ImagesFragment(String.valueOf(product.getP_Id()), true))
                        .commitNow();
                break;
            default:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_main, new ItemDetailsFragment(product, false))
                        .commitNow();
                break;
        }

    }
}