package com.example.plana.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plana.R;
import com.example.plana.bean.My;
import com.example.plana.bean.Plan;
import com.example.plana.bean.PlanBrief;
import com.example.plana.function.plan.PlanDetailActivity;
import com.example.plana.utils.TimeCalcUtil;

import java.util.LinkedList;

/**
 * @program: PlanA
 * @description:
 */
public class PlanBriefAdapter extends RecyclerView.Adapter<PlanBriefAdapter.ViewHolder> {

    static Activity ctx;
    LinkedList<PlanBrief> arr;


    public PlanBriefAdapter(Activity context, LinkedList<PlanBrief> list) {
        this.arr = list;
        this.ctx = context;
    }


    /**
     * ViewHolder： item 中的 view 变量
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ProgressBar progressBar;
        TextView tvLeftDays;
        RelativeLayout rlPlan;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tv_plan_name);
            progressBar = view.findViewById(R.id.progressbar_plan_brief);
            tvLeftDays = view.findViewById(R.id.tv_plan_brief_left_days);
            rlPlan = view.findViewById(R.id.rl_plan_brief);

            // Define click listener for the ViewHolder's View

        }

    }


    /**
     * 创建一个view，填充item的 UI
     * (invoked by the layout manager)
     * */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_plan_brief, parent, false);
        return new ViewHolder(view);
    }


    /**
     * 替换 view 中的数据
     * (invoked by the layout manager)
     * */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        PlanBrief planBrief = arr.get(position);
        viewHolder.tvTitle.setText(planBrief.getPlanName());
        viewHolder.tvLeftDays.setText(TimeCalcUtil.getLeftDay(planBrief.getLeftDays()));

        new Handler().postDelayed(() -> {
            viewHolder.progressBar.setProgress(
                    planBrief.getDone() * 100 / planBrief.getTotal(),
                    true
            );
        }, position*200+200);


        viewHolder.rlPlan.setOnClickListener(v -> {
            My.plan = getPlanDetail(planBrief);
            directToPlanDetailActivity();
        });

    }


    @Override
    public int getItemCount() {
        return arr.size();
    }


    /**
     * 根据planBrief获取plan的全部信息
     */
    private static Plan getPlanDetail(PlanBrief planBrief) {
        Plan plan = new Plan();

        return plan;
    }


    /**
     * 跳转到 PlanDetailActivity
     */
    private static void directToPlanDetailActivity() {
        Intent intent = new Intent(ctx, PlanDetailActivity.class);
        ctx.startActivity(intent);
        ctx.overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out);
    }
}
