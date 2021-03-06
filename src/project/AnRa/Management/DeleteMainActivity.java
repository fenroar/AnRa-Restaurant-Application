package project.AnRa.Management;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DeleteMainActivity extends Activity {
	private static final String deleteUrl = "http://soba.cs.man.ac.uk/~sup9/AnRa/php/deleteMain.php";

	// Gives you an option before deleting all the records.
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.delete_main);

		final TextView mainName = (TextView) findViewById(R.id.main_name);
		final TextView text1 = (TextView) findViewById(R.id.text1);
		final TextView text2 = (TextView) findViewById(R.id.text2);
		
		final Intent i = getIntent();
		final String main = i.getStringExtra("main");
		final String id = i.getStringExtra("id");
		mainName.setText(main);

		final Button yesButton = (Button) findViewById(R.id.yes_button);
		final Button noButton = (Button) findViewById(R.id.no_button);
		final Intent returnIntent = new Intent();
		
		// contains async task that get parameters sent from edit main chooser and
		// does the delete command.
		yesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Starting new intent
				Log.e("deletemain", "HELLO!");
				returnIntent.putExtra("deleteStatus", true);
				setResult(RESULT_OK, returnIntent);

				// Deletes all items with main_id given and the main with id
				// given
				new DeleteItem(DeleteMainActivity.this).execute(deleteUrl, id);

				// Sets the visibility for everything to gone and shows message
				// for user to press back button to continue
				yesButton.setVisibility(View.GONE);
				noButton.setVisibility(View.GONE);
				text1.setVisibility(View.GONE);
				text2.setVisibility(View.GONE);
				mainName.setText("Press back to continue");

			}
		});

		noButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Starting new intent
				returnIntent.putExtra("deleteStatus", false);
				setResult(RESULT_OK, returnIntent);
				finish();
			}
		});

	}

}
