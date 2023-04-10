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
import com.example.store.bean.BillDetail;
import com.example.store.bean.Product;

public class BillDetailAdapter extends RecyclerView.Adapter<BillDetailAdapter.ViewHolder>{
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

    private List<BillDetail> mBillDetails;
    private BillDetail billDetail;
    private Product product;
    DatabaseHandler db;
    private Context mContext;

    public BillDetailAdapter(List<BillDetail> _bill, Context mContext){
        this.mBillDetails = _bill;
        this.mContext = mContext;
    }

    @NonNull
    @NotNull
    @Override
    public BillDetailAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = null;
        view = inflater.inflate(R.layout.item_confirm_order, parent, false);
        BillDetailAdapter.ViewHolder viewHolder = new BillDetailAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BillDetailAdapter.ViewHolder holder, int position) {
        db = new DatabaseHandler(mContext);
        billDetail = (BillDetail) mBillDetails.get(position);
        product = db.getProduct(billDetail.getiIDProduct());
        Locale locale = new Locale("vn","VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        holder.tv_name.setText(product.getsName());
        holder.tv_unitprice.setText(currencyFormatter.format(billDetail.getlUnitPrice()));
        holder.tv_quantity.setText(String.valueOf(billDetail.getiQuantity()));
        double total_price = billDetail.getlUnitPrice() * billDetail.getiQuantity();
        holder.tv_price.setText(currencyFormatter.format(total_price));
    }

    @Override
    public int getItemCount() {
        return mBillDetails.size();
    }
}
