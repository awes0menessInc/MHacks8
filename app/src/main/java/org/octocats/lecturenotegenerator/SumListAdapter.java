package org.octocats.lecturenotegenerator;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nisarg on 9/10/16.
 */

public class SumListAdapter extends RecyclerView.Adapter<SumListAdapter.ViewHolder> {

    private OnItemClickListener mItemClickListener;
    private ArrayList<String> summaries = new ArrayList<>();
    private ArrayList<Double> times = new ArrayList<>();

    public SumListAdapter(ArrayList<String> summaries, ArrayList<Double> times){
        this.summaries = summaries;
        this.times = times;
    }

    @Override
    public SumListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SumListAdapter.ViewHolder holder, int position) {
        holder.summary.setText(summaries.get(position));
        holder.time.setText(""+times.get(position));
    }

    @Override
    public int getItemCount() {
        return summaries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView time;
        public final TextView summary;


        public ViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.time);
            summary = (TextView) itemView.findViewById(R.id.textPoint);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final SumListAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}