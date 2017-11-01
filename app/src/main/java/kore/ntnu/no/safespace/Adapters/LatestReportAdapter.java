package kore.ntnu.no.safespace.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erik on 01.11.2017.
 */

public class LatestReportAdapter extends RecyclerView.Adapter<LatestReportAdapter.LatestReportViewHolder> {

    List<String> list = new ArrayList<>();
    private final Context context;
    LatestReportAdapter.OnClickListener listener;
    public interface OnClickListener{
        void onClick(int position);
    }

    public LatestReportAdapter(Context context) {
        this.context = context;
    }

    @Override
    public LatestReportAdapter.LatestReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(LatestReportAdapter.LatestReportViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class LatestReportViewHolder extends RecyclerView.ViewHolder {
        public LatestReportViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                if(listener != null) {
                    listener.onClick(getAdapterPosition());
                }
            });
        }
    }
}
