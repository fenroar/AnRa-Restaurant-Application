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


//Gets a URL and deletes meal main/type and all menu items containing that main/type 
public class DeleteItem extends
		AsyncTask<String, Void, HttpResponse> {

	private final Context mContext;
	ProgressDialog mProgressDialog = null;

	public DeleteItem(Context c) {
		mContext = c;
	}

	@Override
	protected void onPreExecute() {
		mProgressDialog = ProgressDialog.show(mContext, "Please Wait ...",
				"Deleting meal(s) ...", true);
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
				params[0]);
		//params[0] is the url
		
		HttpResponse result = null;
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("id", params[1]
					.toString()));
			//params[1] is the id

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

}// DeleteItem