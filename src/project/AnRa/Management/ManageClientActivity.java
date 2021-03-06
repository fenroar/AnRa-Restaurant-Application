package project.AnRa.Management;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ManageClientActivity extends Activity {
	// private static final String TAG =
	// ManageClientActivity.class.getSimpleName(); // LOGCAT
	private boolean mIsConnected = false;

	// Checks if there is a connection with the Internet/mobile network
	// Check is done in a separate thread form the UI thread
	private class Check extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {

			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnectedOrConnecting()) {
				return true;
			}
			return false;
		}

		@Override
		protected void onCancelled() {

			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Boolean result) {

			mIsConnected = result;
			super.onPostExecute(result);

			// Add Menu Item
			final Button addMenuItemButton = (Button) findViewById(R.id.button2);
			// Update Price
			final Button updatePriceButton = (Button) findViewById(R.id.button1);
			// Base Price
			final Button basePriceButton = (Button) findViewById(R.id.base_price_button);
			// Edit Main
			final Button editMainButton = (Button) findViewById(R.id.edit_main_button);
			// Edit Type
			final Button editTypeButton = (Button) findViewById(R.id.edit_type_button);
			// View Trend
			final Button viewTrendButton = (Button) findViewById(R.id.trends_button);

			if (result) {
				/*
				 * Add new button which does accesses the Add Menu Item manifest
				 * (to be created) Android menu options to be implemented with
				 * the add meal type/add new main strings
				 */

				addMenuItemButton.setEnabled(true);
				updatePriceButton.setEnabled(true);
				basePriceButton.setEnabled(true);
				editMainButton.setEnabled(true);
				editTypeButton.setEnabled(true);
				viewTrendButton.setEnabled(true);

				// addMenuItemButton click listener
				addMenuItemButton
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {

								Intent myIntent = new Intent(
										ManageClientActivity.this,
										AddMenuItemActivity.class);
								startActivity(myIntent);

							}
						});

				// updatePriceButton click listener
				updatePriceButton
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) { 
								Intent myIntent = new Intent(
										ManageClientActivity.this,
										UpdatePriceActivity.class);
								startActivity(myIntent);
							}
						});

				// basePriceButton click listener
				basePriceButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent myIntent = new Intent(ManageClientActivity.this,
								BasePriceActivity.class);
						startActivity(myIntent);

					}
				});

				// editMainButton click listener
				editMainButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent myIntent = new Intent(ManageClientActivity.this,
								EditMainChooserActivity.class);
						startActivity(myIntent);

					}
				});

				// editTypeButton click listener
				editTypeButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent myIntent = new Intent(ManageClientActivity.this,
								EditTypeChooserActivity.class);
						startActivity(myIntent);

					}
				});
				
				// viewTrendButton click listener
				viewTrendButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent myIntent = new Intent(ManageClientActivity.this,
								ViewTrendActivity.class);
						startActivity(myIntent);

					}
				});
			} else {
				// disables buttons

				addMenuItemButton.setEnabled(false);
				updatePriceButton.setEnabled(false);
				basePriceButton.setEnabled(false);
				editMainButton.setEnabled(false);
				editTypeButton.setEnabled(false);
				viewTrendButton.setEnabled(false);

				// dialog message to say that no connection to internet
				Toast.makeText(
						ManageClientActivity.this,
						"Please connect to the internet to use this application.",
						Toast.LENGTH_LONG).show();
			}

		}
	}

	@Override
	protected void onResume() {

		super.onResume();
		if (check == null) {
			check = new Check();
			check.execute();
		}
	}

	Check check = null;

	@Override
	protected void onPause() {
		// interrupt check
		if (check != null) {
			check.cancel(true);
			check = null;
		}
		super.onPause();
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.manageclient, menu);
		return true;
	}// onCreateOptionsMenu

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (mIsConnected) {
			switch (item.getItemId()) {
			case R.id.viewmenu:
				Intent getMenuIntent = new Intent(ManageClientActivity.this,
						GetMenuListActivity.class);
				startActivity(getMenuIntent);
				break;

			case R.id.addmain:
				Intent addMainIntent = new Intent(ManageClientActivity.this,
						AddMainActivity.class);
				startActivity(addMainIntent);
				break;

			case R.id.addtype:
				Intent addTypeIntent = new Intent(ManageClientActivity.this,
						AddTypeActivity.class);
				startActivity(addTypeIntent);
				break;

			default:
				return super.onOptionsItemSelected(item);
			}// switch
			return true;
		}
		Toast.makeText(this,
				"Please connect to the internet to use this application.",
				Toast.LENGTH_SHORT).show();
		return false;
	}// onOptionsItemSelected
}