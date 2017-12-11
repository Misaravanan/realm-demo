package com.shan.chathuranga.realm.realm_models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ChathurangaShan on 10/19/2017.
 */

public class ActorTable extends RealmObject {

    @PrimaryKey
    private int actorId;
    private String displayLogin;
    private String imagePath = "N/A";

    public int getActorId() {
        return actorId;
    }

    public String getDisplayLogin() {
        return displayLogin;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public void setDisplayLogin(String displayLogin) {
        this.displayLogin = displayLogin;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
