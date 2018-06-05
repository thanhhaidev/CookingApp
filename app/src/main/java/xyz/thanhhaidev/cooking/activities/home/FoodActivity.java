package xyz.thanhhaidev.cooking.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xyz.thanhhaidev.cooking.BaseActivity;
import xyz.thanhhaidev.cooking.Hasura;
import xyz.thanhhaidev.cooking.R;

public class FoodActivity extends BaseActivity {

    private static String TAG = "FoodActivity";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MaterialAdapter materialAdapter;
    ImageView food_image_material;
    TextView food_name_material, txtProtein, txtCalo, txtCholesterol, txtLipid, txtPotassium, txtSodium;
    FloatingActionButton fabFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");
        String image = intent.getStringExtra("image");
        String name = intent.getStringExtra("name");

        txtCalo = findViewById(R.id.txtCalo);
        txtCholesterol = findViewById(R.id.txtCholesterol);
        txtLipid = findViewById(R.id.txtLipid);
        txtProtein = findViewById(R.id.txtProtein);
        txtPotassium = findViewById(R.id.txtPotassium);
        txtSodium = findViewById(R.id.txtSodium);
        fabFavorite = findViewById(R.id.fabFavorite);

        food_image_material = findViewById(R.id.food_image_material);
        food_name_material = findViewById(R.id.food_name_material);
        Picasso.get().load(image).into(food_image_material);
        food_name_material.setText(name);
        recyclerView = findViewById(R.id.recycler_material);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        materialAdapter = new MaterialAdapter();
        if (Hasura.getSessionToken(this) == null) {
            Toast.makeText(this, "You need to be logged in to view user details", Toast.LENGTH_SHORT).show();
        }
        loadMaterial(id);
        recyclerView.setAdapter(materialAdapter);

        fabFavorite.setVisibility(View.GONE);

        HandleFavorite(id);
    }

    private void checkFavorite(String id) {
        String url = Hasura.Config.DATA_URL + "query";
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            JSONObject jsonObject = new JSONObject()
                    .put("type", "insert")
                    .put("args", new JSONObject()
                            .put("table", "favourite")
                            .put("objects", new JSONArray()
                                    .put(new JSONObject()
                                            .put("food_id", id)
                                            .put("hasura_id", Hasura.getSessionID(FoodActivity.this))
                                    )
                            )
                    );

            RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + Hasura.getSessionToken(FoodActivity.this))
                    .build();
            showProgressDialog("Loading...");
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    //Handle failure
                    dismissProgressDialog();
                    //Handle failure
                    showToast("Failed to add into food favorite: " + e.getLocalizedMessage());
                    Log.d(TAG, e.getLocalizedMessage());
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    dismissProgressDialog();
                    //Handle success
                    if (response.isSuccessful()) {
                        showToast("Added to Favorite");
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void unCheckFavorite(String id) {
        String url = Hasura.Config.DATA_URL + "query";
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            JSONObject jsonObject = new JSONObject()
                    .put("type", "delete")
                    .put("args", new JSONObject()
                            .put("table", "favourite")
                            .put("where", new JSONObject()
                                    .put("$and", new JSONArray()
                                            .put(new JSONObject()
                                                    .put("food_id", new JSONObject()
                                                            .put("$eq", id)
                                                    )
                                            )
                                            .put(new JSONObject()
                                                    .put("hasura_id", new JSONObject()
                                                            .put("$eq", Hasura.getSessionID(this))
                                                    )
                                            )
                                    )
                            )
                    );

            RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + Hasura.getSessionToken(FoodActivity.this))
                    .build();
            showProgressDialog("Loading...");
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    //Handle failure
                    dismissProgressDialog();
                    //Handle failure
                    showToast("Failed to add into food favorite: " + e.getLocalizedMessage());
                    Log.d(TAG, e.getLocalizedMessage());
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    dismissProgressDialog();
                    //Handle success
                    if (response.isSuccessful()) {
                        showToast("Remove to Favorite");
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void HandleFavorite(final String id) {
        String url = Hasura.Config.DATA_URL + "query";
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            JSONObject jsonObject = new JSONObject()
                    .put("type", "select")
                    .put("args", new JSONObject()
                            .put("table", "favourite")
                            .put("columns", new JSONArray()
                                    .put("*")
                            )
                            .put("where", new JSONObject()
                                    .put("$and", new JSONArray()
                                            .put(new JSONObject()
                                                    .put("hasura_id", new JSONObject()
                                                            .put("$eq", Hasura.getSessionID(this))
                                                    )
                                            )
                                            .put(new JSONObject()
                                                    .put("food_id", new JSONObject()
                                                            .put("$eq", id)
                                                    )
                                            )
                                    )
                            )
                    );
            RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + Hasura.getSessionToken(this))
                    .build();
            showProgressDialog("Checking...");
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    dismissProgressDialog();
                    //Handle failure
                    showToast("Failed to fetch user details: " + e.getLocalizedMessage());
                    Log.d(TAG, e.getLocalizedMessage());
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    dismissProgressDialog();
                    //Handle success
                    String jsonResponse = response.body().string();
                    if (response.isSuccessful()) {
                        try {
                            //You can also use GSON to convert it to a POJO
                            final JSONArray array = new JSONArray(jsonResponse);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (array.length() > 0) {
                                        fabFavorite.setImageResource(R.drawable.heart_active);
                                        fabFavorite.setVisibility(View.VISIBLE);
                                        fabFavorite.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                unCheckFavorite(id);
                                                fabFavorite.setImageResource(R.drawable.heart_nomal);
                                            }
                                        });
                                    } else {
                                        fabFavorite.setImageResource(R.drawable.heart_nomal);
                                        fabFavorite.setVisibility(View.VISIBLE);
                                        fabFavorite.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                checkFavorite(id);
                                                fabFavorite.setImageResource(R.drawable.heart_active);
                                            }
                                        });
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast("Failed to parse response: " + jsonResponse + "\n error: " + e.getLocalizedMessage());
                        }
                    } else {
                        showToast("Fetching articles failed: " + jsonResponse);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadMaterial(String id) {
        String url = Hasura.Config.DATA_URL + "query";
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            JSONObject jsonObject = new JSONObject()
                    .put("type", "select")
                    .put("args", new JSONObject()
                            .put("table", "food_material")
                            .put("columns", new JSONArray()
                                    .put(new JSONObject()
                                            .put("name", "material")
                                            .put("columns", new JSONArray()
                                                    .put("*")
                                            )
                                    )
                                    .put("weigh")
                            )
                            .put("where", new JSONObject()
                                    .put("id_food", new JSONObject()
                                            .put("$eq", id)
                                    )
                            )
                    );
            RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + Hasura.getSessionToken(this))
                    .build();
            showProgressDialog("Fetching food success");
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    dismissProgressDialog();
                    //Handle failure
                    showToast("Failed to fetch user details: " + e.getLocalizedMessage());
                    Log.d(TAG, e.getLocalizedMessage());
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    dismissProgressDialog();
                    //Handle success
                    String jsonResponse = response.body().string();
                    if (response.isSuccessful()) {
                        try {
                            //You can also use GSON to convert it to a POJO
                            final JSONArray array = new JSONArray(jsonResponse);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    materialAdapter.setData(array);
                                    materialAdapter.notifyDataSetChanged();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast("Failed to parse response: " + jsonResponse + "\n error: " + e.getLocalizedMessage());
                        }
                    } else {
                        showToast("Fetching articles failed: " + jsonResponse);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class MaterialViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtMaterial;
        public ImageView imgMaterial;

        public MaterialViewHolder(View itemView) {
            super(itemView);

            txtMaterial = itemView.findViewById(R.id.txtMaterial);
            imgMaterial = itemView.findViewById(R.id.imgMaterial);
        }

        @Override
        public void onClick(View view) {
            //itemClickListener.OnClick(view, getAdapterPosition(), false);
        }
    }

    class MaterialAdapter extends RecyclerView.Adapter<MaterialViewHolder> {
        JSONArray array = new JSONArray();
        double gramProtein = 0;
        double gramCholesterol = 0;
        double gramLipid = 0;
        double gramPotassium = 0;
        double gramSodium = 0;
        int gramCalo = 0;

        public void setData(JSONArray array) {
            this.array = array;
        }

        @Override
        public MaterialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_item, parent, false);
            return new MaterialViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MaterialViewHolder holder, int position) {
            try {
                JSONObject material = array.getJSONObject(position);
                String image = material.getJSONObject("material").getString("image");
                double protein = Double.valueOf(material.getJSONObject("material").getString("protein"));
                double cholesterol = Double.valueOf(material.getJSONObject("material").getString("cholesterol"));
                double lipid = Double.valueOf(material.getJSONObject("material").getString("lipid"));
                double potassium = Double.valueOf(material.getJSONObject("material").getString("potassium"));
                double sodium = Double.valueOf(material.getJSONObject("material").getString("sodium"));
                int calo = Integer.valueOf(material.getJSONObject("material").getString("calo"));
                double weigh = Double.valueOf(material.getString("weigh"));
                Picasso.get().load(image).into(holder.imgMaterial);
                holder.txtMaterial.setText(weigh + "gr");

                gramProtein += Math.round((((protein * weigh) / 100)*10)/10);
                gramCalo += Math.round((((calo * weigh) / 100)*10)/10);
                gramCholesterol += Math.round((((cholesterol * weigh) / 100)*10)/10);
                gramLipid += ((lipid * weigh) / 100);
                gramPotassium += Math.round((((potassium * weigh) / 100)*10)/10);
                gramSodium += Math.round((((sodium * weigh) / 100)*10)/10);

                txtProtein.setText(gramProtein + "gr");
                txtCalo.setText(gramCalo + "");
                txtCholesterol.setText(gramCholesterol + "mg");
                txtLipid.setText(gramLipid + "gr");
                txtPotassium.setText(gramPotassium + "mg");
                txtSodium.setText(gramSodium + "mg");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return array.length();
        }
    }

}

