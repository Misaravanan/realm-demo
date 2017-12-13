package ktt.com.retrofit_relam;

import android.content.Context;

import com.google.gson.Gson;

import io.reactivex.Observable;
import io.realm.Realm;


public class RealmDAO {
    private Realm realm;

    public RealmDAO(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    public void saveResponse(ResponsePojo data, String url) {
      //  LogUtil.printLogMessage("save_response", "start");
        Gson gson = new Gson();
        String response = gson.toJson(data);

        DataModel model = new DataModel();
        model.setUrl(url);
        model.setResponse(response);

        realm.beginTransaction();
        realm.insertOrUpdate(model);
        realm.commitTransaction();

      //  LogUtil.printLogMessage("save_response", "finished");
    }

    public Observable<ResponsePojo> getOfflineResponse(String key) {
       // LogUtil.printLogMessage("get_offline_response", key);
        DataModel model = realm.where(DataModel.class).equalTo("url", key).findFirst();
        if (model != null) {
            Gson gson = new Gson();
            ResponsePojo responsePojo = gson.fromJson(model.getResponse(), ResponsePojo.class);
            return Observable.just(responsePojo);
        }
        return Observable.just(new ResponsePojo());
    }
}


















