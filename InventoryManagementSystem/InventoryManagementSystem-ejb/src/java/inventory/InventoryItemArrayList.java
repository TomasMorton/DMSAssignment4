/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package inventory;

import com.google.gson.Gson;
import java.util.ArrayList;

/**
 *
 * @author markburton
 */
public class InventoryItemArrayList {

    private ArrayList<InventoryItem> itemArrayList;
    private ArrayList<String> itemNameList;

    public InventoryItemArrayList(ArrayList itemArrayList) {
        this.itemArrayList = itemArrayList;
    }

    public InventoryItemArrayList() {
        this.itemArrayList = new ArrayList<>();
    }

    public InventoryItemArrayList(String json) {
        this.equals(new Gson().fromJson(json, InventoryItemArrayList.class));
    }

    public String convertItemArrayToJSON() {
        return new Gson().toJson(this);
    }

    public void createItemNameList() {
        itemNameList = new ArrayList<>();
        itemArrayList.stream().forEach((item) -> {
            itemNameList.add(item.getName());
        });
    }

    public InventoryItem getItem(String name) {
        for (InventoryItem item : itemArrayList) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public double getItemPrice(String name) {
        InventoryItem item = getItem(name);
        if (item != null) {
            return item.getPrice();
        }
        return -1;
    }

    public double getItemQuantity(String name) {
        InventoryItem item = getItem(name);
        if (item != null) {
            return item.getQuantity();
        }
        return -1;
    }

    public void addItem(InventoryItem item) {
        itemArrayList.add(item);
    }

    public void removeItem(InventoryItem item) {
        itemArrayList.remove(item);
    }

}
