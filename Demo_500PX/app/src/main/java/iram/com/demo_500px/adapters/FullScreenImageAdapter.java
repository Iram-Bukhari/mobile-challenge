package iram.com.demo_500px.adapters;
        import java.util.ArrayList;

        import android.app.Activity;
        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.support.v4.view.PagerAdapter;
        import android.support.v4.view.ViewPager;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.FrameLayout;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.RelativeLayout;
        import android.widget.ScrollView;
        import android.widget.TextView;

        import com.bumptech.glide.Glide;
        import com.bumptech.glide.load.engine.DiskCacheStrategy;

        import iram.com.demo_500px.R;
        import iram.com.demo_500px.pojo.Photo;

public class FullScreenImageAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<Photo> _imagePaths;
    private LayoutInflater inflater;

    // constructor
    public FullScreenImageAdapter(Activity activity,
                                  ArrayList<Photo> imagePaths) {
        this._activity = activity;
        this._imagePaths = imagePaths;
    }

    @Override
    public int getCount() {
        return this._imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgDisplay;
        TextView txtName,txtDesc;


        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);
        txtName = (TextView) viewLayout.findViewById(R.id.txtName);
        txtDesc = (TextView) viewLayout.findViewById(R.id.txtDescription);

        //Loading image from URL through Glide
        Glide.with(_activity)
                .load(_imagePaths.get(position).getImgurl())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgDisplay);
        String name = _imagePaths.get(position).getName();
        //Checking for untitled/empty values in name/description
        if(!name.equalsIgnoreCase("untitled")&&!name.equalsIgnoreCase("null")&&!name.equalsIgnoreCase("")&&name!=null){
            txtName.setText(name);
        }
        else{
            txtName.setVisibility(View.GONE);
        }
        String desc =_imagePaths.get(position).getDescription();
        if(!desc.equalsIgnoreCase("untitled")&&!desc.equalsIgnoreCase("null")&&!desc.equalsIgnoreCase("")&&desc!=null){
            txtDesc.setText(desc);
        }
        else{
            txtDesc.setVisibility(View.GONE);
        }

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

}
