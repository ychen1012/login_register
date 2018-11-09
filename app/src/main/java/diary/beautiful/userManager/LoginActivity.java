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
import java.util.LinkedHashMap;

import diary.beautiful.R;
import diary.beautiful.model.RegisterResult;
import diary.beautiful.util.CommonRequest;
import diary.beautiful.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * created by yangchen on 21:40
 */
public class LoginActivity extends Activity {
    private Button loginButton;
    private EditText emailEditText;
    private EditText passwordText;


    String emailValid;

    void initComponents() {
        loginButton = findViewById(R.id.login_button);
        emailEditText = findViewById(R.id.login_email);
        passwordText = findViewById(R.id.login_password);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initComponents();
        setListeners();

    }


    void setListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(LoginActivity.this, "!!!!!!", Toast.LENGTH_SHORT).show();
                login();

            }
        });
    }

    private void login() {
        CommonRequest request = new CommonRequest();
        String email = emailEditText.getText().toString();
        String password = passwordText.getText().toString();
//        String checkMsg = checkDataValid(email, password);
//        if (checkMsg == null) {
//            return;
//        }
//        if (!checkMsg.equals("succ")) {
//            emailEditText.setError("");
//            passwordText.setError("");
//            return;
//        }
//        loginButton.setEnabled(true);
        LinkedHashMap<String, String> param = new LinkedHashMap<>();
        param.put("email", email);
        param.put("password", password);
        HttpUtil.get("http://192.168.31.49:4396/user/login", param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "netWork error", Toast.LENGTH_SHORT).show();

                    }
                });
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                String loginResult = response.body().string();
                RegisterResult registerResult = gson.fromJson(loginResult, RegisterResult.class);
                if (registerResult == null) {
                    emailValid = "网络连接失败";
                }
                if (registerResult.getResult().equals("true")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "succ", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, registerResult.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

        });

    }

    private String checkDataValid(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("邮箱不能为空");
            return null;
        }


        CommonRequest request = new CommonRequest();
        LinkedHashMap<String, String> param = new LinkedHashMap<>();
        param.put("email", email);
        HttpUtil.get("http://192.168.31.49:4396/user/email", param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "netWork error", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                String loginResult = response.body().string();
                RegisterResult registerResult = gson.fromJson(loginResult, RegisterResult.class);
                if (registerResult == null) {
                    emailValid = "网络连接失败";
                }
                if (registerResult.getResult().equals("false")) {
                    emailValid = "邮箱不存在";
                }
                if (registerResult.getResult().equals("true")) {
                    emailValid = "succ";
                }
            }

        });
        if (!emailValid.equals("succ")) {
            emailEditText.setError(emailValid);
            return "false";
        }
        if (emailValid.equals("succ") && !TextUtils.isEmpty(password)) {
            return "succ";
        }
        return "false";
    }
}
