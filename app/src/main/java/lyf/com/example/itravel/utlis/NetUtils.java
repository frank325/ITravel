package lyf.com.example.itravel.utlis;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/** 
 * 跟网络相关的工具类 
 *
 */  
public class NetUtils  
{  
    private NetUtils()  
    {
        throw new UnsupportedOperationException("cannot be instantiated");
    }  
  
    /** 
     * 判断网络是否连接
     */  
    public static boolean isConnected(Context context)
    {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
  
        if (null != connectivity)  
        {
            NetworkInfo info = connectivity.getActiveNetworkInfo();

            if (null != info && info.isConnected())  
            {  
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {  
                    return true;  
                }  
            }  
        }  
        return false;  
    }  
  
    /** 
     * 判断是否是wifi连接 
     */  
    public static boolean isWifi(Context context)
    {  
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
  
        if (cm == null)  
            return false;  
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }  
  
    /** 
     * 打开网络设置界面 
     */  
    public static void openSetting(Activity activity)
    {  
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");  
        intent.setComponent(cm);  
        intent.setAction("android.intent.action.VIEW");  
        activity.startActivityForResult(intent, 0);  
    }

    /**
     * 获取wifi的id
     */
    public static String getWifiSSid(Activity mActivity){
        WifiManager wifiMgr = (WifiManager) mActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int wifiState = wifiMgr.getWifiState();
        WifiInfo info = wifiMgr.getConnectionInfo();
        String wifiId = info != null ? info.getSSID() : null;
        return wifiId;
    }

    /**
     * 关闭wifi连接
     */
    public static void closeWif(Activity mActivity){
        WifiManager wifiMgr = (WifiManager) mActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiMgr.setWifiEnabled(false);
    }
}  
