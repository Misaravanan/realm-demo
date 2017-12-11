package com.shan.chathuranga.realm.activites;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.shan.chathuranga.realm.R;
import com.shan.chathuranga.realm.adapters.EventListAdapter;
import com.shan.chathuranga.realm.gson_models.EventParser;
import com.shan.chathuranga.realm.realm_models.ActorTable;
import com.shan.chathuranga.realm.realm_models.CommitTable;
import com.shan.chathuranga.realm.realm_models.EventTable;
import com.shan.chathuranga.realm.realm_models.RepoTable;
import com.shan.chathuranga.realm.services.EventDataCacheService;
import com.shan.chathuranga.realm.utility.NetworkStatus;
import com.shan.chathuranga.realm.utility.RecyclerViewDividerVertical;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.list)
    RecyclerView recyclerView;

    @BindView(R.id.parent_layout)
    ConstraintLayout parentLayout;

    private Retrofit retrofit;
    private DisposableSingleObserver<List<EventParser>> eventDisposableSingleObserver;
    private ArrayList<EventParser> eventParserDataList;
    private ArrayList<EventTable> eventTableDataList;
    private Realm realm;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String BASE_URL = "https://api.github.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        Stetho.initializeWithDefaults(this);

        initialization();
        checkNetworkConnectivity();
    }

    private void initialization() {

        eventParserDataList = new ArrayList<>();
        //eventTableDataList = new ArrayList<>();

        realm = Realm.getDefaultInstance();

        //FIXME : Remove client on ship from retrofit
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();


        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build();

    }

    private void checkNetworkConnectivity() {
        NetworkStatus status = new NetworkStatus(MainActivity.this);
        if (status.isOnline()) {
            getDataFromServer();
        } else {
            Snackbar snackbar = Snackbar
                    .make(parentLayout, "Showing Offline Data", Snackbar.LENGTH_LONG);
            snackbar.show();
            getDataFromDatabase();
        }
    }

    public void getDataFromServer() {

        EventService eventService = retrofit.create(EventService.class);
        Single<List<EventParser>> eventsAsSingle = eventService.getEvents();

        eventDisposableSingleObserver = eventsAsSingle
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<EventParser>>() {
                    @Override
                    public void onSuccess(@NonNull List<EventParser> eventParsers) {

                        realm.beginTransaction();
                        realm.delete(EventTable.class);
                        realm.delete(ActorTable.class);
                        realm.delete(RepoTable.class);
                        realm.delete(CommitTable.class);
                        realm.commitTransaction();

                        eventParserDataList.addAll(eventParsers);
                        EventListAdapter listAdapter = new EventListAdapter(MainActivity.this, eventParserDataList,null);
                        recyclerView.setAdapter(listAdapter);
                        recyclerView.addItemDecoration(new RecyclerViewDividerVertical(5));
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                        Intent intent = new Intent(MainActivity.this, EventDataCacheService.class);
                        intent.putParcelableArrayListExtra("event_data", eventParserDataList);
                        startService(intent);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "on Error : " + e.getMessage());
                    }
                });
    }

    private void getDataFromDatabase() {

        RealmResults<EventTable> events =  realm.where(EventTable.class).findAll();
        EventListAdapter listAdapter = new  EventListAdapter(this,null,events);
        listAdapter.getItemCount();
        recyclerView.setAdapter(listAdapter);
        recyclerView.addItemDecoration(new RecyclerViewDividerVertical(5));
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    private interface EventService {
        @GET("events")
        Single<List<EventParser>> getEvents();
    }


}
