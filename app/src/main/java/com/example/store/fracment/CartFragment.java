package com.example.store.fracment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.example.store.db.DatabaseHandler;
import com.example.store.utils.ItemCheckedListener;
import com.example.store.R;
import com.example.store.utils.RecyclerItemTouchHelper;
import com.example.store.activity.order.ConfirmOrderActivity;
import com.example.store.adapter.CartAdapter;
import com.example.store.bean.Cart;
import pl.droidsonroids.gif.GifImageView;

public class CartFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, ItemCheckedListener {


    private static final String ARG_IDUSER = "iduser";
    private String mParamIDUser;
    private RecyclerView rv_cart;
    private List<Cart> lCarts;
    private DatabaseHandler db;
    private CartAdapter adapter;
    private FrameLayout frameLayout;
    private CheckBox cb_all;
    private TextView tv_totalprice;
    private Button btn_placeorder;
    private boolean cb_all_check = true;
    private List<Cart> lCartCheked;
    public static final int REQUEST_CODE_CONFIRM = 1;
    private GifImageView giv_empty;
    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance(String param1) {
        CartFragment fragment = new CartFragment();
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
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        cb_all = view.findViewById(R.id.cb_all_item);
        tv_totalprice = view.findViewById(R.id.tv_totalprice);
        btn_placeorder = view.findViewById(R.id.btn_place_order);
        db = new DatabaseHandler(getContext());
        lCarts = db.getListCartOfUser(Integer.parseInt(mParamIDUser));

        DividerItemDecoration dividerHorizontal =
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        frameLayout = view.findViewById(R.id.frame_container_cart);
        rv_cart = view.findViewById(R.id.item_cart);
        rv_cart.addItemDecoration(dividerHorizontal);
        giv_empty = view.findViewById(R.id.empty_cart);
        if(lCarts.size() > 0){
            rv_cart.setVisibility(View.VISIBLE);
            giv_empty.setVisibility(View.GONE);
            btn_placeorder.setVisibility(View.VISIBLE);
            cb_all.setVisibility(View.VISIBLE);
            tv_totalprice.setVisibility(View.VISIBLE);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            rv_cart.setLayoutManager(layoutManager);
            adapter = new CartAdapter(lCarts, getContext(),this);
            adapter.notifyDataSetChanged();
            rv_cart.setAdapter(adapter);
            ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv_cart);
        }
        else {
            rv_cart.setVisibility(View.GONE);
            giv_empty.setVisibility(View.VISIBLE);
            btn_placeorder.setVisibility(View.GONE);
            cb_all.setVisibility(View.GONE);
            tv_totalprice.setVisibility(View.GONE);
        }

        cb_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                double total_price = 0;
                Locale locale = new Locale("vn","VN");
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
                if(cb_all.isChecked()){
                    db.checkedAllItemCart(Integer.parseInt(mParamIDUser));
                    total_price = db.totalPriceCheckedInCart(Double.parseDouble(mParamIDUser));
                    adapter.selectAll();
                }
                else {
                    if(cb_all_check){
                        db.unCheckedAllItemCart(Integer.parseInt(mParamIDUser));
                        total_price = db.totalPriceCheckedInCart(Integer.parseInt(mParamIDUser));
                        adapter.unSelectAll();
                    }
                }
                cb_all_check = true;
                tv_totalprice.setText(String.valueOf(currencyFormatter.format(total_price)));
            }
        });

        btn_placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lCartCheked = db.getListCartOfUserChecked(Integer.parseInt(mParamIDUser));
                if(lCartCheked.size() > 0){
                    Intent intent = new Intent(getContext(), ConfirmOrderActivity.class);
                    intent.putExtra(ARG_IDUSER,mParamIDUser);
                    startActivityForResult(intent, REQUEST_CODE_CONFIRM);

                }
                else Toast.makeText(getContext(),"No product is selected.",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CONFIRM && resultCode == Activity.RESULT_OK) {
            lCarts = db.getListCartOfUser(Integer.parseInt(mParamIDUser));
            int size = lCarts.size();
            if(lCarts != null){
                for (int i=0; i<size; i++){
                    if (lCarts.get(i).getiChecked() == 1){
                        adapter.removeItem(i);
                        db.deleteItemCart(lCarts.get(i));
                        lCarts.remove(i);
                        size--;
                        i--;
                    }
                    tv_totalprice.setText("");
                }
                if(size == 0)
                {
                    rv_cart.setVisibility(View.GONE);
                    giv_empty.setVisibility(View.VISIBLE);
                    btn_placeorder.setVisibility(View.GONE);
                    cb_all.setVisibility(View.GONE);
                    tv_totalprice.setVisibility(View.GONE);
                }
            }
            Toast.makeText(getContext(),"Order successful. Thank you! Select 'Profile' to view your order history",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartAdapter.ViewHolder) {
            // get the removed item name to display it in snack bar
            int id = lCarts.get(viewHolder.getAdapterPosition()).getiID();

            // backup of removed item for undo purpose
            final Cart deletedItem = lCarts.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            adapter.removeItem(viewHolder.getAdapterPosition());
            db.deleteItemCart(deletedItem);

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(frameLayout, " Removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // undo is selected, restore the deleted item
                    adapter.restoreItem(deletedItem, deletedIndex);
                    db.undoItemCart(deletedItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public void onItemCheckedChange(int position) {
        int total_checked = db.itemCheckedSize(Integer.parseInt(mParamIDUser));
        double total_price = 0;
        total_price = db.totalPriceCheckedInCart(Integer.parseInt(mParamIDUser));
        Locale locale = new Locale("vn","VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        if(total_checked == lCarts.size())
        {
            //cb_all_check = false;
            cb_all.setChecked(true);
        }
        else {
            cb_all_check = false;
            cb_all.setChecked(false);
        }
        tv_totalprice.setText(String.valueOf(currencyFormatter.format(total_price)));
    }
}