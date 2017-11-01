package kore.ntnu.no.safespace.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import kore.ntnu.no.safespace.Data.Image;
import kore.ntnu.no.safespace.R;

/**
 * Created by Kristoffer on 2017-11-01.
 */

public class ImageDisplayAdapter extends RecyclerView.Adapter<ImageDisplayAdapter.ImageDisplayViewHolder> {

    //TODO:Everything -Kristoffer
    List<Image> list = new ArrayList<>();
    private final Context context;
    LatestReportAdapter.OnClickListener listener;

    public interface OnClickListener {
        void onClick(int position);
    }

    public ImageDisplayAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ImageDisplayAdapter.ImageDisplayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ImageDisplayAdapter.ImageDisplayViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class ImageDisplayViewHolder extends RecyclerView.ViewHolder {
        public CardView cv;
        public ImageView iv;
        public ImageDisplayViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.card_display_cardview);
            iv = itemView.findViewById(R.id.card_display_imageview);
            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onClick(getAdapterPosition());
                }
            });
        }
    }
}