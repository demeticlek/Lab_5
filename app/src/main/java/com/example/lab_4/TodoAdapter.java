package com.example.lab_4;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class TodoAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<TodoItem> todoList;

    public TodoAdapter(Context context, ArrayList<TodoItem> todoList) {
        this.context = context;
        this.todoList = todoList;
    }

    @Override
    public int getCount() { return todoList.size(); }

    @Override
    public Object getItem(int position) { return todoList.get(position); }

    @Override
    public long getItemId(int position) { return todoList.get(position).getId(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        TextView textViewTodo = convertView.findViewById(R.id.textViewTodo);
        TextView textViewUrgent = convertView.findViewById(R.id.textViewUrgent);

        TodoItem item = todoList.get(position);
        textViewTodo.setText(item.getTodo());

        if (item.isUrgent()) {
            textViewUrgent.setText("!!!");
            convertView.setBackgroundColor(Color.parseColor("#FFCCCC"));
        } else {
            textViewUrgent.setText("");
            convertView.setBackgroundColor(Color.WHITE);
        }

        return convertView;
    }
}
