package com.example.hanifkf.lostandfound.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hanifkf.lostandfound.Activity.HomeActivity;
import com.example.hanifkf.lostandfound.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * Created by hanifkf on 24/02/2018.
 */

public class PostFragment extends Fragment{

    private static int IMG_CAMERA = 2;
    public static final int PICK_UP = 0;
    private static int REQUEST_CODE = 0;
    private ImageView Locationload;
    private TextView lokasi;

    //Database
    private String info = "";
    private Uri filepath;
    private Spinner spinner;
    private ImageView imagePost, startPost;
    private RadioGroup infobarang;
    private EditText namaEdit, deskripsiEdit, lokasiEdit;

    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private String current_user_id=null;
    private ProgressDialog progressDialog;
    private Bitmap compressedImageFile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_post,container,false);

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){

        }else{
            Toast.makeText(getActivity(), "No internet Connection", Toast.LENGTH_LONG).show();
        }

        //Database
        storageReference = FirebaseStorage.getInstance().getReference();
        startPost = (ImageView) view.findViewById(R.id.start_post);
        imagePost = (ImageView) view.findViewById(R.id.img_post);
        namaEdit = (EditText) view.findViewById(R.id.nama_barang_id);
        deskripsiEdit = (EditText) view.findViewById(R.id.deskripsi_id);
        lokasiEdit = (EditText) view.findViewById(R.id.lokasi_id);
        progressDialog = new ProgressDialog(getContext());

        storageReference=FirebaseStorage.getInstance().getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();

        Locationload=(ImageView)view.findViewById(R.id.open_location);
        lokasi=(TextView)view.findViewById(R.id.lokasi_id);

        //data spinner ambil dari string
        spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.kategori, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //menggunakan gallery
        imagePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(getContext(), PostFragment.this);
            }
        });

        //menggunakan lokasi
        Locationload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlaceAutoComplete(PICK_UP);
            }
        });

        //mengambil data dari radio button hilang / ditemukan
        infobarang=(RadioGroup)view.findViewById(R.id.infobarang);
        infobarang.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.ditemukan:
                        info="Ditemukan";
                        break;
                    case R.id.hilang:
                        info="Hilang";
                        break;
                }
            }
        });

        //mengklik button post
        startPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String randomName = UUID.randomUUID().toString();
                final String kategori = spinner.getSelectedItem().toString();
                final String namaBarang=namaEdit.getText().toString();
                final String deskripsi=deskripsiEdit.getText().toString();
                final String lokasi=lokasiEdit.getText().toString();
                if(spinner.getSelectedItem() == null || imagePost.getDrawable() == null || namaEdit.getText().equals("") || deskripsiEdit.getText().equals("") || lokasiEdit.getText().equals("")){
                    Toast.makeText(getActivity(), "Gagal membuat postingan baru, Masih terdapat data kosong", Toast.LENGTH_SHORT).show();
                }else{

                    progressDialog.setTitle("Posting");
                    progressDialog.show();

                    File newImageFile=new File(filepath.getPath());
                    try {
                        compressedImageFile=new Compressor(getContext())
                                .setMaxHeight(200)
                                .setMaxWidth(200)
                                .setQuality(10)
                                .compressToBitmap(newImageFile);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream baos=new ByteArrayOutputStream();
                    compressedImageFile.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    byte [] data=baos.toByteArray();

                    UploadTask uploadTask=storageReference.child("post_images").child(randomName+".jpg").putBytes(data);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storeFireStore(taskSnapshot,namaBarang,deskripsi,lokasi,kategori);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Penanganan erorr
                        }
                    });

                }

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth=FirebaseAuth.getInstance();
        current_user_id=firebaseAuth.getCurrentUser().getUid();

    }

    private void storeFireStore(UploadTask.TaskSnapshot taskSnapshot,String nama_barang,String deskripsi,String lokasi, String category) {

        Uri downloadthumbUri = null;
        if(taskSnapshot!=null){
            downloadthumbUri=taskSnapshot.getDownloadUrl();
        }
        Map<String,Object>postMap=new HashMap<>();
        postMap.put("nama_barang",nama_barang);
        postMap.put("deskripsi",deskripsi);
        postMap.put("lokasi",lokasi);
        postMap.put("category",category);
        postMap.put("info",info);
        postMap.put("image_url",downloadthumbUri.toString());
        postMap.put("user_id",current_user_id);
        postMap.put("timestamp",FieldValue.serverTimestamp());

        firebaseFirestore.collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Intent main=new Intent(getContext(), HomeActivity.class);
                    startActivity(main);
                    getActivity().finish();
                    Toast.makeText(getContext(),"Berhasil menyimpan ",Toast.LENGTH_SHORT).show();
                }else{
                    String error=task.getException().getMessage();
                    Toast.makeText(getContext(),"FireStore Eror "+error,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showPlaceAutoComplete(int typeLocation) {
        // isi RESUT_CODE tergantung tipe lokasi yg dipilih.
        // titik jmput atau tujuan
        REQUEST_CODE = typeLocation;

        // Filter hanya tmpat yg ada di Indonesia
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setCountry("ID").build();
        try {
            // Intent untuk mengirim Implisit Intent
            Intent mIntent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(typeFilter)
                    .build(getActivity());
            // jalankan intent impilist
            startActivityForResult(mIntent, REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace(); // cetak error
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace(); // cetak error
            // Display Toast
            Toast.makeText(getContext(), "Layanan Play Services Tidak Tersedia", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == RESULT_OK ){

                Log.d(TAG, "onActivityResult: "+resultCode+" "+requestCode);
                // Tampung Data tempat ke variable
                Place placeData = PlaceAutocomplete.getPlace(getContext(), data);

                if (requestCode==REQUEST_CODE){
                    // Show in Log Cat
                    Log.d("autoCompletePlace Data", placeData.toString());

                    // Dapatkan Detail
                    String placeAddress = placeData.getAddress().toString();
                    LatLng placeLatLng = placeData.getLatLng();
                    String placeName = placeData.getName().toString();

                    // Cek user milih titik jemput atau titik tujuan
                    lokasi.setText(placeAddress);
                }
                else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {
                        filepath = result.getUri();
                        imagePost.setImageURI(filepath);
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result.getError();
                    }

                }
                else if (requestCode == IMG_CAMERA) {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    imagePost.setImageBitmap(thumbnail);
                }

            }
        } catch (Exception e) {
            Log.d(TAG, "onActivityResult: "+e.toString());
            Toast.makeText(getContext(), "Silahkan coba lagi", Toast.LENGTH_LONG).show();
        }
    }

}
