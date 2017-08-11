package lyf.com.example.itravel;

import android.app.Application;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import lyf.com.example.itravel.bean.User;
import okhttp3.OkHttpClient;

/**
 * 继承Application的类，在运行App时调用
 */

public class ITravelApplication extends Application {

    private static ITravelApplication iTravelApplication;
    private User user;

    @Override
    public void onCreate() {
        super.onCreate();

        iTravelApplication = ITravelApplication.this;
        initOkhttpUtils();
    }

    public static ITravelApplication getiTravelApplication() {
        return iTravelApplication;
    }

    /**
     * 将用户信息存放在此类，方便调用
     */
    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    /**
     * 初始化OkhttpUtils
     */
    private void initOkhttpUtils() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }
}
