package xyz.thanhhaidev.cooking.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
import xyz.thanhhaidev.cooking.Hasura;
import xyz.thanhhaidev.cooking.R;
import xyz.thanhhaidev.cooking.activities.home.FoodActivity;
import xyz.thanhhaidev.cooking.interfaces.ItemClickListener;

public class HomeFragment extends Fragment {
    ProgressDialog progressDialog;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FoodListAdapter foodListAdapter;

    private static String TAG = "HomeFragment";

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        progressDialog = new ProgressDialog(getContext());

        recyclerView = rootView.findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        foodListAdapter = new FoodListAdapter();
        if (Hasura.getSessionToken(getContext()) == null) {
            Toast.makeText(getContext(), "You need to be logged in to view user details", Toast.LENGTH_SHORT).show();
        }

        loadListFood();
        recyclerView.setAdapter(foodListAdapter);

        return rootView;
    }

    public void showProgressDialog(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMessage(message);
                progressDialog.show();
            }
        });
    }

    public void dismissProgressDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });
    }

    public void showToast(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadListFood() {
        try {
            String url = Hasura.Config.DATA_URL + "query";

            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            JSONObject jsonObject = new JSONObject()
                    .put("type", "select")
                    .put("args", new JSONObject()
                            .put("table", "food")
                            .put("columns", new JSONArray()
                                    .put("*")
                            )
                    );

            RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + Hasura.getSessionToken(getContext()))
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
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    foodListAdapter.setData(array);
                                    foodListAdapter.notifyDataSetChanged();
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

    class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtFoodName;
        public ImageView imgFood;
        public ItemClickListener itemClickListener;

        public FoodViewHolder(View itemView) {
            super(itemView);

            txtFoodName = itemView.findViewById(R.id.food_name);
            imgFood = itemView.findViewById(R.id.food_image);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.OnClick(view, getAdapterPosition(), false);
        }
    }

    class FoodListAdapter extends RecyclerView.Adapter<FoodViewHolder> {
        JSONArray array = new JSONArray();

        public void setData(JSONArray array) {
            this.array = array;
        }

        @Override
        public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
            return new FoodViewHolder(view);
        }

        @Override
        public void onBindViewHolder(FoodViewHolder holder, int position) {
            try {
                JSONObject food = array.getJSONObject(position);
                final String id = food.getString("id");
                final String name = food.getString("name");
                final String image = food.getString("image");

                holder.txtFoodName.setText(name);
                Picasso.get().load(image).into(holder.imgFood);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void OnClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(getContext(), FoodActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("image", image);
                        intent.putExtra("name", name);
                        startActivity(intent);
                    }
                });

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
