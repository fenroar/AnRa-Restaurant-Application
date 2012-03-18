package project.AnRa.Management;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class MealItemAdapter extends ArrayAdapter<MealItem> {

	private ArrayList<MealItem> items;
	private final Context mContext;

	public MealItemAdapter(Context context, int textViewResourceId,
			ArrayList<MealItem> items) {
		super(context, textViewResourceId, items);
		this.items = items;
		mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.trend_row, null);
		}
		MealItem m = items.get(position);
		if (m != null) {
			TextView lt = (TextView) v.findViewById(R.id.left_text);
			TextView tt = (TextView) v.findViewById(R.id.top_text);
			TextView bt = (TextView) v.findViewById(R.id.bottom_text);
			if (lt != null) {
				lt.setText(Integer.toString(position + 1));
			}
			if (tt != null) {
				tt.setText(m.getName());
			}
			if (bt != null) {
				bt.setText("Purchase count: " + m.getPurchase_count());
			}
		}
		return v;
	}

}
