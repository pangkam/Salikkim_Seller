package com.salikkim.seller.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Html;
import android.text.InputType;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.salikkim.seller.Adapters.AddressAdapter;
import com.salikkim.seller.Helper.ApiController;
import com.salikkim.seller.Interfaces.AddressClick;
import com.salikkim.seller.Models.Address;
import com.salikkim.seller.Models.Product;
import com.salikkim.seller.Models.ResponseModel;
import com.salikkim.seller.R;
import com.salikkim.seller.databinding.FragmentAddressBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressFragment extends Fragment implements View.OnClickListener, AddressClick {
    private FragmentAddressBinding addressBinding;
    private final Product product;
    private final boolean isUpdate;
    private AddressAdapter addressAdapter;
    private ArrayList<Address> addresses = new ArrayList<>();


    public AddressFragment(Product product, boolean isUpdate) {
        // Required empty public constructor
        this.product = product;
        this.isUpdate = isUpdate;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addressBinding = FragmentAddressBinding.inflate(inflater, container, false);
        View view = addressBinding.getRoot();
        addressBinding.recViewAddress.setHasFixedSize(true);
        addressBinding.recViewAddress.setLayoutManager(new LinearLayoutManager(getContext()));
        addressAdapter = new AddressAdapter(getContext(), addresses, this, true);
        addressBinding.recViewAddress.setAdapter(addressAdapter);
        getAddressLists();
        addressBinding.btnAddressPrev.setOnClickListener(this);
        addressBinding.btnAddressNext.setOnClickListener(this);
        addressBinding.btnAddressReset.setOnClickListener(this);
        if (isUpdate) {
            addressBinding.btnAddressNext.setText("Save");
            addressBinding.btnAddressNext.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_done, 0);
            addressBinding.btnAddressPrev.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    private void setAddress() throws JSONException {
        JSONArray jsonArray = new JSONArray(product.getAddress());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            int id = obj.getInt("Id");
            double charge = obj.getDouble("Charge");
            for (int j = 0; j < addresses.size(); j++) {
                if (addresses.get(j).getId() == id) {
                    addresses.get(j).setCharge(charge);
                    addresses.get(j).setCheck(true);
                    addressAdapter.notifyItemChanged(j, addresses);
                }
            }
        }
    }

    private void getAddressLists() {
        addresses = new ArrayList<>();
        Call<List<Address>> call = ApiController.getInstance().getApi().getAddresses();
        call.enqueue(new Callback<List<Address>>() {
            @Override
            public void onResponse(Call<List<Address>> call, Response<List<Address>> response) {
                if (response.body() != null) {
                    addresses.addAll(response.body());
                    addressAdapter.setAddresses(addresses);
                    if (isUpdate && !product.getAddress().isEmpty()) {
                        try {
                            setAddress();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Address>> call, Throwable t) {
                //  Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_address_prev:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_main, new ItemDetailsFragment(product, false))
                        .commitNow();
                break;
            case R.id.btn_address_next:
              /*  getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_main, new ImagesFragment(""))
                        .commitNow();*/

                try {
                    if (!isUpdate) {
                        addItem();
                    } else {
                        update();
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                break;

            case R.id.btn_address_reset:
                getAddressLists();
        }
    }

    private void update() throws JSONException {
        JSONArray addressJsonArray = new JSONArray();
        for (int i = 0; i < addresses.size(); i++) {
            if (addresses.get(i).isCheck()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Id", addresses.get(i).getId());
                jsonObject.put("Name", addresses.get(i).getName());
                jsonObject.put("Charge", addresses.get(i).getCharge());
                addressJsonArray.put(jsonObject);
            }
        }
        String address = addressJsonArray.toString();
        if (!address.equals("[]")) {
            Call<ResponseModel> call = ApiController.getInstance().getApi().updateAddress(product.getP_Id(), address);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.body() != null) {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Please select address", Toast.LENGTH_SHORT).show();
        }
    }

    private void addItem() throws JSONException {
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Saving....");
        pd.show();

        JSONArray addressJsonArray = new JSONArray();
        for (int i = 0; i < addresses.size(); i++) {
            if (addresses.get(i).isCheck()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Id", addresses.get(i).getId());
                jsonObject.put("Name", addresses.get(i).getName());
                jsonObject.put("Charge", addresses.get(i).getCharge());
                addressJsonArray.put(jsonObject);
            }
        }
        String address = addressJsonArray.toString();
        if (!address.equals("[]")) {
            Call<ResponseModel> call = ApiController.getInstance().getApi().addProduct(
                    product.getS_id(),
                    product.getTitle(),
                    product.getPrice(),
                    product.getSale_Price(),
                    product.getColor(),
                    product.getSize(),
                    product.getQuantity(),
                    product.getP_Desc(),
                    product.getCategory(),
                    address);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.body() != null) {
                        if (response.body().getStatus()) {
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.container_main, new ImagesFragment(response.body().getMessage(), isUpdate))
                                    .commitNow();
                        } else {

                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        pd.dismiss();
                    }
                }


                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    pd.dismiss();
                    //  Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Please select address", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAddressClick(int position, Address address) {
        if (!addresses.get(position).isCheck()) {
            showInputDialog(position);
        } else {
            addresses.get(position).setCharge(0);
            addresses.get(position).setCheck(false);
            addressAdapter.notifyItemChanged(position);
        }
    }

    private void showInputDialog(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Shipping Charge");
        builder.setMessage(Html.fromHtml("Set shipping charge for: <b>" + addresses.get(pos).getName() + "<b>"));

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setWidth(display.getWidth());
        input.setHint("input shipping charge");

        final LinearLayout layout = new LinearLayout(getContext());
        layout.setPadding(60, 60, 60, 60);
        layout.addView(input);

        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!input.getText().toString().isEmpty()) {
                    addresses.get(pos).setCharge(Double.parseDouble(input.getText().toString().trim()));
                    addresses.get(pos).setCheck(true);
                    addressAdapter.notifyItemChanged(pos, addresses);
                } else {
                    Toast.makeText(getContext(), "Input shipping charge if not input zero(0)", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setIcon(R.drawable.ic_due);
        builder.show();

    }
}