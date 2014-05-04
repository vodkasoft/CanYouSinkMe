package com.vodkasoft.canyousinkme.game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

/**
 * Vodkasoft (R)
 * Created by jomarin on 5/4/14.
 */
public class LeaderboardAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    String[] playerIds;

    public LeaderboardAdapter(Context mainActivity, String[] prgmNameList) {
        // TODO Auto-generated constructor stub
        playerIds = prgmNameList;
        context = mainActivity;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return playerIds.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_leaderboard_item, null);
        holder.tv = (TextView) rowView.findViewById(R.id.textView1);
        holder.profilePicture = (ProfilePictureView) rowView.findViewById(R.id.imageView1);
        holder.tv.setText(playerIds[position]);
        holder.profilePicture.setProfileId(playerIds[position]);

        return rowView;
    }

    public class Holder {
        ProfilePictureView profilePicture;
        TextView tv;
    }
}
