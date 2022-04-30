package com.example.plana.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.plana.R;
import com.example.plana.bean.My;
import com.example.plana.bean.Plan;
import com.example.plana.bean.Todos;
import com.example.plana.config.Constant;
import com.example.plana.database.DeletedTodosDB;
import com.example.plana.database.TodosDB;
import com.example.plana.function.todo.EditTodoActivity;
import com.example.plana.utils.TimeCalcUtil;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @program: PlanA
 * @description:
 */
public class AddPlanAdapter extends ArrayAdapter {

    LayoutInflater inflater;
    List<Plan> arrayList;
    Activity ctx;

    // 内部类
    class AddPlanItemHolder {
        //        RelativeLayout rlAddPlanItem;
        TextView tvTitle;
        TextView tvCount;
    }

    public AddPlanAdapter(Activity context, List<Plan> arr) {
        super(context, R.layout.activity_add_plan, arr);
        this.ctx = context;
        this.arrayList = arr;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Nullable
    @Override
    public Plan getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final AddPlanItemHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_add_plan_list, parent, false);
            holder = new AddPlanItemHolder();
//            holder.rlAddPlanItem = convertView.findViewById(R.id.rl_add_plan_item);
            holder.tvTitle = convertView.findViewById(R.id.tv_add_plan_list_title);
            holder.tvCount = convertView.findViewById(R.id.tv_add_plan_list_count);
            convertView.setTag(holder);
        } else {
            holder = (AddPlanItemHolder) convertView.getTag();
        }

        holder.tvTitle.setText("「" + arrayList.get(position).getPlanBrief().getPlanName() + "」");
        holder.tvCount.setText("总节数：" + arrayList.get(position).getPlanBrief().getTotal());

        convertView.setOnClickListener(l -> {
            ClipboardManager cm = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText("text", new Gson().toJson(arrayList.get(position))));
            if (cm.hasPrimaryClip()) {
                cm.getPrimaryClip().getItemAt(0).getText();
            }
            Toast.makeText(ctx, holder.tvTitle.getText().toString() + "复制成功", Toast.LENGTH_SHORT).show();
        });

        return convertView;
    }

}