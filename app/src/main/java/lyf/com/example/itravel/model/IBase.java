package lyf.com.example.itravel.model;

import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * 网络请求接口
 */
public interface IBase {

    void doPost(String url, Map<String, String> params, Callback callback);
    void doPostJSON(String url, String jsonStr, Callback callback);
    void doPostFile(String url, File file, HashMap<String,String> params, Callback callback);

    void doGet(String url, Map<String, String> params, Callback callback);
    void doPut(String url, RequestBody requestBody, Callback callback);
    
}
