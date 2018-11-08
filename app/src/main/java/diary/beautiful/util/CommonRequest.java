package diary.beautiful.util;


import org.json.JSONObject;

import java.util.HashMap;

public class CommonRequest {
    private HashMap<String, String> requestParam;

    public CommonRequest() {
        requestParam = new HashMap<>();
    }

    public void addRequestParam(String paramKey, String paramValue) {
        requestParam.put(paramKey, paramValue);
    }

    public String getJsonStr() {
        JSONObject object = new JSONObject(requestParam);
        return object.toString();
    }
}
