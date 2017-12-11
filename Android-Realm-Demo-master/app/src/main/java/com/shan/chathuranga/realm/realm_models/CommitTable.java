package com.shan.chathuranga.realm.realm_models;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ChathurangaShan on 10/19/2017.
 */

public class CommitTable extends RealmObject {

    @PrimaryKey
    private String commitId;
    private String message;
    private boolean distinct;

    public String getCommitId() {
        return commitId;
    }

    public String getMessage() {
        return message;
    }

    public boolean getDistinct() {
        return distinct;
    }

    public void setCommitId() {
        this.commitId = UUID.randomUUID().toString();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }
}
