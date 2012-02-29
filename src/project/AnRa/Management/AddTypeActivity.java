package project.AnRa.Management;

import static project.AnRa.Management.Constant.parseInt;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddTypeActivity extends Activity {
	private String name;
	private String onionAmount, greenAmount, muAmount, bsAmount,
			piAmount, ginAmount, springAmount, babyAmount, bambooAmount;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_meal_type);

		final EditText nameEdit = (EditText) this.findViewById(R.id.name_field);
		final EditText onionEdit = (EditText) this
				.findViewById(R.id.onion_field);
		final EditText greenEdit = (EditText) this.findViewById(R.id.green_pepper_field);
		final EditText muEdit = (EditText) this.findViewById(R.id.mushroom_field);
		final EditText bsEdit = (EditText) this
				.findViewById(R.id.beansprouts_field);
		final EditText piEdit = (EditText) this
				.findViewById(R.id.pineapple_field);
		final EditText ginEdit = (EditText) this.findViewById(R.id.ginger_field);
		final EditText springEdit = (EditText) this
				.findViewById(R.id.spring_onion_field);
		final EditText babyEdit = (EditText) this
				.findViewById(R.id.babycorn_field);
		final EditText bambooEdit = (EditText) this
				.findViewById(R.id.bamboo_shoot_field);
		
		final Button addButton = (Button) this
				.findViewById(R.id.add_type_button);

		addButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				name = nameEdit.getText().toString();
				if (!TextUtils.isEmpty(name)) {

					try {

						onionAmount = parseInt(onionEdit);
						greenAmount = parseInt(greenEdit);
						muAmount = parseInt(muEdit);
						bsAmount = parseInt(bsEdit);
						piAmount = parseInt(piEdit);
						ginAmount = parseInt(ginEdit);
						springAmount = parseInt(springEdit);
						babyAmount = parseInt(babyEdit);
						bambooAmount = parseInt(bambooEdit);

						new AddType(AddTypeActivity.this).execute(name,
								onionAmount, greenAmount, muAmount,
								bsAmount, piAmount, ginAmount,
								springAmount, babyAmount, bambooAmount);

					} catch (NumberFormatException e) {
						e.printStackTrace();
						Toast.makeText(AddTypeActivity.this,
								"Types have to be a number format",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(AddTypeActivity.this,
							"Name of new type should not be empty",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

	}
}
