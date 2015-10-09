package lavrikandcompany.food2fork;


import android.os.Parcel;
import android.os.Parcelable;
//lol
//клас що робить нам рецепт для ліст вю, ще він має змогу давати парсебл щоб деяку інфу таскати між актівіті
public class Recipe implements Parcelable {

    private String image_url;
    private String source_url;
    private String f2f_url;
    private String title;
    private String publisher;
    private String publisher_url;
    private int social_rank;
    private String recipe_id;

    public Recipe() {
        this.image_url = image_url;
        this.source_url = source_url;
        this.f2f_url = f2f_url;
        this.title = title;
        this.publisher = publisher;
        this.publisher_url = publisher_url;
        this.social_rank = social_rank;
        this.recipe_id = recipe_id;
    }
    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public String getF2f_url() {
        return f2f_url;
    }

    public void setF2f_url(String f2f_url) {
        this.f2f_url = f2f_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublisher_url() {
        return publisher_url;
    }

    public void setPublisher_url(String publisher_url) {
        this.publisher_url = publisher_url;
    }

    public int getSocial_rank() {
        return social_rank;
    }

    public void setSocial_rank(int social_rank) {
        this.social_rank = social_rank;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeString(recipe_id);

    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        // распаковываем объект из Parcel
        public Recipe createFromParcel(Parcel in) {
                return new Recipe(in);
        }

    public Recipe[] newArray(int size) {
        return new Recipe[size];
    }
};
    //тут цікаво. але див. дальше
    private Recipe(Parcel parcel) {

        recipe_id = parcel.readString();

    }

}
