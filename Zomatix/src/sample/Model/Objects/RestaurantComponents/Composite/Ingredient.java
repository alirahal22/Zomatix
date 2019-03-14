package sample.Model.Objects.RestaurantComponents.Composite;

public class Ingredient extends RestaurantLeaf {
    private String image_path;

    private boolean isRemoved = false;

    public Ingredient(String name, String image_path) {
        this.name = name;
        this.image_path = image_path;
    }

    public Ingredient(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }
}
