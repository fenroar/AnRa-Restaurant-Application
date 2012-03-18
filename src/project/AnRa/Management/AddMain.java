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
import android.widget.Toast;

public class AddMain extends
		AsyncTask<String, Void, HttpResponse> {

	private final Context mContext;
	ProgressDialog mProgressDialog = null;

	public AddMain(Context c) {
		mContext = c;
	}

	@Override
	protected void onPreExecute() {
		// Add new main dialog
		mProgressDialog = ProgressDialog.show(mContext, "Please Wait ...",
				"Adding new main ...", true);
		super.onPreExecute();
	}// onPreExecute

	@Override
	protected HttpResponse doInBackground(String... params) {
		final int timeoutConnection = 5000;
		final HttpClient httpclient = new DefaultHttpClient();
		final HttpParams httpParameters = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		final HttpPost httppost = new HttpPost(
				"http://soba.cs.man.ac.uk/~sup9/AnRa/php/insertIntoMealMainIngredients.php");

		HttpResponse result = null;
		try {
			//needs check to see if ArrayList capacity is exceeded
			//for AddType class as well
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("main_name", params[0]
					.toString()));

			nameValuePairs.add(new BasicNameValuePair("chicken", params[1]
					.toString()));

			nameValuePairs.add(new BasicNameValuePair("beef", params[2]
					.toString()));

			nameValuePairs.add(new BasicNameValuePair("pork", params[3]
					.toString()));

			nameValuePairs.add(new BasicNameValuePair("prawn", params[4]
					.toString()));
			
			nameValuePairs.add(new BasicNameValuePair("char_siu", params[5]
					.toString()));
			
			nameValuePairs.add(new BasicNameValuePair("ham", params[6]
					.toString()));
			
			nameValuePairs.add(new BasicNameValuePair("king_prawn", params[7]
					.toString()));
			
			nameValuePairs.add(new BasicNameValuePair("duck", params[8]
					.toString()));
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			result = httpclient.execute(httppost);
			mProgressDialog.dismiss();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}// doInBackground

	@Override
	protected void onPostExecute(HttpResponse r) {
		// TODO Auto-generated method stub
		super.onPostExecute(r);
		if (r != null) {
			BufferedReader br = null;
			String result = null;
			try {
				br = new BufferedReader(new InputStreamReader(r.getEntity()
						.getContent()));
				result = "";
				String s;
				while ((s = br.readLine()) != null) {
					result += s;
				}
			} catch (final IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (final IOException e) {
						e.printStackTrace();
					} // catch
				}//if
			}// finally
			Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
		}// if
	}

}// CheckIDs
