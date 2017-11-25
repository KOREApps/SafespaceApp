package kore.ntnu.no.safespace.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kore.ntnu.no.safespace.data.Image;
import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.utils.ImageUtils;

/**
 * Class description..
 *
 * @author Kristoffer,
 */
public class ImageDisplayAdapter extends RecyclerView.Adapter<ImageDisplayAdapter.ImageDisplayViewHolder> {

    public interface OnClickListener {
        void onClick(int position);
    }

    public interface OnHoldListener {
        void onHold(int position);
    }

    private OnHoldListener onHoldListener;
    private List<Image> images = new ArrayList<>();
    private final Context context;
    private OnClickListener onClickListener;

    public Image getImage(int position) {
        return images.get(position);
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
        try {
            Image image = images.get(position);
            System.out.println(image.getImageFile().getAbsolutePath());
            holder.imageDisplay.setImageBitmap(ImageUtils.getBitmap(image, 3));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnHoldListener(OnHoldListener onHoldListener) {
        this.onHoldListener = onHoldListener;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    public void addImage(Image image) {
        this.images.add(image);
        notifyDataSetChanged();
    }

    public boolean removeImage(Image image) {
        boolean result = this.images.remove(image);
        notifyDataSetChanged();
        return result;
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
            itemView.setOnLongClickListener(v -> {
                if (onHoldListener != null) {
                    onHoldListener.onHold(getAdapterPosition());
                    return true;
                }
                return false;
            });
            itemView.setOnClickListener(view -> {
                if (onClickListener != null) {
                    onClickListener.onClick(getAdapterPosition());
                }
            });
        }
    }
}