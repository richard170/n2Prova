package com.example.httpretrofit2completo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.httpretrofit2completo.model.CEP;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cep.db";
    private static final int DATABASE_VERSION = 4;

    public static final String TABLE_NAME = "cep_table";
    public static final String COLUMN_CEP = "cep";
    public static final String COLUMN_LOGRADOURO = "logradouro";
    public static final String COLUMN_COMPLEMENTO = "complemento";
    public static final String COLUMN_BAIRRO = "bairro";
    public static final String COLUMN_CIDADE = "cidade";
    public static final String COLUMN_UF = "uf";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_CEP + " TEXT PRIMARY KEY, " +
                    COLUMN_LOGRADOURO + " TEXT, " +
                    COLUMN_COMPLEMENTO + " TEXT, " +
                    COLUMN_BAIRRO + " TEXT, " +
                    COLUMN_CIDADE + " TEXT, " +
                    COLUMN_UF + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void addCEP(CEP cep) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CEP, cep.getCep());
        values.put(COLUMN_LOGRADOURO, cep.getLogradouro());
        values.put(COLUMN_COMPLEMENTO, cep.getComplemento());
        values.put(COLUMN_BAIRRO, cep.getBairro());
        values.put(COLUMN_CIDADE, cep.getCidade());
        values.put(COLUMN_UF, cep.getUf());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<String> getAllCEPs() {
        ArrayList<String> listaCEPs = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String cep = cursor.getString(cursor.getColumnIndex(COLUMN_CEP));
                listaCEPs.add(cep);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listaCEPs;
    }

    public CEP getCEPByCep(String cep) {
        SQLiteDatabase db = this.getReadableDatabase();
        CEP cepDetalhes = null;

        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{COLUMN_CEP, COLUMN_LOGRADOURO, COLUMN_COMPLEMENTO, COLUMN_BAIRRO, COLUMN_CIDADE, COLUMN_UF},
                COLUMN_CEP + "=?",
                new String[]{cep},
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int complementoIndex = cursor.getColumnIndex(COLUMN_COMPLEMENTO);

            // Verifica se a coluna complemento existe antes de tentar acessá-la
            String complemento = complementoIndex != -1 ? cursor.getString(complementoIndex) : null;

            cepDetalhes = new CEP(
                    cursor.getString(cursor.getColumnIndex(COLUMN_CEP)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_LOGRADOURO)),
                    complemento,
                    cursor.getString(cursor.getColumnIndex(COLUMN_BAIRRO)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_CIDADE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_UF))
            );

            cursor.close();
        }

        db.close();
        return cepDetalhes;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

