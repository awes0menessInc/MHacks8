package org.octocats.lecturenotegenerator;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nisarg on 9/10/16.
 */

public class SumListAdapter extends RecyclerView.Adapter<SumListAdapter.ViewHolder> {

    private OnItemClickListener mItemClickListener;
    private ArrayList<String> summaries = new ArrayList<>();
    private HashMap<Integer,String> times = new HashMap<>();
    private String contentID;
    private Context mContext;

    public SumListAdapter(ArrayList<String> summaries, String contentID, HashMap<Integer,String> times){
        this.summaries = summaries;
        this.contentID = contentID;
        //this.mContext = mContext;
        this.times = times;
    }

    @Override
    public SumListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SumListAdapter.ViewHolder holder, final int position)  {
        holder.summary.setText(summaries.get(position));


        if(position < times.size())
            holder.time.setText(""+Math.round(Double.parseDouble(times.get(position))));

    }

    @Override
    public int getItemCount() {
        return summaries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView time;
        public final TextView summary;
        public final CardView cv;
        public final LinearLayout summaryHolder;


        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            summaryHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            time = (TextView) itemView.findViewById(R.id.timeStamp);
            summary = (TextView) itemView.findViewById(R.id.textPoint);

            summaryHolder.setOnClickListener(this);
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
