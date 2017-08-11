package lyf.com.example.itravel.utlis;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import lyf.com.example.itravel.bean.City;
import lyf.com.example.itravel.bean.Scenic;
import lyf.com.example.itravel.bean.TravelNotes;
import lyf.com.example.itravel.bean.User;

/**
 * Created by Administrator on 2017/8/5.
 */

public class JsonAnalysisUtils {

    public static boolean isSuccess = false;

    public static List<City> parseAllCityJson(String jsonData) {
        try {
            isSuccess = false;
            Gson gson = new Gson();
            List<City> citys = gson.fromJson(jsonData, new TypeToken<List<City>>(){}.getType());
            Log.i("Json", citys.toString());
            isSuccess = true;
            return citys;
        } catch (Exception e) {
            e.printStackTrace();
        }
        isSuccess = false;
        return null;
    }

    public static List<TravelNotes> parseAllTravelNotesJson(String jsonData) {
        try {
            isSuccess = false;
            Gson gson = new Gson();
            List<TravelNotes> travelNotes = gson.fromJson(jsonData, new TypeToken<List<TravelNotes>>(){}.getType());
            Log.i("Json", travelNotes.toString());
            isSuccess = true;
            return travelNotes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        isSuccess = false;
        return null;
    }

    public static TravelNotes parseTravelNotesJson(String jsonData) {
        try {
            isSuccess = false;
            Gson gson = new Gson();
            TravelNotes travelNotes = gson.fromJson(jsonData, TravelNotes.class);
            Log.i("Json", travelNotes.toString());
            isSuccess = true;
            return travelNotes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        isSuccess = false;
        return null;
    }

    public static City parseCityJson(String jsonData) {
        try {
            isSuccess = false;
            Gson gson = new Gson();
            City city = gson.fromJson(jsonData, City.class);
            Log.i("Json", city.toString());
            isSuccess = true;
            return city;
        } catch (Exception e) {
            e.printStackTrace();
        }
        isSuccess = false;
        return null;
    }

    public static User parseUserJson(String jsonData) {
        try {
            isSuccess = false;
            Gson gson = new Gson();
            User user = gson.fromJson(jsonData, User.class);
            Log.i("Json", user.toString());
            isSuccess = true;
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        isSuccess = false;
        return null;
    }

    public static List<Scenic> parseCityScenicJson(String jsonData) {
        try {
            isSuccess = false;
            Gson gson = new Gson();
            List<Scenic> scenics = gson.fromJson(jsonData, new TypeToken<List<Scenic>>(){}.getType());
            Log.i("Json", scenics.toString());
            isSuccess = true;
            return scenics;
        } catch (Exception e) {
            e.printStackTrace();
        }
        isSuccess = false;
        return null;
    }

}
