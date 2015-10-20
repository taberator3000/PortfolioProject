package lavrikandcompany.food2fork;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class OneRecipeWindow extends Activity {
    //стрічки, текст вю, і стрічки для замін
    private String MAIN_URL = "http://food2fork.com/api/get?key=";
    private String API = "8eb7a2605361f310207a314fb105170f";
    private String BEFORERECIPE="&rId=";
    private String ID;
    private String LEFT_BRAKET = "[";
    private String RIGHT_BRAKET = "]";
    private String EMPTY = "";
    ProgressDialog progressDialog;
    TextView recipeNameText;
    TextView ingridient_list;
    TextView publisher_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_recipe_layout);
        //Отут ми фактично і робимо те що ловимо з актівіті першої конкретний номер рецепта(ID) до конкретного реценту зі списку, і з цього номера в подальшому вигружаємо все нам потрібне
        Bundle data = getIntent().getExtras();
        Recipe recipe =(Recipe)data.getParcelable("list");
        ID = recipe.getRecipe_id();


        ingridient_list=(TextView)findViewById(R.id.ingridient_list);
        recipeNameText = (TextView)findViewById(R.id.recipeNameText);
        publisher_info =(TextView)findViewById(R.id.publisher_info);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");

        //загузка весія 2.0)
        new JSONTreadOneRecipe().execute(MAIN_URL + API + BEFORERECIPE + ID);

    }

    class JSONTreadOneRecipe extends AsyncTask<String, String, OneRecipe> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();

        }

        @Override
        protected OneRecipe doInBackground(String... params) {
            HttpURLConnection connection=null;
            BufferedReader reader = null;


            //грузимо дані
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader=new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer= new StringBuffer();

                String line ="";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                //вlаим дані в ясон парсер і розбираємо
                String finalJson = buffer.toString();

                JSONObject parrentObject = new JSONObject(finalJson);
                JSONObject sonObject = parrentObject.getJSONObject("recipe");
                OneRecipe oneRecipe = new OneRecipe();
                oneRecipe.setTitleOne(sonObject.getString("title"));
                oneRecipe.setPublisherOne(sonObject.getString("publisher"));
                oneRecipe.setImage_urlOne(sonObject.getString("image_url"));
                //oneRecipe.setSocial_rankOne(sonObject.getInt("social_rankOne"));
                JSONArray receptArr = sonObject.getJSONArray("ingredients");

                //вантажимо наші інградієнти
                ArrayList<String> ingredientsArrayList = new ArrayList<String>();
                for (int i=0; i < receptArr.length(); i++){
                    ingredientsArrayList.add(receptArr.get(i).toString());
                }
                oneRecipe.setIngredientsArrayList(ingredientsArrayList);

                return oneRecipe;
            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            }finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(OneRecipe oneRecipe) {
            super.onPostExecute(oneRecipe);
            //вертаємо сюди готовий 1 оєкт з нашою інфою для рецепта і його картинки і тут відповідно все розкладаємо
            ingridient_list.setText("Ingredients:" + oneRecipe.getIngredientsArrayList().toString().replace(LEFT_BRAKET, EMPTY).replace(RIGHT_BRAKET, EMPTY));
            recipeNameText.setText(oneRecipe.getTitleOne());
            publisher_info.setText("Publisher:" + oneRecipe.getPublisherOne());

            final ProgressBar progressBar=(ProgressBar)findViewById(R.id.progress_one_bar);

            ImageLoader.getInstance().displayImage(oneRecipe.getImage_urlOne(), (ImageView) findViewById(R.id.icon_one), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    progressBar.setVisibility(View.GONE);
                }
            });
            progressDialog.dismiss();
        }
    }

}
