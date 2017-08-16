package iram.com.demo_500px.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Key;
import java.util.ArrayList;
import iram.com.demo_500px.R;
import iram.com.demo_500px.adapters.GridViewAdapter;
import iram.com.demo_500px.extras.APIManager;
import iram.com.demo_500px.extras.Keys;
import iram.com.demo_500px.pojo.Photo;

public class PhotosActivity extends AppCompatActivity {
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private ArrayList<Photo> fetchedPhotos, mainPhotos;
    private RequestQueue mRequestQueue;
    ProgressDialog pDialog;
    static int CURRPAGE = 1;
    static int REQID = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpActionBar();
        setContentView(R.layout.activity_photos);

        gridView = (GridView) findViewById(R.id.gridView);
        mainPhotos = new ArrayList<Photo>();
        triggerVolleyRequest();

        CURRPAGE = 1;
        gridView.setOnScrollListener(new EndlessScrollListener());
    }
    /**
     * Set action bar attribues
     */
    void setUpActionBar() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.rsz_1logo);
        getSupportActionBar().setTitle("  Photos");
        getSupportActionBar().setDisplayUseLogoEnabled(true);

    }

    /**
     * This method triggers a string volley request to fetch response from /photos endpoint.
     */
    void triggerVolleyRequest() {
        final String TAG = "PhotosActivity";
        fetchedPhotos = new ArrayList<Photo>();
        fetchedPhotos.clear();
        String url = APIManager.API_BASE_URL + APIManager.GET_PHOTOS + APIManager.FEATURE + "fresh_today&" + APIManager.IMAGE_SIZE + "5,4,3,2&" + APIManager.PAGE + CURRPAGE + APIManager.CONSUMERKEY;

        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        pDialog = new ProgressDialog(PhotosActivity.this);
        pDialog.setMessage("Loading photos...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                parseJson(response.toString());

                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.dismiss();
            }
        });
        mRequestQueue.add(strReq);
    }

    /**
     * This methods parses the json response in the required Photo object type.
     *
     * @param jsonstr - contains json response in String format
     */
    void parseJson(String jsonstr) {
        try {
            JSONObject mainObject = new JSONObject(jsonstr);
            JSONArray photosArray = mainObject.getJSONArray("photos");

            for (int i = 0; i < photosArray.length(); i++) {
                Photo ph = new Photo();
                ph.setId(photosArray.getJSONObject(i).getInt(Keys.ID));
                ph.setName(photosArray.getJSONObject(i).getString(Keys.NAME));
                ph.setCategory(photosArray.getJSONObject(i).getInt(Keys.CATEGORY));
                ph.setDescription(photosArray.getJSONObject(i).getString(Keys.DESCRIPTION));
                ph.setThumburl(photosArray.getJSONObject(i).getJSONArray("images").getJSONObject(1).getString(Keys.URL));
                ph.setImgurl(photosArray.getJSONObject(i).getJSONArray("images").getJSONObject(2).getString(Keys.URL));
                fetchedPhotos.add(ph);
            }
            mainPhotos.addAll(fetchedPhotos);

            if (gridAdapter == null) {
                //Initializing the GridAdapter when API is called for the first time
                gridAdapter = new GridViewAdapter(PhotosActivity.this, R.layout.grid_item, mainPhotos);
                gridView.setAdapter(gridAdapter);
            } else {
                // Notifying the GridAdapter of the data changed for sequential API calls on scroll
                gridAdapter.notifyDataSetChanged();
            }
            // Launching FullscreenActivity on GridItem click
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Intent i = new Intent(PhotosActivity.this, FullScreenActivity.class);

                    i.putExtra("position", position);
                    ImageView imageView = (ImageView) v.findViewById(R.id.image);
                    //Sending Screen attributes for implementing animation
                    int[] screenLocation = new int[2];
                    imageView.getLocationOnScreen(screenLocation);
                    i.putExtra("left", screenLocation[0]).
                            putExtra("top", screenLocation[1]).
                            putExtra("width", imageView.getWidth()).
                            putExtra("height", imageView.getHeight());

                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("photos", mainPhotos);
                    i.putExtras(bundle);
                    startActivityForResult(i, REQID);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // ScrollListener to detect end of screen scroll event and triggering the volley request for next page
    public class EndlessScrollListener implements AbsListView.OnScrollListener {
        private int visibleThreshold = 5;
        private int previousTotal = 0;
        private boolean loading = true;

        public EndlessScrollListener() {
        }

        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;

                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                CURRPAGE++;
                triggerVolleyRequest();
                loading = true;
            }

        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }

    // Auto-scrolling the gridview to the position of item displayed in viewpager in FullScreen activity
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == REQID) {
            if (resultCode == RESULT_OK) {
                int position = data.getIntExtra("position", 0);
                if (position != 0) {
                    setMarksGridScrolling(position, 10);
                }
            }
        }
    }

    /**
     * Auto-scroll the gridview to supplied position with offset
     *
     * @param position integer position of the element
     * @param offset   offset from top of the screen
     */
    public void setMarksGridScrolling(final int position, final int offset) {
        gridView.setSelection(position);
        gridView.postDelayed(new Runnable() {
            @Override
            public void run() {
                gridView.smoothScrollToPositionFromTop(position, offset, 1);
            }
        }, 1);
    }
}
