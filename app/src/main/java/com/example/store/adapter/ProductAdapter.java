package com.example.store.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.store.R;
import com.example.store.bean.Product;
import com.example.store.constants.Resource;
import com.example.store.db.DatabaseHandler;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends BaseAdapter {
    private Activity activity;
    private List<Product> products;
    private int idcategory;
    DatabaseHandler db;
    public ProductAdapter(Activity activity, List<Product> products) {
        this.activity = activity;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = activity.getLayoutInflater();
        view = inflater.inflate(R.layout.item_product, null);
        TextView tvName = (TextView) view.findViewById(R.id.tv_nameproduct);
        TextView tvPrice = (TextView) view.findViewById(R.id.tv_priceproduct);
        ImageView ivProduct = (ImageView) view.findViewById(R.id.iv_product);
        tvName.setText(products.get(i).getsName());

        Locale locale = new Locale("vn","VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        tvPrice.setText(String.valueOf(currencyFormatter.format(products.get(i).getlPrice())));
        if(products.get(i).getsSource() == null){
            Uri imgUri= Uri.parse(Resource.RESOURCE_PATH);
            ivProduct.setImageURI(null);
            ivProduct.setImageURI(imgUri);
        }
        else{
            Bitmap bitmap = BitmapFactory.decodeByteArray(products.get(i).getsSource(), 0, products.get(i).getsSource().length);
            ivProduct.setImageBitmap(bitmap);
        }

        return view;
    }
}
