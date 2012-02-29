package project.AnRa.Management;

import java.math.BigDecimal;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class UpdatePriceActivity extends Activity {
	private static final String get_menu_url = "http://soba.cs.man.ac.uk/~sup9/AnRa/php/getMenuList.php";
	String name;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_price);
		final Spinner mealSpinner = (Spinner) this
				.findViewById(R.id.meal_spinner);

		final Button updateButton = (Button) this
				.findViewById(R.id.update_button);

		// Fills spinner with existing meal items in database
		new InitialiseSpinner(mealSpinner, this).execute(get_menu_url, "name");

		mealSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,
					View selectedItemView, int position, long id) {
				name = parent.getItemAtPosition(position).toString();
				// Toast.makeText(parent.getContext(), "The meal selected is " +
				// name,
				// Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// Do nothing
			}
		});

		final EditText priceEdit = (EditText) this
				.findViewById(R.id.update_price_field);

		new EditTextListeners().setListeners(priceEdit, true, 5, 0);

		updateButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					BigDecimal price = new BigDecimal(priceEdit.getText()
							.toString());

					// price cannot exceed £1.00, else post message
					if (price.doubleValue() <= 1.00) {
						try {

							String name = mealSpinner.getSelectedItem()
									.toString();
							new UpdateMenuItemPrice(UpdatePriceActivity.this)
									.execute(name, price.toString());
							priceEdit.setText("");
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

}
