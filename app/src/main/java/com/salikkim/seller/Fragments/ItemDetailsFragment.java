package com.salikkim.seller.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.salikkim.seller.Helper.ApiController;
import com.salikkim.seller.Models.Product;
import com.salikkim.seller.Models.ResponseModel;
import com.salikkim.seller.R;
import com.salikkim.seller.databinding.FragmentItemDetailsBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailsFragment extends Fragment implements View.OnTouchListener, View.OnClickListener {
    private FragmentItemDetailsBinding itemDetailsBinding;
    private Product product;
    private boolean isUpdate;

    public ItemDetailsFragment(Product product, boolean isUpdate) {
        // Required empty public constructor
        this.product = product;
        this.isUpdate = isUpdate;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemDetailsBinding = FragmentItemDetailsBinding.inflate(inflater, container, false);
        View view = itemDetailsBinding.getRoot();
        onTextChange();
        if (isUpdate) {
            initViews();
        }
        itemDetailsBinding.btnItemReset.setOnClickListener(this);
        itemDetailsBinding.btnItemNext.setOnClickListener(this);
        return view;
    }

    private void initViews() {
        itemDetailsBinding.tiProductName.setText(product.getTitle());
        itemDetailsBinding.tiPrice.setText("" + product.getPrice());
        itemDetailsBinding.tiSalePrice.setText("" + product.getSale_Price());
        itemDetailsBinding.tiColor.setText(product.getColor());
        itemDetailsBinding.tiSize.setText(product.getSize());
        itemDetailsBinding.tiQuantity.setText("" + product.getQuantity());
        itemDetailsBinding.tiDesc.setText(product.getP_Desc());
        itemDetailsBinding.tiTags.setText(product.getCategory());
        itemDetailsBinding.btnItemNext.setText("Save");
        itemDetailsBinding.btnItemNext.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_done, 0);
        // itemDetailsBinding.btnItemNext.drawable
    }

    private void onTextChange() {
        itemDetailsBinding.tiProductName.setOnTouchListener(this);
        itemDetailsBinding.tiPrice.setOnTouchListener(this);
        itemDetailsBinding.tiSalePrice.setOnTouchListener(this);
        itemDetailsBinding.tiColor.setOnTouchListener(this);
        itemDetailsBinding.tiSize.setOnTouchListener(this);
        itemDetailsBinding.tiQuantity.setOnTouchListener(this);
        itemDetailsBinding.tiDesc.setOnTouchListener(this);
        itemDetailsBinding.tiTags.setOnTouchListener(this);
    }

    private void setBlank() {
        itemDetailsBinding.tiProductName.setText(null);
        itemDetailsBinding.tiPrice.setText(null);
        itemDetailsBinding.tiSalePrice.setText(null);
        itemDetailsBinding.tiColor.setText(null);
        itemDetailsBinding.tiSize.setText(null);
        itemDetailsBinding.tiQuantity.setText(null);
        itemDetailsBinding.tiDesc.setText(null);
        itemDetailsBinding.tiTags.setText(null);
    }

    private void addProduct() {
        if (itemDetailsBinding.tiProductName.getText().toString().isEmpty()) {
            itemDetailsBinding.productName.setError("Product name cannot be blank");
            itemDetailsBinding.productName.setErrorEnabled(true);
        } else if (itemDetailsBinding.tiPrice.getText().toString().isEmpty()) {
            itemDetailsBinding.price.setError("Price cannot be blank");
            itemDetailsBinding.price.setErrorEnabled(true);
        } else if (itemDetailsBinding.tiSalePrice.getText().toString().isEmpty()) {
            itemDetailsBinding.salePrice.setError("Sale price cannot be blank");
            itemDetailsBinding.salePrice.setErrorEnabled(true);
        } else if (itemDetailsBinding.tiColor.getText().toString().isEmpty()) {
            itemDetailsBinding.color.setError("Color cannot be blank");
            itemDetailsBinding.color.setErrorEnabled(true);
        } else if (itemDetailsBinding.tiSize.getText().toString().isEmpty()) {
            itemDetailsBinding.size.setError("Size cannot be blank");
            itemDetailsBinding.size.setErrorEnabled(true);
        } else if (itemDetailsBinding.tiQuantity.getText().toString().isEmpty()) {
            itemDetailsBinding.quantity.setError("Quantity cannot be blank");
            itemDetailsBinding.quantity.setErrorEnabled(true);
        } else {
            if (isUpdate) {
                upDateProduct();
            } else {
                next();
            }
        }
    }

    private void upDateProduct() {
        Call<ResponseModel> call = ApiController.getInstance().getApi().updateProduct(
                product.getP_Id(),
                itemDetailsBinding.tiProductName.getText().toString().trim(),
                Double.valueOf(itemDetailsBinding.tiPrice.getText().toString().trim()),
                Double.valueOf(itemDetailsBinding.tiSalePrice.getText().toString().trim()),
                itemDetailsBinding.tiColor.getText().toString().trim(),
                itemDetailsBinding.tiSize.getText().toString().trim(),
                Integer.parseInt(itemDetailsBinding.tiQuantity.getText().toString().trim()),
                itemDetailsBinding.tiDesc.getText().toString().trim(),
                itemDetailsBinding.tiTags.getText().toString().trim()
        );

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void next() {
        product.setS_id(product.getS_id());
        product.setTitle(itemDetailsBinding.tiProductName.getText().toString().trim());
        product.setPrice(Double.valueOf(itemDetailsBinding.tiPrice.getText().toString().trim()));
        product.setSale_Price(Double.valueOf(itemDetailsBinding.tiSalePrice.getText().toString().trim()));
        product.setColor(itemDetailsBinding.tiColor.getText().toString().trim());
        product.setSize(itemDetailsBinding.tiSize.getText().toString().trim());
        product.setQuantity(Integer.parseInt(itemDetailsBinding.tiQuantity.getText().toString().trim()));
        product.setP_Desc(itemDetailsBinding.tiDesc.getText().toString().trim());
        product.setCategory(itemDetailsBinding.tiTags.getText().toString().trim());
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_main, new AddressFragment(product, false))
                .commitNow();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.ti_product_name:
                itemDetailsBinding.productName.setErrorEnabled(false);
                break;
            case R.id.ti_price:
                itemDetailsBinding.price.setErrorEnabled(false);
                break;
            case R.id.ti_sale_price:
                itemDetailsBinding.salePrice.setErrorEnabled(false);
                break;
            case R.id.ti_color:
                itemDetailsBinding.color.setErrorEnabled(false);
                break;
            case R.id.ti_size:
                itemDetailsBinding.size.setErrorEnabled(false);
                break;
            case R.id.ti_quantity:
                itemDetailsBinding.quantity.setErrorEnabled(false);
                break;
            case R.id.ti_tags:
                itemDetailsBinding.tags.setErrorEnabled(false);
                break;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_item_next:
                addProduct();

                break;
            case R.id.btn_item_reset:
                if (isUpdate) {
                    initViews();
                } else {
                    setBlank();
                }
                break;
        }

    }
}