package org.octocats.lecturenotegenerator;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static org.octocats.lecturenotegenerator.MainActivity.LecList;

/**
 * Created by nisarg on 8/10/16.
 */

public class LecListAdapter extends RecyclerView.Adapter<LecListAdapter.ViewHolder> {

    private OnItemClickListener mItemClickListener;
    private ArrayList<Lecture> lecs = new ArrayList<Lecture>();

    public LecListAdapter(ArrayList<Lecture> lecs) {
        this.lecs = lecs;
    }

    @Override
    public LecListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lec_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LecListAdapter.ViewHolder holder, int position) {
        Lecture lec;

        lec = LecList.get(position);
        holder.title.setText(lec.getTitle());
        holder.date.setText(lec.getDate().toString());

    }

    @Override
    public int getItemCount() {
        return lecs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final RelativeLayout lectureHolder;
        public final TextView title;
        public final TextView date;
        public final ImageView thumb;
        public final CardView cv;


        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            lectureHolder = (RelativeLayout) itemView.findViewById(R.id.mainHolder);
            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);
            thumb = (ImageView) itemView.findViewById(R.id.thumb);
            lectureHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getAdapterPosition());
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
