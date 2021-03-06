package iram.com.demo_500px.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import iram.com.demo_500px.R;
import iram.com.demo_500px.extras.Keys;
import iram.com.demo_500px.pojo.Photo;

/**
 * Created by Iram on 2017-08-11.
 */

public class GridViewAdapter extends BaseAdapter{
    private Context mContext;

    private ArrayList<Photo> data = new ArrayList<Photo>();
    private LayoutInflater inflater ;
    private int resourceId;

    // Constructor
    public GridViewAdapter(Context c,int resourceId,ArrayList<Photo> img){
        this.resourceId = resourceId;
        mContext = c;
        data=img;
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    static class ViewHolder {
        ImageView image;
        TextView overlay;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if (row == null) {
            row = inflater.inflate(resourceId, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.image);
            holder.overlay = (TextView) row.findViewById(R.id.image_overlay);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        //Loading image from URL through Glide
        Glide.with(mContext)
                .load(data.get(position).getThumburl())
                .into(holder.image);

        // Adding an overlay for hiding adult content
        if(data.get(position).getCategory()== Keys.NUDE_CATEGORY){
            holder.overlay.setVisibility(View.VISIBLE);
        }
        else{
            holder.overlay.setVisibility(View.GONE);
           }
        return row;
    }
}
