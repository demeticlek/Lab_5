package com.example.lab_4;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTodo;
    private CheckBox checkBoxUrgent;
    private Button buttonAdd;
    private ListView listViewTodos;

    private ArrayList<TodoItem> todoList;
    private TodoAdapter adapter;

    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTodo = findViewById(R.id.editTextTodo);
        checkBoxUrgent = findViewById(R.id.checkBoxUrgent);
        buttonAdd = findViewById(R.id.buttonAdd);
        listViewTodos = findViewById(R.id.listViewTodos);

        todoList = new ArrayList<>();
        adapter = new TodoAdapter(this, todoList);
        listViewTodos.setAdapter(adapter);

        // Database aç
        dbHelper = new MyDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // Database'den yükle
        loadFromDatabase();

        // Add butonu
        buttonAdd.setOnClickListener(v -> {
            String todoText = editTextTodo.getText().toString();
            if (!todoText.isEmpty()) {
                boolean isUrgent = checkBoxUrgent.isChecked();

                // Database'e kaydet
                ContentValues cv = new ContentValues();
                cv.put(MyDatabaseHelper.COL_TODO, todoText);
                cv.put(MyDatabaseHelper.COL_URGENT, isUrgent ? 1 : 0);
                long id = db.insert(MyDatabaseHelper.TABLE_NAME, null, cv);

                // Listeye ekle
                todoList.add(new TodoItem(id, todoText, isUrgent));
                adapter.notifyDataSetChanged();

                editTextTodo.setText("");
                checkBoxUrgent.setChecked(false);
            }
        });

        // Uzun basınca sil
        listViewTodos.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.delete_title)
                    .setMessage(R.string.delete_message)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        TodoItem item = todoList.get(position);
                        db.delete(MyDatabaseHelper.TABLE_NAME,
                                MyDatabaseHelper.COL_ID + "=?",
                                new String[]{String.valueOf(item.getId())});
                        todoList.remove(position);
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
            return true;
        });
    }

    private void loadFromDatabase() {
        Cursor c = db.query(MyDatabaseHelper.TABLE_NAME, null, null, null, null, null, null);
        printCursor(c);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            long id = c.getLong(c.getColumnIndexOrThrow(MyDatabaseHelper.COL_ID));
            String todo = c.getString(c.getColumnIndexOrThrow(MyDatabaseHelper.COL_TODO));
            boolean urgent = c.getInt(c.getColumnIndexOrThrow(MyDatabaseHelper.COL_URGENT)) == 1;
            todoList.add(new TodoItem(id, todo, urgent));
            c.moveToNext();
        }
        c.close();
        adapter.notifyDataSetChanged();
    }

    private void printCursor(Cursor c) {
        Log.d("DB_INFO", "Database version: " + db.getVersion());
        Log.d("DB_INFO", "Number of columns: " + c.getColumnCount());
        String[] colNames = c.getColumnNames();
        for (String col : colNames) {
            Log.d("DB_INFO", "Column name: " + col);
        }
        Log.d("DB_INFO", "Number of rows: " + c.getCount());
        c.moveToFirst();
        while (!c.isAfterLast()) {
            long id = c.getLong(c.getColumnIndexOrThrow(MyDatabaseHelper.COL_ID));
            String todo = c.getString(c.getColumnIndexOrThrow(MyDatabaseHelper.COL_TODO));
            int urgent = c.getInt(c.getColumnIndexOrThrow(MyDatabaseHelper.COL_URGENT));
            Log.d("DB_INFO", "Row -> id:" + id + " todo:" + todo + " urgent:" + urgent);
            c.moveToNext();
        }
    }
}