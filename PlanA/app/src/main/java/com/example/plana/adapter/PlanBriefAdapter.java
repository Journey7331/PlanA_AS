package com.example.plana.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plana.R;
import com.example.plana.base.MainApplication;
import com.example.plana.bean.My;
import com.example.plana.bean.Plan;
import com.example.plana.bean.PlanBrief;
import com.example.plana.database.TimerDB;
import com.example.plana.function.plan.PlanDetailActivity;
import com.example.plana.utils.SharedPreferencesUtil;
import com.example.plana.utils.TimeCalcUtil;
import com.google.gson.Gson;

import java.util.LinkedList;

/**
 * @program: PlanA
 * @description:
 */
public class PlanBriefAdapter extends RecyclerView.Adapter<PlanBriefAdapter.ViewHolder> {

    Activity ctx;
    LinkedList<PlanBrief> arr;
    RelativeLayout rlEmpty;

    public PlanBriefAdapter(Activity context, LinkedList<PlanBrief> list, RelativeLayout rlEmpty) {
        this.arr = list;
        this.ctx = context;
        this.rlEmpty = rlEmpty;
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
        }

    }


    /**
     * 创建一个view，填充item的 UI
     * (invoked by the layout manager)
     */
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
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        PlanBrief planBrief = arr.get(position);
        viewHolder.tvTitle.setText(planBrief.getPlanName());
//        viewHolder.tvLeftDays.setText(TimeCalcUtil.getLeftDay(planBrief.getLeftDays()));
        int left = (planBrief.getTotal() - planBrief.getDone());
        if (left == 0) {
            viewHolder.tvLeftDays.setText("全部完成啦！！");
        } else {
            viewHolder.tvLeftDays.setText("还剩" + left + "小节");
        }

        new Handler().postDelayed(() -> {
            viewHolder.progressBar.setProgress(
                    planBrief.getDone() * 100 / planBrief.getTotal(),
                    true
            );
        }, position * 80L + 200);


        viewHolder.rlPlan.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, PlanDetailActivity.class);
            My.plan_id = planBrief.getPlanId();
//            intent.putExtra("plan_id", planId);
            ctx.startActivityForResult(intent, 1);
        });

        viewHolder.rlPlan.setOnLongClickListener(v -> {
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

            confirm.setOnClickListener(l -> {
                dialog.dismiss();
                for (int i = 0; i < My.plans.size(); i++) {
                    if (My.plans.get(i).getPlanId() == arr.get(position).getPlanId()) {
                        My.plans.remove(i);
                        break;
                    }
                }
                arr.remove(viewHolder.getAdapterPosition());
                notifyItemRemoved(viewHolder.getAdapterPosition());
                notifyItemRangeChanged(position, arr.size() - position);   // 不可缺的步骤

                Gson gson = new Gson();
                String str_subjectJSON = gson.toJson(My.plans);
                SharedPreferencesUtil.init(
                        MainApplication.getAppContext(),
                        "PLAN_DATA"
                ).putString("PLAN_LIST", str_subjectJSON); //存入json串

                checkEmpty();
                Toast.makeText(ctx, "删除成功", Toast.LENGTH_SHORT).show();
            });
            cancel.setOnClickListener(l -> dialog.dismiss());
            return true;
        });

    }

    /**
     * 显示列表为空图
     */
    public void checkEmpty() {
        if (arr.size() > 0) {
            rlEmpty.setVisibility(View.INVISIBLE);
        } else {
            rlEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }


}
