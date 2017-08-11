package lyf.com.example.itravel.model;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import lyf.com.example.itravel.ITravelConstant;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 发送网络请求类
 */
public class OkhttpModel implements IBase {

    private final MediaType JSON = MediaType.parse("application /json; charset=utf-8");

    private String ROOT_PATH = ITravelConstant.URL;

    @Override
    public void doPost(String url, Map<String, String> params, Callback callback) {
        OkHttpUtils
                .post()
                .url(ROOT_PATH + url)
                .params(params)
                .build()
                .execute(callback);
    }

    @Override
    public void doPostJSON(String url, String jsonStr, Callback callback) {
        OkHttpUtils.postString()
                .url(ROOT_PATH + url)
                .content(jsonStr)
                .mediaType(JSON)
                .build()
                .execute(callback);
    }

    @Override
    public void doPostFile(String url, File file, HashMap<String,String> params, Callback callback) {
        OkHttpUtils
                .post()
                .addFile("mFile", params.get(0) + ".jpg", file)
                .url(ROOT_PATH + url)
                .params(params)
                .build()
                .execute(callback);
    }

    @Override
    public void doGet(String url, Map<String, String> params, Callback callback) {
        OkHttpUtils
                .get()
                .url(ROOT_PATH + url)
                .params(params)
                .build()
                .execute(callback);
    }

    @Override
    public void doPut(String url, RequestBody requestBody, Callback callback) {
        OkHttpUtils
                .put()
                .url(ROOT_PATH + url)
                .requestBody(requestBody)
                .build()
                .execute(callback);
    }
    
}
