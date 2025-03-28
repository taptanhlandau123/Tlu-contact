package com.example.tlucontact;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tlucontact.models.Unit;
import com.example.tlucontact.utils.DatabaseHelper;
import com.example.tlucontact.models.Contact;
import com.example.tlucontact.models.Employee;

public class AddActivity extends AppCompatActivity {
    private TextView txtName, txtPhone, txtEmail, txtUnit, txtPosition, txtAddress;
    private EditText edtName, edtPhone, edtEmail, edtUnit, edtPosition, edtAddress;
    private Button btnSave;
    private DatabaseHelper databaseHelper;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.include_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        databaseHelper = new DatabaseHelper(this);
        edtName = findViewById(R.id.edt_name);
        edtPhone = findViewById(R.id.edt_phone);
        edtEmail = findViewById(R.id.edt_email);
        edtUnit = findViewById(R.id.edt_unit);
        edtPosition = findViewById(R.id.edt_position);
        edtAddress = findViewById(R.id.edt_address);
        txtName = findViewById(R.id.txt_name);
        txtPhone = findViewById(R.id.txt_phone);
        txtEmail = findViewById(R.id.txt_email);
        txtUnit = findViewById(R.id.txt_unit);
        txtPosition = findViewById(R.id.txt_position);
        txtAddress = findViewById(R.id.txt_address);
        btnSave = findViewById(R.id.btn_save);


        type = getIntent().getStringExtra("type");
        if ("unit".equals(type)) {
            toolbar.setTitle("Thêm Khoa");
            edtEmail.setVisibility(View.GONE);
            edtUnit.setVisibility(View.GONE);
            edtPosition.setVisibility(View.GONE);
            txtEmail.setVisibility(View.GONE);
            txtUnit.setVisibility(View.GONE);
            txtPosition.setVisibility(View.GONE);
        } else {
            toolbar.setTitle("Thêm nhân CBGV");
            edtAddress.setVisibility(View.GONE);
            txtAddress.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(v -> saveData());
    }

    private void saveData() {
        String name = edtName.getText().toString();
        String phone = edtPhone.getText().toString();
        String email = edtEmail.getText().toString();
        String unit = edtUnit.getText().toString();
        String position = edtPosition.getText().toString();
        String address = edtAddress.getText().toString();

        if ("unit".equals(type)) {
            databaseHelper.addUnit(new Unit(0,name, phone, R.drawable.avatar, address));
        } else {
            databaseHelper.addEmployee(new Employee(0,name, phone, R.drawable.avatar, email, unit, position));
        }
        Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("type", type);
        setResult(RESULT_OK, resultIntent);
        finish();
        return true;
    }
}
