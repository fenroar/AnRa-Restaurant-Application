package project.AnRa.Management;

import java.math.BigDecimal;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BasePriceActivity extends Activity {
	private BigDecimal basePrice;
	EditText priceEdit = null;

	private final class GetBasePrice extends BasePrice {

		public GetBasePrice(Context c) {
			super(c);
		}//Constructor

		@Override
		protected void onPostExecute(HttpResponse r) {
			super.onPostExecute(r);
			basePrice = getPrice();
			priceEdit = (EditText) findViewById(R.id.edit_field);
			new EditTextListeners().setListeners(priceEdit, true, 5, 0);
			priceEdit.setText(basePrice.toString());
		}//onPostExecute
	}// getBasePrice

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_price);
		new GetBasePrice(this).execute();

		final Button changeButton = (Button) findViewById(R.id.change_button);
		changeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					BigDecimal newPrice = new BigDecimal(priceEdit.getText()
							.toString());
					// price cannot exceed £10.00, else post message
					if (newPrice.doubleValue() <= 10.00) {
						try {
							// AsyncTask updates base price

							Log.i("hi:", "" + newPrice);
							new ChangeBasePrice(BasePriceActivity.this)
									.execute(newPrice.toString());

						} catch (Exception e) {
							e.printStackTrace();
						}
					}// if
					else {
						Toast.makeText(
								BasePriceActivity.this,
								"Base price has to be equal or less than £10.00",
								Toast.LENGTH_SHORT).show();
					}

				} catch (NumberFormatException e) {
					e.printStackTrace();
					Toast.makeText(BasePriceActivity.this,
							"Entry is not in a valid price format",
							Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}
}
