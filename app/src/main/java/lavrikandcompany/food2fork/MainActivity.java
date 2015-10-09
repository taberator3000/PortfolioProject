package lavrikandcompany.food2fork;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
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
import java.util.List;


public class MainActivity extends ActionBarActivity {
    //змінні стрічки і тд
     private static final String TAG ="TRACE" ;
    RecipeAdapter adapter;
    private ListView lvRecipes;
    ProgressDialog progressDialog;
    EditText editTextUrl;
    private String MAIN_URL = "http://food2fork.com/api/search?key=";
    private String COMA = ",";
    private String PROC_SYM = "%2C";
    private String API = "8eb7a2605361f310207a314fb105170f";
    private String PAGE="&page=";
    private String BEFORERECIPES="&q=";
    ArrayList<Recipe>recipeListReady;
    //hmmm
//lol
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        //пошукове урл
        editTextUrl= (EditText)findViewById(R.id.url_searsh);

        // наш список
        lvRecipes = (ListView)findViewById(R.id.lvRecipes);

        //показую процез загрузки рецепта
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Recipes");
        //встроєний загрузчик зображень(так я лінивий-_-)
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);

        //стартуємо пробний потік, щоб каст бачив що тут якісь то рецепти а є, а не закрив програму і побіг додому
        new JSONThread().execute("http://food2fork.com/api/search?key=8eb7a2605361f310207a314fb105170f&q=sugar");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    int i=1;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh_id) {
            //заміняєм в стрічках кому на наш символ для пошуку
            String replaceString = editTextUrl.getText().toString();
            String replacedString =  replaceString.replaceAll(COMA, PROC_SYM);

            //стартуєм по новій коли у нас уже забито нова урл, з норм хавкою, і жмемо старт
            new JSONThread().execute(MAIN_URL+API+PAGE+i+BEFORERECIPES+replacedString);
            i++;//щоб кожен раз нові рецепти ішли типу:page+1
            return true;
        }
        if(id == R.id.info_app){
            //інфа
            Toast.makeText(getApplicationContext(),"Well done!",Toast.LENGTH_LONG).show();
            Intent intentInfo = new Intent(getApplicationContext(),InfoActivity.class);
            startActivity(intentInfo);
        }
        return super.onOptionsItemSelected(item);
    }

        //наш потік
    class JSONThread extends AsyncTask<String,String,List<Recipe> >{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();//показуємо діалог типу закачка пішла
        }

        @Override
        protected List<Recipe> doInBackground(String... params) {
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
               //     Log.v(TAG, "inputline  " + buffer);
                     }

                //валим дані в ясон парсер і розбираємо
                String finalJson = buffer.toString();
                JSONObject parrentObject = new JSONObject(finalJson);
                JSONArray parentArray = parrentObject.getJSONArray("recipes");
                //Список наших рецептів
                    List<Recipe> recipeList= new ArrayList<>();
                    //ітеруємо
                     for (int i=0;i<parentArray.length();i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                         Recipe recipe = new Recipe();
                    recipe.setPublisher(finalObject.getString("publisher"));
                    recipe.setF2f_url(finalObject.getString("f2f_url"));
                    recipe.setSocial_rank(finalObject.getInt("social_rank"));
                    recipe.setImage_url(finalObject.getString("image_url"));
                    recipe.setTitle(finalObject.getString("title"));
                    recipe.setPublisher_url(finalObject.getString("publisher_url"));
                    recipe.setRecipe_id(finalObject.getString("recipe_id"));
                    recipe.setSource_url(finalObject.getString("source_url"));
                    recipeList.add(recipe);//коли прогнали все добавляємо 1 рецептик
                }
                return recipeList;//повертаємо весь список
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
        protected void onPostExecute(List<Recipe> result) {
            super.onPostExecute(result);
            progressDialog.dismiss();//зупиняємо наш діалог
            //адаптер для списку
            RecipeAdapter adapter= new RecipeAdapter(getApplicationContext(),R.layout.row,result);
            lvRecipes.setAdapter(adapter);//пускаємо його
            lvRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {//заставляємо його відчути =3
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(),OneRecipeWindow.class);
                    intent.putExtra("list", (Parcelable) lvRecipes.getAdapter().getItem(position));//сюда ми ліпимо фактично наш список з параментрами які можна проюзати в наступній актівіті
                    startActivity(intent);
                }
            });

        }


        }

            //сам адаптер
    public class RecipeAdapter extends ArrayAdapter {
        private List<Recipe> recipeList;
        private int resource;
        private LayoutInflater inflater;
        public RecipeAdapter(Context context, int resource, List<Recipe> objects) {
            super(context, resource, objects);
            recipeList=objects;
            this.resource=resource;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
         @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            VievHolder vievHolder=null;
            if(convertView==null) {
                vievHolder= new VievHolder();
                convertView = inflater.inflate(resource, null);
                vievHolder.image_icon = (ImageView) convertView.findViewById(R.id.image_icon);
                vievHolder.recipe_name= (TextView)convertView.findViewById(R.id.recipe_name);
                vievHolder.recipe_rate = (TextView)convertView.findViewById(R.id.recipe_rate);
                convertView.setTag(vievHolder);
            }else{
                vievHolder = (VievHolder)convertView.getTag();
            }
            final ProgressBar progressBar=(ProgressBar)convertView.findViewById(R.id.progressBar);//прогресбарка для того щоб бачити як підгружається картинка, клієн нескучає нам добре
            //вантажимо картинку
             ImageLoader.getInstance().displayImage(recipeList.get(position).getImage_url(), vievHolder.image_icon, new ImageLoadingListener() {
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

            vievHolder.recipe_name.setText(recipeList.get(position).getTitle());
            vievHolder.recipe_rate.setText("Rating:  " + recipeList.get(position).getSocial_rank());
            return convertView;
        }
        class VievHolder{
          private  ImageView image_icon;
          private  TextView recipe_name;
          private TextView recipe_rate;
}
    }
}





