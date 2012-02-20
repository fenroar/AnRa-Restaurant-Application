package project.AnRa.Management;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AddMenuItemActivity extends Activity {
	private static final String url1 = "http://soba.cs.man.ac.uk/~sup9/AnRa/php/getMealMain.php";
	private static final String url2 = "http://soba.cs.man.ac.uk/~sup9/AnRa/php/getMealType.php";
	final String tag = "AddMenuItemActivity";

	private class InitialiseSpinner extends
			AsyncTask<String, Void, ArrayAdapter<String>> {
		Spinner spinner;

		private ProgressDialog mProgressDialog = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			mProgressDialog = ProgressDialog.show(AddMenuItemActivity.this,
					"Please Wait ...", "Retrieving data ...", true);
			Log.i(tag, "PREEXECUTE: Dialog");
			super.onPreExecute();
		}

		public InitialiseSpinner(Spinner s) {
			// TODO Auto-generated constructor stub
			spinner = s;

		}

		@Override
		protected ArrayAdapter<String> doInBackground(String... params) {
			try {
				Log.i(tag, "BG: Trying...");
				Log.i(tag, "params legnth is " + params.length);
				if (params.length >= 2) {
					Log.i(tag, "BG: Creating query...");
					return query(params[0].toString(), params[1].toString());
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			}
			return null;

		};

		@Override
		protected void onPostExecute(ArrayAdapter<String> result) {
			// TODO Auto-generated method stub
			mProgressDialog.dismiss();
			Log.i(tag, "POSTEXECUTE: Result");
			spinner.setAdapter(result);
			super.onPostExecute(result);

		}

		public ArrayAdapter<String> query(String url, String check) {
			// JsonArray that stores all the meal types
			JsonArray jsonArray = null;
			final HttpGet httpgetaddress = new HttpGet(url);
			// Default Initialization starts here
			final HttpClient httpclient = new DefaultHttpClient();

			HttpResponse result = null;
			try {
				result = httpclient.execute(httpgetaddress);
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
					}
					jsonArray = new JsonParser().parse(json).getAsJsonArray();
				} catch (final IOException e) {
					e.printStackTrace();
				} finally {
					if (br != null) {
						try {
							br.close();
						} catch (final IOException e) {
							e.printStackTrace();
						} // catch
					} // if
				} // finally
			}

			final String[] array_spinner = new String[jsonArray.size()];

			int i = 0;
			for (final JsonElement je : jsonArray) {
				final JsonObject jo = je.getAsJsonObject();
				final String name = jo.getAsJsonPrimitive(check).getAsString();
				array_spinner[i] = name;
				i++;
			}

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					AddMenuItemActivity.this,
					android.R.layout.simple_spinner_item, array_spinner);

			adapter.setDropDownViewResource(R.layout.spinner);
			return adapter;
		}

	}

	private static boolean[] decComp = new boolean[3], decimal = new boolean[3];

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_menu_item);

		Spinner spinner = (Spinner) this.findViewById(R.id.main_spinner);
		Spinner spinner2 = (Spinner) this.findViewById(R.id.type_spinner);

		new InitialiseSpinner(spinner).execute(url1, "main_name");
		new InitialiseSpinner(spinner2).execute(url2, "type_name");

		EditText priceEdit = (EditText) findViewById(R.id.price_field);
		Button addButton = (Button) findViewById(R.id.load_button);
		
		setListeners(priceEdit, true, 5, 0);
		

		addButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(tag, "Price is: " + price);

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
                        if (v.getText().toString().equals("")) v.setText("0.00");
                        else if (!decimal[index]) v.setText(v.getText().toString() + ".00");
                        else if (!decComp[index]) v.setText(v.getText().toString() + "0");
                    }
                    else {
                        if (v.getText().toString().equals("")) v.setText("0");
                    }
                }
                return true;
            }
        };
        et.setOnEditorActionListener(editListener);
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after){ }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.contains(".")) decimal[index] = true;
                else decimal[index] = false;
                if (!decimal[index]) {
                    if (str.length() == len) {
                        if (str.charAt(str.length() - 1) == '.') return;
                        s.clear();
                        final String st = str.substring(0, str.length() - 1);
                        s.append(st);
                    }
                }
                else {
                    String a = str.substring(str.indexOf(".") + 1);
                    if (a.contains(".")) {
                        a.replace(".", "");
                        str = str.substring(0, str.length() - 1);
                        str.concat(a);
                        s.clear();
                        s.append(str);
                    }
                    if (a.length() != 2) decComp[index] = false;
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

}
