package xyz.thanhhaidev.cooking.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import xyz.thanhhaidev.cooking.R;

/**
 * Created by ThanhHaiDev on 30-May-18.
 */
public class ShopAdapter extends  RecyclerView.Adapter<ShopAdapter.ViewHolder> {
    ArrayList<datashop> datashops;
    Context context;

    public ShopAdapter(ArrayList<datashop> datashops, Context context) {
        this.datashops = datashops;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_row,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.txtName.setText(datashops.get(position).getTen());
        holder.imgHinh.setImageResource(datashops.get(position).getHinhanh());
//        class getImg extends AsyncTask<String,Void,byte[]>{
//            OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                    .build();
//
//            @Override
//            protected byte[] doInBackground(String... strings) {
//                Request.Builder builder = new Request.Builder();
//                builder.url(strings[0]);
//                Request request = builder.build();
//                try {
//                    Response response = okHttpClient.newCall(request).execute();
//                    return  response.body().bytes();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(byte[] bytes) {
//                if (bytes.length>0){
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//
//                    holder.imgHinh.setImageBitmap(bitmap);
//                }
//                super.onPostExecute(bytes);
//            }
//        }
    }

    @Override
    public int getItemCount() {
        return datashops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtName;
        ImageView imgHinh;
        public ViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView)itemView.findViewById(R.id.textView);
            imgHinh = (ImageView)itemView.findViewById(R.id.imageView);
        }
    }
}
