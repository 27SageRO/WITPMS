package com.wittyly.witpms.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wittyly.witpms.R;
import com.wittyly.witpms.model.POJO.FeedModel;
import com.wittyly.witpms.model.Task;
import com.wittyly.witpms.model.Ticket;
import com.wittyly.witpms.model.User;
import com.wittyly.witpms.util.ImageManager;
import com.wittyly.witpms.util.Utilities;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.internal.Util;

public class FeedAdapter extends RecyclerView.Adapter {

    private List<RealmObject> objects;
    private FeedListener listener;

    private static final int VIEW_TASK = 1;
    private static final int VIEW_TICKET = 2;

    public FeedAdapter(List<RealmObject> objects) {
        this.objects = objects;
    }

    public void add(final Task task) {

        boolean validForAppend = true;

        for (RealmObject ref : objects) {
            if (ref instanceof Task && ((Task) ref).getId() == task.getId()) {
                validForAppend = false;
            }
        }

        if (validForAppend) {

            objects.add(task);
            notifyItemInserted(getItemCount());

        }

    }

    public void add(FeedModel feed) {
        if (feed.getTask() != null) {
            add(feed.getTask());
        }
    }

    public void setListener(FeedListener listener) {
        this.listener = listener;
    }

    public void clear() {
        int size = getItemCount();
        objects.clear();
        this.notifyItemRangeRemoved(0, size);
    }

    @Override
    public int getItemViewType(int position) {

        if (objects.get(position) instanceof Task) {
            return VIEW_TASK;
        } else if (objects.get(position) instanceof Ticket) {
            return VIEW_TICKET;
        }

        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TASK) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
            return new TaskVH(itemView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof TaskVH) {

            final Task task = (Task) objects.get(position);
            final TaskVH vh = (TaskVH) holder;

            String number = Integer.toString(task.getSeriesNo()).replace(Integer.toString(task.getTicket().getId()), "");
            String timeAgo = Utilities.getTimeAgo(task.getDateAdded());

            vh.names.setText(task.getAssignees().get(0).getFullName());
            vh.comment.setText(task.getNumberOfComments() + " Comments");
            vh.date.setText(timeAgo);
            vh.subname.setText(
                    task.getTicket().getProject().getShortName()
                            + "-" + Utilities.withZeros(task.getTicket().getSeriesNo())
                            + " > #" + number);

            vh.description.setText(Utilities.fromHtml(
                    "<font color=\""+ Utilities.getStatusRGB(task.getStatus()) +"\"><small>"+ task.getStatus() +"</small></font>" +
                    "<br/>" +
                    task.getDescription()
            ));

            vh.imageContainer.removeAllViews();
            vh.imageContainer.addView(makeImageForUser(vh.convertView.getContext(), task.getAssignees().get(0), 0));

            vh.convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.taskOnClick(task);
                    }
                }
            });

        }

    }

    public ImageView makeImageForUser(Context context, User user, int n) {

        int hw = Utilities.dpToPx(35);

        ImageView img = new ImageView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(hw, hw);
        img.setLayoutParams(layoutParams);

        ImageManager.drawUserImg(context, user, img, hw, hw);

        return img;

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public interface FeedListener {
        void taskOnClick(Task task);
    }

    static class TaskVH extends RecyclerView.ViewHolder {

        public View convertView;

        public LinearLayout imageContainer;
        public TextView names;
        public TextView subname;
        public TextView date;
        public TextView description;
        public TextView comment;

        public TaskVH(View itemView) {
            super(itemView);
            this.convertView = itemView;
            this.imageContainer = (LinearLayout) itemView.findViewById(R.id.image_container);
            this.names = (TextView) itemView.findViewById(R.id.names);
            this.subname = (TextView) itemView.findViewById(R.id.subname);
            this.date = (TextView) itemView.findViewById(R.id.date);
            this.description = (TextView) itemView.findViewById(R.id.description);
            this.comment = (TextView) ((LinearLayout) itemView.findViewById(R.id.comments)).getChildAt(1);
        }
    }

}
