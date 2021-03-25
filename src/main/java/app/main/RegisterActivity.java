package app.main;

import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import app.dataAccess.ConnectionFactory;

public class RegisterActivity extends AppCompatActivity {

    private EditText reg_username;
    private EditText reg_password;
    private Button btn;
    ConnectionFactory db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        this.reg_username = findViewById(R.id.reg_username);
        this.reg_password = findViewById(R.id.reg_password);
        btn = findViewById(R.id.register_btn);

        db = new ConnectionFactory(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = reg_username.getText().toString();
                String passWord = reg_password.getText().toString();

                //testez toate cazurile pe care le-ar putea introduce utilizatorul
                //daca utilizatorul vrea sa fie admin
                if (userName.equals("Admin")) {
                    Toast.makeText(RegisterActivity.this, "You can't be an administrator", Toast.LENGTH_SHORT).show();
                }//daca utilizatorul nu intoduce Password-ul
                else if (userName.equals("")) {
                    Toast.makeText(RegisterActivity.this, "No Username", Toast.LENGTH_SHORT).show();
                }//daca utilizatorul nu intoduce Username-ul
                else if (passWord.equals("")) {
                    Toast.makeText(RegisterActivity.this, "No Password", Toast.LENGTH_SHORT).show();
                }//inregistrarea utilizatorului
                else {
                    int ok = 1;
                    Cursor cursor = db.viewData("Users");
                    if (cursor.moveToFirst()) {
                        do {
                            if (userName.equals(cursor.getString(0))) {
                                ok = 0;
                                break;
                            }
                        } while (cursor.moveToNext());
                    }
                    if (ok == 1) {
                        db.insertUser(userName, passWord);
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    //daca utilizatorul foloseste un nume deja existent
                    else {
                        Toast.makeText(RegisterActivity.this, "This Username already exists", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}