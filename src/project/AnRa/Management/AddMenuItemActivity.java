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
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AddMenuItemActivity extends Activity {


	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_menu_item);
		final HttpGet httpget1 = new HttpGet(
				"http://soba.cs.man.ac.uk/~sup9/AnRa/php/getMealType.php");
		final HttpGet httpget2 = new HttpGet(
				"http://soba.cs.man.ac.uk/~sup9/AnRa/php/getMealMain.php");

		Spinner spinner = (Spinner) this.findViewById(R.id.type_spinner);
		Spinner spinner2 = (Spinner) this.findViewById(R.id.main_spinner);

		final ArrayAdapter<String> adapter = query(httpget1, "type_name");
		final ArrayAdapter<String> adapter2 = query(httpget2, "main_name");

		spinner2.setAdapter(adapter2);
		spinner.setAdapter(adapter);

		
	}

	// This should be done in Async Task
	public ArrayAdapter<String> query(HttpGet url, String check) {
		// Json Array that stores all the meal types
		JsonArray jsonArray = null;
		HttpGet httpgetaddress = url;
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

		final String[] type_array_spinner = new String[jsonArray.size()];
		int show_total = jsonArray.size();
		Toast.makeText(this, show_total + " types", Toast.LENGTH_LONG).show();

		int i = 0;
		for (final JsonElement je : jsonArray) {
			final JsonObject jo = je.getAsJsonObject();
			final String name = jo.getAsJsonPrimitive(check).getAsString();
			type_array_spinner[i] = name;
			i++;
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, type_array_spinner);

		adapter.setDropDownViewResource(R.layout.spinner);
		return adapter;
	}

}
