package project.AnRa.Management;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditMainActivity extends Activity {

	private String chicken, beef, pork, prawn, charSiu, ham, kingPrawn;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_meal_main);

		TextView mainName = (TextView) findViewById(R.id.main_name);

		Intent i = getIntent();
		final String main = i.getStringExtra("main");

		final EditText chickenEdit = (EditText) findViewById(R.id.chicken_field);
		chickenEdit.setText(i.getStringExtra("chicken"));
		final EditText beefEdit = (EditText) findViewById(R.id.beef_field);
		beefEdit.setText(i.getStringExtra("beef"));
		final EditText porkEdit = (EditText) findViewById(R.id.pork_field);
		porkEdit.setText(i.getStringExtra("pork"));
		final EditText prawnEdit = (EditText) findViewById(R.id.prawn_field);
		prawnEdit.setText(i.getStringExtra("prawn"));
		final EditText charSiuEdit = (EditText) findViewById(R.id.charsiu_field);
		charSiuEdit.setText(i.getStringExtra("charSiu"));
		final EditText hamEdit = (EditText) findViewById(R.id.ham_field);
		hamEdit.setText(i.getStringExtra("ham"));
		final EditText kingPrawnEdit = (EditText) findViewById(R.id.kingprawn_field);
		kingPrawnEdit.setText(i.getStringExtra("kingPrawn"));
		mainName.setText(main);

		Button editButton = (Button) findViewById(R.id.edit_main_button);

		editButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					chicken = chickenEdit.getText().toString();
					beef = beefEdit.getText().toString();
					pork = porkEdit.getText().toString();
					prawn = prawnEdit.getText().toString();
					charSiu = charSiuEdit.getText().toString();
					ham = hamEdit.getText().toString();
					kingPrawn = kingPrawnEdit.getText().toString();

					new EditMain(EditMainActivity.this).execute(main, chicken,
							beef, pork, prawn, charSiu, ham, kingPrawn);

				} catch (NumberFormatException e) {
					e.printStackTrace();
					Toast.makeText(EditMainActivity.this,
							"Number format error", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}
}
