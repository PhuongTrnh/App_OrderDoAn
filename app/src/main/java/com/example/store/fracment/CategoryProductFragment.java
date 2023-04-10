package com.example.store.fracment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.example.store.db.DatabaseHandler;
import com.example.store.R;
import com.example.store.adapter.CategoryProductAdapter;
import com.example.store.bean.CategoryProduct;

public class CategoryProductFragment extends Fragment {


    private static final String ARG_IDUSER = "iduser";

    private String mParamIDUser;

    private List<CategoryProduct> lCategoryProducts;
    DatabaseHandler db;

    public CategoryProductFragment() {
        // Required empty public constructor
    }

    public static CategoryProductFragment newInstance(String param1) {
        CategoryProductFragment fragment = new CategoryProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IDUSER, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamIDUser = getArguments().getString(ARG_IDUSER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_category_product, container, false);

        db = new DatabaseHandler(getContext());
        lCategoryProducts = db.getListCategoryProduct();
        if(lCategoryProducts.size() == 0){
            CategoryProduct categoryProduct = new CategoryProduct();
            categoryProduct.insertDefaultCategory(db);
//            Product product = new Product();
//            product.insertDefaultProduct(db);
        }
        //show

        RecyclerView rv_category = view.findViewById(R.id.item_category);
        /*rv_category.setHasFixedSize(true);*/
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_category.setLayoutManager(layoutManager);
        CategoryProductAdapter adapter = new CategoryProductAdapter(lCategoryProducts, getContext(), mParamIDUser);
        rv_category.setAdapter(adapter);

        return view;
    }

    private byte[] getByteArrayImage(String sURL) throws MalformedURLException {
        URL url = new URL(sURL);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (InputStream inputStream = url.openStream()) {
            int n = 0;
            byte [] buffer = new byte[ 1024 ];
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toByteArray();
    }
}