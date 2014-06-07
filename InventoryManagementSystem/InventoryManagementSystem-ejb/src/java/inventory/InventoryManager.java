package inventory;

import javax.ejb.Remote;

/**
 *
 * @author Tomas
 */
@Remote
public interface InventoryManager
{
    public String getInventory();
    public String getItem(String itemName);
    public String putItem(String itemName, int itemValue);
    
}
