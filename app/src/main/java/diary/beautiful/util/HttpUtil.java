package diary.beautiful.util;

import java.util.Iterator;
import java.util.LinkedHashMap;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {
    public static final MediaType JSON = MediaType.parse("application/json;chaeset=utf-8");
    private static OkHttpClient client = new OkHttpClient();


    /**
     * 无参数的get请求
     *
     * @param address
     * @param callback
     */
    public static void get(String address, Callback callback) {
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 带map参数的get请求
     */
    public static void get(String address, LinkedHashMap<String, String> params, Callback callback) {
        address = attachHttpGetParams(address, params);
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }


    private static String attachHttpGetParams(String url, LinkedHashMap<String, String> params) {
        Iterator<String> keys = params.keySet().iterator();
        Iterator<String> values = params.values().iterator();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("?");

        for (int i = 0; i < params.size(); i++) {
            stringBuffer.append(keys.next() + "=" + values.next());
            if (i != params.size() - 1) {
                stringBuffer.append("&");
            }
        }
        System.out.println("url:" + url + stringBuffer.toString());
        return url + stringBuffer.toString();
    }

    public static void post(String address, LinkedHashMap<String, String> params, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        Iterator<String> keys = params.keySet().iterator();
        Iterator<String> values = params.values().iterator();
        for (int i = 0; i < params.size(); i++) {
            builder.add(keys.next(), values.next());
            System.out.println(i);
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void post(String address,String json ,Callback callback){
        RequestBody requestBody =RequestBody.create(JSON,json);
        Request request =new Request.Builder().url(address).post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }


}
