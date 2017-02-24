package com.tip.capstone.mlearning.ui.lesson.detail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.helper.ResourceHelper;
import com.tip.capstone.mlearning.helper.TouchImageView;

public class ZoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

        TouchImageView touchImageView = (TouchImageView) findViewById(R.id.imageView);
        String pic = getIntent().getStringExtra("pic");
       // Bitmap icon = BitmapFactory.decodeResource(this.getResources(), ResourceHelper.getDrawableResourceId(this, pic));
        Glide.with(this)
                .load(ResourceHelper.getDrawableResourceId(this, pic))
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.ic_photo)
                .error(R.drawable.ic_photo)
                .into(touchImageView);
    }
}
