package kore.ntnu.no.safespace.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.data.Project;

/**
 * Created by robert on 11/13/17.
 */

public class ProjectSpinnerAdapter extends ArrayAdapter<Project> {

    private Context context;
    private List<Project> values;

    public ProjectSpinnerAdapter(@NonNull Context context, int resource, List<Project> values) {
        super(context, resource);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Nullable
    @Override
    public Project getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return values.get(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getName());
        return label;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getName());
        return label;
    }

    public void setData(List<Project> values){
        this.values = values;
        notifyDataSetChanged();
    }
}
