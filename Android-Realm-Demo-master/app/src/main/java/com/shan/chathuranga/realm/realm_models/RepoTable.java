package com.shan.chathuranga.realm.realm_models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ChathurangaShan on 10/19/2017.
 */

public class RepoTable extends RealmObject {

    @PrimaryKey
    private long repoId;
    private String name;

    public long getRepoId() {
        return repoId;
    }

    public String getName() {
        return name;
    }

    public void setRepoId(long repoId) {
        this.repoId = repoId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
