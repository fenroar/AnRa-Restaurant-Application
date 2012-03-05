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

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GetMealTypeInformation extends AsyncTask<String, Void, JsonArray> {
	private ProgressDialog mProgressDialog = null;
	private MealType mealType;
	private final Context mContext;
	private String type_name;

	public GetMealTypeInformation(Context c) {
		mContext = c;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		mProgressDialog = ProgressDialog.show(mContext, "Please Wait ...",
				"Getting data ...", true);
		super.onPreExecute();
	}

	@Override
	protected JsonArray doInBackground(String... params) {
		final int timeoutConnection = 5000;
		final HttpClient httpclient = new DefaultHttpClient();
		final HttpParams httpParameters = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		final HttpPost httppost = new HttpPost(params[0]);
		type_name = params[1];

		HttpResponse result = null;
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("name", type_name));
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
				final String id = jo.getAsJsonPrimitive("id").getAsString();
				final String on = jo.getAsJsonPrimitive("onion").getAsString();
				final String gp = jo.getAsJsonPrimitive("green_pepper")
						.getAsString();
				final String mu = jo.getAsJsonPrimitive("mushroom")
						.getAsString();
				final String bs = jo.getAsJsonPrimitive("beansprouts")
						.getAsString();
				final String pi = jo.getAsJsonPrimitive("pineapple")
						.getAsString();
				final String ging = jo.getAsJsonPrimitive("ginger")
						.getAsString();
				final String spring = jo.getAsJsonPrimitive("spring_onion")
						.getAsString();
				final String corn = jo.getAsJsonPrimitive("babycorn")
						.getAsString();
				final String bamboo = jo.getAsJsonPrimitive("bamboo_shoot")
						.getAsString();

				Log.e("id", id);
				Log.e("Onion", on);
				Log.e("Green Pepper", gp);
				Log.e("Mushroom", mu);
				Log.e("Beansprouts", bs);
				Log.e("Pineapple", pi);
				Log.e("Ginger", ging);
				Log.e("Spring Onion", spring);
				Log.e("Baby Corn", corn);
				Log.e("Bamboo", bamboo);
				mealType = new MealType(id, type_name, on, gp, mu, bs, pi,
						ging, spring, corn, bamboo);

			}
			super.onPostExecute(ja);
		}
	}

	protected MealType getMealType() {
		return mealType;
	}
}