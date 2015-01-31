package pl.lublin.wsei.pum.ppd.orders;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class AddOrder extends ActionBarActivity {
    public final static String IDDB = "pl.lubln.wsei.pum.ppd.orders.IDDB";
    public final static String FROM_ORDER = "pl.lublin.wsei.pum.ppd.orders.FROM_ORDER";
    private DBAdapter myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        openDB();

        populateClientList();
        registerClientListViewClick();
    }

    private void openDB(){
        myDB = new DBAdapter(this);
        myDB.open();
    }

    private void closeDB(){
        myDB.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    public void addClient(){
        Intent intent = new Intent(this, AddClient.class);
        intent.putExtra(FROM_ORDER, true);
        startActivity(intent);
    }

    private void populateClientList(){
        Cursor cursor = myDB.getAllClients();

        // zamykanie kursora
        startManagingCursor(cursor);

        // mapowanie dla elementu listy
        String [] clientFiledNames = new String[] { DBAdapter.C_KEY_NAME, DBAdapter.C_KEY_ADDRESS};
        int [] toViewID = new int [] {R.id.clientNameLy, R.id.clientAddressLy};

        SimpleCursorAdapter clientCurosrAdapter = new SimpleCursorAdapter(
                this,           //context
                R.layout.client_layout,     //Row layout
                cursor,                     //curosr
                clientFiledNames,           //mapowanie
                toViewID                    //mapowanie
        );

        ListView clientList = (ListView) findViewById(R.id.clientListView);
        clientList.setAdapter(clientCurosrAdapter);
    }

    private void registerClientListViewClick(){
        ListView clientList = (ListView) findViewById(R.id.clientListView);
        clientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AddOrder.this, AddOrderDetail.class);
                intent.putExtra(IDDB, id);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_client, menu);
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
        if (id == R.id.addClientMenu){
            addClient();
            return true;
        }
        if (id == R.id.mainScreenMenu){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}