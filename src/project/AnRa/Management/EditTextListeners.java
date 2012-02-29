package project.AnRa.Management;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class EditTextListeners {
	private static boolean[] decComp = new boolean[3],
			decimal = new boolean[3];
	
	public void setListeners(EditText et, final boolean doesEdit,
			final int len, final int index) {
		TextView.OnEditorActionListener editListener = new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_NULL) {
					if (doesEdit) {
						if (v.getText().toString().equals(""))
							v.setText("0.00");
						else if (!decimal[index])
							v.setText(v.getText().toString() + ".00");
						else if (!decComp[index])
							v.setText(v.getText().toString() + "0");
					} else {
						if (v.getText().toString().equals(""))
							v.setText("0");
					}
				}
				return true;
			}
		};
		et.setOnEditorActionListener(editListener);
		TextWatcher watcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String str = s.toString();
				if (str.contains("."))
					decimal[index] = true;
				else
					decimal[index] = false;
				if (!decimal[index]) {
					if (str.length() == len) {
						if (str.charAt(str.length() - 1) == '.')
							return;
						s.clear();
						final String st = str.substring(0, str.length() - 1);
						s.append(st);
					}
				} else {
					String a = str.substring(str.indexOf(".") + 1);
					if (a.contains(".")) {
						a.replace(".", "");
						str = str.substring(0, str.length() - 1);
						str.concat(a);
						s.clear();
						s.append(str);
					}
					if (a.length() != 2)
						decComp[index] = false;
					if (a.length() == 3) {
						a = a.substring(0, 2);
						str = str.substring(0, str.length() - 1);
						str.concat(a);
						s.clear();
						s.append(str);
						decComp[index] = true;
					}
					if (str.length() == (len + 3)) {
						s.clear();
						final String st = str.substring(0, str.length() - 1);
						s.append(st);
					}
				}
			}
		};
		et.addTextChangedListener(watcher);
	}

}
