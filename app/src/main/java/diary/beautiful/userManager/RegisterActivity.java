package diary.beautiful.userManager;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import diary.beautiful.R;
import diary.beautiful.model.RegisterResult;
import diary.beautiful.util.CommonRequest;
import diary.beautiful.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by zfz on 2018/11/3.
 */

public class RegisterActivity extends Activity {

    private Button register_bt;
    private EditText userName_et;
    private EditText email_et;
    private EditText password_et;
    private EditText password_et2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initComponents();
        setListeners();
    }

    void initComponents() {
        register_bt = findViewById(R.id.register_bt);
        userName_et = findViewById(R.id.username);
        email_et = findViewById(R.id.email);
        password_et = findViewById(R.id.et_password);
        password_et2 = findViewById(R.id.et_password2);
    }

    void setListeners() {
        register_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
                //toast("test");
            }
        });

    }

    private void register() {
        CommonRequest request = new CommonRequest();
        String email = email_et.getText().toString();
        String userName = userName_et.getText().toString();
        String password = password_et.getText().toString();
        String passwordConfirm = password_et2.getText().toString();
        String checkMsg = checkDataValid(email, userName, password, passwordConfirm);
        if (!checkMsg.equals("ok")) {
            return;
        }
        request.addRequestParam("email", email);
        request.addRequestParam("userName", userName);
        request.addRequestParam("password", password);

        HttpUtil.post("http://192.168.31.49:4396/user/register", request.getJsonStr(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                toast("NetWork error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                String registerResult = response.body().string();
                //toast(registerResult);
                RegisterResult result = gson.fromJson(registerResult, RegisterResult.class);
                if (result == null) {
                    toast("注册失败");
                }
                if (result.getResult().equals("true")) {
                    toast("success");
                } else {
                    toast(result.getMsg());
                }


            }
        });

    }

    //todo 改成 void，直接刷新界面，提示用户错误信息；
    private String checkDataValid(String email, String userName, String password, String passwordConfirm) {
//        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)
//                || TextUtils.isEmpty(passwordConfirm)) {
//
//            return "信息不能为空";
//        }
        if (TextUtils.isEmpty(email)) {
            email_et.setError("邮箱不能为空");
        }
        if (TextUtils.isEmpty(userName)) {
            userName_et.setError("用户名不能为空");
        }
        if (TextUtils.isEmpty(password)) {
            password_et.setError("密码不能为空");
        }
        if (TextUtils.isEmpty(passwordConfirm)) {
            password_et2.setError("密码不能为空");
        }
        //密码8-20位
        if (password.length() < 8 || password.length() > 20) {
            //return "密码的长度在8-20位之间";
            password_et.setError("密码的长度在8-20位之间");
        }
        if (!password.equals(passwordConfirm)) {
            password_et2.setError("两次密码不同");
        }
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(passwordConfirm)) {

            return "ok";
        }
        return "false";
    }

    private void toast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

}
