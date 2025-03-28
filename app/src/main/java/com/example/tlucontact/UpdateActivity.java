package com.example.tlucontact;

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

import com.example.tlucontact.models.Contact;
import com.example.tlucontact.models.Employee;
import com.example.tlucontact.models.Unit;
import com.example.tlucontact.utils.DatabaseHelper;

public class UpdateActivity extends AppCompatActivity {
    private EditText edtName, edtPhone, edtEmail, edtUnit, edtPosition, edtAddress;
    private TextView txtName, txtPhone, txtEmail, txtUnit, txtPosition, txtAddress;
    private Button btnSave;
    private DatabaseHelper databaseHelper;
    private Contact contact;

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


        contact = (Contact) getIntent().getSerializableExtra("contact");

        if (contact == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy dữ liệu!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        edtName.setText(contact.getName());
        edtPhone.setText(contact.getPhone());

        if (contact instanceof Employee) {
            toolbar.setTitle("Cập nhật CBGV");
            Employee employee = (Employee) contact;
            edtEmail.setText(employee.getEmail());
            edtUnit.setText(employee.getUnit());
            edtPosition.setText(employee.getPosition());
            edtAddress.setVisibility(View.GONE);
            txtAddress.setVisibility(View.GONE);
        } else if (contact instanceof Unit) {
            toolbar.setTitle("Cập nhật Khoa");
            Unit unit = (Unit) contact;
            edtAddress.setText(unit.getAddress());
            edtEmail.setVisibility(View.GONE);
            edtUnit.setVisibility(View.GONE);
            edtPosition.setVisibility(View.GONE);
            txtEmail.setVisibility(View.GONE);
            txtUnit.setVisibility(View.GONE);
            txtPosition.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(v -> updateData());
    }

    private void updateData() {
        String name = edtName.getText().toString();
        String phone = edtPhone.getText().toString();

        if (contact instanceof Employee) {
            String email = edtEmail.getText().toString();
            String unit = edtUnit.getText().toString();
            String position = edtPosition.getText().toString();
            Employee updatedEmployee = new Employee(contact.getId(), name, phone, contact.getAvatar(), email, unit, position);
            databaseHelper.updateEmployee(updatedEmployee);
        } else if (contact instanceof Unit) {
            String address = edtAddress.getText().toString();
            Unit updatedUnit = new Unit(contact.getId(), name, phone, contact.getAvatar(), address);
            databaseHelper.updateUnit(updatedUnit);
        }

        Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        String type = "";
        Contact contact = (Contact) getIntent().getSerializableExtra("contact");
        if (contact instanceof Employee) {
            type = "employee";
        } else {
            type = "unit";
        }
        Intent resultIntent = new Intent();
        resultIntent.putExtra("type", type);
        setResult(RESULT_OK, resultIntent);
        finish();
        return true;
    }
}
