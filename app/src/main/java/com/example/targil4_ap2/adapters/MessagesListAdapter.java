package com.example.targil4_ap2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.targil4_ap2.R;
import com.example.targil4_ap2.items.MessageToGet;

import java.util.List;

public class MessagesListAdapter extends RecyclerView.Adapter<MessagesListAdapter.MessageViewHolder> {

    class MessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView message;
        private final TextView hourSent;

        private MessageViewHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message); //Or message in
            hourSent = itemView.findViewById(R.id.hourSent);
        }
    }

    private final LayoutInflater mInflater;
    private List<MessageToGet> messsages;

    public MessagesListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.message_layout, parent, false);
        return new MessageViewHolder(itemView);
    }

    public void onBindViewHolder(MessageViewHolder holder, int position) {
        if (messsages != null) {
            final MessageToGet current = messsages.get(position);
            holder.message.setText(current.getContent());
            holder.hourSent.setText(current.getCreated());

            if (current.getSender().getUsername().equals("username")) { // TODO change to your username
                holder.message.setBackgroundResource(R.drawable.message_out_bubble);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.message.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_END);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.setMargins(200, 4, 30, 8);
                holder.message.setLayoutParams(params);

                RelativeLayout.LayoutParams hourParams = (RelativeLayout.LayoutParams) holder.hourSent.getLayoutParams();
                hourParams.addRule(RelativeLayout.ALIGN_PARENT_END);
                hourParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                hourParams.addRule(RelativeLayout.ALIGN_TOP, holder.message.getId());
                holder.hourSent.setLayoutParams(hourParams);
            } else {
                holder.message.setBackgroundResource(R.drawable.message_in_bubble);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.message.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_START);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.setMargins(30, 4, 200, 8);
                holder.message.setLayoutParams(params);

                RelativeLayout.LayoutParams hourParams = (RelativeLayout.LayoutParams) holder.hourSent.getLayoutParams();
                hourParams.addRule(RelativeLayout.ALIGN_PARENT_START);
                hourParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                hourParams.addRule(RelativeLayout.ALIGN_TOP, holder.message.getId());
                holder.hourSent.setLayoutParams(hourParams);
            }
        }
    }


    public void setMessages(List<MessageToGet> m) {
        messsages = m;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (messsages != null)
            return messsages.size();
        else return 0;
    }

    public List<MessageToGet> getMessages() {
        return messsages;
    }

}