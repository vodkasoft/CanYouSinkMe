package com.vodkasoft.canyousinkme.game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.vodkasoft.canyousinkme.dataaccess.model.User;

import java.util.List;

/**
 * Vodkasoft (R)
 * Created by jomarin on 5/4/14.
 */
public class LeaderboardAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    List<User> users;

    public LeaderboardAdapter(Context mainActivity, List<User> pUsers) {
        // TODO Auto-generated constructor stub
        this.users = pUsers;
        context = mainActivity;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return users.size();
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
        holder.playerId = (TextView) rowView.findViewById(R.id.textViewPlayerId);
        holder.profilePicture = (ProfilePictureView) rowView.findViewById(R.id.imageView1);
        holder.rank = (TextView) rowView.findViewById(R.id.textViewRank);
        holder.score = (TextView) rowView.findViewById(R.id.textViewScore);

        holder.playerId.setText(users.get(position).getmId());
        holder.profilePicture.setProfileId(users.get(position).getmId());
        holder.rank.setText("Rank: " + String.valueOf(users.get(position).getRank()));
        holder.score.setText("Points: " + String.valueOf(users.get(position).getExperience()));
        return rowView;
    }

    public class Holder {
        ProfilePictureView profilePicture;
        TextView playerId;
        TextView score;
        TextView rank;
    }
}
