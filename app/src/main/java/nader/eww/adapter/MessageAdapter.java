package nader.eww.model.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import nader.eww.R;
import nader.eww.model.Message;

public class MessageAdapter extends ListAdapter<Message, MessageAdapter.MessageViewHolder> {

    private final OnMessageClickListener listener;

    public MessageAdapter(OnMessageClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Message> DIFF_CALLBACK = new DiffUtil.ItemCallback<Message>() {
        @Override
        public boolean areItemsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.content.equals(newItem.content) &&
                   oldItem.seenCount == newItem.seenCount &&
                   oldItem.reactions.equals(newItem.reactions);
        }
    };

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = getItem(position);
        holder.bind(message, listener);
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView contentView;
        private TextView seenCountView;
        private TextView reactionsView;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            contentView = itemView.findViewById(R.id.messageContent);
            seenCountView = itemView.findViewById(R.id.seenCount);
            reactionsView = itemView.findViewById(R.id.reactions);
        }

        void bind(Message message, OnMessageClickListener listener) {
            contentView.setText(message.content);
            seenCountView.setText("Seen: " + message.seenCount);
            
            StringBuilder reactionsText = new StringBuilder();
            for (String reaction : message.reactions.keySet()) {
                reactionsText.append(reaction).append(": ").append(message.reactions.get(reaction)).append(" ");
            }
            reactionsView.setText(reactionsText.toString());

            itemView.setOnClickListener(v -> listener.onMessageClick(message));
        }
    }

    public interface OnMessageClickListener {
        void onMessageClick(Message message);
    }
}

