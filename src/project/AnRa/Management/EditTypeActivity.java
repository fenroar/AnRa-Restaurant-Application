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

public class EditTypeActivity extends Activity {
	private static final String url = "http://soba.cs.man.ac.uk/~sup9/AnRa/php/getAllMealType.php";
	final String tag = "EditTypeActivity";
	String type;
	Integer type_id = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_meal_type);

		final Spinner typeSpinner = (Spinner) this
				.findViewById(R.id.type_spinner);

		// Populate spinner widget with items from database
		new InitialiseSpinner(typeSpinner, this).execute(url, "type_name");

		typeSpinner
				.setOnItemSelectedListener(new TypeSpinnerSelectedListener());

		final EditText priceEdit = (EditText) findViewById(R.id.onion_field);
		Button editButton = (Button) findViewById(R.id.edit_type_button);

		editButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					BigDecimal price = new BigDecimal(priceEdit.getText()
							.toString());

					// price cannot exceed £1.00, else post message
					if (price.doubleValue() <= 1.00) {
						try {

						} catch (Exception e) {
							e.printStackTrace();
						}
					}// if
					else {
						Toast.makeText(EditTypeActivity.this,
								"Price has to be equal or less than £1.00",
								Toast.LENGTH_SHORT).show();
					}

				} catch (NumberFormatException e) {
					e.printStackTrace();
					Toast.makeText(EditTypeActivity.this,
							"Price has to be a price format",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	private final class TypeSpinnerSelectedListener implements
			OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent,
				View selectedItemView, int position, long id) {
			type = parent.getItemAtPosition(position).toString();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// Do nothing
		}
	}

}
