package com.p2jj.wesportif.Model;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.p2jj.wesportif.R;

public class Upload_img_CustomDialog extends Dialog implements View.OnClickListener {

    public Context activity;
    ImageView getImg_FromGallery;
    ImageView getImg_byCAmera;

    public Upload_img_CustomDialog(@NonNull Context context) {
        super(context);
        this.activity=context;
        sutupLayout();
    }

    public Upload_img_CustomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public void sutupLayout(){}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.upload_img_choice);

        /*getImg_FromGallery.findViewById(R.id.getImg_fromGallery);
        getImg_byCAmera.findViewById(R.id.getImg_cameraByCamera);


        getImg_FromGallery.setOnClickListener(this);
        getImg_byCAmera.setOnClickListener(this);*/

    }

    @Override
    public void onClick(View v) {

    }
}
