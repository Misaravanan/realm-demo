package com.shan.chathuranga.realm.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.shan.chathuranga.realm.R;
import com.shan.chathuranga.realm.gson_models.EventParser;
import com.shan.chathuranga.realm.realm_models.EventTable;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by ChathurangaShan on 10/15/2017.
 */

public class EventListAdapter extends RealmRecyclerViewAdapter<EventTable,EventListAdapter.ViewHolder>{

    private ArrayList<EventParser> eventParsers;
    private OrderedRealmCollection<EventTable> eventTables;
    private Context context;

    public EventListAdapter(Context context, ArrayList<EventParser> onlineData, OrderedRealmCollection<EventTable> offlineData) {
        super(offlineData, true);
        this.eventParsers = onlineData;
        this.eventTables = offlineData;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.user_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(eventParsers != null){
            final EventParser eventParser = eventParsers.get(position);
            holder.typeTextView.setText(eventParser.getType());
            holder.nameTextView.setText(eventParser.getActorParser().getDisplayLogin());
            holder.repoNameTextView.setText(eventParser.getRepoParser().getName());

            Picasso.with(context)
                    .load(eventParser.getActorParser().getAvatarUrl())
                    .into(holder.avatarImageView);
        }else{
            EventTable eventTable = eventTables.get(position);
            String type = eventTable.getType();
            holder.typeTextView.setText(type);
            holder.nameTextView.setText(eventTable.getActor().getDisplayLogin());
            holder.repoNameTextView.setText(eventTable.getRepo().getName());

            Picasso.with(context)
                    .load(eventTable.getActor().getImagePath())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.default_image)
                    .into(holder.avatarImageView);
        }

    }

    @Override
    public int getItemCount() {
        if(eventParsers!=null){
            return eventParsers.size();
        }else{
            return eventTables.size();
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.avatar_url)
        CircleImageView avatarImageView;

        @BindView(R.id.name_text_view)
        TextView nameTextView;

        @BindView(R.id.type_text_view)
        TextView typeTextView;

        @BindView(R.id.repo_name_text_view)
        TextView repoNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
