package com.salikkim.seller.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.salikkim.seller.Adapters.PaymentAdapter;
import com.salikkim.seller.Models.Payment;
import com.salikkim.seller.R;
import com.salikkim.seller.databinding.ActivityPaymentBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    private ActivityPaymentBinding paymentBinding;
    private List<Payment> paymentList = new ArrayList<>();
    private PaymentAdapter paymentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paymentBinding = ActivityPaymentBinding.inflate(getLayoutInflater());
        View view = paymentBinding.getRoot();
        setContentView(view);
        paymentBinding.toolbarPaymentActivity.setTitle("Add UPI Payments");
        setSupportActionBar(paymentBinding.toolbarPaymentActivity);
        paymentBinding.toolbarPaymentActivity.setNavigationIcon(R.drawable.baseline_arrow_back);
        paymentBinding.toolbarPaymentActivity.setNavigationOnClickListener(v -> finish());
        String seller_id = getIntent().getExtras().getString("seller_id");
        String payments = getIntent().getExtras().getString("payments");
        paymentAdapter = new PaymentAdapter(PaymentActivity.this, paymentList);
        paymentBinding.viewPagerPayment.setAdapter(paymentAdapter);
        try {
            getPaymentLists(payments);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void getPaymentLists(String payments) throws JSONException {
        JSONArray jsonArray = new JSONArray(payments);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String mode = obj.getString("Mode");
            String id = obj.getString("Id");
            String number = obj.getString("Number");
            String screenshot = obj.getString("Img");
            paymentList.add(new Payment(screenshot, id, number, mode));
        }
        paymentAdapter.notifyDataSetChanged();
        TabLayoutMediator layoutMediator = new TabLayoutMediator(paymentBinding.paymentTaLayout, paymentBinding.viewPagerPayment, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (paymentList.get(position).getMode()) {
                    case "GooglePay":
                        tab.setIcon(R.drawable.gpay);
                        break;
                    case "Paytm":
                        tab.setIcon(R.drawable.paytm);
                        break;
                    case "PhonePe":
                        tab.setIcon(R.drawable.phonepe);
                        break;
                }
            }
        });
        layoutMediator.attach();
    }
}