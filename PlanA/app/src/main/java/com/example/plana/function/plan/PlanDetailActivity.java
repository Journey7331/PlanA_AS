package com.example.plana.function.plan;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plana.R;
import com.example.plana.adapter.PlanListAdapter;
import com.example.plana.adapter.viewholder.ItemData;
import com.example.plana.adapter.viewholder.OnScrollToListener;
import com.example.plana.base.BaseActivity;

import com.example.plana.bean.My;
import com.example.plana.bean.Plan;
import com.google.android.material.appbar.AppBarLayout;

import java.util.List;
import java.util.Optional;

/**
 * @program: PlanA
 * @description:
 */
public class PlanDetailActivity extends BaseActivity {

    RecyclerView recyclerView;
    PlanListAdapter myAdapter;
    ImageView ivBanner;

    AppBarLayout ablAppBar;
    Toolbar tbToolBar;

    TextView tvTitle;
    ImageButton btBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail);

        tvTitle = findViewById(R.id.tv_plan_detail_title);
        ivBanner = findViewById(R.id.iv_plan_detail_img);
        btBack = findViewById(R.id.bt_plan_detail_back);
        ablAppBar = findViewById(R.id.abl_plan_detail_bar);
        tbToolBar = findViewById(R.id.tb_Toolbar);
        recyclerView = findViewById(R.id.rv_plan_list);

        initRecyclerView();
        initToDataSet();

        ablAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float alpha = Math.abs(verticalOffset) * 1.0F / (ablAppBar.getHeight() - tbToolBar.getHeight());
                setToolbarBackgroundColor(alpha);
            }
        });

        Plan plan = new Plan();
        for (Plan p : My.plans) {
            if (p.getPlanId() == My.plan_id) {
                plan = p;
                break;
            }
        }

        Optional<Plan> first = My.plans.stream().filter(p -> p.getPlanId() == My.plan_id).findFirst();
        if (first.isPresent()) {
            ivBanner.setImageResource(images[first.get().getImg_id()]);
        } else {
            ivBanner.setImageResource(images[0]);
        }

        btBack.setOnClickListener(v -> {
            finish();
        });

    }


    public final int[] images = {
            R.mipmap.img_study_0,
            R.mipmap.img_study_1,
            R.mipmap.img_study_2,
            R.mipmap.img_study_3,
    };

    /**
     * 初始化 RecyclerView
     */
    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(PlanDetailActivity.this, 1);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        myAdapter = new PlanListAdapter(PlanDetailActivity.this);
        recyclerView.setAdapter(myAdapter);
        myAdapter.setOnScrollToListener(new OnScrollToListener() {
            @Override
            public void scrollTo(int position) {
                recyclerView.scrollToPosition(position);
            }
        });

    }

    /**
     * 填充 RecyclerView 的数据
     */
    private void initToDataSet() {
        List<ItemData> list = myAdapter.getChildrenFromPlanList(-1, -1);
        myAdapter.addAll(list, 0);
    }

    /**
     * 更改标题栏的底色
     */
    private void setToolbarBackgroundColor(float alpha) {
        tbToolBar.setBackgroundColor(getGradientOverlayColor(ContextCompat.getColor(this, R.color.bar_grey), alpha > 1 ? 1 : alpha));
    }

    /**
     * 获取渐变颜色
     *
     * @param color    原始颜色
     * @param fraction 透明度比率
     * @return 渐变颜色
     */
    private int getGradientOverlayColor(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }
}
