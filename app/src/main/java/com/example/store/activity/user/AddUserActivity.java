package com.example.store.activity.user;

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

import java.util.ArrayList;
import java.util.List;

import com.example.store.R;
import com.example.store.activity.user.AddEditUserActivity;
import com.example.store.bean.User;
import com.example.store.db.DatabaseHandler;

public class AddUserActivity extends AppCompatActivity {
    private ListView listView;
    private static final int MENU_ITEM_VIEW = 111;
    private static final int MENU_ITEM_EDIT = 222;
    private static final int MENU_ITEM_CREATE = 333;
    private static final int MENU_ITEM_DELETE = 444;
    private static final int MY_REQUEST_CODE = 1000;
    private static final String CHANNEL_ID = "user";

    private final List<User> userList = new ArrayList<>();
    private ArrayAdapter<User> listViewAdapter;
    private NotificationCompat.Builder builder;
    private NotificationManagerCompat notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        // Get ListView object from xml
        this.listView = (ListView) findViewById(R.id.listViewUser);
        DatabaseHandler db = new DatabaseHandler(this);

        List<User> listViewUser = db.getListUser();
        this.userList.addAll(listViewUser);


        this.listViewAdapter = new ArrayAdapter<User>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, this.userList);

        // Assign adapter to ListView
        this.listView.setAdapter(this.listViewAdapter);

        // Register the ListView for Context menu
        registerForContextMenu(this.listView);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "user channel";
            String description = "Xoa user";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle("Select The Action");

        menu.add(0, MENU_ITEM_CREATE, 1, "Create user");
        menu.add(0, MENU_ITEM_EDIT, 2, "Edit user");
        menu.add(0, MENU_ITEM_DELETE, 4, "Delete user");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final User selectedUser = (User) this.listView.getItemAtPosition(info.position);

        if (item.getItemId() == MENU_ITEM_VIEW) {
            Toast.makeText(getApplicationContext(), selectedUser.toString(), Toast.LENGTH_LONG).show();
        } else if (item.getItemId() == MENU_ITEM_CREATE) {
            Intent intent = new Intent(this, AddEditUserActivity.class);

            // Start AddEditCategoryActivity, (with feedback).
            this.startActivityForResult(intent, MY_REQUEST_CODE);
        } else if (item.getItemId() == MENU_ITEM_EDIT) {
            Intent intent = new Intent(this, AddEditUserActivity.class);
            intent.putExtra("user", selectedUser);

            // Start AddEditCategoryActivity, (with feedback).
            this.startActivityForResult(intent, MY_REQUEST_CODE);
        } else if (item.getItemId() == MENU_ITEM_DELETE) {
            // Ask before deleting.
            new AlertDialog.Builder(this)
                    .setMessage(selectedUser.getsName() + ". Are you sure you want to delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(AddUserActivity.this, "Xoá thành công", Toast.LENGTH_SHORT).show();
                            deleteProduct(selectedUser);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            return false;
        }
        return true;
    }

    // Delete a record
    private void deleteProduct(User user) {
        DatabaseHandler db = new DatabaseHandler(this);
        db.deleteUser(user);
        this.userList.remove(user);
        // Refresh ListView.
        this.listViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == MY_REQUEST_CODE) {
            boolean needRefresh = data.getBooleanExtra("needRefresh", true);
            // Refresh ListView
            if (needRefresh) {
                this.userList.clear();
                DatabaseHandler db = new DatabaseHandler(this);
                List<User> list = db.getListUser();
                this.userList.addAll(list);

                // Notify the data change (To refresh the ListView).
                this.listViewAdapter.notifyDataSetChanged();
            }
        }
    }
}