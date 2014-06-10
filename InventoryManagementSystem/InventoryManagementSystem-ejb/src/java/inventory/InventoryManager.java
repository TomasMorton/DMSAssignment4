package inventory;

import javax.ejb.Local;

/**
 *
 * @author Tomas
 */
@Local
public interface InventoryManager
{
    //Queries
    public String[] getInventory();
    public String getInventoryTable();
    public String getItem(String itemName);
    public double getItemPrice(String itemName);
    public String getItemPriceText(String itemName);
    public int getItemQuantity(String itemName);
    //Updates
    public String addItem(String itemName, double itemValue, int quantity);
    public void increaseStockQuantity(String itemName, int numberToAdd);
    public void decreaseStockQuantity(String itemName, int numberToAdd);
    public void updateStockQuantity(String itemName, int newQuantity);
    public void removeItem(String itemName);
    public void updatePrice(String itemName, double newPrice);
    
}
