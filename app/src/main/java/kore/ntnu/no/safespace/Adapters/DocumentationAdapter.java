package kore.ntnu.no.safespace.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kore.ntnu.no.safespace.Data.Report;
import kore.ntnu.no.safespace.R;

/**
 * Created by KristoffMisbruker on 2017-10-27.
 */

public class DocumentationAdapter extends RecyclerView.Adapter<DocumentationAdapter.DocumentationViewHolder> {
    List<Report> list = new ArrayList<>();
    private final Context context;
    OnClickListener listener;

    public interface OnClickListener {
        void onClick(int position);
    }

    public DocumentationAdapter(Context context) {
        this.context = context;
    }

    @Override
    public DocumentationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context cont = parent.getContext();
        int layoutId = R.layout.reports_list_item;
        LayoutInflater inflater = LayoutInflater.from(cont);
        View view = inflater.inflate(layoutId, parent, false);
        DocumentationViewHolder viewHolder = new DocumentationAdapter.DocumentationViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DocumentationViewHolder holder, int position) {
        Report report = list.get(position);

        holder.textView.setText(report.getDescription()); //TODO FIKS IMGES OG SÅNT
        holder.imageView.setImageResource(R.mipmap.ic_ss_logo_launcher);

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

    public class DocumentationViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;

        public DocumentationViewHolder(View itemView) {
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

    public Report getReportFromList(int position) {
        return list.get(position);
    }
}
