package mm.pndaza.piktaguide.uiactivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import mm.pndaza.piktaguide.adapter.TagAdapter;
import mm.pndaza.piktaguide.database.DBOpenHelper;
import mm.pndaza.piktaguide.R;
import mm.pndaza.piktaguide.utils.MDetect;
import mm.pndaza.piktaguide.utils.Rabbit;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private DBOpenHelper db = null;
    private SQLiteDatabase sqLiteDatabase = null;
    private Cursor cursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        MDetect.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String title = "               ပိဋကလမ္းၫႊန္";
        if(MDetect.isUnicode())
            title = "               ပိဋကလမ်းညွှန်";
        setTitle(title);



        ArrayList<String> list = new ArrayList<String>();
        list.add("ပိဋကလမ္းၫႊန္ အတြဲ (၁)");
        list.add("ပိဋကလမ္းၫႊန္ အတြဲ (၂)");
        list.add("ပိဋကလမ္းၫႊန္ အတြဲ (၃)");

        db = DBOpenHelper.getInstance(this);
        sqLiteDatabase = db.getReadableDatabase();

        cursor = sqLiteDatabase.rawQuery("select name from ranges", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor = sqLiteDatabase.rawQuery("select name from tags", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }


        if(MDetect.isUnicode()){
            for (int i =0; i < 23; i++) {
                String name_uni = Rabbit.zg2uni(list.get(i));
                list.set(i,name_uni);
            }
        }


        TagAdapter tagAdapter = new TagAdapter(this,list);
        ListView listView = findViewById(R.id.main_listview);
        listView.setAdapter(tagAdapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                int id = position + 1;
                String title = (String)adapterView.getItemAtPosition(position);

                if ( id >= 1 && id <= 3) {

                    Intent bookView = new Intent(MainActivity.this.getApplicationContext(),BookView.class);
                    bookView.putExtra("page", 0);
                    bookView.putExtra("volume", id);
                    MainActivity.this.startActivity(bookView);
                } else {

                    Intent result = new Intent(MainActivity.this.getApplicationContext(), TagContent.class);
                    result.putExtra("id", id);
                    result.putExtra("title",title);
                    MainActivity.this.startActivity(result);
                }


            }
        });


        FloatingActionButton f_search = findViewById(R.id.float_search);
        f_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent search = new Intent(MainActivity.this.getApplicationContext(),SearchActivity.class);
                MainActivity.this.startActivity(search);

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_about, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.menu_about:
                //start search dialog
                Intent intent = new Intent(MainActivity.this.getApplicationContext(),About.class);
                MainActivity.this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
