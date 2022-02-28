package com.wittyly.witpms.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wittyly.witpms.R;
import com.wittyly.witpms.model.Comment;
import com.wittyly.witpms.model.Task;
import com.wittyly.witpms.util.ImageManager;
import com.wittyly.witpms.util.Utilities;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentVH> {

    private final Task task;
    private final List<Comment> comments;

    public CommentAdapter(Task task, List<Comment> comments) {
        this.task = task;
        this.comments = comments;
    }

    public void add(Comment comment) {

        boolean validForAppend = true;

        for (Comment ref : comments) {
            if (ref.getId() == comment.getId()) {
                validForAppend = false;
            }
        }

        if (validForAppend) {
            comments.add(comment);
            notifyItemInserted(getItemCount());
        }

    }

    public void clear() {
        int size = getItemCount();
        comments.clear();
        this.notifyItemRangeRemoved(0, size);
    }

    @Override
    public CommentVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentVH(view);
    }

    @Override
    public void onBindViewHolder(CommentVH holder, int position) {

        final Context c = holder.convertView.getContext();

        if (position == 0) {

            String timeAgo = "&nbsp;<font color=\"#4C4C4C\"><small>" + Utilities.getTimeAgo(task.getDateAdded()) + "</small></font>";

            holder.header.setText(Utilities.fromHtml(task.getAssignees().get(0).getFullName() + timeAgo));
            holder.comment.setText(Utilities.fromHtml(
                    "<font color=\""+ Utilities.getStatusRGB(task.getStatus()) +"\"><small>"+ task.getStatus() +"</small></font>" +
                            "<br/>" +
                            task.getDescription()
            ));

            ImageManager.drawUserImg(c, task.getAssignees().get(0), holder.image, 40, 40);

        } else {

            final Comment comment = comments.get(--position);
            String timeAgo = "&nbsp;<font color=\"#4C4C4C\"><small>" + Utilities.getTimeAgo(comment.getDateAdded()) + "</small></font>";

            holder.header.setText(Utilities.fromHtml(comment.getCreator().getFullName() + timeAgo));
            holder.comment.setText(Utilities.fromHtml(comment.getDescription()));
            ImageManager.drawUserImg(c, comment.getCreator(), holder.image, 40, 40);

        }



    }

    @Override
    public int getItemCount() {
        // It is because of the task at the first element
        return comments.size() + 1;
    }

    static class CommentVH extends RecyclerView.ViewHolder {

        public View convertView;
        public ImageView image;
        public TextView header;
        public TextView comment;

        public CommentVH(View itemView) {
            super(itemView);
            this.convertView = itemView;
            this.image = (ImageView) itemView.findViewById(R.id.image);
            this.header = (TextView) itemView.findViewById(R.id.header);
            this.comment = (TextView) itemView.findViewById(R.id.comment);
        }

    }

}
