package com.salikkim.seller.Fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.salikkim.seller.Adapters.ImageAdapter;
import com.salikkim.seller.Helper.ApiController;
import com.salikkim.seller.Helper.FileUtils;
import com.salikkim.seller.Interfaces.ImageClick;
import com.salikkim.seller.Models.Images;
import com.salikkim.seller.Models.ResponseModel;
import com.salikkim.seller.R;
import com.salikkim.seller.databinding.FragmentImagesBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImagesFragment extends Fragment implements View.OnClickListener, ImageClick {
    private FragmentImagesBinding imagesBinding;
    private String product_id;
    private boolean isUpdate;
    private ArrayList<Images> imagesLists = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private ActivityResultLauncher<Intent> imageActivityResultLauncher;

    public ImagesFragment(String product_id, boolean isUpdate) {
        this.product_id = product_id;
        this.isUpdate = isUpdate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imageActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Uri image = result.getData().getData();
                            String selectedImage = FileUtils.getPath(getActivity(), image);
                            addImage(selectedImage);
                        }
                    }
                });

        imagesBinding = FragmentImagesBinding.inflate(getLayoutInflater());
        View view = imagesBinding.getRoot();
        imagesBinding.btnAddImages.setOnClickListener(this);
        imagesBinding.btnDoneImages.setOnClickListener(this);
        imagesBinding.recViewImages.setHasFixedSize(true);
        imagesBinding.recViewImages.setLayoutManager(new GridLayoutManager(getContext(), 2));
        imageAdapter = new ImageAdapter(getContext(), imagesLists, this);
        imagesBinding.recViewImages.setAdapter(imageAdapter);
        if (isUpdate)
            getImages(product_id);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_images:
                pickImage();
                break;
            case R.id.btn_done_images:
                getActivity().finish();
                break;
        }
    }

    private void pickImage() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        } else {
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imageActivityResultLauncher.launch(i);
        }
    }

    private void addImage(String path) {
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setCancelable(false);
        pd.setMessage("Updating image, please wait...");
        pd.show();

        File file = new File(Uri.parse(path).getPath());
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part image = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        RequestBody p_id = RequestBody.create(MediaType.parse("text/plain"), product_id);
        Call<ResponseModel> call = ApiController.getInstance().getApi().addImage(image, p_id);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        getImages(product_id);
                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }

    private void getImages(String product_id) {
        Call<List<Images>> call = ApiController.getInstance().getApi().getImages(product_id);
        call.enqueue(new Callback<List<Images>>() {
            @Override
            public void onResponse(Call<List<Images>> call, Response<List<Images>> response) {
                if (response.body() != null) {
                    imagesLists.removeAll(imagesLists);
                    imagesLists.addAll(response.body());
                    imageAdapter.setImageLists(imagesLists);
                }
            }

            @Override
            public void onFailure(Call<List<Images>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDeleteClick(int position, Images images) {
        Call<ResponseModel> call = ApiController.getInstance().getApi().deleteImage(images.getI_Id(), images.getUrl());
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        imagesLists.remove(position);
                        imageAdapter.notifyItemRemoved(position);
                    }else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                //  Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}