package com.example.tlucontact;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlucontact.models.Contact;
import com.example.tlucontact.utils.DatabaseHelper;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> originalList;
    private List<Contact> filteredList;
    private Activity activity;
    private DatabaseHelper databaseHelper;

    public ContactAdapter(List<Contact> contactList, Activity activity) {
        this.originalList = new ArrayList<>(contactList);
        this.filteredList = new ArrayList<>(contactList);
        this.activity = activity;
        databaseHelper = new DatabaseHelper(activity);
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = filteredList.get(position);
        holder.txtName.setText(contact.getName());
        holder.txtPhone.setText(contact.getPhone());
        holder.imvAvatar.setImageResource(contact.getAvatar());

        // Ẩn phần chữ cái nhóm
        holder.txtSectionHeader.setVisibility(View.GONE);

//        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(activity, i8DetailActivity.class);
//            intent.putExtra("contact", contact);
//            activity.startActivity(intent);
//        });

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(activity, UpdateActivity.class);
            intent.putExtra("contact", contact);
            activity.startActivityForResult(intent, 1);
        });

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(activity)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc muốn xóa liên hệ này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        databaseHelper.deleteContact(contact.getId());
                        filteredList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(activity, "Đã xóa liên hệ", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            String searchText = text.toLowerCase();
            for (Contact contact : originalList) {
                if (contact.getName().toLowerCase().contains(searchText)) {
                    filteredList.add(contact);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPhone, txtSectionHeader;
        ShapeableImageView imvAvatar;
        ImageButton btnDelete, btnEdit;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSectionHeader = itemView.findViewById(R.id.txt_section_header);
            txtName = itemView.findViewById(R.id.txt_name);
            txtPhone = itemView.findViewById(R.id.txt_phone);
            imvAvatar = itemView.findViewById(R.id.imv_ava);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnEdit = itemView.findViewById(R.id.btn_edit);
        }
    }
}