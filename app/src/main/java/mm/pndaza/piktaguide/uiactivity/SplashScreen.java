package mm.pndaza.piktaguide.uiactivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import mm.pndaza.piktaguide.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        File file = new File(getFilesDir()+ "/databases/pitakaguide.db");

        if (file.exists() == false) {
            new CopyDB().execute(new File[]{file});
        }
         else {
            startMainActivity();
        }

    }


    private void startMainActivity(){

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                SplashScreen.this.startActivity(intent);
                SplashScreen.this.finish();
            }
        },1000);

    }


    public class CopyDB extends AsyncTask<File, Double, Void> {


        protected Void doInBackground(File... files) {

            File file = files[0];

            // check database folder is exist and if not, make folder.
            if (file.getParentFile().exists() == false){
                file.getParentFile().mkdirs();
            }

            try {
                InputStream input = SplashScreen.this.getAssets().open("databases/pitakaguide.db");
                OutputStream output = new FileOutputStream(file);

                int bufferSize;
                final int size = input.available();
                long alreadyCopy = 0;

                byte[] buffer = new byte[1024];
                while ((bufferSize = input.read(buffer) ) > 0) {
                    alreadyCopy += bufferSize;
                    output.write(buffer);
                    publishProgress(1.0d * alreadyCopy / size );
                }
                input.close();
                output.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }

            return null;
        }



        @Override
        protected void onPostExecute(Void result) {

            startMainActivity();
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }
    }

}
