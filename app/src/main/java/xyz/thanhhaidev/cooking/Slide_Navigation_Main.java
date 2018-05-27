package xyz.thanhhaidev.cooking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xyz.thanhhaidev.cooking.activities.auth.LauncherActivity;
import xyz.thanhhaidev.cooking.fragments.HomeFragment;

public class Slide_Navigation_Main extends FragmentActivity implements View.OnClickListener {

    private ResideMenu resideMenu;
    private Slide_Navigation_Main mContext;
    private ResideMenuItem itemHome;

    private ProgressDialog progressDialog;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_navigation_main);
        mContext = this;

        progressDialog = new ProgressDialog(this);

        setUpMenu();
        if (savedInstanceState == null) {
            changeFragment(new HomeFragment());
        }
    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setUse3D(true);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
        resideMenu.setScaleValue(1.0f);

        // create menu items;
        itemHome = new ResideMenuItem(this, R.drawable.home, "Trang chủ");

        itemHome.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);

        // You can disable a direction by setting ->
        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        findViewById(R.id.title_bar_right_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogout();
                Toast.makeText(mContext, "Logout", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        return resideMenu.dispatchTouchEvent(ev);
//    }

    @Override
    public void onClick(View view) {

        if (view == itemHome) {
            changeFragment(new HomeFragment());
        }

        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            //Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            //Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    private void changeFragment(Fragment targetFragment) {
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private void performLogout() {
        showProgressDialog("Logging out...");
        String url = Hasura.Config.AUTH_URL + "user/logout";
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        //Username Provider
        JSONObject jsonObject = new JSONObject();
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        Request request = new Request.Builder()
                .url(url)
                //Add authorization header as only authenticated users can logout
                .addHeader("Authorization", "Bearer " + Hasura.getSessionToken(mContext))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                dismissProgressDialog();
                //Handle failure
                showToast("Logout Failed: " + e.getLocalizedMessage());

            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                dismissProgressDialog();
                //Handle success
                String jsonString = response.body().string();
                if (response.isSuccessful()) {
                    showToast("Logout successful");
                    //Delete the saved response
                    Hasura.saveSessionToken(null, mContext);
                    refreshLayout();
                } else {
                    showToast("Logout Failed: " + jsonString);
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
                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    //if logout, show login layout
    private void refreshLayout() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Hasura.getSessionToken(mContext) == null) {
                    Intent intent = new Intent(mContext, LauncherActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu() {
        return resideMenu;
    }
}
