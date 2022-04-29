package com.example.plana.function;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.plana.R;
import com.example.plana.adapter.DeletedTodoAdapter;
import com.example.plana.adapter.TodoAdapter;
import com.example.plana.base.BaseActivity;
import com.example.plana.bean.My;
import com.example.plana.bean.Todos;
import com.example.plana.database.DeletedTodosDB;
import com.example.plana.database.TodosDB;

import java.util.ArrayList;

/**
 * @program: PlanA
 * @description:
 */
public class TrashBinActivity extends BaseActivity {

    ImageButton btBack;
    ListView listView;
    Button btDeleteAll;
    Button btRecoverAll;

    RelativeLayout rlEmpty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash_bin);

        listView = findViewById(R.id.lv_deleted_todo_list);
        btBack = findViewById(R.id.bt_trash_bin_back);
        btDeleteAll = findViewById(R.id.btn_delete_all);
        btRecoverAll = findViewById(R.id.btn_recover_all);
        rlEmpty = findViewById(R.id.trash_bin_empty_status);

        ArrayList<Todos> list = DeletedTodosDB.queryAllTodos(sqlite);

        DeletedTodoAdapter adapter = new DeletedTodoAdapter(this, list, rlEmpty);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.checkEmpty();

        btBack.setOnClickListener(v -> finish());
        btDeleteAll.setOnClickListener(v -> adapter.deleteAll());
        btRecoverAll.setOnClickListener(v-> adapter.recoverAll());

    }
}
