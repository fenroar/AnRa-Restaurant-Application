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

public class EditMainChooserActivity extends Activity {
	private static final int REQUEST_CODE = 11;
	private static final String getAllMealMainUrl = "http://soba.cs.man.ac.uk/~sup9/AnRa/php/getAllMealMain.php";
	private String getMealMainUrl = "http://soba.cs.man.ac.uk/~sup9/AnRa/php/getMealMain.php";
	final String tag = "EditMainActivity";
	private MealMain mealMain;
	String chicken, beef, pork, prawn, charSiu, ham, kingPrawn, main;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_main_chooser);

		final Spinner mainSpinner = (Spinner) this
				.findViewById(R.id.main_spinner);

		// Populate spinner widget with items from database
		new InitialiseSpinner(mainSpinner, this).execute(getAllMealMainUrl, "main_name");
		mainSpinner.setOnItemSelectedListener(new MainSpinnerSelectedListener());

		Button editButton = (Button) findViewById(R.id.edit_main_button);
		editButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Starting new intent
				Intent myIntent = new Intent(getApplicationContext(),
						EditMainActivity.class);

				// Sending selected type of meal to EditTypeActivity
				myIntent.putExtra("main", mainSpinner.getSelectedItem()
						.toString());
				myIntent.putExtra("chicken", mealMain.getChicken());
				myIntent.putExtra("beef", mealMain.getBeef());
				myIntent.putExtra("pork", mealMain.getPorkAmount());
				myIntent.putExtra("prawn", mealMain.getPrawnAmount());
				myIntent.putExtra("charSiu", mealMain.getCharSiuAmount());
				myIntent.putExtra("ham", mealMain.getHamAmount());
				myIntent.putExtra("kingPrawn", mealMain.getKingPrawnAmount());
				Log.e("n", mainSpinner.getSelectedItem().toString());
				startActivityForResult(myIntent, REQUEST_CODE);
			}
		});

	}

	private final class GetMealMain extends GetMealMainInformation {

		public GetMealMain(Context c) {
			super(c);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onPostExecute(JsonArray ja) {
			super.onPostExecute(ja);
			mealMain = getMealMain();

		}
	}

	private final class MainSpinnerSelectedListener implements
			OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent,
				View selectedItemView, int position, long id) {
			main = parent.getItemAtPosition(position).toString();
			Log.e("EditMain", "Getting MealMain...");
			new GetMealMain(EditMainChooserActivity.this).execute(getMealMainUrl, main);
			Log.e("EditMain", "MealMain has changed");
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
			new GetMealMain(EditMainChooserActivity.this).execute(
					getMealMainUrl, main);

		}
	}
}
