package xyz.thanhhaidev.cooking.activities.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class SignUpActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    Button btnSignUp;
    EditText ed_password, ed_username, ed_confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progressDialog = new ProgressDialog(this);
        btnSignUp = findViewById(R.id.btnSignUp_Now);
        ed_confirm_password = findViewById(R.id.ed_confirm_password_signup);
        ed_password = findViewById(R.id.ed_password_signup);
        ed_username = findViewById(R.id.ed_username_signup);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkIfFormIsValid()) {
                    performAuthentication(ed_username.getText().toString(), ed_password.getText().toString());
                } else {
                    ed_password.setText("");
                    ed_confirm_password.setText("");
                }
            }
        });

    }

    private Boolean checkIfFormIsValid() {
        if (ed_username.getText().toString().isEmpty()) {
            showToast("Username cannot be left empty");
            return false;
        }
        if (ed_password.getText().toString().isEmpty()) {
            showToast("Password cannot be left empty");
            return false;
        }

        if (ed_confirm_password.getText().toString().isEmpty()) {
            showToast("Confirm Password cannot be left empty");
            return false;
        }
        if (!ed_password.getText().toString().equals(ed_confirm_password.getText().toString())) {
            showToast("Confirm Password is not match");
            return false;
        }
        return true;
    }

    private void performAuthentication(String username, String password) {
        String url = Hasura.Config.AUTH_URL + "signup";
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

    private void refreshLayout() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Hasura.getSessionToken(SignUpActivity.this) == null) {
                    Intent intent = new Intent(SignUpActivity.this, LauncherActivity.class);
                    startActivity(intent);
                }
            }
        });
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
                Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
