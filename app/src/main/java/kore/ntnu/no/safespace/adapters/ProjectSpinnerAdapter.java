package kore.ntnu.no.safespace.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.data.Project;

/**
 * The purpose of this class is to serve as an interface between the spinner view and the list of
 * projects received from the AsyncTask GetAllProjectsTask.
 * @author Robert
 */
public class ProjectSpinnerAdapter extends ArrayAdapter<Project> implements AdapterView.OnItemSelectedListener {

    private Context context;
    private List<Project> values;
    private OnSelectListener onSelectListener;

    public ProjectSpinnerAdapter(@NonNull Context context, int resource, List<Project> values) {
        super(context, resource);
        this.context = context;
        this.values = values;
    }

    public interface OnSelectListener {
        void onSelect(Project project);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (onSelectListener != null) {
            onSelectListener.onSelect(values.get(position));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
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
        label.setTextSize(18);
        label.setOnClickListener(v -> {

        });
        return label;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getName());
        label.setTextSize(18);
        label.setPadding(4,2,4,2);
        label.setBackgroundResource(R.drawable.border_rectangle_thin);
        return label;
    }

    public void setData(List<Project> values){
        this.values = values;
        notifyDataSetChanged();
    }
}
