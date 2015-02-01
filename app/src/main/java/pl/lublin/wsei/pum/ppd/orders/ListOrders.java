package pl.lublin.wsei.pum.ppd.orders;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class ListOrders extends ActionBarActivity {

    private DBAdapter myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_orders);
        openDB();

        populateOrderList();
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

    private void populateOrderList(){
        Cursor cursor = myDB.getAllOrders();

        // zamykanie kursora
        startManagingCursor(cursor);

        // mapowanie dla elementu listy
        String [] orderFields = new String[] { DBAdapter.O_KEY_CLIENT, DBAdapter.O_KEY_CONTENT};
        int [] toViewID = new int [] {R.id.orderClientNameLy, R.id.orderContentLy};

        SimpleCursorAdapter clientCurosrAdapter = new SimpleCursorAdapter(
                this,           //context
                R.layout.order_layout,     //Row layout
                cursor,                     //curosr
                orderFields,           //mapowanie
                toViewID                    //mapowanie
        );

        ListView orderList = (ListView) findViewById(R.id.orderListView);
        orderList.setAdapter(clientCurosrAdapter);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_orders, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
