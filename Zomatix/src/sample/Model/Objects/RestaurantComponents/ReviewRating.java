package sample.Model.Objects.RestaurantComponents;

public class ReviewRating {
    private int ratingid;
    private int restaurantid;
    private float rating;
    private String review;
    private String datetime;

    public ReviewRating(){
        //empty constructor
    }

    public ReviewRating(int ratingid, int restaurantid, float rating, String review, String datetime) {
        this.ratingid = ratingid;
        this.restaurantid = restaurantid;
        this.rating = rating;
        this.review = review;
        this.datetime = datetime;
    }

    public ReviewRating(int restaurantid, int rating, String review, String datetime) {
        this.restaurantid = restaurantid;
        this.rating = rating;
        this.review = review;
        this.datetime = datetime;
    }

    public ReviewRating(int restaurantid, int rating, String review) {
        this.restaurantid = restaurantid;
        this.rating = rating;
        this.review = review;
    }

    public int getRatingid() {
        return ratingid;
    }

    public void setRatingid(int ratingid) {
        this.ratingid = ratingid;
    }

    public int getRestaurantid() {
        return restaurantid;
    }

    public void setRestaurantid(int restaurantid) {
        this.restaurantid = restaurantid;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
