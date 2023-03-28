package com.salikkim.seller.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.salikkim.seller.Adapters.OrdersAdapter;
import com.salikkim.seller.Helper.ApiController;
import com.salikkim.seller.Interfaces.OrderItemClick;
import com.salikkim.seller.Models.Orders;
import com.salikkim.seller.Models.ResponseModel;
import com.salikkim.seller.R;
import com.salikkim.seller.databinding.ActivityOrdersBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersActivity extends AppCompatActivity implements OrderItemClick {
    private List<Orders> ordersList = new ArrayList<>();
    private OrdersAdapter ordersAdapter;
    private ActivityOrdersBinding ordersBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ordersBinding = ActivityOrdersBinding.inflate(getLayoutInflater());
        View view = ordersBinding.getRoot();
        setContentView(view);
        ordersBinding.toolbarOrderActivity.setTitle("Orders");
        setSupportActionBar(ordersBinding.toolbarOrderActivity);
        ordersBinding.toolbarOrderActivity.setNavigationIcon(R.drawable.baseline_arrow_back);
        ordersBinding.toolbarOrderActivity.setNavigationOnClickListener(v -> finish());
        String seller_id = getIntent().getExtras().getString("seller_id");

        ordersAdapter = new OrdersAdapter(OrdersActivity.this, ordersList, this);
        ordersBinding.orderRecyclerView.setHasFixedSize(true);
        ordersBinding.orderRecyclerView.setLayoutManager(new LinearLayoutManager(OrdersActivity.this));
        ordersBinding.orderRecyclerView.setAdapter(ordersAdapter);
        getOrderLists(seller_id);
    }

    private void getOrderLists(String seller_id) {
        Call<List<Orders>> call = ApiController.getInstance().getApi().getOrders(String.valueOf(seller_id));
        call.enqueue(new Callback<List<Orders>>() {
            @Override
            public void onResponse(Call<List<Orders>> call, Response<List<Orders>> response) {
                if (!response.isSuccessful()) {
                    snackAlert("Code: " + response.code());
                    return;
                }
                if (response.body() != null) {
                    ordersList.addAll(response.body());
                    ordersAdapter.setOrdersList(ordersList);
                }
            }

            @Override
            public void onFailure(Call<List<Orders>> call, Throwable t) {
                snackAlert(t.getMessage());
            }
        });

    }

    private void snackAlert(String s) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), s, Snackbar.LENGTH_LONG)
                .setAction("Action", null);
        View view = snackbar.getView();
        view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.bright_background));
        snackbar.show();
    }

    @Override
    public void onImageClick(int position, Orders orders) {
        Toast.makeText(this, orders.getThumbnail(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMenuItemClick(int position, MenuItem menuItem, Orders order) {
        switch (menuItem.getItemId()) {
            case R.id.order_contact:
                Toast.makeText(this, "Contact", Toast.LENGTH_SHORT).show();
                break;
            case R.id.order_screenshot:
                Toast.makeText(this, "Screenshot", Toast.LENGTH_SHORT).show();
                break;
            case R.id.order_action:
                Toast.makeText(this, "Action", Toast.LENGTH_SHORT).show();
                break;
            case R.id.order_delete:
                new AlertDialog.Builder(this)
                        .setTitle("Delete order")
                        .setMessage("Are you sure you want to delete this order?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteOrder(position, order);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_delete)
                        .show();
                break;
        }
    }

    private void deleteOrder(int position, Orders order) {
        Toast.makeText(this, ""+order.getO_Id(), Toast.LENGTH_SHORT).show();
        Call<ResponseModel> call = ApiController.getInstance().getApi().deleteOrder(order.getO_Id());
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.body() != null) {
                    ResponseModel responseModel = response.body();
                    if (responseModel.getStatus()) {
                        ordersList.remove(position);
                        ordersAdapter.notifyItemRemoved(position);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                snackAlert(t.getMessage());
            }
        });
    }
}