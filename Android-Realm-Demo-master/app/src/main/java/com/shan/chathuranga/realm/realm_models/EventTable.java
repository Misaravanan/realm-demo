package com.shan.chathuranga.realm.realm_models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ChathurangaShan on 10/19/2017.
 */

public class EventTable extends RealmObject {

    @PrimaryKey
    private long eventId;
    private String type;
    private ActorTable actor;
    private RepoTable repo;
    private RealmList<CommitTable> commits;


    public long getEventId() {
        return eventId;
    }

    public String getType() {
        return type;
    }

    public ActorTable getActor() {
        return actor;
    }

    public RepoTable getRepo() {
        return repo;
    }

    public RealmList<CommitTable> getCommits() {
        return commits;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setActor(ActorTable actor) {
        this.actor = actor;
    }

    public void setRepo(RepoTable repo) {
        this.repo = repo;
    }

    public void setCommits(RealmList<CommitTable> commits) {
        this.commits = commits;
    }
}
