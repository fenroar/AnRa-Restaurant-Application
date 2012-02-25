package project.AnRa.Management;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UpdatePriceActivity extends Activity {
	private static final String get_menu_url = "http://soba.cs.man.ac.uk/~sup9/AnRa/php/getMenuList.php";
	private static boolean[] decComp = new boolean[3],
			decimal = new boolean[3];
	String name;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_price);
		final Spinner mealSpinner = (Spinner) this
				.findViewById(R.id.meal_spinner);

		final Button updateButton = (Button) this
				.findViewById(R.id.update_button);
		
		//Fills spinner with existing meal items in database
		new InitialiseSpinner(mealSpinner, this).execute(get_menu_url, "name");
		
		mealSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,
					View selectedItemView, int position, long id) {
				name = parent.getItemAtPosition(position).toString();
				//Toast.makeText(parent.getContext(), "The meal selected is " + name,
				//		Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// Do nothing
			}
		});
		
		final EditText priceEdit = (EditText) this.findViewById(R.id.update_price_field);
		setListeners(priceEdit, true, 5, 0);
		
		updateButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					double price = Double.parseDouble(priceEdit.getText()
							.toString());

					// price cannot exceed £1.00, else post message
					if (price <= 1.00) {
						try {
							
							String name = mealSpinner.getSelectedItem().toString();
							new UpdateMenuItemPrice(UpdatePriceActivity.this).execute(name, Double.toString(price));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}// if
					else {
						Toast.makeText(UpdatePriceActivity.this,
								"Price has to be equal or less than £1.00",
								Toast.LENGTH_SHORT).show();
					}

				} catch (NumberFormatException e) {
					e.printStackTrace();
				}

			}
		});
		
	
	}

	
	private void setListeners(EditText et, final boolean doesEdit,
			final int len, final int index) {
		TextView.OnEditorActionListener editListener = new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_NULL) {
					if (doesEdit) {
						if (v.getText().toString().equals(""))
							v.setText("0.00");
						else if (!decimal[index])
							v.setText(v.getText().toString() + ".00");
						else if (!decComp[index])
							v.setText(v.getText().toString() + "0");
					} else {
						if (v.getText().toString().equals(""))
							v.setText("0");
					}
				}
				return true;
			}
		};
		et.setOnEditorActionListener(editListener);
		TextWatcher watcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String str = s.toString();
				if (str.contains("."))
					decimal[index] = true;
				else
					decimal[index] = false;
				if (!decimal[index]) {
					if (str.length() == len) {
						if (str.charAt(str.length() - 1) == '.')
							return;
						s.clear();
						final String st = str.substring(0, str.length() - 1);
						s.append(st);
					}
				} else {
					String a = str.substring(str.indexOf(".") + 1);
					if (a.contains(".")) {
						a.replace(".", "");
						str = str.substring(0, str.length() - 1);
						str.concat(a);
						s.clear();
						s.append(str);
					}
					if (a.length() != 2)
						decComp[index] = false;
					if (a.length() == 3) {
						a = a.substring(0, 2);
						str = str.substring(0, str.length() - 1);
						str.concat(a);
						s.clear();
						s.append(str);
						decComp[index] = true;
					}
					if (str.length() == (len + 3)) {
						s.clear();
						final String st = str.substring(0, str.length() - 1);
						s.append(st);
					}
				}
			}
		};
		et.addTextChangedListener(watcher);
	}

	

}
