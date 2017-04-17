package cmsc434.doorboardprototype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Huang on 4/1/2017.
 */

public class PersonAdapter extends ArrayAdapter<Person> {
    public PersonAdapter(Context context, ArrayList<Person> users) {
        super(context, 0, users);
    }
    public PersonAdapter(Context context, ArrayList<Person> users, boolean hours) {
        this(context, users);
        this.hours = hours;
    }
    boolean hours = false;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Person p = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.person_row, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.name);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.title);
        TextView tvHours = (TextView) convertView.findViewById(R.id.hours);
        ImageView iv = (ImageView) convertView.findViewById(R.id.profile);
        // Populate the data into the template view using the data object
        tvName.setText(p.name);
        tvTitle.setText(p.title);
        tvHours.setText(p.hours);
        iv.setVisibility(p.profile_visible ? View.VISIBLE : View.INVISIBLE);
        if(iv.getVisibility() == View.VISIBLE) {
            iv.setImageResource(p.default_profile);
        }
        // Return the completed view to render on screen
        return convertView;
    }
}