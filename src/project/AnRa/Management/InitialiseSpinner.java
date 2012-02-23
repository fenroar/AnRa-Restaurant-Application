package project.AnRa.Management;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class InitialiseSpinner extends
		AsyncTask<String, Void, ArrayAdapter<String>> {
	private final Spinner spinner;
	private final Context mContext; 
	final String tag = "AddMenuItemActivity";

	private ProgressDialog mProgressDialog = null;

	public InitialiseSpinner(Spinner s, Context c) {
		// TODO Auto-generated constructor stub
		spinner = s;
		mContext = c;

	}//Constructor
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		mProgressDialog = ProgressDialog.show(mContext,
				"Please Wait ...", "Retrieving data ...", true);
		Log.i(tag, "PREEXECUTE: Dialog");
		super.onPreExecute();
	}// OnPreExecute

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

	}//doInBackground

	@Override
	protected void onPostExecute(ArrayAdapter<String> result) {
		// TODO Auto-generated method stub
		mProgressDialog.dismiss();
		Log.i(tag, "POSTEXECUTE: Result");
		spinner.setAdapter(result);
		super.onPostExecute(result);

	}// onPostExecute

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
		}//for

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				mContext, android.R.layout.simple_spinner_item,
				array_spinner);

		adapter.setDropDownViewResource(R.layout.spinner);
		return adapter;
	}// query

}// InitialiseSpinner