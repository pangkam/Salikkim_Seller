package com.salikkim.seller.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.salikkim.seller.Adapters.AddressAdapter;
import com.salikkim.seller.Adapters.ProductAdapter;
import com.salikkim.seller.Helper.ApiController;
import com.salikkim.seller.Interfaces.AddressClick;
import com.salikkim.seller.Interfaces.ProductClick;
import com.salikkim.seller.Models.Address;
import com.salikkim.seller.Models.Product;
import com.salikkim.seller.Models.ResponseModel;
import com.salikkim.seller.R;
import com.salikkim.seller.databinding.ActivityProductBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity implements ProductClick, AddressClick {
    private ActivityProductBinding productBinding;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private Dialog address_dialog;
    private String seller_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productBinding = ActivityProductBinding.inflate(getLayoutInflater());
        View view = productBinding.getRoot();
        setContentView(view);
        productBinding.toolbarProductActivity.setTitle("My Products");
        setSupportActionBar(productBinding.toolbarProductActivity);
        productBinding.toolbarProductActivity.setNavigationIcon(R.drawable.baseline_arrow_back);
        productBinding.toolbarProductActivity.setNavigationOnClickListener(v -> finish());
        seller_id = getIntent().getExtras().getString("seller_id");

        productBinding.recViewProduct.setHasFixedSize(true);
        productBinding.recViewProduct.setLayoutManager(new LinearLayoutManager(ProductActivity.this));
        productAdapter = new ProductAdapter(ProductActivity.this, productList, this);
        productBinding.recViewProduct.setAdapter(productAdapter);
        getProductLists(seller_id);

        address_dialog = new Dialog(this);
        address_dialog.setContentView(R.layout.available_address_dialog);
        address_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        address_dialog.setCancelable(true); //Optional
        address_dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        ImageView btn_close = address_dialog.findViewById(R.id.btn_add_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (address_dialog.isShowing())
                    address_dialog.dismiss();
            }
        });
    }

    private void getProductLists(String s_id) {
        Call<List<Product>> call = ApiController.getInstance().getApi().getProducts(s_id);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.body() != null) {
                    productList = new ArrayList<>();
                    productList.addAll(response.body());
                    productAdapter.setProductsList(productList);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(ProductActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSeeAddressClick(int position, Product product) {
        /*startActivity(new Intent(ProductActivity.this, AddProductActivity.class)
                .putExtra("edit", "address")
                .putExtra("seller_id", seller_id)
                .putExtra("product", product));*/


        address_dialog.show();
        try {
            getAvailableAddressLists(product.getAddress());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    private void getAvailableAddressLists(String address) throws JSONException {
        ArrayList<Address> available_addresses = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(address);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            Address dataSet = new Address();
            dataSet.setId(obj.getInt("Id"));
            dataSet.setName(obj.getString("Name"));
            dataSet.setCharge(obj.getDouble("Charge"));
            available_addresses.add(dataSet);

        }
        RecyclerView availableAddressRecycler = address_dialog.findViewById(R.id.see_address_recyclerView);
        availableAddressRecycler.setHasFixedSize(false);
        availableAddressRecycler.setLayoutManager(new LinearLayoutManager(address_dialog.getContext()));
        AddressAdapter addressAdapter = new AddressAdapter(ProductActivity.this, available_addresses, this, false);
        availableAddressRecycler.setAdapter(addressAdapter);
    }

    @Override
    public void onMenuItemClick(int position, MenuItem menuItem, Product products) {
        switch (menuItem.getItemId()) {
            case R.id.product_edit:
                startActivity(new Intent(ProductActivity.this, AddProductActivity.class)
                        .putExtra("edit", "item")
                        .putExtra("seller_id", seller_id)
                        .putExtra("product", products));
                break;
            case R.id.product_change_address:
                startActivity(new Intent(ProductActivity.this, AddProductActivity.class)
                        .putExtra("edit", "address")
                        .putExtra("seller_id", seller_id)
                        .putExtra("product", products));
                break;
            case R.id.product_edit_image:
                startActivity(new Intent(ProductActivity.this, AddProductActivity.class)
                        .putExtra("edit", "image")
                        .putExtra("seller_id", seller_id)
                        .putExtra("product", products));
                break;
            case R.id.product_delete:
                new AlertDialog.Builder(this)
                        .setTitle("Delete item")
                        .setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteProduct(position, products);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_delete)
                        .show();

        }
    }


    private void deleteProduct(int position, Product product) {
        Call<ResponseModel> call = ApiController.getInstance().getApi().deleteProduct(product.getP_Id());
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        productList.remove(position);
                        productAdapter.notifyItemRemoved(position);
                    }
                    Toast.makeText(ProductActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(ProductActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1, Menu.NONE, "Add item")
                .setIcon(R.drawable.baseline_add)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                startActivity(new Intent(ProductActivity.this, AddProductActivity.class)
                        .putExtra("edit", "add")
                        .putExtra("seller_id", seller_id)
                        .putExtra("product", new Product()));
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getProductLists(seller_id);
    }

    @Override
    public void onAddressClick(int position, Address address) {
        Toast.makeText(this, "Shipping charge: " + address.getCharge(), Toast.LENGTH_SHORT).show();
    }
}