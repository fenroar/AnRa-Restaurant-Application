package project.AnRa.Management;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AddMenuItemActivity extends Activity {
	private static final String url1 = "http://soba.cs.man.ac.uk/~sup9/AnRa/php/getMealMain.php";
	private static final String url2 = "http://soba.cs.man.ac.uk/~sup9/AnRa/php/getMealType.php";
	final String tag = "AddMenuItemActivity";
	String main;
	String type;
	Integer main_id = null;
	Integer type_id = null;

	private static boolean[] decComp = new boolean[3],
			decimal = new boolean[3];

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_menu_item);

		final Spinner spinner = (Spinner) this.findViewById(R.id.main_spinner);
		final Spinner spinner2 = (Spinner) this.findViewById(R.id.type_spinner);

		new InitialiseSpinner(spinner, this).execute(url1, "main_name");
		new InitialiseSpinner(spinner2, this).execute(url2, "type_name");

		spinner.setOnItemSelectedListener(new MainSpinnerSelectedListener());
		spinner2.setOnItemSelectedListener(new TypeSpinnerSelectedListener());

		final EditText priceEdit = (EditText) findViewById(R.id.price_field);
		Button addButton = (Button) findViewById(R.id.add_button1);

		setListeners(priceEdit, true, 5, 0);

		addButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					double price = Double.parseDouble(priceEdit.getText()
							.toString());

					// price cannot exceed £1.00, else post message
					if (price <= 1.00) {
						try {
							
							String name = spinner.getSelectedItem().toString()
									+ " "
									+ spinner2.getSelectedItem().toString();
							new AddMenuItemIntoDatabase(
									AddMenuItemActivity.this).execute(name,
									main_id.toString(), type_id.toString(),
									Double.toString(price));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}// if
					else {
						Toast.makeText(AddMenuItemActivity.this,
								"Price has to be equal or less than £1.00",
								Toast.LENGTH_SHORT).show();
					}

				} catch (NumberFormatException e) {
					e.printStackTrace();
				}

			}
		});

	}

	private void setListeners(EditText et, final boolean doesEdit,
			final int len, final int index) {
		TextView.OnEditorActionListener editListener = new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_NULL) {
					if (doesEdit) {
						if (v.getText().toString().equals(""))
							v.setText("0.00");
						else if (!decimal[index])
							v.setText(v.getText().toString() + ".00");
						else if (!decComp[index])
							v.setText(v.getText().toString() + "0");
					} else {
						if (v.getText().toString().equals(""))
							v.setText("0");
					}
				}
				return true;
			}
		};
		et.setOnEditorActionListener(editListener);
		TextWatcher watcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String str = s.toString();
				if (str.contains("."))
					decimal[index] = true;
				else
					decimal[index] = false;
				if (!decimal[index]) {
					if (str.length() == len) {
						if (str.charAt(str.length() - 1) == '.')
							return;
						s.clear();
						final String st = str.substring(0, str.length() - 1);
						s.append(st);
					}
				} else {
					String a = str.substring(str.indexOf(".") + 1);
					if (a.contains(".")) {
						a.replace(".", "");
						str = str.substring(0, str.length() - 1);
						str.concat(a);
						s.clear();
						s.append(str);
					}
					if (a.length() != 2)
						decComp[index] = false;
					if (a.length() == 3) {
						a = a.substring(0, 2);
						str = str.substring(0, str.length() - 1);
						str.concat(a);
						s.clear();
						s.append(str);
						decComp[index] = true;
					}
					if (str.length() == (len + 3)) {
						s.clear();
						final String st = str.substring(0, str.length() - 1);
						s.append(st);
					}
				}
			}
		};
		et.addTextChangedListener(watcher);
	}

	// CheckIds
	private class CheckIDs extends AsyncTask<String, Void, JsonArray> {
		String whichCheck = "";
		private ProgressDialog mProgressDialog = null;

		@Override
		protected void onPreExecute() {
			// Dialog here to prevent user from clicking the
			// "Add new meal to database button"
			// so correct ID will be present when ID is finally changed
			mProgressDialog = ProgressDialog.show(AddMenuItemActivity.this,
					"Please Wait ...", "Getting ID ...", true);
			super.onPreExecute();
		}// onPreExecute

		@Override
		protected JsonArray doInBackground(String... params) {
			final int timeoutConnection = 5000;
			final HttpClient httpclient = new DefaultHttpClient();
			final HttpParams httpParameters = new BasicHttpParams();

			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			final HttpPost httppost = new HttpPost(
					"http://soba.cs.man.ac.uk/~sup9/AnRa/php/checker.php");

			HttpResponse result = null;
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("query", params[0]
						.toString()));
				whichCheck = params[0].toString();

				nameValuePairs.add(new BasicNameValuePair("search_item",
						params[1].toString()));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				result = httpclient.execute(httppost);
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (result != null) {
				BufferedReader br = null;
				String json;
				try {
					br = new BufferedReader(new InputStreamReader(result
							.getEntity().getContent()));
					json = "";
					String s;
					while ((s = br.readLine()) != null) {
						json += s;
					}// while
					return new JsonParser().parse(json).getAsJsonArray();
				} catch (final IOException e) {
					e.printStackTrace();
				} finally {
					if (br != null) {
						try {
							br.close();
						} catch (final IOException e) {
							e.printStackTrace();
						} // catch
					} // if (br != null)
				} // finally
			}// if (result!=null)

			return null;
		}// doInBackground

		@Override
		protected void onPostExecute(JsonArray result) {
			super.onPostExecute(result);
			int[] idArray = new int[result.size()];
			Log.i("CheckIDs", "Size of array is: " + result.size());

			// The size of the array should be 1
			if (result != null) {

				int i = 0;
				for (final JsonElement je : result) {
					final JsonObject jo = je.getAsJsonObject();
					final String id = jo.getAsJsonPrimitive("id").getAsString();
					idArray[i] = Integer.parseInt(id);
					i++;
				}// for
			}// if
			try {
				if (whichCheck == "mainidcheck") {
					main_id = idArray[0];
					Log.i(tag, "" + main_id);
				} else if (whichCheck == "typeidcheck") {
					type_id = idArray[0];
				}
			} catch (Exception e) {
				e.printStackTrace();
			}// catch

			mProgressDialog.dismiss();

		}// OnPostExecute

	}// CheckIDs

	// Spinner selected listeners to get correct ID for selected items.
	private final class MainSpinnerSelectedListener implements
			OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent,
				View selectedItemView, int position, long id) {
			main = parent.getItemAtPosition(position).toString();
			Toast.makeText(parent.getContext(), "The main selected is " + main,
					Toast.LENGTH_SHORT).show();

			new CheckIDs().execute("mainidcheck", main);

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// Do nothing
		}
	}

	private final class TypeSpinnerSelectedListener implements
			OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent,
				View selectedItemView, int position, long id) {
			type = parent.getItemAtPosition(position).toString();
			Toast.makeText(parent.getContext(), "The type selected is " + type,
					Toast.LENGTH_SHORT).show();

			new CheckIDs().execute("typeidcheck", type);

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// Do nothing
		}
	}

}
