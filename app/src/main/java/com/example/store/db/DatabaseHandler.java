package com.example.store.db;

import static com.example.store.constants.DB.TABLE_BILL;
import static com.example.store.constants.DB.TABLE_BILLDETAIL;
import static com.example.store.constants.DB.TABLE_CART;
import static com.example.store.constants.DB.TABLE_CATEGORYPRODUCT;
import static com.example.store.constants.DB.TABLE_PRODUCT;
import static com.example.store.constants.DB.TABLE_ROLE;
import static com.example.store.constants.DB.TABLE_USER;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;


import com.example.store.bean.Bill;
import com.example.store.bean.BillDetail;
import com.example.store.bean.Cart;
import com.example.store.bean.CategoryProduct;
import com.example.store.bean.Product;
import com.example.store.bean.Role;
import com.example.store.bean.User;
import com.example.store.constants.DB;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context) {
        super(context, DB.DATABASE_NAME, null, DB.DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        initDB(db);
    }


    public void initDB(SQLiteDatabase db) {
        String CREATE_CATEGORYPRODUCT_TABLE = "CREATE TABLE " + TABLE_CATEGORYPRODUCT + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name NVARCHAR(255) NOT NULL UNIQUE,"
                + "source BLOB(255)"
                + ")";
        db.execSQL(CREATE_CATEGORYPRODUCT_TABLE);

        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_PRODUCT
                + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name NVARCHAR(255) NOT NULL UNIQUE,"
                + "category int NOT NULL,"
                + "price DOUBLE DEFAULT(0) NOT NULL,"
                + "description VARCHAR(255),"
                + "source BLOB,"
                + "quantity int NOT NULL DEFAULT 0 CHECK(quantity < 1000),"
                + "state int NOT NULL DEFAULT 0,"
                + "CONSTRAINT fk_product_idcategory FOREIGN KEY(category) REFERENCES categoryproduct(id)"
                + ")";
        db.execSQL(CREATE_PRODUCT_TABLE);

        String CREATE_ROLE_TABLE = "CREATE TABLE " + TABLE_ROLE
                + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name VARCHAR(255) NOT NULL UNIQUE"
                + ")";

        db.execSQL(CREATE_ROLE_TABLE);
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER
                + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name NVARCHAR(255) NOT NULL,"
                + "email VARCHAR(255) NOT NULL UNIQUE,"
                + "phone VARCHAR(10) NOT NULL UNIQUE,"
                + "password VARCHAR(255),"
                + "verifyemail INTEGER,"
                + "state INTEGER,"
                + "role INTEGER,"
                + "source BLOB(255),"
                + "address NVARCHAR(255),"
                + "CONSTRAINT fk_user_idrole FOREIGN KEY(role) REFERENCES role(id)"
                + ")";
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART
                + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "idproduct INTEGER NOT NULL,"
                + "iduser INTEGER NOT NULL,"
                + "quantity INTEGER CHECK(quantity > 0),"
                + "checked INTEGER DEFAULT 0,"
                + "CONSTRAINT fk_cart_idproduct FOREIGN KEY(idproduct) REFERENCES product(id),"
                + "CONSTRAINT fk_cart_iduser FOREIGN KEY(iduser) REFERENCES user(id)"
                + ")";

        db.execSQL(CREATE_CART_TABLE);

        String CREATE_BILL_TABLE = "CREATE TABLE " + TABLE_BILL
                + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "iduser INTEGER NOT NULL,"
                + "quantity INTEGER NOT NULL,"
                + "personname NVARCHAR(255) NOT NULL,"
                + "phone VARCHAR(10) NOT NULL,"
                + "address NVARCHAR(255) NOT NULL,"
                + "date TEXT NOT NULL,"
                + "state INT DEFAULT 0," // 0. Dang giao 1. Da nhan 2. Da huy
                + "totalprice DOUBLE NOT NULL,"
                + "CONSTRAINT fk_bill_iduser FOREIGN KEY(iduser) REFERENCES user(id)"
                + ")";

        db.execSQL(CREATE_BILL_TABLE);

        String CREATE_BILLDETAIL_TABLE = "CREATE TABLE " + TABLE_BILLDETAIL
                + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "idbill INTEGER NOT NULL,"
                + "idproduct INTEGER NOT NULL,"
                + "quantity INTEGER NOT NULL,"
                + "unitprice DECIMAL DEFAULT(0) NOT NULL,"
                + "CONSTRAINT fk_billdetail_idbill FOREIGN KEY(idbill) REFERENCES bill(id)"
                + ")";
        db.execSQL(CREATE_BILLDETAIL_TABLE);
        String INSEST_VALUE_TABLEROLE_ADMIN = "INSERT INTO " + TABLE_ROLE + " VALUES(1, 'ADMIN')";
        String INSEST_VALUE_TABLEROLE_USER = "INSERT INTO " + TABLE_ROLE + " VALUES(2, 'USER')";
        db.execSQL(INSEST_VALUE_TABLEROLE_ADMIN);
        db.execSQL(INSEST_VALUE_TABLEROLE_USER);

        String INSERT_VALUE_USER = "INSERT INTO user (name, email, phone, password, verifyemail, state, role)\n" +
                "VALUES\n" +
                "('admin', 'admin@gmail.com', '0123456789', 'abcdabcd', 1, 1, 1),\n" +
                "('user1', 'user@gmail.com', '0987654321', 'abcdabcd', 1, 1, 2);";
        String INSERT_VALUE_CATEGORY_PRODUCT = "INSERT INTO categoryproduct (name, source)\n" +
                "VALUES \n" +
                "    ('Beverages', NULL),\n" +
                "    ('Snacks', NULL),\n" +
                "    ('Sandwiches', NULL);\n";
        String INSERT_VALUE_PRODUCT = "INSERT INTO product (name, category, price, description, source, quantity, state)\n" +
                "VALUES \n" +
                "    ('Cola', 1, 2000, '12 oz can', NULL, 100, 1),\n" +
                "    ('Chips', 2, 1000, 'Bag of potato chips', NULL, 50, 1),\n" +
                "    ('Turkey Club', 3, 8000, 'Bacon, lettuce, tomato, turkey', NULL, 25, 1),\n" +
                "    ('Burger', 3, 6000, 'Quarter-pound beef patty with lettuce, tomato, onion, and pickles', NULL, 50, 1),\n" +
                "    ('Fries', 2, 2000, 'Large order of French fries', NULL, 100, 1),\n" +
                "    ('Hot Dog', 3, 3000, 'All-beef hot dog with ketchup, mustard, and relish', NULL, 25, 1),\n" +
                "    ('Pizza', 3, 12000, 'Large cheese pizza with tomato sauce and crust', NULL, 10, 1),\n" +
                "    ('Chicken Tenders', 3, 8000, 'Crispy chicken strips with dipping sauce', NULL, 30, 1),\n" +
                "    ('Soft Drink', 1, 1000, '20 oz cup of soda', NULL, 75, 1),\n" +
                "    ('Ice Cream', 2, 3000, 'Single scoop of vanilla ice cream', NULL, 20, 1),\n" +
                "    ('Taco', 3, 2000, 'Beef or chicken taco with lettuce, tomato, and cheese', NULL, 40, 1),\n" +
                "    ('Nachos', 2, 4000, 'Tortilla chips with melted cheese and jalapenos', NULL, 15, 1),\n" +
                "    ('Burrito', 3, 7000, 'Large burrito with rice, beans, meat, cheese, and salsa', NULL, 20, 1);";
        db.execSQL(INSERT_VALUE_USER);
        db.execSQL(INSERT_VALUE_CATEGORY_PRODUCT);
        db.execSQL(INSERT_VALUE_PRODUCT);

    }

    // Upgrading database
    // Auto call when exist DB on Storage but another version.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Delete old tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORYPRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLDETAIL);
        // Create new tables
        onCreate(db);
    }

    //region ROLE

    public void insertRole(Role role) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", role.getsName());
        db.insert(TABLE_ROLE, null, values);
        db.close();
    }


    //endregion

    //region CATEGORY PRODUCT
    public void insertCategoryProduct(CategoryProduct categoryProduct) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", categoryProduct.getsName());
        values.put("source", categoryProduct.getsSource());
        db.insert(TABLE_CATEGORYPRODUCT, null, values);
        db.close();
    }

    public int updateCategoryProduct(CategoryProduct categoryProduct) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", categoryProduct.getsName());
        values.put("source", categoryProduct.getsSource());
        return db.update(TABLE_CATEGORYPRODUCT, values, "id = ?", new String[]{String.valueOf(categoryProduct.getiID())});
    }

    public void deleteCategoryProduct(CategoryProduct categoryProduct) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCT, "category = ?", new String[]{String.valueOf(categoryProduct.getiID())});
        db.delete(TABLE_CATEGORYPRODUCT, "id = ? ", new String[]{String.valueOf(categoryProduct.getiID())});
        db.close();
    }

    public CategoryProduct getCategoryProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CATEGORYPRODUCT + " WHERE id = " + String.valueOf(id);

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        CategoryProduct categoryProduct = new CategoryProduct();
        categoryProduct.setiID(Integer.parseInt(cursor.getString(0)));
        categoryProduct.setsName(cursor.getString(1));
        categoryProduct.setsSource(cursor.getBlob(2));
        cursor.close();
        return categoryProduct;
    }

    public List<CategoryProduct> getListCategoryProduct() {
        List<CategoryProduct> categoryProductList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_CATEGORYPRODUCT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                CategoryProduct categoryProduct = new CategoryProduct();
                categoryProduct.setiID(Integer.parseInt(cursor.getString(0)));
                categoryProduct.setsName(cursor.getString(1));
                categoryProduct.setsSource(cursor.getBlob(2));
                // Adding contact to list
                categoryProductList.add(categoryProduct);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categoryProductList;
    }

    //
    public List<Role> getListRoles() {
        List<Role> roleList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_ROLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Role role = new Role();
                role.setiID(Integer.parseInt(cursor.getString(0)));
                role.setsName(cursor.getString(1));
                // Adding contact to list
                roleList.add(role);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return roleList;
    }

    //
    public Role getRoleById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ROLE + " WHERE id = " + String.valueOf(id);

        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        Role role = new Role();
        role.setiID(Integer.parseInt(cursor.getString(0)));
        role.setsName(cursor.getString(1));
        cursor.close();
        return role;
    }
//endregion
//
//    //region PRODUCT
    public void insertProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", product.getsName());
        values.put("category", product.getiIDCategory());
        values.put("price", product.getlPrice());
        values.put("description", product.getsDescription());
        values.put("source", product.getsSource());
        values.put("quantity", product.getiQuantity());
        values.put("state", product.getiState());
        db.insert(TABLE_PRODUCT, null, values);
        db.close();
    }

    public int updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", product.getsName());
        values.put("category", product.getiIDCategory());
        values.put("price", product.getlPrice());
        values.put("description", product.getsDescription());
        values.put("source", product.getsSource());
        values.put("quantity", product.getiQuantity());
        values.put("state", product.getiState());
        return db.update(TABLE_PRODUCT, values, "id = ?", new String[]{String.valueOf(product.getiID())});
    }

    public void deleteProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_PRODUCT, "id = ? ", new String[]{String.valueOf(product.getiID())});
        db.close();
    }
    public void updateQuantityProduct(int quantity, int product_id ) {
        String query = "UPDATE product SET quantity = quantity - ? WHERE id = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement statement = db.compileStatement(query);
        statement.clearBindings();
        statement.bindLong(1, quantity);
        statement.bindLong(2, product_id);
        statement.execute();
        db.close();
    }

    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_USER, "id = ? ", new String[]{String.valueOf(user.getiID())});
        db.close();
    }

    public Product getProduct(int id) {
        String query = "SELECT * FROM " + TABLE_PRODUCT + " WHERE id = ? ";
        Product product = new Product();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{id + ""});
        if (cursor.moveToFirst()) {
            product.setiID(cursor.getInt(0));
            product.setsName(cursor.getString(1));
            product.setiIDCategory(cursor.getInt(2));
            product.setlPrice(cursor.getLong(3));
            product.setsDescription(cursor.getString(4));
            product.setsSource(cursor.getBlob(5));
            product.setiQuantity(cursor.getInt(6));
            product.setiState(cursor.getInt(7));
        }
        cursor.close();
        return product;
    }

    public List<Product> getListProduct() {
        List<Product> productList = new ArrayList<Product>();
        String query = "SELECT * FROM " + TABLE_PRODUCT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setiID(cursor.getInt(0));
                product.setsName(cursor.getString(1));
                product.setiIDCategory(cursor.getInt(2));
                product.setlPrice(cursor.getLong(3));
                product.setsDescription(cursor.getString(4));
                product.setsSource(cursor.getBlob(5));
                product.setiQuantity(cursor.getInt(6));
                product.setiState(cursor.getInt(7));
                // Adding contact to list
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public List<Product> getListProductByCategory(int idcategory) {
        List<Product> productList = new ArrayList<Product>();
        String query = "SELECT * FROM " + TABLE_PRODUCT + " WHERE category = ? AND state = 1 ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{idcategory + ""});
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setiID(cursor.getInt(0));
                product.setsName(cursor.getString(1));
                product.setiIDCategory(idcategory);
                product.setlPrice(cursor.getLong(3));
                product.setsDescription(cursor.getString(4));
                product.setsSource(cursor.getBlob(5));
                product.setiQuantity(cursor.getInt(6));
                product.setiState(cursor.getInt(7));
                // Adding contact to list
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    //endregion

    //    //region REGISTER
    public boolean checkIfEmailExists(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select email from " + TABLE_USER;
        Cursor cursor = db.rawQuery(query, null);
        String existEmail;

        if (cursor.moveToFirst()) {
            do {

                existEmail = cursor.getString(0);
                if (existEmail.equals(userEmail)) {
                    return true;
                }
            } while (cursor.moveToNext());
        }
        return false;
    }

    //
    public boolean checkIfPhoneExists(String userPhone) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select phone from " + TABLE_USER;
        Cursor cursor = db.rawQuery(query, null);
        String existPhone;

        if (cursor.moveToFirst()) {
            do {
                existPhone = cursor.getString(0);
                if (existPhone.equals(userPhone)) {
                    return true;
                }
            } while (cursor.moveToNext());
        }
        return false;
    }

    //
    public boolean checkIfPhoneExistsEdit(String userPhone) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select phone from " + TABLE_USER;
        Cursor cursor = db.rawQuery(query, null);
        String existPhone;
        int check = 0;
        if (cursor.moveToFirst()) {
            do {
                existPhone = cursor.getString(0);
                if (existPhone.equals(userPhone)) {
                    check = check + 1;
                }
            } while (cursor.moveToNext());
            if (check != 1)
                return true;
        }
        return false;
    }

    //
    public boolean checkOldPassword(int id, String oldpass) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select password from " + TABLE_USER + " where id = ? ";
        Cursor cursor = db.rawQuery(query, new String[]{id + ""});
        String oldPass;
        if (cursor.moveToFirst()) {
            do {
                oldPass = cursor.getString(0);
                if (oldPass.equals(oldpass))
                    return true;
            } while (cursor.moveToNext());
        }
        return false;
    }

    //
    public void registerUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", user.getsName());
        values.put("email", user.getsEmail());
        values.put("phone", user.getsPhone());
        values.put("password", user.getsPassword());
        values.put("verifyemail", user.getiVerifyEmail());
        values.put("state", user.getiState());
        values.put("role", user.getiRole());
        values.put("source", user.getsSource());
        values.put("address", user.getsAddress());
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    //    //endregion
//
//    //region User
    public List<User> getListUser() {
        List<User> userList = new ArrayList<User>();
        String query = "SELECT id,name,email,phone,role,state,source,address FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setiID(cursor.getInt(0));
                user.setsName(cursor.getString(1));
                user.setsEmail(cursor.getString(2));
                user.setsPhone(cursor.getString(3));
                user.setiRole(cursor.getInt(4));
                user.setbState(cursor.getInt(5));
                user.setsSource(cursor.getBlob(6));
                user.setsAddress(cursor.getString(7));
                // Adding contact to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return userList;
    }

    //
    public int getIDUser(String emailInput) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT id FROM " + TABLE_USER + " WHERE email = '" + emailInput + "'";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        int iduser = Integer.parseInt(cursor.getString(0));
        cursor.close();
        return iduser;
    }

    //
    public User getUser(int id) {
        String query = "SELECT id,name,email,phone,role,state,source,address FROM " + TABLE_USER + " WHERE id = ? ";
        User user = new User();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{id + ""});
        if (cursor.moveToFirst()) {
            user.setiID(cursor.getInt(0));
            user.setsName(cursor.getString(1));
            user.setsEmail(cursor.getString(2));
            user.setsPhone(cursor.getString(3));
            user.setiRole(cursor.getInt(4));
            user.setbState(cursor.getInt(5));
            user.setsSource(cursor.getBlob(6));
            user.setsAddress(cursor.getString(7));
        }
        cursor.close();
        return user;
    }
    public User getUserByEmail(String email) {
        String query = "SELECT id,name,email,phone,role,state,source,address FROM " + TABLE_USER + " WHERE email = ? ";
        User user = new User();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{email});
        if (cursor.moveToFirst()) {
            user.setiID(cursor.getInt(0));
            user.setsName(cursor.getString(1));
            user.setsEmail(cursor.getString(2));
            user.setsPhone(cursor.getString(3));
            user.setiRole(cursor.getInt(4));
            user.setbState(cursor.getInt(5));
            user.setsSource(cursor.getBlob(6));
            user.setsAddress(cursor.getString(7));
        }
        cursor.close();
        return user;
    }

    //
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", user.getsName());
        values.put("phone", user.getsPhone());
        values.put("address", user.getsAddress());
        values.put("source", user.getsSource());

        return db.update(TABLE_USER, values, "id = ?", new String[]{String.valueOf(user.getiID())});

    }

    //
    public int changePassword(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("password", user.getsPassword());

        return db.update(TABLE_USER, values, "id = ?", new String[]{String.valueOf(user.getiID())});

    }

    //    //endregion
//
//    //region Login
    public boolean checkLogin(String userEmail, String passWord) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select email,password from " + TABLE_USER;
        Cursor cursor = db.rawQuery(query, null);
        String email;
        String password;

        if (cursor.moveToFirst()) {
            do {
                email = cursor.getString(0);
                password = cursor.getString(1);
                if (email.equals(userEmail) && password.equals(passWord)) {
                    return true;
                }
            } while (cursor.moveToNext());
        }
        return false;
    }
//    //endregion
//
//    //region Cart
    public void insertItemCart(Cart cart){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("idproduct", cart.getiIDProduct());
        values.put("iduser", cart.getiIDUser());
        values.put("quantity", cart.getiQuantity());

        db.insert(TABLE_CART, null, values);
        db.close();
    }

    public void undoItemCart(Cart cart){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("id", cart.getiID());
        values.put("idproduct", cart.getiIDProduct());
        values.put("iduser", cart.getiIDUser());
        values.put("quantity", cart.getiQuantity());

        db.insert(TABLE_CART, null, values);
        db.close();
    }

    public int updateQuantityCart(Cart cart){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("idproduct", cart.getiIDProduct());
        values.put("iduser", cart.getiIDUser());
        values.put("quantity", cart.getiQuantity());

        return db.update(TABLE_CART, values, "id = ?", new String[]{String.valueOf(cart.getiID())});
    }

    public void deleteItemCart(Cart cart){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_CART, "id = ? ", new String[]{String.valueOf(cart.getiID())});
        db.close();
    }

    public List<Cart> getListCartOfUser(int iduser){
        List<Cart> cartList = new ArrayList<Cart>();
        String query = "SELECT * FROM " + TABLE_CART + " WHERE iduser = " + String.valueOf(iduser);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Cart cart = new Cart();
                cart.setiID(cursor.getInt(0));
                cart.setiIDProduct(cursor.getInt(1));
                cart.setiIDUser(iduser);
                cart.setiQuantity(cursor.getInt(3));
                cart.setiChecked(cursor.getInt(4));
                // Adding contact to list
                cartList.add(cart);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartList;
    }

    public List<Cart> getListCartOfUserChecked(int iduser){
        List<Cart> cartList = new ArrayList<Cart>();
        String query = "SELECT * FROM " + TABLE_CART + " WHERE iduser = " + String.valueOf(iduser) + " AND checked = 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Cart cart = new Cart();
                cart.setiID(cursor.getInt(0));
                cart.setiIDProduct(cursor.getInt(1));
                cart.setiIDUser(iduser);
                cart.setiQuantity(cursor.getInt(3));
                cart.setiChecked(cursor.getInt(4));
                // Adding contact to list
                cartList.add(cart);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartList;
    }

    public int hasProductInCart(int idproduct, int iduser) {
        List<Cart> cartList = new ArrayList<Cart>();
        String query = "SELECT id FROM " + TABLE_CART + " WHERE iduser = ? AND idproduct = ? ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{iduser + "", idproduct + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return 0;
    }

    public Cart getCart(int id){
        String query = "SELECT * FROM " + TABLE_CART + " WHERE id = ? ";
        Cart cart = new Cart();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{id + ""});
        if (cursor.moveToFirst()) {
            cart.setiID(cursor.getInt(0));
            cart.setiIDProduct(cursor.getInt(1));
            cart.setiIDUser(cursor.getInt(2));
            cart.setiQuantity(cursor.getInt(3));
            cart.setiChecked(cursor.getInt(4));
        }
        cursor.close();
        return cart;
    }

    public int checkedItemCart(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("checked", 1);
        return db.update(TABLE_CART, values, "id = ?", new String[]{String.valueOf(id)});
    }

    public int checkedAllItemCart(int iduser){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("checked", 1);
        return db.update(TABLE_CART, values, "iduser = ?", new String[]{String.valueOf(iduser)});
    }

    public int unCheckedItemCart(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("checked", 0);
        return db.update(TABLE_CART, values, "id = ?", new String[]{String.valueOf(id)});
    }

    public int unCheckedAllItemCart(int iduser){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("checked", 0);
        return db.update(TABLE_CART, values, "iduser = ?", new String[]{String.valueOf(iduser)});
    }

    public int itemCheckedSize(int iduser){
        String query = "SELECT count(id) FROM " + TABLE_CART + " WHERE iduser = ? AND checked = 1 " ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{iduser + ""});
        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return 0;
    }

    public double totalPriceCheckedInCart(double iduser){
        String query = "SELECT sum(product.price * cart.quantity) FROM " + TABLE_CART + " INNER JOIN " + TABLE_PRODUCT + " WHERE iduser = ? AND cart.checked = 1 AND cart.idproduct = product.id" ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{iduser + ""});
        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return 0;
    }

    public int totalQuantityCheckedInCart(int iduser){
        String query = "SELECT sum(cart.quantity) FROM " + TABLE_CART + " WHERE iduser = ? AND cart.checked = 1" ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{iduser + ""});
        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return 0;
    }
    //endregion

    //region BILL
    public int insertBill(Bill bill){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("iduser", bill.getiIDUser());
        values.put("quantity", bill.getiQuantity());
        values.put("personname", bill.getsPersonName());
        values.put("phone", bill.getsPhone());
        values.put("address", bill.getsAddress());
        values.put("date", bill.getsDate());
        values.put("state", bill.getiState());
        values.put("totalprice", bill.getlTotalPrice());
        db.insert(TABLE_BILL, null, values);
        db.close();
        db = this.getReadableDatabase();
        String query = "SELECT MAX(id)  FROM " + TABLE_BILL ;
        Cursor cursor = db.rawQuery(query,null);
        int idbill = 0;
        if(cursor.moveToFirst()){
             idbill = cursor.getInt(0);
        }
        return idbill;
    }

    public int updateStateBill(Bill bill){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("state", bill.getiState());
        return db.update(TABLE_BILL, values, "id = ?", new String[]{String.valueOf(bill.getiID())});
    }
    public List<Bill> getListBill() {
        List<Bill> billList = new ArrayList<Bill>();
        String query = "SELECT id, date, state FROM " + TABLE_BILL + " ORDER BY id DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Bill bill = new Bill();
                bill.setiID(cursor.getInt(0));
                bill.setsDate(cursor.getString(1));
                bill.setiState(cursor.getInt(2));
                // Adding contact to list
                billList.add(bill);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return billList;
    }

    public List<Bill> getListBillOfUser(int iduser){
        List<Bill> billList = new ArrayList<Bill>();
        String query = "SELECT id, date, state FROM " + TABLE_BILL + " WHERE iduser = " + String.valueOf(iduser) + " ORDER BY id DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Bill bill = new Bill();
                bill.setiID(cursor.getInt(0));
                bill.setsDate(cursor.getString(1));
                bill.setiState(cursor.getInt(2));
                // Adding contact to list
                billList.add(bill);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return billList;
    }

    public Bill getBill(int id){
        String query = "SELECT personname, phone, address, quantity, state, totalprice FROM " + TABLE_BILL + " WHERE id = ? ";
        Bill bill = new Bill();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{id + ""});
        if (cursor.moveToFirst()) {
            bill.setsPersonName(cursor.getString(0));
            bill.setsPhone(cursor.getString(1));
            bill.setsAddress(cursor.getString(2));
            bill.setiQuantity(cursor.getInt(3));
            bill.setiState(cursor.getInt(4));
            bill.setlTotalPrice(cursor.getLong(5));
        }
        cursor.close();
        return bill;
    }
    //endregion

    //region BILL DETAIL
    public void insertBillDetail(BillDetail bill){
        updateQuantityProduct(bill.getiQuantity(),bill.getiIDProduct());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idbill", bill.getiIDBill());
        values.put("idproduct", bill.getiIDProduct());
        values.put("quantity", bill.getiQuantity());
        values.put("unitprice", bill.getlUnitPrice());
        db.insert(TABLE_BILLDETAIL, null, values);
        db.close();
    }

    public List<BillDetail> getListBillDetail(int idbill){
        List<BillDetail> billList = new ArrayList<BillDetail>();
        String query = "SELECT idproduct, quantity, unitprice FROM " + TABLE_BILLDETAIL + " WHERE idbill = " + String.valueOf(idbill);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                BillDetail bill = new BillDetail();
                bill.setiIDProduct(cursor.getInt(0));
                bill.setiQuantity(cursor.getInt(1));
                bill.setlUnitPrice(cursor.getLong(2));
                // Adding contact to list
                billList.add(bill);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return billList;
    }
    //endregion

    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Kiểm tra tài khoản trong bảng user
        String[] columns = { "email" };
        String selection = "email" + " = ?";
        String[] selectionArgs = { email };
        Cursor cursor = db.query("user", columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }
}

