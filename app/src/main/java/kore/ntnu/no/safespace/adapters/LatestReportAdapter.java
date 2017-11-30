package kore.ntnu.no.safespace.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.data.Report;
import kore.ntnu.no.safespace.tasks.ImageLoaderTask;

/**
 * The purpose of this class is to serve as an interface between the recycler view that is created
 * in the LatestReportActivity and the Report object data received from the AsyncTasks -
 * GetDocumentationTask & GetReportsTask
 *
 * @author Kristoffer
 */
public class LatestReportAdapter extends RecyclerView.Adapter<LatestReportAdapter.ReportViewHolder> {
    private List<Report> list = new ArrayList<>();
    private final Context context;
    private OnClickListener listener;
    private HashMap<ReportViewHolder, ImageLoaderTask> holderList = new HashMap<>();

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
        if (report.getImages().isEmpty()) {
            holder.imageView.setImageResource(R.mipmap.ic_ss_logo_launcher);
        } else {
            try {
                AsyncTask task = holderList.get(holder);
                if(task != null){
                    task.cancel(false);
                    holderList.remove(holder);
                }
                holder.imageView.setImageBitmap(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888));
                ImageLoaderTask imageTask = new ImageLoaderTask(8, bm -> {holder.imageView.setImageBitmap(bm);
                holderList.remove(holder);});
                holderList.put(holder, imageTask);
                imageTask.execute(report.getImages().get(0));
//                holder.imageView.setImageBitmap(ImageUtils.getBitmap(report.getImages().get(0),8));
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

    public void filterList(List<Report> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
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

    public void setReports(List<? extends Report> reports) {
        list.clear();
        list.addAll(reports);
        notifyDataSetChanged();
    }

    public void addReports(List<? extends Report> reports) {
        list.addAll(reports);
        notifyDataSetChanged();
    }

    public Report getReportFromList(int position) {
        return list.get(position);
    }
}
