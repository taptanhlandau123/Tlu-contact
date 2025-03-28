package com.example.tlucontact.utils;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tlucontact.models.Contact;
import com.example.tlucontact.models.Employee;
import com.example.tlucontact.models.Unit;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tlucontact.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CONTACTS = "Contacts";
    private static final String TABLE_EMPLOYEES = "Employees";
    private static final String TABLE_UNITS = "Units";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_AVATAR = "avatar";

    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_UNIT = "unit";
    private static final String COLUMN_POSITION = "position";

    private static final String COLUMN_ADDRESS = "address";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createContactsTable = "CREATE TABLE " + TABLE_CONTACTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_AVATAR + " INTEGER)";
        db.execSQL(createContactsTable);

        String createEmployeesTable = "CREATE TABLE " + TABLE_EMPLOYEES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_UNIT + " TEXT, " +
                COLUMN_POSITION + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_ID + ") REFERENCES " + TABLE_CONTACTS + "(" + COLUMN_ID + ") ON DELETE CASCADE)";
        db.execSQL(createEmployeesTable);

        String createUnitsTable = "CREATE TABLE " + TABLE_UNITS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_ADDRESS + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_ID + ") REFERENCES " + TABLE_CONTACTS + "(" + COLUMN_ID + ") ON DELETE CASCADE)";
        db.execSQL(createUnitsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UNITS);
        onCreate(db);
    }
    //get all employees
    public List<Contact> getAllEmployees() {
        List<Contact> employees = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_CONTACTS + " INNER JOIN " + TABLE_EMPLOYEES + " ON " + TABLE_CONTACTS + "." + COLUMN_ID + " = " + TABLE_EMPLOYEES + "." + COLUMN_ID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                employees.add(new Employee(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AVATAR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UNIT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSITION))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return employees;
    }
    // get all units
    public List<Contact> getAllUnits() {
        List<Contact> units = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_CONTACTS + " INNER JOIN " + TABLE_UNITS + " ON " + TABLE_CONTACTS + "." + COLUMN_ID + " = " + TABLE_UNITS + "." + COLUMN_ID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                units.add(new Unit(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AVATAR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return units;
    }
    // add employee
    public void addEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, employee.getName());
            values.put(COLUMN_PHONE, employee.getPhone());
            values.put(COLUMN_AVATAR, employee.getAvatar());
            long id = db.insert(TABLE_CONTACTS, null, values);

            ContentValues employeeValues = new ContentValues();
            employeeValues.put(COLUMN_ID, id);
            employeeValues.put(COLUMN_EMAIL, employee.getEmail());
            employeeValues.put(COLUMN_UNIT, employee.getUnit());
            employeeValues.put(COLUMN_POSITION, employee.getPosition());
            db.insert(TABLE_EMPLOYEES, null, employeeValues);

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }
    // add unit
    public void addUnit(Unit unit) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, unit.getName());
            values.put(COLUMN_PHONE, unit.getPhone());
            values.put(COLUMN_AVATAR, unit.getAvatar());
            long id = db.insert(TABLE_CONTACTS, null, values);

            ContentValues unitValues = new ContentValues();
            unitValues.put(COLUMN_ID, id);
            unitValues.put(COLUMN_ADDRESS, unit.getAddress());
            db.insert(TABLE_UNITS, null, unitValues);

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }
    // update employee
    public void updateEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, employee.getName());
            values.put(COLUMN_PHONE, employee.getPhone());
            values.put(COLUMN_AVATAR, employee.getAvatar());
            db.update(TABLE_CONTACTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(employee.getId())});

            ContentValues employeeValues = new ContentValues();
            employeeValues.put(COLUMN_EMAIL, employee.getEmail());
            employeeValues.put(COLUMN_UNIT, employee.getUnit());
            employeeValues.put(COLUMN_POSITION, employee.getPosition());
            db.update(TABLE_EMPLOYEES, employeeValues, COLUMN_ID + " = ?", new String[]{String.valueOf(employee.getId())});

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }
    // update unit
    public void updateUnit(Unit unit) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, unit.getName());
            values.put(COLUMN_PHONE, unit.getPhone());
            values.put(COLUMN_AVATAR, unit.getAvatar());
            db.update(TABLE_CONTACTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(unit.getId())});

            ContentValues unitValues = new ContentValues();
            unitValues.put(COLUMN_ADDRESS, unit.getAddress());
            db.update(TABLE_UNITS, unitValues, COLUMN_ID + " = ?", new String[]{String.valueOf(unit.getId())});

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_CONTACTS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }
}
