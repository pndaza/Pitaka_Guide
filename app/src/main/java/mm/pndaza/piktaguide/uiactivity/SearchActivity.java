package mm.pndaza.piktaguide.uiactivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import mm.pndaza.piktaguide.adapter.SearchAdapter;
import mm.pndaza.piktaguide.database.DBOpenHelper;
import mm.pndaza.piktaguide.R;
import mm.pndaza.piktaguide.utils.MDetect;
import mm.pndaza.piktaguide.utils.Rabbit;


public class SearchActivity extends AppCompatActivity {

    private Context context;
    private DBOpenHelper db = null;
    private SQLiteDatabase sqLiteDatabase = null;
    private Cursor cursor = null;
    private  ListView listView;
    private SearchAdapter adapter = null;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        MDetect.init(this);

        tv = findViewById(R.id.tv_empty);

        db = DBOpenHelper.getInstance(context);
        sqLiteDatabase = db.getReadableDatabase();
        // Create empty adapter
        adapter = new SearchAdapter(context,null);
        listView = findViewById(R.id.search_listview);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                int _id  = ((Cursor)adapterView.getItemAtPosition(position)).getInt(0);

                db = DBOpenHelper.getInstance(context);
                sqLiteDatabase = db.getReadableDatabase();
                Cursor cursor = sqLiteDatabase.rawQuery("select page, volume from indexes where _id = " + _id , null);
                cursor.moveToFirst();


                int page = cursor.getInt(0);
                int volume = cursor.getInt(1);

                cursor.close();

                Intent bookView = new Intent(SearchActivity.this.getApplicationContext(),BookView.class);
                bookView.putExtra("page", page);
                bookView.putExtra("volume", volume);
                SearchActivity.this.startActivity(bookView);


            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem search_item = menu.findItem(R.id.search_menu);
        SearchView searchView = (SearchView) search_item.getActionView();
        searchView.setFocusable(true);
        searchView.setIconified(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                if(query.length() < 2 ){
                    cursor = null;
                } else {

                    String name_col = "name";
                    if (MDetect.isUnicode())
                        name_col = "name_uni";

                    db = DBOpenHelper.getInstance(context);
                    sqLiteDatabase = db.getReadableDatabase();
                    String select = "SELECT _id, " + name_col + " FROM indexes WHERE " + name_col + " LIKE '%" + query + "%'";
                    cursor = sqLiteDatabase.rawQuery(select, null);

                    if(cursor.getCount() < 1 ) {

                        String empty = "႐ွာလိုေသာ စကားလုံး မေတြ႕ပါ";
                        if (MDetect.isUnicode())
                            empty = Rabbit.zg2uni(empty);
                        tv.setText(empty);
                    } else {
                        tv.setText("");
                    }
                }

                adapter.changeCursor(cursor);

                return false;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);

    }
}
