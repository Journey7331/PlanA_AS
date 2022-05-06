//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.ContentValues;
//import android.content.Intent;
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CheckBox;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.plana.R;
//import com.example.plana.bean.My;
//import com.example.plana.bean.Todos;
//import com.example.plana.config.Constant;
//import com.example.plana.database.DeletedTodosDB;
//import com.example.plana.database.MyDatabaseHelper;
//import com.example.plana.database.TodosDB;
//import com.example.plana.function.todo.EditTodoActivity;
//import com.example.plana.utils.ColorUtil;
//import com.example.plana.utils.TimeCalcUtil;
//
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.ArrayList;
//
///**
// * @program: PlanA
// * @description:
// */
//public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {
//
//    Activity ctx;
//    ArrayList<Todos> arr;
//    MyDatabaseHelper sqlite;
//    RelativeLayout rlEmpty;
//
//    public TodoListAdapter(Activity context, ArrayList<Todos> list, RelativeLayout rlEmpty) {
//        this.arr = list;
//        this.ctx = context;
//        this.rlEmpty = rlEmpty;
//        this.sqlite = new MyDatabaseHelper(ctx);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Todos todo = arr.get(position);
//        holder.tvContent.setText(todo.getContent());
//        holder.cbIsDone.setChecked(todo.isDone());
//
//        // DateTime
//        String dateTime = TimeCalcUtil.getTodoDateStr(todo.getDate(), todo.getTime());
//        if (StringUtils.isNotEmpty(dateTime) && dateTime.length() > 1 && dateTime.charAt(0) == '-') {
//            holder.tvDate.setTextColor(0xFFDE3143);
//            dateTime = dateTime.substring(1);
//        }
//        holder.tvDate.setText(dateTime);
//
//        // Done
//        holder.cbIsDone.setOnClickListener(v -> {
//            boolean status = holder.cbIsDone.isChecked();
//            todo.setDone(status);
//            TodosDB.updateEventDoneState(sqlite, todo.get_id() + "", status);
//        });
//        if (todo.getLevel() != -1) {
//            holder.cbIsDone.setButtonTintList(ColorUtil.ColorStateList((int) (todo.getLevel() * 2)));
//        }
//
//        // ClickListener
//        holder.rlTodoListItem.setOnClickListener(l -> {
//            String memo = todo.getMemo();
//            if (StringUtils.isNotEmpty(memo)) {
//                Toast.makeText(ctx, memo, Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(ctx, "没有备注", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // LongClickListener
//        holder.rlTodoListItem.setOnLongClickListener(l -> {
//            View titleView = ctx.getLayoutInflater().inflate(R.layout.dialog_add_title, null);
//            TextView title = titleView.findViewById(R.id.dialog_title);
//            title.setText("删除 / 更改");
//
//            AlertDialog alertDialog = new AlertDialog
//                    .Builder(ctx, R.style.AlertDialogTheme)
//                    .setCustomTitle(titleView)
//                    .setPositiveButton("更改", (dialog, which) -> {
//                        My.editTodo = todo;
//                        directToEditActivity();
//                    })
//                    .setNegativeButton("删除", (dialog, which) -> {
//                        ContentValues todo_values = new ContentValues();
//                        todo_values.put(TodosDB._id, todo.get_id());
//                        todo_values.put(TodosDB.content, todo.getContent());
//                        todo_values.put(TodosDB.memo, todo.getMemo());
//                        todo_values.put(TodosDB.done, todo.isDone());
//                        todo_values.put(TodosDB.date, todo.getDate());
//                        todo_values.put(TodosDB.time, todo.getTime());
//                        todo_values.put(TodosDB.level, todo.getLevel());
//
//                        DeletedTodosDB.insertTodo(sqlite, todo_values);
//                        TodosDB.deleteEventById(sqlite, todo.get_id() + "");
//
//                        My.todosList.remove(todo);
//                        arr.remove(holder.getAdapterPosition());
//                        notifyItemRemoved(holder.getAdapterPosition());
//                        notifyItemRangeChanged(position, arr.size() - position);
//
//                        checkEmpty();
//                        Toast.makeText(ctx, "成功删除", Toast.LENGTH_SHORT).show();
//                    })
//                    .setNeutralButton("取消", null).create();
//
//            alertDialog.show();
//            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Constant.MyColor.BlueGrey);
//            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Constant.MyColor.BlueGrey);
//            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Constant.MyColor.BlueGrey);
//            return true;
//        });
//
//    }
//
//    /**
//     * 更新全部数据
//     */
//    public void setDataList(ArrayList<Todos> list) {
//        arr.clear();
//        arr.addAll(list);
//        notifyItemRangeInserted(0, arr.size());
//    }
//
//    /**
//     * 显示列表为空图
//     */
//    public void checkEmpty() {
//        if (arr.size() > 0) {
//            rlEmpty.setVisibility(View.INVISIBLE);
//        } else {
//            rlEmpty.setVisibility(View.VISIBLE);
//        }
//    }
//
//    /**
//     * 跳转到更改界面
//     */
//    private void directToEditActivity() {
//        Intent intent = new Intent(ctx, EditTodoActivity.class);
//        ctx.startActivity(intent);
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater
//                .from(parent.getContext())
//                .inflate(R.layout.item_todos_list, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public int getItemCount() {
//        return arr.size();
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder {
//        RelativeLayout rlTodoListItem;
//        CheckBox cbIsDone;
//        TextView tvContent;
//        TextView tvDate;
//
//        public ViewHolder(@NonNull View view) {
//            super(view);
//            rlTodoListItem = view.findViewById(R.id.rl_todo_list_item);
//            cbIsDone = view.findViewById(R.id.cb_is_done);
//            tvContent = view.findViewById(R.id.tv_content);
//            tvDate = view.findViewById(R.id.tv_date);
//        }
//    }
//
//}
