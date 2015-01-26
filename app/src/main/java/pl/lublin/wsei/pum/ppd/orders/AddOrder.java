package pl.lublin.wsei.pum.ppd.orders;
/**
 * Created by proczniak on 25/01/15.
 */

/*
lubudubu
 */
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class AddOrder extends ActionBarActivity {

    DBAdapter myDB;
    //EditText nameEdit;
    //EditText addressEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        openDB();
    }
    private void openDB() {
        myDB = new DBAdapter(this);
        myDB.open();
    }
    private void closeDB() {
        myDB.close();
    }


}