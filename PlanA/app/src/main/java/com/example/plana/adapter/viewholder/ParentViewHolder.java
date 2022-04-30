package com.example.plana.adapter.viewholder;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;
import com.example.plana.R;
import com.example.plana.adapter.PlanListAdapter;
import com.example.plana.base.BaseViewHolder;
import com.example.plana.bean.My;
import com.example.plana.bean.Plan;
import com.example.plana.bean.PlanBean;
import com.example.plana.config.Constant;

import java.util.List;

/**
 * @program: PlanA
 * @description:
 */
public class ParentViewHolder extends BaseViewHolder {

    public RelativeLayout rlPlanListItem;
    public TextRoundCornerProgressBar pgBar;
    public TextView tvItemTitle;
    public View vSpace;
    private final int itemMargin;
    private final int itemHeight;
    private final int itemIncrease;

    public ParentViewHolder(View view) {
        super(view);
        rlPlanListItem = view.findViewById(R.id.rl_plan_list_parent);
        pgBar = view.findViewById(R.id.pg_plan_list_item);
        tvItemTitle = view.findViewById(R.id.tv_plan_list_item_title);
        vSpace = view.findViewById(R.id.v_space);
        itemMargin = view.getContext().getResources().getDimensionPixelSize(R.dimen.item_margin);
        itemHeight = view.getContext().getResources().getDimensionPixelSize(R.dimen.item_height);
        itemIncrease = view.getContext().getResources().getDimensionPixelSize(R.dimen.item_increase);
    }

    public void bindView(final ItemData itemData, final int position, final ItemDataClickListener itemDataClickListener) {
        ViewGroup.LayoutParams vSpaceLayoutParams = vSpace.getLayoutParams();
        vSpaceLayoutParams.width = itemMargin * itemData.getTreeDepth();
        vSpace.setLayoutParams(vSpaceLayoutParams);

        ViewGroup.LayoutParams params = rlPlanListItem.getLayoutParams();
        params.height = (Constant.rowColor.length - itemData.getTreeDepth()) * itemIncrease + itemHeight;
        rlPlanListItem.setLayoutParams(params);

        tvItemTitle.setText(itemData.getText());
        pgBar.setProgressColor(Constant.rowColor[itemData.getTreeDepth()]);

        int progress = getProgress(itemData.getId());

        if (progress >= 90) {
            pgBar.setTextProgressColor(Constant.MyColor.white);
        } else if (progress >= 80) {
            pgBar.setTextProgressColor(Constant.MyColor.grey);
        } else {
            pgBar.setTextProgressColor(Constant.rowColor[itemData.getTreeDepth()]);
        }

        if (progress >= 50) {
            tvItemTitle.setTextColor(Constant.MyColor.white);
        }

        pgBar.setProgress(progress);
        pgBar.setProgressText(progress + "%");

        rlPlanListItem.setOnClickListener(v -> {
            if (itemDataClickListener != null) {
                if (itemData.isExpand()) {
                    itemDataClickListener.onHideChildren(itemData);
                    itemData.setExpand(false);
                } else {
                    itemDataClickListener.onExpandChildren(itemData);
                    itemData.setExpand(true);
                }
            }
        });
    }

    private int getProgress(int id) {
        Plan plan = new Plan();
        for (Plan p : My.plans) {
            if (p.getPlanId() == My.plan_id) {
                plan = p;
                break;
            }
        }
        if (id > plan.getCurPoint()) return 0;

        List<PlanBean> planBeans = plan.getPlanBeans();
        for (PlanBean bean : planBeans) {
            if (bean.getId() == id) {
                return bean.getDoneCnt() * 100 / bean.getTotalCnt();
            }
        }
        return 0;
    }

}
