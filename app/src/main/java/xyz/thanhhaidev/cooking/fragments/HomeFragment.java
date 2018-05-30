package xyz.thanhhaidev.cooking.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xyz.thanhhaidev.cooking.Hasura;
import xyz.thanhhaidev.cooking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    ProgressDialog progressDialog;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        progressDialog = new ProgressDialog(getContext());
        RecyclerView recyclerView = rootView.findViewById(R.id.luandeptrai);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<datashop> arrayList = new ArrayList<>();




        arrayList.add(new datashop(R.drawable.menu_background,"menu_background"));



        ShopAdapter shopAdapter = new ShopAdapter(arrayList,getContext().getApplicationContext());

        recyclerView.setAdapter(shopAdapter);

        return rootView;
    }
    public void getDataFood(String token){
        String url = Hasura.Config.AUTH_URL + "query";
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
//                        refreshLayout();
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
}
