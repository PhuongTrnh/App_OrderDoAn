package com.example.store.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.example.store.utils.ItemClickListener;
import com.example.store.R;
import com.example.store.bean.CategoryProduct;
import com.example.store.constants.Resource;

public class CategoryProductAdapter extends RecyclerView.Adapter<CategoryProductAdapter.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private View itemview;
        public TextView name;
        public ImageView img;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            itemview = itemView;
            name = itemView.findViewById(R.id.tv_namecategoryproduct);
            img = itemView.findViewById(R.id.iv_category);
            itemView.setOnClickListener(this);
        }

        //Tạo setter cho biến itemClickListenenr
        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }
        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),true);
        }
    }

    private List mCategoryProducts;
    // Lưu context để dễ dàng truy cập
    private Context mContext;
    private String mIDUser;

    public CategoryProductAdapter(List _categoryproduct, Context mContext, String IDUser){
        this.mCategoryProducts = _categoryproduct;
        this.mContext = mContext;
        this.mIDUser = IDUser;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Nạp layout view cho phần tử product
        View productView = inflater.inflate(R.layout.item_category, parent, false);
        ViewHolder viewHolder = new ViewHolder(productView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CategoryProduct categoryproduct = (CategoryProduct) mCategoryProducts.get(position);

        holder.name.setText(categoryproduct.getsName());
        ImageView imageView = holder.img;
        if(categoryproduct.getsSource() == null){
            Uri imgUri= Uri.parse(Resource.RESOURCE_PATH);
            imageView.setImageURI(null);
            imageView.setImageURI(imgUri);
            Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] source = baos.toByteArray();
            Log.d("HINH ANH", String.valueOf(source));
        }
        else{
            Bitmap bitmap = BitmapFactory.decodeByteArray(categoryproduct.getsSource(), 0, categoryproduct.getsSource().length);
            imageView.setImageBitmap(bitmap);
        }
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isClick) {
                if(isClick){
                    CategoryProduct categoryproduct = (CategoryProduct) mCategoryProducts.get(position);
//                    Intent intentProduct = new Intent(mContext.getApplicationContext(), ProductActivity.class);
//                    intentProduct.putExtra("idcategory",categoryproduct.getiID());
//                    intentProduct.putExtra("iduser",mIDUser);
//                    mContext.startActivity(intentProduct);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategoryProducts.size();
    }

}
