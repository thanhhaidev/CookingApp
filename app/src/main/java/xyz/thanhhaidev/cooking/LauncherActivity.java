package xyz.thanhhaidev.cooking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import xyz.thanhhaidev.cooking.auth.AuthActivity;
import xyz.thanhhaidev.cooking.data.DataActivity;
import xyz.thanhhaidev.cooking.filestore.FilestoreActivity;
import xyz.thanhhaidev.cooking.websockets.WebsocketActivity;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        findViewById(R.id.auth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthActivity.startActivity(LauncherActivity.this);
            }
        });

        findViewById(R.id.data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataActivity.startActivity(LauncherActivity.this);
            }
        });

        findViewById(R.id.filestore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilestoreActivity.startActivity(LauncherActivity.this);
            }
        });
        findViewById(R.id.websocket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebsocketActivity.startActivity(LauncherActivity.this);
            }
        });
    }
}
