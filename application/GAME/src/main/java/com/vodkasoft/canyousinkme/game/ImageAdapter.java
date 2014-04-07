package com.vodkasoft.canyousinkme.game;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

    private Context Context;
    private Integer[] IMGs;

    public ImageAdapter(Context C, Integer[] IMGs) {
        this.Context = C;
        this.IMGs = IMGs;
    }

    @Override
    public int getCount() {
        return IMGs.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(Context);
        imageView.setImageResource(IMGs[i]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(50,50));
        return imageView;
    }
}
