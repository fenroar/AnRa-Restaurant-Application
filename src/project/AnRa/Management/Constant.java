package project.AnRa.Management;

import android.text.TextUtils;
import android.widget.EditText;

public class Constant {

	public static String parseInt(final EditText et)
			throws NumberFormatException {
		
		int number;
		if (TextUtils.isEmpty(et.getText().toString())) {
			number = 0;
		} else {
			number = Integer.parseInt(et.getText().toString());
		}

		return Integer.toString(number);

	}

}
