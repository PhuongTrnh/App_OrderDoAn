package com.example.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.example.store.db.DatabaseHandler;
import com.example.store.R;
import com.example.store.bean.Cart;
import com.example.store.bean.Product;

public class ConfirmOrderAdapter extends RecyclerView.Adapter<ConfirmOrderAdapter.ViewHolder>{
    public class ViewHolder extends RecyclerView.ViewHolder{
        private View itemview;
        public TextView tv_name;
        public TextView tv_unitprice;
        public TextView tv_price;
        public TextView tv_quantity;

        public ViewHolder(View itemView) {
            super(itemView);
            itemview = itemView;
            tv_name = itemView.findViewById(R.id.tv_name_confirm);
            tv_unitprice = itemView.findViewById(R.id.tv_unitprice);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
        }
    }
    private List mCarts;
    private Cart cart;
    private Product product;
    DatabaseHandler db;
    private Context mContext;

    public ConfirmOrderAdapter(List _cart, Context mContext){
        this.mCarts = _cart;
        this.mContext = mContext;
    }

    @NonNull
    @NotNull
    @Override
    public ConfirmOrderAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Nạp layout view cho phần tử giỏ hàng
        View cartView = null;
        cartView = inflater.inflate(R.layout.item_confirm_order, parent, false);
        ConfirmOrderAdapter.ViewHolder viewHolder = new ConfirmOrderAdapter.ViewHolder(cartView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ConfirmOrderAdapter.ViewHolder holder, int position) {
        db = new DatabaseHandler(mContext);
        cart = (Cart) mCarts.get(position);
        product = db.getProduct(cart.getiIDProduct());
        Locale locale = new Locale("vn","VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        holder.tv_name.setText(product.getsName());
        holder.tv_unitprice.setText(currencyFormatter.format(product.getlPrice()));
        holder.tv_quantity.setText(String.valueOf(cart.getiQuantity()));
        double total_price = (product.getlPrice()) * cart.getiQuantity();
        holder.tv_price.setText(currencyFormatter.format(total_price));
    }

    @Override
    public int getItemCount() {
        return mCarts.size();
    }
}
