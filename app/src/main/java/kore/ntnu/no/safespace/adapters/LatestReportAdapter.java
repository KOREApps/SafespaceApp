package kore.ntnu.no.safespace.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.data.Report;
import kore.ntnu.no.safespace.utils.ImageUtils;

/**
 * Created by KristoffMisbruker on 2017-10-27.
 */

public class LatestReportAdapter extends RecyclerView.Adapter<LatestReportAdapter.ReportViewHolder> {
    List<Report> list = new ArrayList<>();
    private final Context context;
    OnClickListener listener;

    public interface OnClickListener {
        void onClick(int position);
    }

    public LatestReportAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context cont = parent.getContext();
        int layoutId = R.layout.reports_list_item;
        LayoutInflater inflater = LayoutInflater.from(cont);
        View view = inflater.inflate(layoutId, parent, false);
        ReportViewHolder viewHolder = new ReportViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReportViewHolder holder, int position) {
        Report report = list.get(position);

        holder.textView.setText(report.getTitle());
        if(report.getImages().isEmpty()) {
            holder.imageView.setImageResource(R.mipmap.ic_ss_logo_launcher);
        } else {
            try {
                holder.imageView.setImageBitmap(ImageUtils.getBitmap(report.getImages().get(0),8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class ReportViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;

        public ReportViewHolder(View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.tv_reports);
            this.imageView = itemView.findViewById(R.id.iv_reports);
            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onClick(getAdapterPosition());
                }
            });
        }
    }

    public void addReport(Report report) {
        list.add(report);
        notifyDataSetChanged();
    }

    public void setReports(List<? extends Report> reports){
        list.clear();
        list.addAll(reports);
        notifyDataSetChanged();
    }

    public void addReports(List<? extends Report> reports){
        list.addAll(reports);
        notifyDataSetChanged();
    }
    public Report getReportFromList(int position) {
        return list.get(position);
    }
}
