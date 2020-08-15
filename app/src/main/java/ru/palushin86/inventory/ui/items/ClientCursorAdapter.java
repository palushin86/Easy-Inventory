package ru.palushin86.inventory.ui.items;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import androidx.cursoradapter.widget.ResourceCursorAdapter;

import ru.palushin86.inventory.R;

public class ClientCursorAdapter extends ResourceCursorAdapter {

    public ClientCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
        super(context, layout, cursor, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView) view.findViewById(R.id.search_result_item_key);
        name.setText(cursor.getString(cursor.getColumnIndex("name")));
    }
}
