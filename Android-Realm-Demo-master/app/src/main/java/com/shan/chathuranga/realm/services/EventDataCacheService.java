package com.shan.chathuranga.realm.services;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.shan.chathuranga.realm.gson_models.ActorParser;
import com.shan.chathuranga.realm.gson_models.CommitParser;
import com.shan.chathuranga.realm.gson_models.EventParser;
import com.shan.chathuranga.realm.gson_models.RepoParser;
import com.shan.chathuranga.realm.realm_models.ActorTable;
import com.shan.chathuranga.realm.realm_models.CommitTable;
import com.shan.chathuranga.realm.realm_models.EventTable;
import com.shan.chathuranga.realm.realm_models.RepoTable;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class EventDataCacheService extends IntentService {

    private static final String TAG = EventDataCacheService.class.getSimpleName();

    public EventDataCacheService() {
        super("EventDataCacheService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            ArrayList<EventParser> eventParserData = intent.getParcelableArrayListExtra("event_data");
            Realm realm = Realm.getDefaultInstance();

            for (EventParser eventParserObj : eventParserData) {

                realm.beginTransaction();

                //Save actorParser data
                ActorParser actorParser = eventParserObj.getActorParser();
                ActorTable actorTable = new ActorTable();
                actorTable.setActorId(actorParser.getId());
                actorTable.setDisplayLogin(actorParser.getDisplayLogin());
                actorTable.setImagePath(actorParser.getAvatarUrl());
                realm.insertOrUpdate(actorTable);

                //Save repoParser data
                RepoParser repoParser = eventParserObj.getRepoParser();
                RepoTable repoTable = new RepoTable();
                repoTable.setRepoId(repoParser.getId());
                repoTable.setName(repoParser.getName());
                realm.insertOrUpdate(repoTable);

                // Save Event data
                EventTable eventTable = new EventTable();
                Long aLong = Long.valueOf(eventParserObj.getId());
                eventTable.setEventId(aLong);
                eventTable.setType(eventParserObj.getType());
                eventTable.setActor(actorTable);
                eventTable.setRepo(repoTable);

                //Save commit data
                List<CommitParser> commitParsers = eventParserObj.getPayloadParser()
                        .getCommitParsers();

                RealmList<CommitTable> commitCollection = new RealmList<>();

                for (CommitParser commitObj : commitParsers) {
                    //Number maxId = realm.where(CommitTable.class).max("commitId");
                    //int commitPrimaryKey = (maxId != null) ? maxId.intValue() + 1 : 0;

                    CommitTable commitTable = new CommitTable();
                    commitTable.setCommitId();
                    commitTable.setMessage(commitObj.getMessage());
                    commitTable.setDistinct(commitObj.getDistinct());
                    realm.insertOrUpdate(commitTable);

                    commitCollection.add(commitTable);
                }

                eventTable.setCommits(commitCollection);
                realm.insertOrUpdate(eventTable);

                realm.commitTransaction();

                /*RealmResults<EventTable> events =  realm.where(EventTable.class).findAll();
                Log.d(TAG,"result size: "+events.size());*/

            }

            realm.close();

        }
    }

    public void downloadImages(String imageURL, final int actorId) {

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File folder = new File(sd, "/eventCacheImages/");
                if (!folder.exists()) {
                    if (!folder.mkdir()) {
                        Log.e("ERROR", "Cannot create a directory!");
                    } else {
                        folder.mkdir();
                    }
                }

                File imageFile = new File(folder, String.valueOf(actorId));
                /*try {

                    FileOutputStream outputStream = new FileOutputStream(imageFile);
                    boolean compress = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.close();
                    if(compress){
                        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                        ActorTable actorTable = new ActorTable();
                        actorTable.setActorID(actorId);
                        actorTable.setImagePath(imageFile.getPath());
                        try {
                            dbHelper.createOrUpdate(actorTable);
                            dbHelper.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(getApplicationContext()).load(imageURL).into(target);
    }
}
