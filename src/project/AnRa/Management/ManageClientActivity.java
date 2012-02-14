package project.AnRa.Management;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ManageClientActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// final Button button = (Button) findViewById(R.id.button);
		/*
		 * button.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub new GetMenuList().getData(); } });
		 */
	}

	public void onButtonClick(final View v) {
			Intent myIntent = new Intent(ManageClientActivity.this, GetMenuListActivity.class);
			startActivity(myIntent);
	}
}