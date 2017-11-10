package me.loki2302;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.loki2302.app.data.Task;

public class TaskListAdapter extends BaseAdapter {
    private final Context context;
    private List<Task> tasks = new ArrayList<Task>();

    public TaskListAdapter(Context context) {
        this.context = context;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tasks.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if(view == null) {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(android.R.layout.simple_list_item_1, null);
        }

        Task task = tasks.get(position);
        ((TextView)view.findViewById(android.R.id.text1)).setText(task.description);

        return view;
    }
}
