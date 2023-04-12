package com.example.store.activity.category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.store.R;
import com.example.store.bean.CategoryProduct;
import com.example.store.db.DatabaseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddCategoryActivity extends AppCompatActivity {
    private ListView listView;
    private static final int MENU_ITEM_VIEW = 111;
    private static final int MENU_ITEM_EDIT = 222;
    private static final int MENU_ITEM_CREATE = 333;
    private static final int MENU_ITEM_DELETE = 444;
    private static final int MY_REQUEST_CODE = 1000;
    private static final String CHANNEL_ID = "category";

    private final List<CategoryProduct> categoryList = new ArrayList<CategoryProduct>();
    private ArrayAdapter<CategoryProduct> listViewAdapter;

    private NotificationCompat.Builder builder;
    private NotificationManagerCompat notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        // Get ListView object from xml
        this.listView = (ListView) findViewById(R.id.listViewCategory);
        DatabaseHandler db = new DatabaseHandler(this);

        List<CategoryProduct> listViewCategory = db.getListCategoryProduct();
        this.categoryList.addAll(listViewCategory);


        this.listViewAdapter = new ArrayAdapter<CategoryProduct>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, this.categoryList);

        // Assign adapter to ListView
        this.listView.setAdapter(this.listViewAdapter);

        builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_delete_24)
                .setContentTitle("Notification")
                .setContentText("You have been delete successful")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager = NotificationManagerCompat.from(this);

        // Register the ListView for Context menu
        registerForContextMenu(this.listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo)    {

        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle("Select The Action");


        menu.add(0, MENU_ITEM_CREATE , 1, "Create Category");
        menu.add(0, MENU_ITEM_EDIT , 2, "Edit Category");
        menu.add(0, MENU_ITEM_DELETE, 4, "Delete Category");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final CategoryProduct selectedCategory = (CategoryProduct)this.listView.getItemAtPosition(info.position);

        if(item.getItemId() == MENU_ITEM_VIEW){
            Toast.makeText(getApplicationContext(),selectedCategory.toString(), Toast.LENGTH_LONG).show();
        }
        else if(item.getItemId() == MENU_ITEM_CREATE){
            Intent intent = new Intent(this, AddEditCategoryActivity.class);

            // Start AddEditCategoryActivity, (with feedback).
            this.startActivityForResult(intent, MY_REQUEST_CODE);
        }
        else if(item.getItemId() == MENU_ITEM_EDIT ){
            Intent intent = new Intent(this, AddEditCategoryActivity.class);
            intent.putExtra("category",  selectedCategory);

            // Start AddEditCategoryActivity, (with feedback).
            this.startActivityForResult(intent,MY_REQUEST_CODE);
        }
        else if(item.getItemId() == MENU_ITEM_DELETE){
            // Ask before deleting.
            new AlertDialog.Builder(this)
                    .setMessage(selectedCategory.getsName()+". Are you sure you want to delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deleteCategory(selectedCategory);
                            notificationManager.notify(100, builder.build());
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
    private void deleteCategory(CategoryProduct category)  {
        DatabaseHandler db = new DatabaseHandler(this);
        db.deleteCategoryProduct(category);
        this.categoryList.remove(category);
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
                this.categoryList.clear();
                DatabaseHandler db = new DatabaseHandler(this);
                List<CategoryProduct> list = db.getListCategoryProduct();
                this.categoryList.addAll(list);


                // Notify the data change (To refresh the ListView).
                this.listViewAdapter.notifyDataSetChanged();
            }
        }
    }

}