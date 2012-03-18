package project.AnRa.Management;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditTypeActivity extends Activity {

	private String onion, green_pepper, mushroom, beansprouts, pineapple,
			ginger, spring_onion, baby_corn, bamboo_shoot, tomato, cashew;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_meal_type);

		TextView typeName = (TextView) findViewById(R.id.type_name);

		Intent i = getIntent();
		final String type = i.getStringExtra("type");

		final EditText onionEdit = (EditText) findViewById(R.id.onion_field);
		onionEdit.setText(i.getStringExtra("onion"));
		final EditText greenEdit = (EditText) findViewById(R.id.green_pepper_field);
		greenEdit.setText(i.getStringExtra("green"));
		final EditText muEdit = (EditText) findViewById(R.id.mushroom_field);
		muEdit.setText(i.getStringExtra("mushroom"));
		final EditText bsEdit = (EditText) findViewById(R.id.beansprouts_field);
		bsEdit.setText(i.getStringExtra("bs"));
		final EditText piEdit = (EditText) findViewById(R.id.pineapple_field);
		piEdit.setText(i.getStringExtra("pi"));
		final EditText ginEdit = (EditText) findViewById(R.id.ginger_field);
		ginEdit.setText(i.getStringExtra("ging"));
		final EditText springEdit = (EditText) findViewById(R.id.spring_onion_field);
		springEdit.setText(i.getStringExtra("spring"));
		final EditText babyEdit = (EditText) findViewById(R.id.babycorn_field);
		babyEdit.setText(i.getStringExtra("corn"));
		final EditText bambooEdit = (EditText) findViewById(R.id.bamboo_shoot_field);
		bambooEdit.setText(i.getStringExtra("bamboo"));
		
		final EditText tomatoEdit = (EditText) findViewById(R.id.tomato_field);
		tomatoEdit.setText(i.getStringExtra("tomato"));
		final EditText cashewEdit = (EditText) findViewById(R.id.cashew_nuts_field);
		cashewEdit.setText(i.getStringExtra("cashew"));
		Log.e("Second Screen", "" + type);
		typeName.setText(type);

		Button editButton = (Button) findViewById(R.id.edit_type_button);

		editButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					onion = onionEdit.getText().toString();
					green_pepper = greenEdit.getText().toString();
					mushroom = muEdit.getText().toString();
					beansprouts = bsEdit.getText().toString();
					pineapple = piEdit.getText().toString();
					ginger = ginEdit.getText().toString();
					spring_onion = springEdit.getText().toString();
					baby_corn = babyEdit.getText().toString();
					bamboo_shoot = bambooEdit.getText().toString();
					tomato = tomatoEdit.getText().toString();
					cashew = cashewEdit.getText().toString();

					new EditType(EditTypeActivity.this).execute(type, onion,
							green_pepper, mushroom, beansprouts, pineapple,
							ginger, spring_onion, baby_corn, bamboo_shoot, tomato, cashew);
					
					//Intent backIntent = new Intent(getApplicationContext(),
					//		EditTypeChooserActivity.class);
					//startActivity(backIntent);

				} catch (NumberFormatException e) {
					e.printStackTrace();
					Toast.makeText(EditTypeActivity.this,
							"Number format error", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
