package app.main;


import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import app.dataAccess.ConnectionFactory;

import java.util.ArrayList;


public class FriendsActivity extends AppCompatActivity {

    private ConnectionFactory db;
    private ListView listView;

    ArrayList<String> names = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_layout);

        listView = findViewById(R.id.listView);
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

        showAllUsers(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FriendsActivity.this, ChatActivity.class);
                //mai adaug si numele user-ului care se logheaza
                intent.putExtra("SENDER", logedUser);
                intent.putExtra("RECEIVER", names.get(position));
                startActivity(intent);
            }
        });

    }

    private void showAllUsers(ListView listView) {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, names);
        listView.setAdapter(arrayAdapter);
    }

}