package com.matrimony.vtest;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;


public class MainActivity extends ActionBarActivity {

    NetworkImageView mNetworkImageView;
    ImageView mImageView;
    ImageLoader mImageLoader;
    String url;
    LruCache<String,Bitmap> mMemoryCache;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNetworkImageView = (NetworkImageView)findViewById(R.id.networkImageView);
        mImageView = (ImageView)findViewById(R.id.imageView);

        final int maxMemory = (int)Runtime.getRuntime().maxMemory()/1024;
        final int cacheMemory = maxMemory / 8;
        mMemoryCache = new LruCache<String,Bitmap>(cacheMemory){

            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }

        };

        url = "http://thesource.com/wp-content/uploads/2015/02/fast-and-furious-7.jpg";
        RequestQueue mRequestQueue = MySingleton.getInstance(this).getRequestQueue();
        mImageLoader = MySingleton.getInstance(this).getImageLoader();
        //mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(
        //        LruBitmapCache.getCacheSize(this)));

        Bitmap bitmap = mImageLoader.get(url,mImageLoader.getImageListener(mImageView,R.mipmap.ic_launcher,R.mipmap.ic_launcher),100,100).getBitmap();

        //mImageView.setImageUrl(url,mImageLoader);
        mImageView.setImageBitmap(bitmap);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
}
