package pl.lublin.wsei.pum.ppd.orders;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class AddOrderDetail extends ActionBarActivity {
    private long IDDB;
    private DBAdapter myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order_details);
        Intent intent = getIntent();
        IDDB = intent.getLongExtra(ListClient.IDDB, -1);
        openDB();
        Toast.makeText(this, "" + IDDB, Toast.LENGTH_SHORT).show();

//        populateClientDetail();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_order_detail, menu);
        return true;
    }

    public void addnewOrder (View view){
        EditText content = (EditText) findViewById(R.id.content);
        String scontent = content.getText().toString();
        myDB.insertOrder(IDDB, scontent);
        Intent intent = new Intent(this, ListOrders.class);
        startActivity(intent);
    }


    /*

    public void updateClient(View view) {
        EditText name = (EditText) findViewById(R.id.clientDetailName);
        EditText address = (EditText) findViewById(R.id.clientDetailAddress);
        String client_name = name.getText().toString();
        String client_address = address.getText().toString();
        if (IDDB >= 0) {
            if (!client_name.equals("")) {
                Boolean result = myDB.updateClient(IDDB, client_name, client_address);
                if (result) {
                    Toast.makeText(this, "Dane zostały zapisane", Toast.LENGTH_SHORT).show();
                    populateClientDetail();
                } else {
                    Toast.makeText(this, "Dane nie zostały zapisane - bład bazy", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Nazwa klienta nie może być pusta", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Błędne id", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteClient(View view){
        if (IDDB >= 0) {
            Boolean result = myDB.deleteClient(IDDB);
            if (result){
                Toast.makeText(this, "Dane zostały usunięte", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ListClient.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Dane nie zostały usunięte", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Błędne id", Toast.LENGTH_SHORT).show();
        }
    }
    */

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
            Intent intent = new Intent(this, AddOrder.class);
            startActivity(intent);
        }
        if (id == R.id.mainScreenMenu){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}