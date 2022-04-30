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
import com.example.plana.adapter.viewholder.OnFreshStartToPoint;
import com.example.plana.adapter.viewholder.OnScrollToListener;
import com.example.plana.adapter.viewholder.ParentViewHolder;
import com.example.plana.base.BaseViewHolder;
import com.example.plana.bean.My;
import com.example.plana.bean.Plan;
import com.example.plana.bean.PlanBean;
import com.example.plana.bean.PlanBrief;
import com.example.plana.function.fragment.PlanFragment;

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
        public void OnRefreshList(ItemData itemData, int position) {
            for (Plan plan : My.plans) {
                if (plan.getPlanId() == My.plan_id) {
                    if (itemData.isDone()) {
                        plan.setCurPoint(itemData.getId());
                    } else {
                        int lastPosition = getLastPosition(itemData.getId(), position);
                        plan.setCurPoint(lastPosition);
                    }
                    updatePlanListSelf(plan);
                    notifyItemRangeChanged(0, dataSet.size());
                    break;
                }
            }
        }
    };

    public void updatePlanListSelf(Plan plan) {
        List<PlanBean> beans = plan.getPlanBeans();
        for (PlanBean bean : beans) {
            bean.setDoneCnt(0);
            bean.setTotalCnt(0);
        }
        PlanBean bean = beans.get(0);
        loopUpdate(beans, bean, plan.getCurPoint(), 0);

        PlanBrief brief = plan.getPlanBrief();
        brief.setTotal(bean.getTotalCnt());
        brief.setDone(bean.getDoneCnt());
    }

    private void loopUpdate(List<PlanBean> beans, PlanBean bean, int curPoint, int level) {
        if (bean.getLevel() != level) return;

        int id = bean.getId();
        if (bean.isLeaf()) {
            bean.setTotalCnt(1);
            bean.setDoneCnt(0);
            if (id <= curPoint) {
                bean.setISDoneOrNot(true);
                bean.setDoneCnt(1);
            }
        } else {
            for (int i = id + 1; i < beans.size(); i++) {
                PlanBean planBean = beans.get(i);
                if (planBean.getPid() == id) {
                    loopUpdate(beans, planBean, curPoint, level + 1);
                    bean.setTotalCnt(bean.getTotalCnt() + planBean.getTotalCnt());
                    bean.setDoneCnt(bean.getDoneCnt() + planBean.getDoneCnt());
                }
            }
        }
    }


    /**
     * 取消的时候需要使用
     */
    private int getLastPosition(int id, int position) {
        List<PlanBean> beans = null;
        for (Plan p : My.plans) {
            if (p.getPlanId() == My.plan_id) {
                beans = p.getPlanBeans();
                break;
            }
        }
        if (beans == null) beans = new ArrayList<>();
        for (int i = beans.size() - 1; i >= 0; i--) {
            PlanBean bean = beans.get(i);
            if (bean.isLeaf() && bean.getId() < id && bean.isDone()) {
                if (bean.getLevel() >= dataSet.get(position).getTreeDepth()) {
                    if (bean.getPid() <= dataSet.get(position).getPid()){
                        return bean.getId();
                    }else {
                        // TODO
                    }
                }
            }

        }
        return -1;
    }


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
        List<PlanBean> beans = null;
        for (Plan p : My.plans) {
            if (p.getPlanId() == My.plan_id) {
                beans = p.getPlanBeans();
                break;
            }
        }
        if (beans == null) beans = new ArrayList<>();
        for (PlanBean bean : beans) {
            if (bean.getPid() == pid) {
                ItemData data = new ItemData(bean.getId(), pid, bean.getTitle(), treeDepth + 1, bean.getDoneCnt()>0);
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
