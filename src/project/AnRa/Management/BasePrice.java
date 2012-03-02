package project.AnRa.Management;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class BasePrice extends AsyncTask<String, Void, HttpResponse> {
	private BigDecimal price;

	private final Context mContext;
	ProgressDialog mProgressDialog = null;

	public BasePrice(Context c) {
		mContext = c;
	}

	@Override
	protected void onPreExecute() {

		mProgressDialog = ProgressDialog.show(mContext, "Please Wait ...",
				"Retrieving data ...", true);
		super.onPreExecute();
	}// onPreExecute

	@Override
	protected HttpResponse doInBackground(String... params) {
		final int timeoutConnection = 5000;
		final HttpClient httpclient = new DefaultHttpClient();
		final HttpParams httpParameters = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		final HttpGet httpget = new HttpGet(
				"http://soba.cs.man.ac.uk/~sup9/AnRa/php/getBasePrice.php");

		HttpResponse result = null;
		try {
			result = httpclient.execute(httpget);
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
		mProgressDialog.dismiss();
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
				}// if
			}// finally
			price = new BigDecimal(result);
		}// if
	}
	
	protected BigDecimal getPrice() {
		return price;
	}

}// BasePrice
