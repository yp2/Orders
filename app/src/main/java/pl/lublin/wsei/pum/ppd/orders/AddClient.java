package pl.lublin.wsei.pum.ppd.orders;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class AddClient extends ActionBarActivity {

    DBAdapter myDB;
    EditText nameEdit;
    EditText addressEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        openDB();
    }

    public void saveClient(View view){
        // TODO: obsługa pustych pól w formie bo się zapisują
        nameEdit = (EditText) findViewById(R.id.clientName);
        addressEdit = (EditText) findViewById(R.id.clientAddress);
        String client_name = nameEdit.getText().toString();
        String client_address = addressEdit.getText().toString();

        if (client_name.equals("")){
            Toast.makeText(this, "Nazwa klienta nie może być pusta", Toast.LENGTH_SHORT).show();
        } else {
            myDB.insertClient(client_name, client_address);
            Toast.makeText(this, "Klient " + client_name + " został zapisany", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ListClient.class);
            startActivity(intent);
        }
    }

    public void cancel(View view){
        super.onBackPressed();
        closeDB();
    }

    public void clientList(){
        Intent intent = new Intent(this, ListClient.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();

    }

    private void openDB() {
        myDB = new DBAdapter(this);
        myDB.open();
    }

    private void closeDB() {
        myDB.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_client, menu);
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
        if (id == R.id.clientListMenu){
            clientList();
        }
        if (id == R.id.mainScreenMenu){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
