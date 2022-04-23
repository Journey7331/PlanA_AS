package com.example.plana.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plana.R;
import com.example.plana.bean.TimerRecorder;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @program: PlanA
 * @description:
 */
public class TimerRecorderAdapter extends RecyclerView.Adapter<TimerRecorderAdapter.ViewHolder> {

    static Activity ctx;
    ArrayList<TimerRecorder> arr;

    public TimerRecorderAdapter(Activity context, ArrayList<TimerRecorder> list) {
        this.arr = list;
        this.ctx = context;
    }


    /**
     * ViewHolder： item 中的 view 变量
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay;
        TextView tvPeriod;
        TextView tvTimeCount;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvDay = view.findViewById(R.id.tv_timer_day);
            tvPeriod = view.findViewById(R.id.tv_timer_time);
            tvTimeCount = view.findViewById(R.id.tv_timer_count);
        }
    }


    /**
     * 创建一个view，填充item的 UI
     * */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_timer_recorder, parent, false);

        return new ViewHolder(view);
    }

    /**
     * 替换 view 中的数据
     * */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimerRecorder recorder = arr.get(position);
        String time = recorder.getStartTime() + " - " + recorder.getEndTime();
        String count = recorder.getTime() + "分钟";

        holder.tvDay.setText(recorder.getDay());
        holder.tvPeriod.setText(time);
        holder.tvTimeCount.setText(count);
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

}
