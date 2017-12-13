package ktt.com.retrofit_relam;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * Created by misaravanan on 13/12/17.
 */

interface ApiService {

    @POST("5a25878e2e0000392aa90676")
    Observable<ResponsePojo> getDataFromAPI();
}