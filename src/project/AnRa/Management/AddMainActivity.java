package project.AnRa.Management;

import static project.AnRa.Management.Constant.parseInt;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddMainActivity extends Activity {
	private String name;
	private String chickenAmount, beefAmount, porkAmount, prawnAmount,
			charsiuAmount, hamAmount, kingprawnAmount;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_meal_main);

		final EditText nameEdit = (EditText) this.findViewById(R.id.name_field);
		final EditText chickenEdit = (EditText) this
				.findViewById(R.id.chicken_field);
		final EditText beefEdit = (EditText) this.findViewById(R.id.beef_field);
		final EditText porkEdit = (EditText) this.findViewById(R.id.pork_field);
		final EditText prawnEdit = (EditText) this
				.findViewById(R.id.prawn_field);
		final EditText charsiuEdit = (EditText) this
				.findViewById(R.id.charsiu_field);
		final EditText hamEdit = (EditText) this.findViewById(R.id.ham_field);
		final EditText kingprawnEdit = (EditText) this
				.findViewById(R.id.kingprawn_field);
		final Button addButton = (Button) this
				.findViewById(R.id.add_main_button);

		addButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				name = nameEdit.getText().toString();
				if (!TextUtils.isEmpty(name)) {

					try {

						chickenAmount = parseInt(chickenEdit);
						beefAmount = parseInt(beefEdit);
						porkAmount = parseInt(porkEdit);
						prawnAmount = parseInt(prawnEdit);
						charsiuAmount = parseInt(charsiuEdit);
						hamAmount = parseInt(hamEdit);
						kingprawnAmount = parseInt(kingprawnEdit);

						new AddMain(AddMainActivity.this).execute(name,
								chickenAmount, beefAmount, porkAmount,
								prawnAmount, charsiuAmount, hamAmount,
								kingprawnAmount);

					} catch (NumberFormatException e) {
						e.printStackTrace();
						Toast.makeText(AddMainActivity.this,
								"Meats has to be a number format",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(AddMainActivity.this,
							"Name of new main should not be empty",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

	}
}
