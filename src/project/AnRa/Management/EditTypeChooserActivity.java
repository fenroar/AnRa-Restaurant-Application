package project.AnRa.Management;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

import com.google.gson.JsonArray;

public class EditTypeChooserActivity extends Activity {
	private static final int REQUEST_CODE = 10;
	private static final String getAllMealTypeUrl = "http://soba.cs.man.ac.uk/~sup9/AnRa/php/getAllMealType.php";
	private String getMealTypeUrl = "http://soba.cs.man.ac.uk/~sup9/AnRa/php/getMealType.php";
	final String tag = "EditTypeActivity";
	private MealType mealType;
	String onion, green_pepper, mushroom, beansprouts, pineapple, ginger,
			spring_onion, babycorn, bamboo_shoot, type;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_type_chooser);

		final Spinner typeSpinner = (Spinner) this
				.findViewById(R.id.type_spinner);

		// Populate spinner widget with items from database
		new InitialiseSpinner(typeSpinner, this).execute(getAllMealTypeUrl,
				"type_name");
		typeSpinner
				.setOnItemSelectedListener(new TypeSpinnerSelectedListener());

		Button editButton = (Button) findViewById(R.id.edit_type_button);
		editButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Starting new intent
				Intent myIntent = new Intent(EditTypeChooserActivity.this,
						EditTypeActivity.class);

				// Sending selected type of meal to EditTypeActivity
				myIntent.putExtra("type", typeSpinner.getSelectedItem()
						.toString());
				myIntent.putExtra("onion", mealType.getOnionAmount().toString());
				myIntent.putExtra("green", mealType.getGreenPepperAmount());
				myIntent.putExtra("mushroom", mealType.getMushroomAmount());
				myIntent.putExtra("bs", mealType.getBeansproutsAmount());
				myIntent.putExtra("pi", mealType.getPineappleAmount());
				myIntent.putExtra("ging", mealType.getGingerAmount());
				myIntent.putExtra("spring", mealType.getSpringOnionAmount());
				myIntent.putExtra("corn", mealType.getBabyCornAmount());
				myIntent.putExtra("bamboo", mealType.getBambooShootAmount());
				Log.e("n", typeSpinner.getSelectedItem().toString());
				startActivityForResult(myIntent, REQUEST_CODE);
			}
		});

	}

	private final class GetMealType extends GetMealTypeInformation {

		public GetMealType(Context c) {
			super(c);
		}

		@Override
		protected void onPostExecute(JsonArray ja) {
			super.onPostExecute(ja);
			mealType = getMealType();

		}
	}

	private final class TypeSpinnerSelectedListener implements
			OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent,
				View selectedItemView, int position, long id) {
			type = parent.getItemAtPosition(position).toString();
			Log.e("EditType", "Getting MealType...");
			Log.e("Default type is:", type);
			new GetMealType(EditTypeChooserActivity.this).execute(
					getMealTypeUrl, type);
			Log.e("EditType", "MealType has changed");
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// Do nothing
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (requestCode == REQUEST_CODE) {
			Log.i("EDITTYPE", "Back button does this function here");
			new GetMealType(EditTypeChooserActivity.this).execute(
					getMealTypeUrl, type);

		}
	}
}
