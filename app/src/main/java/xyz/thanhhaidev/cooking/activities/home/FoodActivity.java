package xyz.thanhhaidev.cooking.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import xyz.thanhhaidev.cooking.BaseActivity;
import xyz.thanhhaidev.cooking.R;

public class FoodActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }
}
