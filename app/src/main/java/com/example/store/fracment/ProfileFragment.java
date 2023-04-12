package com.example.store.fracment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.store.R;
import com.example.store.activity.bill.BillActivity;
import com.example.store.activity.product.AddProductActivity;
import com.example.store.activity.user.AddUserActivity;
import com.example.store.activity.user.ChangePasswordActivity;
import com.example.store.activity.user.EditProfileActivity;
import com.example.store.activity.login.LoginActivity;
import com.example.store.bean.Role;
import com.example.store.bean.User;
import com.example.store.db.DatabaseHandler;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_IDUSER = "iduser";
    private static final String ARG_IDROLE = "idRole";
    private TextView tv_name;
    private ImageView iv_avatar;
    private String mParamIDUser;
    private String mparamIDRole;
    private User user;
    private Role role;
    private ListView lv_profile;
    private DatabaseHandler db;

    public static final int REQUEST_CODE_EDIT = 0;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
            mparamIDRole = getArguments().getString(ARG_IDROLE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        tv_name = view.findViewById(R.id.tv_nameuser);
        iv_avatar = view.findViewById(R.id.iv_avatar_user);
        db = new DatabaseHandler(getContext());
        user = db.getUser(Integer.parseInt(mParamIDUser));
        setValue();

        // Inflate the layout for this fragment
        String[] lItem = {"Edit Profile", "Change Password", "Order History", "Log Out"};
        if (Integer.parseInt(mparamIDRole) == 1) {
            lItem = new String[]{"Edit Profile", "Change Password", "Order History", "Management Product", "Management Category", "Management User", "Log Out"};
        }

        lv_profile = (ListView) view.findViewById(R.id.lv_profile);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, lItem);
        lv_profile.setAdapter(adapter);
        if (Integer.parseInt(mparamIDRole) == 1) {
            lv_profile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            Intent intentEdit = new Intent(getContext(), EditProfileActivity.class);
                            intentEdit.putExtra(ARG_IDUSER, mParamIDUser);
                            startActivityForResult(intentEdit, REQUEST_CODE_EDIT);
                            break;
                        case 1:
                            Intent intentChange = new Intent(getContext(), ChangePasswordActivity.class);
                            intentChange.putExtra(ARG_IDUSER, mParamIDUser);
                            startActivity(intentChange);
                            break;
                        case 2:
                            Intent intentHistory = new Intent(getContext(), BillActivity.class);
                            intentHistory.putExtra(ARG_IDUSER, mParamIDUser);
                            startActivity(intentHistory);
                            break;
                        case 3:
                            Intent intentAddProduct = new Intent(getContext(), AddProductActivity.class);
                            intentAddProduct.putExtra(ARG_IDUSER, mParamIDUser);
                            startActivity(intentAddProduct);
                            break;
                        case 4:
//                            Intent intentAddCategory = new Intent(getContext(), AddCategoryActivity.class);
//                            intentAddCategory.putExtra(ARG_IDUSER, mParamIDUser);
//                            startActivity(intentAddCategory);
//                            break;
                        case 5:
                            Intent intentAddUser = new Intent(getContext(), AddUserActivity.class);
                            intentAddUser.putExtra(ARG_IDUSER, mParamIDUser);
                            startActivity(intentAddUser);
                            break;
                        case 6:
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("Are you sure you want to exit?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            getActivity().finish();
                                            Intent intentLogin = new Intent(getContext(), LoginActivity.class);
                                            startActivity(intentLogin);
                                        }
                                    })
                                    .setNegativeButton("No", null);
                            AlertDialog alert = builder.create();
                            alert.show();
                            break;
                    }
                }
            });
        } else {
            lv_profile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            Intent intentEdit = new Intent(getContext(), EditProfileActivity.class);
                            intentEdit.putExtra(ARG_IDUSER, mParamIDUser);
                            startActivityForResult(intentEdit, REQUEST_CODE_EDIT);
                            break;
                        case 1:
                            Intent intentChange = new Intent(getContext(), ChangePasswordActivity.class);
                            intentChange.putExtra(ARG_IDUSER, mParamIDUser);
                            startActivity(intentChange);
                            break;
                        case 2:
                            Intent intentHistory = new Intent(getContext(), BillActivity.class);
                            intentHistory.putExtra(ARG_IDUSER, mParamIDUser);
                            startActivity(intentHistory);
                            break;
                        case 3:
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("Are you sure you want to exit?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            getActivity().finish();
                                            Intent intentLogin = new Intent(getContext(), LoginActivity.class);
                                            startActivity(intentLogin);
                                        }
                                    })
                                    .setNegativeButton("No", null);
                            AlertDialog alert = builder.create();
                            alert.show();
                            break;
                    }
                }
            });
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_EDIT && resultCode == Activity.RESULT_OK) {
            setValue();
        }
    }

    public void setValue() {
        user = db.getUser(Integer.parseInt(mParamIDUser));
        tv_name.setText(user.getsName());
        if (user.getsSource() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(user.getsSource(), 0, user.getsSource().length);
            iv_avatar.setImageBitmap(bitmap);
        }
    }
}