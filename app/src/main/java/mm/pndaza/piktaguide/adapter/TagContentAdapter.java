package mm.pndaza.piktaguide.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mm.pndaza.piktaguide.R;
import mm.pndaza.piktaguide.utils.MDetect;

public class TagContentAdapter extends CursorAdapter {
    private static Boolean isUnicode = null;

    public TagContentAdapter(Context context, Cursor c){
        super(context, c);
        MDetect.init(context);
       // isUnicode = MDetect.isUnicode();
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.content_list_item, viewGroup, false);
    }


    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String name_col = "name";
        if(MDetect.isUnicode())
            name_col = "name_uni";
        String name = cursor.getString(cursor.getColumnIndexOrThrow(name_col));
        TextView textView_word = view.findViewById(R.id.tv_name_content);
        textView_word.setText(name);
        textView_word.setTextColor(Color.BLACK);

        RelativeLayout rl = view.findViewById(R.id.background);

        // set background color
        int position = cursor.getPosition();
        if (position % 2 == 0) {
            rl.setBackgroundColor(Color.argb( 90,225, 245, 254));
        } else {
            rl.setBackgroundColor(Color.argb(90, 179, 229, 252));
        }
    }
}
