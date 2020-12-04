package com.p2jj.wesportif.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import com.p2jj.wesportif.Activities.MainActivity;
import com.p2jj.wesportif.BuildConfig;
import com.p2jj.wesportif.CustomEditProfileDialog;
import com.p2jj.wesportif.Data_Managers.RetrofitClient;

import com.p2jj.wesportif.Model.Session;
import com.p2jj.wesportif.Model.Upload_img_CustomDialog;
import com.p2jj.wesportif.Model.User;
import com.p2jj.wesportif.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment{

    private static final int GALLERY_REQUEST_CODE = 1;
    private static final int COVER_REQUEST_CODE = 3;
    private static final int CAMERA_REQUEST_CODE = 2;
    private String cameraFilePath;
    ImageView profil_img;
    ImageView getImgButton;
    Button depuisCamera;
    Button depuitGallery;
    RelativeLayout bigbackgraound;

    EditText imc_poids;
    EditText imc_taille;
    TextView imc_resultat;
    Button imc_calculer;
    ImageView back_button;
    ImageView coverImgButton;
    ImageView coverImage;
    Bitmap profileImgToUpload;
    Bitmap coverImgToUpload;
    ImageView editProfile;

    TextView userPhone;
    TextView userMail;
    TextView userDdn;
    TextView userUsername;
    TextView imcResultDesc;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;


    @Override
    public View onCreateView(LayoutInflater  inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewroot = inflater.inflate(R.layout.fragment_profile, container, false);

        profil_img = viewroot.findViewById(R.id.profileImg);
        bigbackgraound =viewroot.findViewById(R.id.big_background);
        depuisCamera =viewroot.findViewById(R.id.depuit_camera);
        depuitGallery=viewroot.findViewById(R.id.depuit_gallery);

        depuisCamera.setVisibility(View.INVISIBLE);
        depuitGallery.setVisibility(View.INVISIBLE);

        editProfile=viewroot.findViewById(R.id.profile_button_edit_mesInformation);
        coverImgButton=viewroot.findViewById(R.id.getCover_button);
        coverImage=viewroot.findViewById(R.id.coverPicture);

        userPhone=viewroot.findViewById(R.id.profile_user_phoneNumber);
        userMail=viewroot.findViewById(R.id.profile_userEmail);
        userDdn=viewroot.findViewById(R.id.profile_userDate_naissance);

        userUsername=viewroot.findViewById(R.id.editProfileUsername);
        String userName=Session.getInstance().getUser().getPrenom()+" "+Session.getInstance().getUser().getNom();
        userUsername.setText(userName);

        imcResultDesc=viewroot.findViewById(R.id.imcResultDesc);


        userPhone.setText(Session.getInstance().getUser().getNumTelephone()+"");
        userMail.setText(Session.getInstance().getUser().getEmail());
        userDdn.setText(Session.getInstance().getUser().getDate_naissance());

        getImgButton = viewroot.findViewById(R.id.getImg_button);
        getImgButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                depuisCamera.setVisibility(View.VISIBLE);
                depuitGallery.setVisibility(View.VISIBLE);
                bigbackgraound.setBackgroundResource(R.color.BackgroundChangeColor);


                if(depuisCamera.isClickable()){
                    depuisCamera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            depuisCamera.setVisibility(View.INVISIBLE);
                            depuitGallery.setVisibility(View.INVISIBLE);
                            bigbackgraound.setBackgroundResource(R.color.whiteTextColor);

                            //captureFromCamera();
                            checkPermission(1, Manifest.permission.CAMERA,CAMERA_PERMISSION_CODE);
                        }
                    });

                }if(depuitGallery.isClickable()){

                    depuitGallery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            depuisCamera.setVisibility(View.INVISIBLE);
                            depuitGallery.setVisibility(View.INVISIBLE);
                            bigbackgraound.setBackgroundResource(R.color.whiteTextColor);
                           // pickFromGallery();
                            checkPermission(0, Manifest.permission.WRITE_EXTERNAL_STORAGE,STORAGE_PERMISSION_CODE);
                        }
                    });
                }



            }
        });
        coverImgButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

               // depuisCamera.setVisibility(View.VISIBLE);
                //depuitGallery.setVisibility(View.VISIBLE);
                bigbackgraound.setBackgroundResource(R.color.BackgroundChangeColor);

               // pickFromGalleryCover();
                checkPermission(3, Manifest.permission.WRITE_EXTERNAL_STORAGE,STORAGE_PERMISSION_CODE);




            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomEditProfileDialog cepd=new CustomEditProfileDialog(getContext(),getFragmentManager().findFragmentById(R.id.big_background));
                cepd.show();

                Window window = cepd.getWindow();
                window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

                cepd.setCanceledOnTouchOutside(true);
            }
        });

        // calculer l'IMC

        imc_poids =viewroot.findViewById(R.id.IMC_poids);
        imc_taille=viewroot.findViewById(R.id.IMC_taille);
        imc_resultat=viewroot.findViewById(R.id.IMC_Result);
        imc_calculer=viewroot.findViewById(R.id.IMC_Calculer);

        imc_calculer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String heightStr = imc_taille.getText().toString();
                String weightStr = imc_poids.getText().toString();

                if (heightStr != null && !"".equals(heightStr)
                        && weightStr != null  &&  !"".equals(weightStr)) {
                    float heightValue = Float.parseFloat(heightStr) / 100;
                    float weightValue = Float.parseFloat(weightStr);

                    float bmi = weightValue / (heightValue * heightValue);

                    displayBMI(bmi);


                    postImc(heightValue,weightValue,bmi);


                }
            }
        });
        String url="http://wessporitf.eu-4.evennode.com/uploads/users/"+Session.getInstance().getUser().getImg_user();

        Glide.with(this).load(url).into(profil_img);

        url="http://wessporitf.eu-4.evennode.com/uploads/users/"+Session.getInstance().getUser().getCoverImg();

        Glide.with(this).load(url).into(coverImage);

        // back button

        return viewroot;
    }




    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }
    private void pickFromGalleryCover() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, COVER_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image

        Uri selectedImage;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor;
        int columnIndex;
        String imgDecodableString;
        File imgFile;

        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    //data.getData return the content URI for the selected Image
                    selectedImage = data.getData();
                    // Get the cursor
                    cursor = (getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null));
                    // Move to first row
                    cursor.moveToFirst();
                    //Get the column index of MediaStore.Images.Media.DATA
                    columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    //Gets the String value in the column
                    imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    // Set the Image in ImageView after decoding the String

                    profil_img.setImageBitmap( BitmapFactory.decodeFile(imgDecodableString));


                    imgFile = new  File(imgDecodableString);
                    if(imgFile.exists()){
                        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());



                        uploadImage(bitmap,Session.getInstance().getUser().getCin(),"profile");


                    }
                    break;
                case COVER_REQUEST_CODE:
                    //data.getData return the content URI for the selected Image
                    //data.getData return the content URI for the selected Image
                    selectedImage = data.getData();
                    // Get the cursor
                    cursor = (getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null));
                    // Move to first row
                    cursor.moveToFirst();
                    //Get the column index of MediaStore.Images.Media.DATA
                    columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    //Gets the String value in the column
                    imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    // Set the Image in ImageView after decoding the String




                    imgFile = new  File(imgDecodableString);
                    if(imgFile.exists()){
                        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        coverImage.setImageBitmap(bitmap);
                        uploadImage(bitmap,Session.getInstance().getUser().getCin(),"cover");
                    }
                    break;

                case CAMERA_REQUEST_CODE:
                    profil_img.setImageURI(Uri.parse(cameraFilePath));
                    break;
            }


    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //This is the directory in which the file will be created. This is the default location of Camera photos
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for using again
        cameraFilePath = "file://" + image.getAbsolutePath();
        return image;
    }


    private void captureFromCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }




    private void displayBMI(float bmi) {
        String bmiLabel = "";

        if (Float.compare(bmi, 16.5f) <= 0) {
            bmiLabel = getString(R.string.famine);
            imcResultDesc.setTextColor(Color.parseColor("#99B898"));
        } else if (Float.compare(bmi, 16.5f) > 0  &&  Float.compare(bmi, 18.5f) <= 0) {
            bmiLabel = getString(R.string.maigreur);
            imcResultDesc.setTextColor(Color.parseColor("#FECEAB"));
        } else if (Float.compare(bmi, 18.5f) > 0  &&  Float.compare(bmi, 25f) <= 0) {
            bmiLabel = getString(R.string.Corpulance_normal);
            imcResultDesc.setTextColor(Color.parseColor("#FF847C"));
        } else if (Float.compare(bmi, 25f) > 0  &&  Float.compare(bmi, 30f) <= 0) {
            bmiLabel = getString(R.string.Surpoids);
            imcResultDesc.setTextColor(Color.parseColor("#E84A5F"));
        } else if (Float.compare(bmi, 30f) > 0  &&  Float.compare(bmi, 35f) <= 0) {
            bmiLabel = getString(R.string.Obésité_modérée);
            imcResultDesc.setTextColor(Color.parseColor("#C06C84"));
        } else if (Float.compare(bmi, 35f) > 0  &&  Float.compare(bmi, 40f) <= 0) {
            bmiLabel = getString(R.string.Obésité_severe);
            imcResultDesc.setTextColor(Color.parseColor("#6C5B7B"));
        } else {
            bmiLabel = getString(R.string.obésité_morbide_ou_massive);
            imcResultDesc.setTextColor(Color.parseColor("#355C7D"));
        }


        imc_resultat.setText(bmi+"");
        imcResultDesc.setText(bmiLabel);
    }

    public void postImc(float height,float weight,float imc)
    {
        Call call = RetrofitClient
                .getInstance()
                .getApi_service_node()
                .addImc(height,weight,imc,Session.getInstance().getUser().getCin());
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                try {

                    JSONObject jsonResp=new JSONObject(new Gson().toJson(response.body()));
                    String msg=jsonResp.getString("userInformations");
                    if(msg.equals("User Imc added"))
                    {
                        Toast.makeText(getContext(), "Imc Added", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Erreur", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void uploadImage(Bitmap mBitmap, String cin,String target) {
        try {

            if (mBitmap != null) {
                File filesDir = getContext().getFilesDir();

                File file = new File(filesDir, "image" + ".png");

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                byte[] bitmapdata = bos.toByteArray();


                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();


                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
                RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");



                if(target.equals("profile"))
                {

                    Call call = RetrofitClient
                            .getInstance()
                            .getApi_service_node()
                            .uploadImageUser(body, name, cin);
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {

                            try {

                                updateSession();


                                Toast.makeText(getContext(), "Profile Image uploaded", Toast.LENGTH_LONG).show();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {

                            Toast.makeText(getContext(), "Erreur uploading Image", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else
                {
                    Call call = RetrofitClient
                            .getInstance()
                            .getApi_service_node()
                            .uploadCoverUser(body, name, cin);
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {

                            try {

                                updateSession();

                                Toast.makeText(getContext(), "Cover Image uploaded", Toast.LENGTH_LONG).show();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {

                            Toast.makeText(getActivity(), "Erreur uploading Image", Toast.LENGTH_LONG).show();
                        }
                    });
                }




            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void updateSession()
    {
        Call call = RetrofitClient
                .getInstance()
                .getApi_service_node()
                .GetUserDetails(Session.getInstance().getUser().getCin());
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                try {

                    JSONObject jsonResp=new JSONObject(new Gson().toJson(response.body()));
                    String msg=jsonResp.getString("userInformations");

                    if(msg.equals("User retrieved")){
                        JSONObject userJson=jsonResp.getJSONObject("user");

                        User currentUser=new User();
                        try {
                            String cin = userJson.getString("cin");
                            String nom=userJson.getString("nom");
                            String prenom=userJson.getString("prenom");
                            String email=userJson.getString("email");
                            int numTel=userJson.getInt("numTel");
                            String ddn=userJson.getString("ddn");
                            String img=userJson.getString("img");
                            String role=userJson.getString("role");
                            String coverImg=userJson.getString("coverImg");

                            currentUser.setCin(cin);
                            currentUser.setNom(nom);
                            currentUser.setPrenom(prenom);
                            currentUser.setEmail(email);
                            currentUser.setNumTelephone(numTel);
                            currentUser.setDate_naissance(ddn);
                            currentUser.setImg_user(img);
                            currentUser.setRole(role);
                            currentUser.setCoverImg(coverImg);

                            Session.getInstance().setUser(currentUser);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {

                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }



    public void checkPermission(int TAG ,String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(getContext(), permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] { permission },
                    requestCode);
        }
        else {
            Toast.makeText(getContext(),
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();

            pickFromGallery();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(),
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(getContext(),
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(),
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(getContext(),
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


}