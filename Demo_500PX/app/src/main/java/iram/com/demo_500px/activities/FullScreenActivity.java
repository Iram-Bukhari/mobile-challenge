package iram.com.demo_500px.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import iram.com.demo_500px.R;
import iram.com.demo_500px.adapters.FullScreenImageAdapter;
import iram.com.demo_500px.pojo.Photo;

public class FullScreenActivity extends AppCompatActivity {
    private static final int ANIM_DURATION = 600;
    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;
    private RelativeLayout mainlayout;
    private ArrayList<Photo> photos = new ArrayList<Photo>();

    private int thumbnailTop;
    private int thumbnailLeft;
    private int thumbnailWidth;
    private int thumbnailHeight;

    int position;

    ColorDrawable colorDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        viewPager = (ViewPager) findViewById(R.id.pager);
        mainlayout = (RelativeLayout) findViewById(R.id.mainlayout);


        Intent i = getIntent();
        position = i.getIntExtra("position", 0);
        Bundle bundle = getIntent().getExtras();
        colorDrawable = new ColorDrawable(0x000000);
        if (bundle != null) {

            thumbnailTop = bundle.getInt("top");
            thumbnailLeft = bundle.getInt("left");
            thumbnailWidth = bundle.getInt("width");
            thumbnailHeight = bundle.getInt("height");
            photos = bundle.getParcelableArrayList("photos");
        }

        adapter = new FullScreenImageAdapter(FullScreenActivity.this,
                photos);

        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(position);


    }

    @Override
    public void onBackPressed() {
        Intent resultData = new Intent();
        if (position != viewPager.getCurrentItem()) {
            resultData.putExtra("position", viewPager.getCurrentItem());
        }
        setResult(Activity.RESULT_OK, resultData);
        finish();
    }
}
