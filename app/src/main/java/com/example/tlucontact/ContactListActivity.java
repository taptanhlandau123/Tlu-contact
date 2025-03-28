package com.example.tlucontact;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlucontact.models.Contact;
import com.example.tlucontact.utils.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {
    private RecyclerView rcvContact;
    private ContactAdapter contactAdapter;
    private List<Contact> contactList;
    private String type;
    private EditText edtSearch;
    private DatabaseHelper databaseHelper = new DatabaseHelper(this);
    private FloatingActionButton fabAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.include_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        edtSearch = (EditText) findViewById(R.id.edt_search);
        rcvContact = (RecyclerView) findViewById(R.id.rcv_contact);
        rcvContact.setLayoutManager(new LinearLayoutManager(this));
        contactList = new ArrayList<>();
        type = getIntent().getStringExtra("type");
        if (type.equals("employee")) {
            toolbar.setTitle("Danh bạ Cán bộ");
        } else {
            toolbar.setTitle("Danh bạ Phòng ban");
        }

        loadContacts();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                contactAdapter.filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        fabAdd.setOnClickListener(view -> {
            Intent intent = new Intent(ContactListActivity.this, AddActivity.class);
            intent.putExtra("type", type);
            startActivityForResult(intent, 1);
        });


    }
    public void loadContacts(){
        if (type.equals("employee")) {
            contactList = databaseHelper.getAllEmployees();
        } else if (type.equals("unit")) {
            contactList = databaseHelper.getAllUnits();
        }
        Collections.sort(contactList, new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {
                return c1.getName().compareToIgnoreCase(c2.getName());
            }
        });
        contactAdapter = new ContactAdapter(contactList, this);
        rcvContact.setAdapter(contactAdapter);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String type = data.getStringExtra("type");
            if (type != null) {
                Log.d("DEBUG", "Nhận lại type: " + type);
            }
        }
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadContacts();
        }
    }
}