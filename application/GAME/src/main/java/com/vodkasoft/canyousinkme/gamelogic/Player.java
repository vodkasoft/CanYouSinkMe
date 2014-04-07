package com.vodkasoft.canyousinkme.gamelogic;

import android.media.Image;

import java.util.UUID;

/**
 * Vodkasoft (R)
 * Created by jomarin on 4/5/14.
 */
public class Player {

    private Image avatar;
    private String displayName;
    private UUID id;
    private int rank;

    public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
