package kore.ntnu.no.safespace.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Kristoffer on 2017-10-27.
 */

public class DocumentationAdapter extends RecyclerView.Adapter<DocumentationAdapter.DocumentationViewHolder> {
    List<DataClass> list = new ArrayList<>();
    private final Context context;
    OnClickListener listener;
    public interface OnClickListener{
        void onClick(int position);
    }

    public DocumentationAdapter(Context context) {
        this.context = context;
    }

    @Override
    public DocumentationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(DocumentationViewHolder holder, int position) {

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
        public DocumentationViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                if(listener != null) {
                    listener.onClick(getAdapterPosition());
                }
            });
        }
    }
}
