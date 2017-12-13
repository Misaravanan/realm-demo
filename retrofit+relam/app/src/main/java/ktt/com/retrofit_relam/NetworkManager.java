package ktt.com.retrofit_relam;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager {
    public static String BASE_URL = "http://www.mocky.io/v2/";

    public static Retrofit getRetrofit() {


       /* OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    LogUtil.printLogMessage("URL", chain.request().url().toString());
                    return chain.proceed(chain.request());
                })
                .build();*/
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
               // .client(client)
                .baseUrl(BASE_URL)
                .build();
        return retrofit;
    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null;
    }
}