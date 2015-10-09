package lavrikandcompany.food2fork;

import java.util.ArrayList;

//клас що відповідає за один конкретний реценпт що буде в окремому актівіті
public class OneRecipe  {

    private String publisherOne;
    private String f2f_urlOne;
    private String source_urlOne;
    private String recipe_idOne;
    private String image_urlOne;
    private int social_rankOne;
    private String publisher_urlOne;
    private String titleOne;
    private ArrayList<String> ingredientsArrayList;

    public void setIngredientsArrayList(ArrayList<String> ingredientsArrayList) {
        this.ingredientsArrayList = ingredientsArrayList;
    }

    public ArrayList<String> getIngredientsArrayList() {
        return ingredientsArrayList;
    }

    public String getPublisherOne() {
        return publisherOne;
    }

    public void setPublisherOne(String publisherOne) {
        this.publisherOne = publisherOne;
    }

    public String getF2f_urlOne() {
        return f2f_urlOne;
    }

    public void setF2f_urlOne(String f2f_urlOne) {
        this.f2f_urlOne = f2f_urlOne;
    }

    public String getSource_urlOne() {
        return source_urlOne;
    }

    public void setSource_urlOne(String source_urlOne) {
        this.source_urlOne = source_urlOne;
    }

    public String getRecipe_idOne() {
        return recipe_idOne;
    }

    public void setRecipe_idOne(String recipe_idOne) {
        this.recipe_idOne = recipe_idOne;
    }

    public String getImage_urlOne() {
        return image_urlOne;
    }

    public void setImage_urlOne(String image_urlOne) {
        this.image_urlOne = image_urlOne;
    }

    public int getSocial_rankOne() {
        return social_rankOne;
    }

    public void setSocial_rankOne(int social_rankOne) {
        this.social_rankOne = social_rankOne;
    }

    public String getPublisher_urlOne() {
        return publisher_urlOne;
    }

    public void setPublisher_urlOne(String publisher_urlOne) {
        this.publisher_urlOne = publisher_urlOne;
    }

    public String getTitleOne() {
        return titleOne;
    }

    public void setTitleOne(String titleOne) {
        this.titleOne = titleOne;
    }
}
