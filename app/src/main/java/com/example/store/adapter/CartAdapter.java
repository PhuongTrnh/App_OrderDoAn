package com.example.store.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import com.example.store.db.DatabaseHandler;
import com.example.store.utils.ItemCheckedListener;
import com.example.store.utils.ItemClickListener;
import com.example.store.R;
import com.example.store.bean.Cart;
import com.example.store.bean.Product;
import com.example.store.constants.Resource;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private View itemview;
        public TextView tv_name;
        public TextView tv_price;
        public EditText et_quantity;
        public ImageView iv_productcart;
        public CheckBox cb_item;
        public CardView cv_decrease;
        public CardView cv_increase;
        private ItemClickListener itemClickListener;
        public RelativeLayout viewBackground;
        public ConstraintLayout viewForeground;

        public ViewHolder(View itemView) {
            super(itemView);
            itemview = itemView;
            tv_name = itemView.findViewById(R.id.tv_nameproduct);
            tv_price = itemView.findViewById(R.id.tv_priceproduct);
            et_quantity = itemView.findViewById(R.id.et_quantity);
            iv_productcart = itemView.findViewById(R.id.iv_product);
            cb_item = itemView.findViewById(R.id.cb_choose);
            cv_decrease = itemView.findViewById(R.id.cv_decrease);
            cv_increase = itemView.findViewById(R.id.cv_increase);
            et_quantity.setTransformationMethod(null);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);

            //itemView.setOnClickListener(this);
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

    private List mCarts;
    private Cart cart;
    private Product product;
    DatabaseHandler db;
    private Context mContext;
    private boolean isSelectedAll = false;
    ItemCheckedListener checkedListener;

    public static final int TYPE0 = 0; // Sản phẩm hết hàng
    public static final int TYPE1 = 1; // Có sản phẩm

    public void selectAll(){
        isSelectedAll=true;
        notifyDataSetChanged();
    }
    public void unSelectAll(){
        isSelectedAll=false;
        notifyDataSetChanged();
    }

    public CartAdapter(List _cart, Context mContext, ItemCheckedListener listener){
        this.mCarts = _cart;
        this.mContext = mContext;
        this.checkedListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        db = new DatabaseHandler(mContext);
        cart = (Cart) mCarts.get(position);
        product = db.getProduct(cart.getiIDProduct());
        if (product.getiState() == 0)
            return TYPE0;
        else
            return TYPE1;
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Nạp layout view cho phần tử giỏ hàng
        View cartView = null;
        switch (viewType)
        {
            case TYPE0:
                cartView = inflater.inflate(R.layout.item_cart_no, parent, false);
                break;
            case TYPE1:
                cartView = inflater.inflate(R.layout.item_cart, parent, false);
                break;
        }
        CartAdapter.ViewHolder viewHolder = new CartAdapter.ViewHolder(cartView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CartAdapter.ViewHolder holder, int position) {
        db = new DatabaseHandler(mContext);
        cart = (Cart) mCarts.get(position);
        product = db.getProduct(cart.getiIDProduct());
        holder.tv_name.setText(product.getsName());
        Locale locale = new Locale("vn","VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        holder.tv_price.setText(String.valueOf(currencyFormatter.format(product.getlPrice())));
        holder.et_quantity.setText(String.valueOf(cart.getiQuantity()));
        ImageView imageView = holder.iv_productcart;
        if(product.getsSource() == null){
            Uri imgUri= Uri.parse(Resource.RESOURCE_PATH);
            imageView.setImageURI(null);
            imageView.setImageURI(imgUri);
        }
        else{
            Bitmap bitmap = BitmapFactory.decodeByteArray(product.getsSource(), 0, product.getsSource().length);
            imageView.setImageBitmap(bitmap);
        }

        if (!isSelectedAll){
            holder.cb_item.setChecked(false);
        }
        else holder.cb_item.setChecked(true);

        holder.et_quantity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                cart = (Cart) mCarts.get(position);
                int quantity = cart.getiQuantity();
                product = db.getProduct(cart.getiIDProduct());
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event != null &&
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (event == null || !event.isShiftPressed()) {
                        String regex = "[0-9]+[\\.]?[0-9]*";
                        if(v.getText().length() <= 0 || v.getText().toString() == "0" ||  !Pattern.matches(regex, v.getText().toString())){
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setMessage("Are you sure you want to remove the product?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            db.deleteItemCart(cart);
                                            mCarts.remove(position);
                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(position, getItemCount());
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            v.setText(String.valueOf(cart.getiQuantity()));
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        else if(Integer.parseInt(v.getText().toString()) > product.getiQuantity()){
                            v.setText(String.valueOf(quantity));
                            Toast.makeText(mContext.getApplicationContext(), "Product exceeds the allowed quantity", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            cart.setiQuantity(Integer.parseInt(v.getText().toString()));
                            db.updateQuantityCart(cart);
                        }
                        return true; // consume.
                    }
                }
                return false; // pass on to other listeners.
            }
        });

        holder.cv_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart = (Cart) mCarts.get(position);
                int quantity = cart.getiQuantity();
                if(quantity > 1){
                    quantity = quantity - 1;
                    holder.et_quantity.setText(String.valueOf(quantity));
                    cart.setiQuantity(quantity);
                    db.updateQuantityCart(cart); // Cập nhật lại giỏ hàng
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage("Are you sure you want to remove the product?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    db.deleteItemCart(cart);
                                    mCarts.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, getItemCount());
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    holder.et_quantity.setText(String.valueOf(1));
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                if(holder.cb_item.isChecked()){
                    checkedListener.onItemCheckedChange(holder.getAdapterPosition());
                }
            }
        });

        holder.cv_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart = (Cart) mCarts.get(position);
                int quantity = cart.getiQuantity();
                product = db.getProduct(cart.getiIDProduct());
                if(quantity == product.getiQuantity()){
                    Toast.makeText(mContext.getApplicationContext(), "Product exceeds the allowed quantity", Toast.LENGTH_SHORT).show();
                    holder.et_quantity.setText(String.valueOf(quantity));
                }
                else {
                    quantity = quantity + 1;
                    holder.et_quantity.setText(String.valueOf(quantity));
                    cart.setiQuantity(quantity);
                    db.updateQuantityCart(cart);
                }
                if(holder.cb_item.isChecked()){
                    checkedListener.onItemCheckedChange(holder.getAdapterPosition());
                }
            }
        });

        holder.cb_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart = (Cart)mCarts.get(position);
                if(holder.cb_item.isChecked()){
                    db.checkedItemCart(cart.getiID());
                }
                else db.unCheckedItemCart(cart.getiID());
                checkedListener.onItemCheckedChange(position);
            }
        });
    }

    public void removeItem(int position) {
        mCarts.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Cart item, int position) {
        mCarts.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return mCarts.size();
    }
}
