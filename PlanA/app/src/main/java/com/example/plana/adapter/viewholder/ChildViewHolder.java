package com.example.plana.adapter.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.emoji2.widget.EmojiButton;

import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;
import com.example.plana.R;
import com.example.plana.base.BaseViewHolder;
import com.example.plana.bean.My;
import com.example.plana.bean.Plan;
import com.example.plana.config.Constant;

import java.sql.Struct;
import java.util.Optional;

/**
 * @program: PlanA
 * @description:
 */
public class ChildViewHolder extends BaseViewHolder {

    public RelativeLayout rlPlanListItem;
    public TextRoundCornerProgressBar pgBar;
    public TextView tvItemTitle;
    public View vSpace;
    public EmojiButton btDone;
    private final int itemMargin;
    private final int offsetMargin;
    private final String emojiDone;
    private final String emojiUndone;

    public ChildViewHolder(View view) {
        super(view);
        pgBar = view.findViewById(R.id.pg_plan_list_item);
        tvItemTitle = view.findViewById(R.id.tv_plan_list_item_title);
        vSpace = view.findViewById(R.id.v_space);
        btDone = view.findViewById(R.id.emoji_plan_item);
        itemMargin = view.getContext().getResources().getDimensionPixelSize(R.dimen.item_margin);
        offsetMargin = view.getContext().getResources().getDimensionPixelSize(R.dimen.expand_size);
        emojiDone = view.getContext().getResources().getString(R.string.emoji_plan_done);
        emojiUndone = view.getContext().getResources().getString(R.string.emoji_plan_undone);
    }

    public void bindView(final ItemData itemData, int position, OnDoneChangeListener doneChangeListener) {
        ViewGroup.LayoutParams vSpaceLayoutParams = vSpace.getLayoutParams();
        // TODO: 2022/4/27 see the show result
        vSpaceLayoutParams.width = itemMargin * itemData.getTreeDepth()+offsetMargin;
        vSpace.setLayoutParams(vSpaceLayoutParams);

        tvItemTitle.setText(itemData.getText());
        pgBar.setProgressText("");
        pgBar.setProgressColor(Constant.rowColor[itemData.getTreeDepth()]);

        setChecked(itemData.isDone());

        btDone.setOnClickListener(v->{
            Boolean done = itemData.isDone();
            itemData.setDone(!done);
            setChecked(!done);
//            // TODO: 2022/4/28
//            if (doneChangeListener != null) {
//                doneChangeListener.OnRefreshList(itemData, position);
//            }
        });
    }


    /**
     * 改变是否完成的状态
     * */
    private void setChecked(boolean checked) {
        if (checked) {
            pgBar.setProgress(100);
            btDone.setText(emojiDone);
        }else {
            pgBar.setProgress(0);
            btDone.setText(emojiUndone);
        }
    }

}
