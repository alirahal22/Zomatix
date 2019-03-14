package sample.Controller.Customer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import sample.Model.Objects.RestaurantComponents.ReviewRating;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerReviewNodeController implements Initializable {

    @FXML
    public ImageView customerReviewImageView;

    @FXML
    public Label customerReviewNameLabel;
    @FXML public Label customerReviewRatingLabel, customerReviewLabel, customerReviewDateLabel;

    @FXML
    public HBox customerReviewRatingHBox;

    ReviewRating reviewRating;

    public void setReviewRating(ReviewRating reviewRating) {
        this.reviewRating = reviewRating;

        customerReviewImageView.setImage(new Image("sample/Resources/avatars/1.jpg"));
        customerReviewNameLabel.setText("King Raha");
        customerReviewRatingLabel.setText("" + reviewRating.getRating());
        customerReviewLabel.setText(reviewRating.getReview());
        customerReviewDateLabel.setText(reviewRating.getDatetime());
        RestaurantsController.displayRating(customerReviewRatingHBox, reviewRating.getRating());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
