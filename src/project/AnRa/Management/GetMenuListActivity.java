package project.AnRa.Management;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GetMenuListActivity extends Activity {
	private ArrayList<Meal> mealList = new ArrayList<Meal>();
	private MealAdapter mAdapter;
	private BigDecimal basePrice;
	private String defaultUrl = "http://soba.cs.man.ac.uk/~sup9/AnRa/php/getMenuList.php";
	private String url2 = "http://soba.cs.man.ac.uk/~sup9/AnRa/php/getMenuList2.php";

	// Asynchronous Task to perform http get in background with interference
	// with UI thread
	private class Get extends AsyncTask<String, Void, JsonArray> {
		final NumberFormat df = NumberFormat.getCurrencyInstance(Locale.UK);
		private ProgressDialog mProgressDialog = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			mProgressDialog = ProgressDialog.show(GetMenuListActivity.this,
					"Please Wait ...", "Retrieving data ...", true);
			super.onPreExecute();
		}

		@Override
		protected JsonArray doInBackground(String... params) {
			final int timeoutConnection = 5000;
			final HttpClient httpclient = new DefaultHttpClient();
			final HttpParams httpParameters = new BasicHttpParams();

			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			final HttpGet httpget = new HttpGet(params[0]);

			HttpResponse result = null;
			try {
				result = httpclient.execute(httpget);
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
					} // if
				} // finally
			}
			return null;
		}

		@Override
		protected void onPostExecute(JsonArray ja) {
			// TODO Auto-generated method stub
			mProgressDialog.dismiss();

			if (ja != null) {

				for (final JsonElement je : ja) {
					final JsonObject jo = je.getAsJsonObject();
					final String name = jo.getAsJsonPrimitive("name")
							.getAsString();
					final String price = df.format(jo.getAsJsonPrimitive(
							"price").getAsDouble()
							+ basePrice.doubleValue());

					Meal meal = new Meal(name, price);
					mealList.add(meal);
				}
				mAdapter.notifyDataSetChanged();
				super.onPostExecute(ja);
			}
		}
	}

	// Gets JSON array of menu list
	public void getMenulist(String url) {
		new Get().execute(url);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_menu);
		mAdapter = new MealAdapter(this, R.layout.row, mealList);
		final ListView lv = (ListView) findViewById(R.id.list);
		LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View v = li.inflate(R.layout.header, null);
		lv.addHeaderView(v);
		lv.setAdapter(mAdapter);
		new GetBasePrice(GetMenuListActivity.this).execute();

	}
	
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.getmenulist, menu);
		return true;
	}// onCreateOptionsMenu

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {

		switch (item.getItemId()) {
		case R.id.typefirst:
			mealList.clear();
			new GetBasePrice(GetMenuListActivity.this).execute();
			break;

		case R.id.mainfirst:
			mealList.clear();
			new GetBasePrice2(GetMenuListActivity.this).execute();
			break;

		default:
			return super.onOptionsItemSelected(item);
		}// switch
		return true;

	}// onOptionsItemSelected
	
	private final class GetBasePrice extends BasePrice {
		public GetBasePrice(Context c) {
			super(c);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onPostExecute(HttpResponse r) {
			// TODO Auto-generated method stub
			super.onPostExecute(r);
			basePrice = getPrice();
			getMenulist(defaultUrl);
		}
	}
	
	private final class GetBasePrice2 extends BasePrice {
		public GetBasePrice2(Context c) {
			super(c);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onPostExecute(HttpResponse r) {
			// TODO Auto-generated method stub
			super.onPostExecute(r);
			basePrice = getPrice();
			getMenulist(url2);
		}
	}

}
