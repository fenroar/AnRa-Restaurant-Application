package project.AnRa.Management;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class MealAdapter extends ArrayAdapter<Meal>{
	
	private ArrayList<Meal> items;
	private final Context mContext;
	
	public MealAdapter(Context context, int textViewResourceId, ArrayList<Meal> items) {
		super(context, textViewResourceId, items);
		this.items = items;
		mContext = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null){
			LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row, null);
		}
		Meal m = items.get(position);
		if (m != null) {
			TextView tt = (TextView) v.findViewById(R.id.left_text);
			TextView bt = (TextView) v.findViewById(R.id.right_text);
			if (tt != null) {
				tt.setText(m.getMealName()); }
			if (bt != null) {
				bt.setText(m.getMealPrice()); }
		}
		return v;
	}
	

}
