package com.example.store.activity.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.store.R;
import com.example.store.bean.Product;
import com.example.store.db.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {
    private ListView listView;
    private static final int MENU_ITEM_VIEW = 111;
    private static final int MENU_ITEM_EDIT = 222;
    private static final int MENU_ITEM_CREATE = 333;
    private static final int MENU_ITEM_DELETE = 444;
    private static final int MY_REQUEST_CODE = 1000;
    private static final String CHANNEL_ID = "product";

    private final List<Product> productList = new ArrayList<Product>();
    private ArrayAdapter<Product> listViewAdapter;

    private NotificationCompat.Builder builder;
    private NotificationManagerCompat notificationManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Get ListView object from xml
        this.listView = (ListView) findViewById(R.id.listViewProduct);
        DatabaseHandler db = new DatabaseHandler(this);

        List<Product> listViewCategory = db.getListProduct();
        this.productList.addAll(listViewCategory);


        this.listViewAdapter = new ArrayAdapter<Product>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, this.productList);

        // Assign adapter to ListView
        this.listView.setAdapter(this.listViewAdapter);

        // Register the ListView for Context menu
        registerForContextMenu(this.listView);
        createNotificationChannel();
        builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_delete_24)
                .setContentTitle("Notification")
                .setContentText("You have been delete successful")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager = NotificationManagerCompat.from(this);

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo)    {

        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle("Select The Action");


        menu.add(0, MENU_ITEM_CREATE , 1, "Create Product");
        menu.add(0, MENU_ITEM_EDIT , 2, "Edit Product");
        menu.add(0, MENU_ITEM_DELETE, 4, "Delete Product");
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Product channel";
            String description = "Delete Product Success!";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final Product selectedProduct = (Product)this.listView.getItemAtPosition(info.position);

        if(item.getItemId() == MENU_ITEM_VIEW){
            Toast.makeText(getApplicationContext(),selectedProduct.toString(), Toast.LENGTH_LONG).show();
        }
        else if(item.getItemId() == MENU_ITEM_CREATE){
            Intent intent = new Intent(this, AddEditProductActivity.class);

            // Start AddEditCategoryActivity, (with feedback).
            this.startActivityForResult(intent, MY_REQUEST_CODE);
        }
        else if(item.getItemId() == MENU_ITEM_EDIT ){
            Intent intent = new Intent(this, AddEditProductActivity.class);
            intent.putExtra("product",  selectedProduct);

            // Start AddEditCategoryActivity, (with feedback).
            this.startActivityForResult(intent,MY_REQUEST_CODE);
        }
        else if(item.getItemId() == MENU_ITEM_DELETE){
            // Ask before deleting.
            new AlertDialog.Builder(this)
                    .setMessage(selectedProduct.getsName()+". Are you sure you want to delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deleteProduct(selectedProduct);
                            // notificationId is a unique int for each notification that you must define
//                            notificationManager.notify(100, builder.build());
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        else {
            return false;
        }
        return true;
    }

    // Delete a record
    private void deleteProduct(Product product)  {
        DatabaseHandler db = new DatabaseHandler(this);
        db.deleteProduct(product);
        this.productList.remove(product);
        // Refresh ListView.
        this.listViewAdapter.notifyDataSetChanged();
    }

    // When AddEditCategoryActivity completed, it sends feedback.
    // (If you start it using startActivityForResult ())
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == MY_REQUEST_CODE) {
            boolean needRefresh = data.getBooleanExtra("needRefresh", true);
            // Refresh ListView
            if (needRefresh) {
                this.productList.clear();
                DatabaseHandler db = new DatabaseHandler(this);
                List<Product> list = db.getListProduct();
                this.productList.addAll(list);


                // Notify the data change (To refresh the ListView).
                this.listViewAdapter.notifyDataSetChanged();
            }
        }
    }
}