package com.p2jj.wesportif;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class ImageHandler extends AppCompatActivity {

    ImageView img;

    private static final int GALLERY_REQUEST_CODE = 101;

    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent 
        startActivityForResult(intent,GALLERY_REQUEST_CODE);


    }


    protected void onactivityResult(int requestCode, int resultCode, @Nullable Intent data) {

            // Result code is RESULT_OK only if the user selects an Image
            if (resultCode == Activity.RESULT_OK)
                switch (requestCode) {
                    case GALLERY_REQUEST_CODE:
                        //data.getData returns the content URI for the selected Image
                        Uri selectedImage = data.getData();
                        img.setImageURI(selectedImage);
                        break;
                }

        }

}
