package app.dataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import app.model.User;


public class ConnectionFactory extends SQLiteOpenHelper {

    //Declararea bazei de date
    private static final String dbName = "Chat.db";
    private static final String usersTable = "Users";
    private static final String messageTable = "Messages";

    //coloanele tabelei Users
    private static final String userName = "UserName";
    private static final String password = "Password";

    //coloanele tabelei Messages
    private static final String sender = "Sender";
    private static final String receiver = "Receiver";
    private static final String message = "Content";

    private static final String createUserTable = "CREATE TABLE " + usersTable +
            " (" + userName + " TEXT, " + password + " TEXT )";
    private static final String createMessageTable = "CREATE TABLE " + messageTable +
            " (" + sender + " TEXT, " + receiver + " TEXT, " + message + " TEXT )";

    public ConnectionFactory(Context context) {
        super(context, dbName, null, 1);
    }

    //crearea tabelului
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createUserTable);
        db.execSQL(createMessageTable);

        //inserez date "default" in baza de date LA CREAREA TABELEI
        User u1[] = {new User("Admin", "1234"),
                new User("Ion", "1234"),
                new User("Dan", "1234"),
                new User("Andrei", "1234"),
                new User("MaxInfinite", "1234"),
                new User("Colo", "1234"),
                new User("Foca", "1234"),
                new User("Mikey Hash", "1234"),
                new User("Bill Gates", "1234"),
                new User("Mark Zuckerberg", "1234"),};
        for (User i : u1) {
            db.execSQL("INSERT INTO " + usersTable + "(UserName, Password) VALUES('"
                    + i.getUserName() + "', " + "'" + i.getPassword() + "')");
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + usersTable);
        db.execSQL("DROP TABLE IF EXISTS " + messageTable);
        onCreate(db);
    }

    //inserare user in tabel
    public boolean insertUser(String name, String pass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(userName, name);
        content.put(password, pass);
        if (db.insert(usersTable, null, content) != -1)
            return true;
        return false;
    }

    public boolean insertMessage(String sender, String receiver, String mess) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(this.sender, sender);
        content.put(this.receiver, receiver);
        content.put(this.message, mess);
        if (db.insert(messageTable, null, content) != -1)
            return true;
        return false;
    }

    public Cursor viewData(String table) {
        SQLiteDatabase db = this.getReadableDatabase();
        String querry = "SELECT * FROM " + table;
        Cursor cursor = db.rawQuery(querry, null);
        if (cursor.getCount() != -1) {
            return cursor;
        } else {
            return null;
        }
    }

    public boolean searchUser(String name, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        String querry = "Select * FROM " + usersTable + " WHERE " +
                userName + " LIKE '%" + name + "%' AND " +
                password + " LIKE '%" + pass + "%'";
        Cursor cursor = db.rawQuery(querry, null);
        if (cursor.getCount() > 0)
            return true;
        return false;
    }

    public void deleteUser(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String querry = "DELETE FROM " + usersTable + " WHERE " +
                userName + " = '" + name + "'";
        db.execSQL(querry);
    }

    public void deleteMessagesFromUser(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String querry = "DELETE FROM " + messageTable + " WHERE " +
                sender + " = '" + name + "'" + " OR " + receiver + " = '" + name + "'";
        db.execSQL(querry);
    }

    //sterge tot inafara de Administrator
    public void clearUsersTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM Users WHERE UserName NOT IN('Admin')");
    }

    public void clearMesagesTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM Messages");
    }
}
