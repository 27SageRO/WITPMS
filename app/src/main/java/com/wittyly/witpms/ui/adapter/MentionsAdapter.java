package com.wittyly.witpms.ui.adapter;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wittyly.witpms.R;
import com.wittyly.witpms.model.POJO.HashtagMentionModel;
import com.wittyly.witpms.model.Project;
import com.wittyly.witpms.model.Ticket;
import com.wittyly.witpms.model.TicketCategory;
import com.wittyly.witpms.model.User;
import com.wittyly.witpms.util.ImageManager;
import com.wittyly.witpms.util.Utilities;

import java.util.List;

import io.realm.RealmObject;

public class MentionsAdapter extends RecyclerView.Adapter<MentionsAdapter.MentionsAdapterVH> {

    private List<RealmObject> realmObjects;
    private MentionsAdapter.MentionsListener listener;
    private ConstraintLayout.LayoutParams shrinkParams;

    private boolean ticketsVisibility;
    private boolean projectsVisibility;
    private boolean ticketCategoryVisibility;

    public MentionsAdapter(List<RealmObject> realmObjects) {
        this.realmObjects = realmObjects;
        ticketsVisibility = false;
        projectsVisibility = false;
        ticketCategoryVisibility = false;
        shrinkParams = new ConstraintLayout.LayoutParams(0, Utilities.dpToPx(40));
    }

    public boolean isTicketsVisible() {
        return ticketsVisibility;
    }

    public void setTicketsVisibility(boolean ticketsVisibility) {
        this.ticketsVisibility = ticketsVisibility;
    }

    public boolean isProjectsVisible() {
        return projectsVisibility;
    }

    public void setProjectsVisibility(boolean projectsVisibility) {
        this.projectsVisibility = projectsVisibility;
    }

    public boolean isTicketCategoryVisible() {
        return ticketCategoryVisibility;
    }

    public void setTicketCategoryVisibility(boolean ticketCategoryVisibility) {
        this.ticketCategoryVisibility = ticketCategoryVisibility;
    }

    public void add(RealmObject object) {

        boolean validForAppend = true;

        if (object instanceof User) {

            User user = (User) object;

            for (RealmObject ref : realmObjects) {
                if (ref instanceof User && ((User) ref).getId() == user.getId()) {
                    validForAppend = false;
                }
            }

        } else if (object instanceof Project) {

            Project project = (Project) object;

            for (RealmObject ref : realmObjects) {
                if (ref instanceof Project && ((Project) ref).getId() == project.getId()) {
                    validForAppend = false;
                }
            }

        } else if (object instanceof Ticket) {

            Ticket ticket = (Ticket) object;

            for (RealmObject ref : realmObjects) {
                if (ref instanceof Ticket && ((Ticket) ref).getId() == ticket.getId()) {
                    validForAppend = false;
                }
            }

        } else if (object instanceof TicketCategory) {

            TicketCategory category = (TicketCategory) object;

            for (RealmObject ref : realmObjects) {
                if (ref instanceof TicketCategory && ((TicketCategory) ref).getId() == category.getId()) {
                    validForAppend = false;
                }
            }


        }

        if (validForAppend) {
            realmObjects.add(object);
            notifyItemInserted(getItemCount());
        }

    }

    public void add(HashtagMentionModel model) {

        if (isProjectsVisible()) {
            for (Project project : model.getProjects()) {
                add(project);
            }
        }

        if (isTicketsVisible()) {
            for (Ticket ticket : model.getTickets()) {
                add(ticket);
            }
        }

        if (isTicketCategoryVisible()) {
            for (TicketCategory category : model.getTicketCategories()) {
                add(category);
            }
        }

    }

    public void clear() {
        int size = getItemCount();
        realmObjects.clear();
        this.notifyItemRangeRemoved(0, size);
    }

    public void setListener(MentionsAdapter.MentionsListener listener) {
        this.listener = listener;
    }

    @Override
    public MentionsAdapterVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_mentions_item, parent, false);
        return new MentionsAdapterVH(itemView);
    }

    @Override
    public void onBindViewHolder(MentionsAdapterVH holder, int position) {

        if (realmObjects.get(position) instanceof User) {

            final User user = (User) realmObjects.get(position);

            holder.name.setText(user.getFullName());
            holder.info.setText(user.getEmail());
            ImageManager.drawUserImg(holder.convertView.getContext(), user, holder.icon, 130);

            holder.convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onSelectMentionedUser(user);
                    }
                }
            });

        } else if (realmObjects.get(position) instanceof Project) {

            final Project project = (Project) realmObjects.get(position);

            holder.name.setText(project.getName());
            holder.info.setText(project.getDescription());
            ImageManager.drawShortname(holder.convertView.getContext(), project, holder.icon, 40, 40);

        } else if (realmObjects.get(position) instanceof Ticket) {

            final Ticket ticket = (Ticket) realmObjects.get(position);

            holder.name.setText(ticket.getTitle());
            holder.info.setText("Ticket");
            ImageManager.drawShortname(holder.convertView.getContext(), ticket.getGeneratedId(), holder.icon, 40, 40);

            holder.convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onSelectMentionedTicket(ticket);
                    }
                }
            });

        } else if (realmObjects.get(position) instanceof TicketCategory) {

            final TicketCategory category = (TicketCategory) realmObjects.get(position);

            // TODO

        }

    }

    @Override
    public int getItemCount() {
        return realmObjects.size();
    }

    public interface MentionsListener {
        void onSelectMentionedUser(User user);
        void onSelectMentionedTicket(Ticket ticket);
    }

    static class MentionsAdapterVH extends RecyclerView.ViewHolder {

        public View convertView;
        public ImageView icon;
        public TextView name;
        public TextView info;

        public MentionsAdapterVH(View itemView) {
            super(itemView);
            convertView = itemView;
            icon    = (ImageView) itemView.findViewById(R.id.mention_icon);
            name    = (TextView) itemView.findViewById(R.id.mention_name);
            info    = (TextView) itemView.findViewById(R.id.mention_info);
        }
    }

}
