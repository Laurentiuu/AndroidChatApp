package app.main;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import app.dataAccess.ConnectionFactory;


public class LoginActivity extends AppCompatActivity {

    private ConnectionFactory db;

    private EditText username;
    private EditText password;
    private TextView regBtn;
    private Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btn = findViewById(R.id.login_btn);
        regBtn = findViewById(R.id.infoTxtCredits);

        db = new ConnectionFactory(this);

        startRegister(regBtn);

        showFriends(btn);

    }

    private void startRegister(TextView btn) {
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showFriends(Button btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                String pass = password.getText().toString();

                if (db.searchUser(name, pass)) {
                    //daca utilizatorul nu intoduce Username-ul
                    if (name.equals("")) {
                        Toast.makeText(LoginActivity.this, "No Username", Toast.LENGTH_SHORT).show();
                    }//daca utilizatorul nu intoduce Password-ul
                    else if (pass.equals("")) {
                        Toast.makeText(LoginActivity.this, "No Password", Toast.LENGTH_SHORT).show();
                    }//daca user-ul se afla in baza de date afisez noul layout
                    else {
                        Intent intent;
                        if (name.equals("Admin")) {
                            intent = new Intent(LoginActivity.this, AdministratorActivity.class);
                            intent.putExtra("NAME", name);
                            startActivity(intent);
                        } else {
                            intent = new Intent(LoginActivity.this, FriendsActivity.class);
                            //mai adaug si numele user-ului care se logheaza
                            intent.putExtra("NAME", name);
                            startActivity(intent);
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "You must register if you wanna go in", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}