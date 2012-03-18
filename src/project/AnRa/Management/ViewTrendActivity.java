package project.AnRa.Management;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ViewTrendActivity extends Activity {
	private String TYPE_URL = "http://soba.cs.man.ac.uk/~sup9/AnRa/php/getMenuList.php";
	private String MAIN_URL = "http://soba.cs.man.ac.uk/~sup9/AnRa/php/getMenuList2.php";
	private String ALL_TYPE_URL = "http://soba.cs.man.ac.uk/~sup9/AnRa/php/getAllMealType.php";
	private String ALL_MAIN_URL = "http://soba.cs.man.ac.uk/~sup9/AnRa/php/getAllMealMain.php";
	private MealItemAdapter mAdapter;
	private ArrayList<MealItem> typeList = new ArrayList<MealItem>();
	private ArrayList<MealItem> mainList = new ArrayList<MealItem>();
	private ArrayList<MealItem> popularList = new ArrayList<MealItem>();
	private HashMap<String, String> mainDetail = new HashMap<String, String>();
	private HashMap<String, String> typeDetail = new HashMap<String, String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_trends);

		new Get().execute(MAIN_URL);
		new Get().execute(TYPE_URL);
		new GetDetails().execute(ALL_MAIN_URL, "main_name");
		new GetDetails().execute(ALL_TYPE_URL, "type_name");

		final TextView tv = (TextView) findViewById(R.id.result_text);
		final ListView lv = (ListView) findViewById(R.id.list);
		mAdapter = new MealItemAdapter(ViewTrendActivity.this,
				R.layout.trend_row, popularList);
		lv.setAdapter(mAdapter);

		final Button topButton = (Button) findViewById(R.id.meal_button);
		// onClickListener to get top 20 meals ordered by purchase count
		topButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				lv.setVisibility(View.VISIBLE);
				tv.setVisibility(View.GONE);
				Collections.sort(typeList, new purchaseComparator());
				// clears list to prevent adding to current list
				popularList.clear();
				for (int i = 0; i < 20; i++) {
					popularList.add(typeList.get(i));
				}
				mAdapter.notifyDataSetChanged();
			}
		});

		final Button topMainTypeButton = (Button) findViewById(R.id.main_type_button);
		topMainTypeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				lv.setVisibility(View.GONE);
				tv.setVisibility(View.VISIBLE);
				if (!(typeList.size() > 0) || !(mainList.size() > 0)) {
					Toast.makeText(ViewTrendActivity.this,
							"No meals in database", Toast.LENGTH_SHORT).show();
				} else {
					Collections.sort(typeList, new typeComparator());
					Collections.sort(mainList, new mainComparator());

					String bestType, bestMain;
					int i, permCount, purchaseCount;
					bestType = typeList.get(0).getType_id();
					bestMain = mainList.get(0).getMain_id();
					try {
						i = 0;
						permCount = 0;
						purchaseCount = 0;
						while (typeList.iterator().hasNext()
								&& i != typeList.size() - 1) {

							if (typeList.get(i).getType_id() == typeList.get(
									i + 1).getType_id()) {
								purchaseCount += Integer.parseInt(typeList.get(
										i).getPurchase_count());
								i++;
							} else if (typeList.get(i).getType_id() != typeList
									.get(i + 1).getType_id()) {
								purchaseCount += Integer.parseInt(typeList.get(
										i).getPurchase_count());
								if (purchaseCount > permCount) {
									permCount = purchaseCount;
									bestType = typeList.get(i).getType_id();
									purchaseCount = 0;
								}
								i++;
							}
						}
						Log.e("Best type is: ", bestType);
						Log.e(bestType, Integer.toString(permCount));
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						i = 0;
						permCount = 0;
						purchaseCount = 0;
						while (mainList.iterator().hasNext()
								&& i != mainList.size() - 1) {

							if (mainList.get(i).getMain_id() == mainList.get(
									i + 1).getMain_id()) {
								purchaseCount += Integer.parseInt(mainList.get(
										i).getPurchase_count());
								i++;
							} else if (mainList.get(i).getMain_id() != mainList
									.get(i + 1).getMain_id()) {
								purchaseCount += Integer.parseInt(mainList.get(
										i).getPurchase_count());
								if (purchaseCount > permCount) {
									permCount = purchaseCount;
									bestMain = mainList.get(i).getMain_id();
									purchaseCount = 0;
								}
								i++;
							}
						}
						Log.e("Best main is: ", bestMain);
						Log.e(bestMain, Integer.toString(permCount));
					} catch (Exception e) {
						e.printStackTrace();
					}
					tv.setText("Best Main is: " + mainDetail.get(bestMain)
							+ "\nBest Type is: " + typeDetail.get(bestType));

				}
			}
		});

	}

	private class Get extends AsyncTask<String, Void, JsonElement> {
		private ProgressDialog mProgressDialog = null;

		// false being main, true being type
		private Boolean isType = false;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			mProgressDialog = ProgressDialog.show(ViewTrendActivity.this,
					"Please Wait ...", "Retrieving data ...", true);
			super.onPreExecute();
		}

		@Override
		protected JsonElement doInBackground(String... params) {
			final int timeoutConnection = 5000;
			final HttpClient httpclient = new DefaultHttpClient();
			final HttpParams httpParameters = new BasicHttpParams();

			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);

			final HttpGet httpget = new HttpGet(params[0]);
			if (params[0].toString().equalsIgnoreCase(TYPE_URL))
				isType = true;
			else
				isType = false;

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
					return new JsonParser().parse(json);
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
		protected void onPostExecute(JsonElement j) {
			// TODO Auto-generated method stub
			mProgressDialog.dismiss();

			if (j != null) {
				try {
					final JsonArray ja = j.getAsJsonArray();

					for (final JsonElement je : ja) {
						final JsonObject jo = je.getAsJsonObject();
						final String id = jo.getAsJsonPrimitive("id")
								.getAsString();
						final String name = jo.getAsJsonPrimitive("name")
								.getAsString();
						final String main_id = jo.getAsJsonPrimitive("main_id")
								.getAsString();
						final String type_id = jo.getAsJsonPrimitive("type_id")
								.getAsString();
						final String purchase_count = jo.getAsJsonPrimitive(
								"purchase_count").getAsString();

						MealItem m = new MealItem(id, name, main_id, type_id,
								purchase_count);

						if (isType) {
							typeList.add(m);
						} else if (!isType)
							mainList.add(m);
					}
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
				super.onPostExecute(j);

			}
		}
	}

	private class purchaseComparator implements Comparator<MealItem> {
		@Override
		public int compare(MealItem meal1, MealItem meal2) {
			// TODO Auto-generated method stub
			if (Integer.parseInt(meal1.getPurchase_count()) < Integer
					.parseInt(meal2.getPurchase_count()))
				return 1;
			else if (Integer.parseInt(meal1.getPurchase_count()) > Integer
					.parseInt(meal2.getPurchase_count()))
				return -1;
			else
				return 0;
		}
	}

	private class mainComparator implements Comparator<MealItem> {
		@Override
		public int compare(MealItem meal1, MealItem meal2) {
			// TODO Auto-generated method stub
			if (Integer.parseInt(meal1.getMain_id()) > Integer.parseInt(meal2
					.getMain_id()))
				return 1;
			else if (Integer.parseInt(meal1.getMain_id()) < Integer
					.parseInt(meal2.getMain_id()))
				return -1;
			else
				return 0;
		}
	}

	private class typeComparator implements Comparator<MealItem> {
		@Override
		public int compare(MealItem meal1, MealItem meal2) {
			// TODO Auto-generated method stub
			if (Integer.parseInt(meal1.getType_id()) > Integer.parseInt(meal2
					.getType_id()))
				return 1;
			else if (Integer.parseInt(meal1.getType_id()) < Integer
					.parseInt(meal2.getType_id()))
				return -1;
			else
				return 0;
		}
	}

	private class GetDetails extends AsyncTask<String, Void, HttpResponse> {
		private ProgressDialog mProgressDialog = null;
		private String check;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			mProgressDialog = ProgressDialog.show(ViewTrendActivity.this,
					"Please Wait ...", "Retrieving data ...", true);
			super.onPreExecute();
		}// OnPreExecute

		@Override
		protected HttpResponse doInBackground(String... params) {
			try {
				check = params[1];
				final HttpGet httpgetaddress = new HttpGet(params[0]);
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

				return result;

			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			}
			return null;

		}// doInBackground

		@Override
		protected void onPostExecute(HttpResponse result) {
			mProgressDialog.dismiss();
			super.onPostExecute(result);
			JsonElement jsonElement = null;

			if (result != null) {
				// JsonArray that stores all the meal types
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
					jsonElement = new JsonParser().parse(json);
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

			try {
				final JsonArray jsonArray = jsonElement.getAsJsonArray();

				for (final JsonElement je : jsonArray) {
					final JsonObject jo = je.getAsJsonObject();
					final String id = jo.getAsJsonPrimitive("id").getAsString();
					final String name = jo.getAsJsonPrimitive(check)
							.getAsString();
					if (check.equalsIgnoreCase("type_name")) {
						typeDetail.put(id, name);
					} else
						mainDetail.put(id, name);
				}// for

			} catch (IllegalStateException e) {
				e.printStackTrace();
			}

		}// onPostExecute
	}
}
