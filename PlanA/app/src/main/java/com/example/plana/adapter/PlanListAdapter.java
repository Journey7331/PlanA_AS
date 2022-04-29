package com.example.plana.adapter;

import android.app.Activity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plana.R;
import com.example.plana.adapter.viewholder.ChildViewHolder;
import com.example.plana.adapter.viewholder.ItemData;
import com.example.plana.adapter.viewholder.ItemDataClickListener;
import com.example.plana.adapter.viewholder.OnDoneChangeListener;
import com.example.plana.adapter.viewholder.OnScrollToListener;
import com.example.plana.adapter.viewholder.ParentViewHolder;
import com.example.plana.base.BaseViewHolder;
import com.example.plana.bean.My;
import com.example.plana.bean.Plan;
import com.example.plana.bean.PlanBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: PlanA
 * @description:
 */
public class PlanListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private final Activity ctx;
    private List<ItemData> dataSet;
    private OnScrollToListener onScrollToListener;

    public void setOnScrollToListener(OnScrollToListener onScrollToListener) {
        this.onScrollToListener = onScrollToListener;
    }

    public PlanListAdapter(Activity context) {
        this.ctx = context;
        this.dataSet = new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ItemData.ITEM_TYPE_PARENT:
                ParentViewHolder parentViewHolder = (ParentViewHolder) holder;
                parentViewHolder.bindView(dataSet.get(position), position, itemDataClickListener);
                break;
            case ItemData.ITEM_TYPE_CHILD:
                ChildViewHolder childViewHolder = (ChildViewHolder) holder;
                childViewHolder.bindView(dataSet.get(position), position, doneChangeListener);
                break;
            default:
                break;
        }
    }

    private final OnDoneChangeListener doneChangeListener = new OnDoneChangeListener() {
        @Override
        public void OnRefreshList(ItemData itemData) {
            // TODO: 2022/4/28
        }
    };


    private final ItemDataClickListener itemDataClickListener = new ItemDataClickListener() {

        @Override
        public void onExpandChildren(ItemData itemData) {
            int position = getCurrentPosition(itemData.getId());
            List<ItemData> children = getChildrenFromPlanList(itemData.getId(), itemData.getTreeDepth());
            addAll(children, position + 1);     // 插入到点击点的下方
            itemData.setChildren(children);
            if (onScrollToListener != null) {
                onScrollToListener.scrollTo(position + children.size());
            }
        }

        @Override
        public void onHideChildren(ItemData itemData) {
            int position = getCurrentPosition(itemData.getId());
            List<ItemData> children = itemData.getChildren();
            if (children == null) return;

            removeAll(position + 1, getChildrenCount(itemData) - 1);
            if (onScrollToListener != null) {
                onScrollToListener.scrollTo(position);
            }
            itemData.setChildren(null);
        }
    };

    /**
     * 从 PlanListSource 中获取 当前节点的 children 列表
     */
    public List<ItemData> getChildrenFromPlanList(int pid, int treeDepth) {
        List<ItemData> list = new ArrayList<>();
        Plan plan = new Plan();
        for (Plan p : My.plans) {
            if (p.getPlanId() == My.plan_id) {
                plan = p;
                break;
            }
        }
        for (PlanBean bean : plan.getPlanBeans()) {
            if (bean.getPid() == pid) {
                ItemData data = new ItemData(bean.getId(), pid, bean.getTitle(), treeDepth + 1, bean.isDone());
                if (bean.isLeaf()) {
                    data.setType(ItemData.ITEM_TYPE_CHILD);
                } else {
                    data.setType(ItemData.ITEM_TYPE_PARENT);
                }
                list.add(data);
            }
        }
        return list;
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case ItemData.ITEM_TYPE_PARENT:
            default:
                view = LayoutInflater.from(ctx).inflate(
                        R.layout.item_plan_list_parent, parent, false);
                return new ParentViewHolder(view);
            case ItemData.ITEM_TYPE_CHILD:
                view = LayoutInflater.from(ctx).inflate(
                        R.layout.item_plan_list_child, parent, false);
                return new ChildViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    /**
     * 从position开始删除，收起列表
     */
    protected void removeAll(int position, int itemCount) {
        for (int i = 0; i < itemCount; i++) {
            dataSet.remove(position);
        }
        notifyItemRangeRemoved(position, itemCount);
    }

    /**
     * 根据 id 定位到 dataset 中的位置
     */
    protected int getCurrentPosition(int id) {
        for (int i = 0; i < dataSet.size(); i++) {
            if (dataSet.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }


    /**
     * 获取子节点列表，插入到position，并更新到 RecyclerView
     */
    public void addAll(List<ItemData> list, int position) {
        dataSet.addAll(position, list);
        notifyItemRangeInserted(position, list.size());
    }

    /**
     * 获取子节点数量
     */
    private int getChildrenCount(ItemData item) {
        List<ItemData> list = new ArrayList<>();
        AddToChildrenList(item, list);
        return list.size();
    }

    private void AddToChildrenList(ItemData item, List<ItemData> list) {
        list.add(item);
        if (item.getChildren() != null) {
            for (int i = 0; i < item.getChildren().size(); i++) {
                AddToChildrenList(item.getChildren().get(i), list);
            }
        }
    }

    /**
     * dp->px
     */
    public int dp2px(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, ctx.getResources().getDisplayMetrics());
    }


}
