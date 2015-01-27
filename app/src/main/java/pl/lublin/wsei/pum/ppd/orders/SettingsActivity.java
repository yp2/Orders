package pl.lublin.wsei.pum.ppd.orders;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SettingsActivity extends ActionBarActivity {
    private DBAdapter myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        openDB();
        populateSettings();
    }

    private void openDB() {
        myDB = new DBAdapter(this);
        myDB.open();
    }

    private void closeDB() {
        myDB.close();
    }

    public void cancel(View view){
        super.onBackPressed();
        closeDB();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void populateSettings(){
        Cursor cursor = myDB.getSettings();
        if (cursor.moveToFirst()) {
            EditText username = (EditText) findViewById(R.id.settingsUsername);
            EditText host = (EditText) findViewById(R.id.settingsHost);
            String settingsUsername = cursor.getString(DBAdapter.S_COL_USERNAME);
            String settingsHost = cursor.getString(DBAdapter.S_COL_HOST);
            username.setText(settingsUsername, TextView.BufferType.EDITABLE);
            host.setText(settingsHost, TextView.BufferType.EDITABLE);
        }
        cursor.close();
    }

    public void updateSettings(View view){
        EditText username = (EditText) findViewById(R.id.settingsUsername);
        EditText host = (EditText) findViewById(R.id.settingsHost);
        String settingsUsername = username.getText().toString();
        String settingsHost = host.getText().toString();
        if (!username.equals("")){
            Boolean result = myDB.updateSettings(settingsUsername, settingsHost);
            if (result){
                Toast.makeText(this, "Dane zostały zapisane", Toast.LENGTH_SHORT).show();
                populateSettings();
            } else {
                Toast.makeText(this, "Dane nie zostały zapisane - bład bazy", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Nazwa użytkownika nie może być pusta", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
