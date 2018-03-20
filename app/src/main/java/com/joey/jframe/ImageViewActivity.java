package com.joey.jframe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.joey.ui.general.BaseActivity;

/**
 * Created by fangxin on 2018/3/20.
 */

public class ImageViewActivity extends BaseActivity {
    private Button btnShowImage;
    private ImageView imgShowImage;

    @Override
    public void onBindView() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        imgShowImage = findViewById(R.id.img_show_image);
        btnShowImage = findViewById(R.id.btn_show_image);
        btnShowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage();
            }
        });

    }

    private void showImage() {
        String url = "http://d.hiphotos.baidu.com/image/pic/item/f9198618367adab45913c15e87d4b31c8601e4e8.jpg";
        Glide.with(this)
                .load(url)
                .thumbnail(0.1f)
                .placeholder(R.drawable.ic_launcher_background)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgShowImage);
    }
}
