package app.main;

import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.model.Chat;
import app.adapter.ChatAdapter;
import app.dataAccess.ConnectionFactory;

import java.util.ArrayList;


public class ChatActivity extends AppCompatActivity {

    private ConnectionFactory db;
    private TextView name;
    private ImageButton sendBtn;
    private EditText text;
    private ImageView deleteBtn;

    ChatAdapter chatAdapter;
    ArrayList<Chat> mChat;

    RecyclerView recyclerView;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);

        name = findViewById(R.id.name);
        sendBtn = findViewById(R.id.btn_send);
        text = findViewById(R.id.text_send);
        deleteBtn = findViewById(R.id.delete_btn);

        db = new ConnectionFactory(this);

        intent = getIntent();

        //afisarea numelui prietenului
        final String sender = getIntent().getStringExtra("SENDER");
        final String receiver = getIntent().getStringExtra("RECEIVER");
        name.setText(receiver);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //decid daca sa afisez butonul de stergere a utilizatorului sau nu
        //numai administratorul poate sa-l vada
        if (sender.equals("Admin")) {
            deleteBtn.setVisibility(View.VISIBLE);
        } else {
            deleteBtn.setVisibility(View.INVISIBLE);
        }

        mChat = new ArrayList<>();
        Cursor cursor = db.viewData("Messages");
        // curat array ul deoarece il actualizez de fiecare data cand inserez un mesaj
        // cu un anumit receiver
        //afisez utilizatorii
        mChat.clear();
        if (cursor.moveToFirst()) {
            do {
                Chat chat = new Chat(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                //Dan=sender
                //Ion= Receiver
                if ((chat.getSender().equals(sender) || chat.getSender().equals(receiver))
                        && (chat.getReceiver().equals(receiver) || chat.getReceiver().equals(sender))) {
                    mChat.add(chat);
                }
                chatAdapter = new ChatAdapter(ChatActivity.this, mChat, sender);
                recyclerView.setAdapter(chatAdapter);
            } while (cursor.moveToNext());
        }

        //cand apas butonul de trimitere a mesajului
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text.getText().toString();
                if (msg.equals("") || msg.equals("\n")) {
                    Toast.makeText(ChatActivity.this, "Write a message", Toast.LENGTH_SHORT).show();
                } else {
                    db.insertMessage(sender, receiver, msg);
                    readMessagges(sender, receiver, msg);
                }
                //dupa ce trimit mesajul, curat Edit Text-ul
                text.setText("");
            }
        });

        //cand apas butonul de delete
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this, "User DELETED", Toast.LENGTH_SHORT).show();

                //stergerea din baza de date userul si toate mesajele cu acesta
                db.deleteUser(receiver);
                db.deleteMessagesFromUser(receiver);

                intent = new Intent(ChatActivity.this, AdministratorActivity.class);
                //mai adaug si numele user-ului care se logheaza
                intent.putExtra("NAME", "Admin");
                startActivity(intent);
            }
        });

    }

    private void readMessagges(String sender, String receiver, String msg) {

        Cursor cursor = db.viewData("Messages");
        // curat array ul deoarece il actualizez de fiecare data cand inserez un mesaj
        // cu un anumit receiver
        mChat.add(new Chat(sender, receiver, msg));
        chatAdapter = new ChatAdapter(ChatActivity.this, mChat, sender);
        recyclerView.setAdapter(chatAdapter);

    }
}
