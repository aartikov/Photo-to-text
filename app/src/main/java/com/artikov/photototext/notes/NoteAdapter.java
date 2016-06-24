package com.artikov.photototext.notes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.artikov.photototext.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date: 24/6/2016
 * Time: 12:28
 *
 * @author Artur Artikov
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT);
    Context mContext;
    List<Note> mNotes;
    OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Note note, int position);
    }

    public NoteAdapter(Context context, List<Note> notes, OnItemClickListener onItemClickListener) {
        mContext = context;
        mNotes = notes;
        mOnItemClickListener = onItemClickListener;
    }

    public NoteAdapter(Context context, OnItemClickListener onItemClickListener) {
        this(context, new ArrayList<Note>(), onItemClickListener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.note_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note = getItem(position);
        holder.nameTextView.setText(note.getName());
        holder.textTextView.setText(note.getText());
        holder.dateTextView.setText(DATE_FORMAT.format(note.getDate()));
    }

    private Note getItem(int position) {
        return mNotes.get(position);
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public void setNotes(List<Note> notes) {
        mNotes = notes;
    }

    public void removeItem(int position) {
        mNotes.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.note_item_text_view_name)
        TextView nameTextView;

        @BindView(R.id.note_item_text_view_text)
        TextView textTextView;

        @BindView(R.id.note_item_text_view_date)
        TextView dateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mOnItemClickListener.onItemClick(getItem(position), position);
                }
            });
        }
    }
}
