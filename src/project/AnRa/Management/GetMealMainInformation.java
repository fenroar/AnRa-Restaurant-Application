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

public class GetMealMainInformation extends AsyncTask<String, Void, JsonArray> {	
	private ProgressDialog mProgressDialog = null;
	private MealMain mealMain; 
	private final Context mContext;
	private String main_name;

	public GetMealMainInformation(Context c) {
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
		main_name = params[1];

		HttpResponse result = null;
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("name", main_name));
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
				final String ch = jo.getAsJsonPrimitive("Chicken").getAsString();
				final String bf = jo.getAsJsonPrimitive("Beef")
						.getAsString();
				final String po = jo.getAsJsonPrimitive("Pork")
						.getAsString();
				final String pw = jo.getAsJsonPrimitive("Prawn")
						.getAsString();
				final String charsiu = jo.getAsJsonPrimitive("Char_Siu")
						.getAsString();
				final String ham = jo.getAsJsonPrimitive("Ham")
						.getAsString();
				final String kp = jo.getAsJsonPrimitive("King_Prawn")
						.getAsString();

				Log.e("id", id);
				Log.e("ch", ch);
				Log.e("bf", bf);
				Log.e("po", po);
				Log.e("pw", pw);
				Log.e("charsiu", charsiu);
				Log.e("ham", ham);
				Log.e("kp", kp);
				mealMain = new MealMain(id, main_name, ch, bf, po, pw, charsiu, ham, kp);

			}
			super.onPostExecute(ja);
		}
	}
	
	protected MealMain getMealMain(){
		return mealMain;
	}
}