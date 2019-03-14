package sample.Model.Objects.RestaurantComponents.Composite;

import java.util.ArrayList;
import java.util.HashMap;

public class Menu extends RestaurantComponent {

    private HashMap<String, ArrayList<MenuItem>> menuSectionsHashMap;

    public Menu(int menuid, String name, int restaurantid, ArrayList<RestaurantLeaf> itemArrayList) {
        this.id = menuid;
        this.name = name;
        this.parentid = restaurantid;
        this.children = itemArrayList;

        menuSectionsHashMap = new HashMap<>();
        divideMenu();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void divideMenu() {
        for (RestaurantLeaf leaf : children) {
            MenuItem menuItem = (MenuItem) leaf;
            if (!menuSectionsHashMap.containsKey(menuItem.getCategoryName()))
                menuSectionsHashMap.put(menuItem.getCategoryName(), new ArrayList<>());
            menuSectionsHashMap.get(menuItem.getCategoryName()).add(menuItem);
            menuSectionsHashMap.get(menuItem.getCategoryName());
        }
    }

    public HashMap<String, ArrayList<MenuItem>> getMenuSectionsHashMap() {
        return menuSectionsHashMap;
    }

    @Override
    public void add(RestaurantLeaf restaurantLeaf) {
        super.add(restaurantLeaf);
        restaurantLeaf.setParentid(this.id);
        databaseModel.addMenuItem((MenuItem) restaurantLeaf, parentid);
    }

    @Override
    public void delete(RestaurantLeaf restaurantLeaf) {
        super.delete(restaurantLeaf);
        databaseModel.deleteItem(restaurantLeaf.getId());
    }
}
