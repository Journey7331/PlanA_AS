package com.example.plana.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.emoji2.widget.EmojiButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plana.R;
import com.example.plana.bean.TimerRecorder;
import com.example.plana.database.MyDatabaseHelper;
import com.example.plana.database.TimerDB;
import com.example.plana.database.TodosDB;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @program: PlanA
 * @description:
 */
public class TimerRecorderAdapter extends RecyclerView.Adapter<TimerRecorderAdapter.ViewHolder> {

    static Activity ctx;
    ArrayList<TimerRecorder> arr;
    MyDatabaseHelper sqlite;

    public TimerRecorderAdapter(Activity context, ArrayList<TimerRecorder> list) {
        this.arr = list;
        this.ctx = context;
        this.sqlite = new MyDatabaseHelper(ctx);
    }


    /**
     * ViewHolder： item 中的 view 变量
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay;
        TextView tvPeriod;
        TextView tvTimeCount;

        RelativeLayout rlTimerRecorderList;
        EmojiButton emojiTimer;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvDay = view.findViewById(R.id.tv_timer_day);
            tvPeriod = view.findViewById(R.id.tv_timer_time);
            tvTimeCount = view.findViewById(R.id.tv_timer_count);
            rlTimerRecorderList = view.findViewById(R.id.rl_timer_recorder_list);
            emojiTimer = view.findViewById(R.id.emoji_timer);
        }
    }


    /**
     * 创建一个view，填充item的 UI
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_timer_recorder, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    /**
     * 替换 view 中的数据
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimerRecorder record = arr.get(position);
        String time = record.getStartTime() + " - " + record.getEndTime();
        String count = record.getTime() + "分钟";

        holder.tvDay.setText(record.getDay());
        holder.tvPeriod.setText(time);
        holder.tvTimeCount.setText(count);

        if (record.getCount_type().equals(TimerRecorder.POSITIVE)) {
            holder.emojiTimer.setText(ctx.getResources().getString(R.string.emoji_tick_timer));
        }

        holder.rlTimerRecorderList.setOnLongClickListener(l -> {
            View view = ctx.getLayoutInflater().inflate(R.layout.dialog_confirm, null);
            TextView text = view.findViewById(R.id.text);
            text.setText("确认删除？");
            TextView confirm = view.findViewById(R.id.confirm);
            TextView cancel = view.findViewById(R.id.cancel);

            AlertDialog.Builder builder = new AlertDialog
                    .Builder(ctx)
                    .setView(view);
            final AlertDialog dialog = builder.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(500, WindowManager.LayoutParams.WRAP_CONTENT);

            confirm.setOnClickListener(v -> {
                dialog.dismiss();

                TimerDB.deleteRecorderById(sqlite, record.get_id() + "");
                arr.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(position,arr.size()-position);   // 不可缺的步骤

                Toast.makeText(ctx, "删除成功", Toast.LENGTH_SHORT).show();
            });
            cancel.setOnClickListener(v -> dialog.dismiss());

            return true;
        });

    }

}
