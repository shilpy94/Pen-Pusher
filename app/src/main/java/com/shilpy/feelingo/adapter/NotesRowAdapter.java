package com.shilpy.feelingo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shilpy.feelingo.R;
import com.shilpy.feelingo.activity.AddNotesActivity;
import com.shilpy.feelingo.activity.HomeActivity;
import com.shilpy.feelingo.other.NotelyDBHelper;
import com.shilpy.feelingo.other.NotelyDataModel;
import com.shilpy.feelingo.other.NotelyUtility;

import java.util.List;

/**
 * Created by shilpysamaddar on 24/01/18.
 */

public class NotesRowAdapter extends RecyclerView.Adapter<NotesRowAdapter.NotelyViewHolder> {

    private Context context;
    private List<NotelyDataModel> notelyDataModelsList;

    public NotesRowAdapter(List<NotelyDataModel> notelyDataModelsList, Context context) {
        this.notelyDataModelsList = notelyDataModelsList;
        this.context = context;
    }

    public void notifyDataChange(List<NotelyDataModel> notelyDataModelsList) {
        this.notelyDataModelsList = notelyDataModelsList;
        notifyDataSetChanged();
    }

    public class NotelyViewHolder extends RecyclerView.ViewHolder {
        public TextView row_heading, row_content, row_createdat;
        public ImageView row_starred, row_hearted;
        public LinearLayout main_row, viewForeground;
        public RelativeLayout view_background;

        public NotelyViewHolder(View itemView) {
            super(itemView);
            row_heading = itemView.findViewById(R.id.row_heading);
            row_content = itemView.findViewById(R.id.row_content);
            row_createdat = itemView.findViewById(R.id.row_createdat);
            row_starred = itemView.findViewById(R.id.row_starred);
            row_hearted = itemView.findViewById(R.id.row_hearted);
            main_row = itemView.findViewById(R.id.main_row);
            view_background = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_forground);
        }
    }

    @Override
    public NotelyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_row, parent, false);

        return new NotelyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotelyViewHolder holder, int position) {
        holder.row_heading.setText(notelyDataModelsList.get(position).getTitle());
        holder.row_content.setText(notelyDataModelsList.get(position).getContent());

        NotelyUtility notelyUtility = new NotelyUtility();
        holder.row_createdat.setText(notelyUtility.getDate(notelyDataModelsList.get(position).getCreatedAt()));

        if (notelyDataModelsList.get(position).isFavourite()) {
            holder.row_starred.setColorFilter(context.getResources().getColor(R.color.yellow_starred));
        } else {
            holder.row_starred.setColorFilter(context.getResources().getColor(R.color.light_grey));

        }
        if (notelyDataModelsList.get(position).isHearted()) {
            holder.row_hearted.setColorFilter(context.getResources().getColor(R.color.red_heart));
        } else {
            holder.row_hearted.setColorFilter(context.getResources().getColor(R.color.light_grey));
        }

        holder.row_hearted.setTag(position);
        holder.row_hearted.setOnClickListener(onClickListener);

        holder.row_starred.setTag(position);
        holder.row_starred.setOnClickListener(onClickListener);

        holder.main_row.setTag(position);
        holder.main_row.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return notelyDataModelsList.size();
    }

    /**
     * on Click listener to detect a click on any item of adapter
     */
    public OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int index;
            NotelyDataModel notelyDataModel;
            switch (v.getId()) {
                case R.id.row_starred:
                    index = (int) v.getTag();
                    notelyDataModel = notelyDataModelsList.get(index);
                    notelyDataModel.setFavourite(!notelyDataModelsList.get(index).isFavourite());
                    notifyItemChanged(index, notelyDataModel);
                    updateDatabase(notelyDataModel);
                    break;
                case R.id.row_hearted:
                    index = (int) v.getTag();
                    notelyDataModel = notelyDataModelsList.get(index);
                    notelyDataModel.setHearted(!notelyDataModelsList.get(index).isHearted());
                    notifyItemChanged(index, notelyDataModel);
                    updateDatabase(notelyDataModel);
                    break;
                case R.id.main_row:
                    index = (int) v.getTag();
                    Intent intent = new Intent(context, AddNotesActivity.class);
                    intent.putExtra(NotelyUtility.ROW_ID, notelyDataModelsList.get(index).getId());
                    ((HomeActivity) context).startActivityForResult(intent, NotelyUtility.ACTIVITY_REQUEST_CODE);
                    break;
            }
        }
    };


    /**
     * update database on click of favourite or hearted
     * notify the activity that database is updated
     *
     * @param notelyDataModel
     */
    private void updateDatabase(NotelyDataModel notelyDataModel) {
        NotelyDBHelper.getInstance(context).updateNotes(notelyDataModel);
    }

    /**
     * on swipe of view when the item is deleted this method is called
     *
     * @param position
     */
    public void removeItem(int position) {
        notelyDataModelsList.remove(position);
        // notify the item removed by position
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,notelyDataModelsList.size()-(position)+1);
    }


}
