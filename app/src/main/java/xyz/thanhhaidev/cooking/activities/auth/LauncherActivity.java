package xyz.thanhhaidev.cooking.activities.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xyz.thanhhaidev.cooking.Hasura;
import xyz.thanhhaidev.cooking.R;
import xyz.thanhhaidev.cooking.Slide_Navigation_Main;

public class LauncherActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    RelativeLayout relay1, relay2;
    Button btnAuth, btnSignUp, btnForgotPassword;
    EditText ed_username, ed_password;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            relay1.setVisibility(View.VISIBLE);
            relay2.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        progressDialog = new ProgressDialog(this);

        relay1 = findViewById(R.id.relay1);
        relay2 = findViewById(R.id.relay2);
        btnAuth = findViewById(R.id.btnAuth);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        ed_password = findViewById(R.id.ed_password);
        ed_username = findViewById(R.id.ed_username);

        handler.postDelayed(runnable, 2000);

        btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkIfFormIsValid()) {
                    performAuthentication(ed_username.getText().toString(), ed_password.getText().toString());
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LauncherActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LauncherActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        refreshLayout();
    }

    //if logged in, show menu layout
    private void refreshLayout() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Hasura.getSessionToken(LauncherActivity.this) != null) {
                    Intent intent = new Intent(LauncherActivity.this, Slide_Navigation_Main.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    //check username and password isEmpty?
    private Boolean checkIfFormIsValid() {
        if (ed_username.getText().toString().isEmpty()) {
            showToast("Username cannot be left empty");
            return false;
        }
        if (ed_password.getText().toString().isEmpty()) {
            showToast("Password cannot be left empty");
            return false;
        }
        return true;
    }

    // Handle Authentication
    private void performAuthentication(String username, String password) {
        String url = Hasura.Config.AUTH_URL + "login";
        showProgressDialog("Please wait");
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            //Username Provider
            JSONObject jsonObject = new JSONObject()
                    .put("provider", "username")
                    .put("data", new JSONObject()
                            .put("username", username)
                            .put("password", password)
                    );

            RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    dismissProgressDialog();
                    //Handle failure
                    showToast("Authentication failed: " + e.getLocalizedMessage());
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    dismissProgressDialog();
                    //Handle success
                    String jsonString = response.body().string();
                    if (response.isSuccessful()) {
                        AuthResponse authResponse = new Gson().fromJson(jsonString, AuthResponse.class);
                        //Save the response offline to be used later
                        Hasura.saveSessionToken(authResponse.authToken, LauncherActivity.this);
                        showToast("Authenticated successfully!");
                        refreshLayout();
                    } else {
                        showToast("Authentication failed: " + jsonString);
                    }
                }
            });

        } catch (JSONException e) {
            dismissProgressDialog();
            e.printStackTrace();
            showToast("Authentication failed: " + e.getLocalizedMessage());
        }
    }

    public void showProgressDialog(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMessage(message);
                progressDialog.show();
            }
        });
    }

    public void dismissProgressDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });
    }

    public void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LauncherActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

}
