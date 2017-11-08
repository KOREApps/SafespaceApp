package kore.ntnu.no.safespace.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
    List<Image> images = new ArrayList<>();
    private final Context context;
    OnClickListener listener;

    public Image getImage(int position) {
        return images.get(position);
    }

    public interface OnClickListener {
        void onClick(int position);
    }

    public ImageDisplayAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ImageDisplayAdapter.ImageDisplayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_display_image, parent, false);
        return new ImageDisplayViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageDisplayAdapter.ImageDisplayViewHolder holder, int position) {
        Image image = images.get(position);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 3;
        Bitmap imageBitmap = BitmapFactory.decodeFile(image.getImageFile().getAbsolutePath(), opts);
        holder.imageDisplay.setImageBitmap(imageBitmap);
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (images != null) {
            return images.size();
        }
        return 0;
    }

    public class ImageDisplayViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public ImageView imageDisplay;

        public ImageDisplayViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_display_cardView);
            imageDisplay = itemView.findViewById(R.id.card_display_imageView);
            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onClick(getAdapterPosition());
                }
            });
        }
    }
}