package net.idolon.crashcatch;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SecondActivity extends Activity {

  public static final int DIVIDEND = 100;

  @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TextView dividend = (TextView) findViewById(R.id.txtDividend);
        dividend.setText(String.valueOf(DIVIDEND));
        TextView name = (TextView) findViewById(R.id.txtName);
        name.setText(R.string.act2);
        View next = findViewById(R.id.btnNext);
        next.setVisibility(View.GONE);
    }

    public void onCrashClick(View v) {
        EditText editNumber = (EditText) findViewById(R.id.edtNumber);
        String value = editNumber.getText().toString();
        int result = DIVIDEND / Integer.parseInt(value);
        TextView txtResult = (TextView) findViewById(R.id.txtResult);
        txtResult.setText(String.valueOf(result));
    }

    public void onBackgroundCrashClick(View v) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                // generate IndexOutOfBoundsException
                params[-1] = null;
                return null;
            }

        }.execute();
    }

}
