package mm.pndaza.piktaguide.uiactivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import mm.pndaza.piktaguide.adapter.TagContentAdapter;
import mm.pndaza.piktaguide.database.DBOpenHelper;
import mm.pndaza.piktaguide.R;
import mm.pndaza.piktaguide.utils.MDetect;


public class TagContent extends AppCompatActivity {

    private Context context;
    private DBOpenHelper db = null;
    private SQLiteDatabase sqLiteDatabase = null;
    private Cursor cursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_tag);

        context = this;
        MDetect.init(this);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String title = intent.getStringExtra("title");

        setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        db = DBOpenHelper.getInstance(this);
        sqLiteDatabase = db.getReadableDatabase();

        String name_col = "name";
        if(MDetect.isUnicode())
            name_col = "name_uni";

        if ( id >= 1 && id <= 3) {

            Intent bookView = new Intent(TagContent.this.getApplicationContext(),BookView.class);
            bookView.putExtra("page", 0);
            bookView.putExtra("volume", id);
            TagContent.this.startActivity(bookView);

        }
        else if ( id >= 4 && id <= 10){
            id = id - 3;
            Log.i("id :", String.valueOf(id));
            String query = "select start,end from ranges where _id = " + String.valueOf(id);
            cursor = sqLiteDatabase.rawQuery(query, null);
            cursor.moveToFirst();
            int start = cursor.getInt(0);
            int end = cursor.getInt(1);

            String select_query = "select indexes._id, " + name_col + " from indexes where _id >= " + String.valueOf(start) + " AND _id <= " + String.valueOf(end);
            cursor = sqLiteDatabase.rawQuery(select_query, null);
        }
        else if ( id >= 10 && id <= 23) {
            id = id - 10;
            String select_query = "select indexes._id, " + name_col + " from indexes join index_tag on index_tag.index_id = indexes._id where index_tag.tag_id = " + String.valueOf(id);
            cursor = sqLiteDatabase.rawQuery(select_query, null);
        }




        TagContentAdapter tagContentAdapter = new TagContentAdapter(context,cursor);

        ListView listView = findViewById(R.id.tag_listview);
        listView.setFastScrollEnabled(true);
        listView.setAdapter(tagContentAdapter);

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

                Intent bookView = new Intent(TagContent.this.getApplicationContext(),BookView.class);
                bookView.putExtra("page", page);
                bookView.putExtra("volume", volume);
                TagContent.this.startActivity(bookView);


            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
