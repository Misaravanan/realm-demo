package ktt.com.retrofit_relam;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class DataModel extends RealmObject {
    @PrimaryKey
    String url;

    String response;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
