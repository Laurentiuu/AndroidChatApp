package app.main;

import android.content.Intent;
import android.database.Cursor;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import app.dataAccess.ConnectionFactory;

import java.util.ArrayList;

public class AdministratorActivity extends AppCompatActivity {

    private ConnectionFactory db;
    private ListView listView;
    private ImageView popIcon;

    ArrayList<String> names = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administrator_layout);

        listView = findViewById(R.id.listView);
        this.popIcon = findViewById(R.id.popIcon);
        db = new ConnectionFactory(this);

        final String logedUser = getIntent().getStringExtra("NAME");
        Cursor cursor = db.viewData("Users");

        //parcurg coloana Username din baza de date
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                if (!name.equals(logedUser))
                    names.add(name);
            } while (cursor.moveToNext());
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, names);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AdministratorActivity.this, ChatActivity.class);
                //mai adaug si numele user-ului care se logheaza
                intent.putExtra("SENDER", logedUser);
                intent.putExtra("RECEIVER", names.get(position));
                startActivity(intent);
            }
        });

        popIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(AdministratorActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.one) {
                            db.clearUsersTable();
                            Toast.makeText(AdministratorActivity.this, "All users have been removed", Toast.LENGTH_SHORT).show();
                        }
                        if (item.getItemId() == R.id.two) {
                            db.clearMesagesTable();
                            Toast.makeText(AdministratorActivity.this, "All messages have neen removed", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

    }
}