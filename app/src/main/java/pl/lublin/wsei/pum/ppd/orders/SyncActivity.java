package pl.lublin.wsei.pum.ppd.orders;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class SyncActivity extends ActionBarActivity {

    private DBAdapter myDB;
    private String host;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        openDB();
        username = myDB.getSettingsUsername();
        host = myDB.getSettingsHost();
//        Toast.makeText(this, String.format("%s %s", username, host), Toast.LENGTH_SHORT).show();

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

    // JSON
    private JSONObject writeClientJSON(String clientName, String clientAddress, long id) throws JSONException{
        JSONObject json = new JSONObject();
        json.put("user", username);
        json.put("remote_id", id);
        json.put("name", clientName);
        json.put("address", clientAddress);
        return json;
    }

    private Boolean sendCreateJSON(JSONObject json, String syncUrl)
            throws UnsupportedEncodingException, ClientProtocolException, IOException {
        DefaultHttpClient client = new DefaultHttpClient();


        HttpPost httpPost = new HttpPost(syncUrl);

        StringEntity se = new StringEntity(json.toString());

        httpPost.setEntity(se);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
//        ResponseHandler responseHandler = new BasicResponseHandler();
//        String response = (String) client.execute(httpPost, responseHandler);
//        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        HttpResponse response = client.execute(httpPost);
        StatusLine statusLine = response.getStatusLine();
        if ( statusLine.getStatusCode() == 201){
            // status 201 created - utworzony
            return true;
        }
        return false;
    }

    private Boolean sendModifiedJSON(JSONObject json, String syncUrl, long id)
            throws UnsupportedEncodingException, ClientProtocolException, IOException {

        syncUrl = String.format("%s%s/%d/", syncUrl, username, id);

        DefaultHttpClient client = new DefaultHttpClient();

        HttpPut httpPut = new HttpPut(syncUrl);

        StringEntity se = new StringEntity(json.toString());

        httpPut.setEntity(se);
        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-type", "application/json");
//        ResponseHandler responseHandler = new BasicResponseHandler();
//        String response = (String) client.execute(httpPost, responseHandler);
//        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        HttpResponse response = client.execute(httpPut);
        StatusLine statusLine = response.getStatusLine();
        if ( statusLine.getStatusCode() == 200){
            // status 200 ok
            return true;
        }
        return false;
    }

    private void syncClients(Cursor cursor, String syncUrl, int syncType){
//        Cursor cursor = myDB.getAllCreatedClients();
//        String syncUrl = String.format("http://%s/client/", host);
//        String syncUrl = "http://192.168.2.2:8000/client/";
        JSONObject clientJSON = new JSONObject();
        if(cursor.moveToFirst()){
            do{
                long id = cursor.getLong(DBAdapter.COL_ROWID);
                String clientName = cursor.getString(DBAdapter.C_COL_NAME);
                String clientAddress = cursor.getString(DBAdapter.C_COL_ADDRESS);
                // tworznie JSON
                try {
                    clientJSON = writeClientJSON(clientName, clientAddress, id);
                } catch (JSONException e) {
                    Toast.makeText(this, "Błąd tworzenia JSON" , Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                // Synchronizacja z serwerm
                try {
                    Boolean result = null;
                    if (syncType == DBAdapter.SYNC_CREATE){
                        result = sendCreateJSON(clientJSON, syncUrl);
                    } else if (syncType == DBAdapter.SYNC_MODIFIED){
                        result = sendModifiedJSON(clientJSON, syncUrl, id);
                    }
                    if (result){
                        myDB.setClientSync(id, syncType);
                        Toast.makeText(this, String.format("Klient %s synchronizowany", clientName), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, String.format("Klient %s nie zsynchronizowany", clientName), Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(this, "Błąd send JSON" , Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        else {
            if (syncType == DBAdapter.SYNC_CREATE) {
                Toast.makeText(this, "Nowo utoworzone rekordy zsynchronizowane", Toast.LENGTH_SHORT).show();
            } else if (syncType == DBAdapter.SYNC_MODIFIED){
                Toast.makeText(this, "Zmodyfikowane rekordy zsynchronizowane", Toast.LENGTH_SHORT).show();
            }

        }
        cursor.close();
    }

    public void syncAllClients(View view){
        String syncUrl = String.format("http://%s/client/", host);
        Cursor cursorNewClients = myDB.getAllCreatedClients();
        this.syncClients(cursorNewClients, syncUrl, DBAdapter.SYNC_CREATE);
        Cursor cursorModifiedClients = myDB.getAllModifiedClients();
        this.syncClients(cursorModifiedClients, syncUrl, DBAdapter.SYNC_MODIFIED);
        Toast.makeText(this, "Synchronizacja klientów zakończona", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sync, menu);
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
        if (id == R.id.mainScreenMenu){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
