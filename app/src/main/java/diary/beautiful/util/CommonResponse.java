package diary.beautiful.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class CommonResponse {
    private HashMap<String, String> propertyMap;
    private ArrayList<HashMap<String,String>>mapList;

    public CommonResponse(String responString){
        Log.d("Response",responString);
        propertyMap =new HashMap<>();
        mapList= new ArrayList<>();
    }
}
