
package inventory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 *
 * @author Tomas
 */
@Stateless
@Path("/manager")
public class InventoryManagerBeanRESTfulJSON
{

    @EJB private DatabaseBean dbManager;
    @DefaultValue("") @QueryParam("json") String jsonString;
    
    
    /**
     * Returns a list of names of all items in the 
     * database.
     * @return 
     */
    @GET
    @Produces("text/plain")
    public String[] getInventory()
    {
        return dbManager.getItemNames();
    }


    /**
     * Get the name, price and quantity of an item provided
     * as a <code>name</code> query parameter. <br>
     * If no name parameter is provided, a list of the entire
     * database is returned.
     * @return 
     */
//    @GET
//    @Path("/get")
//    @Produces("text/plain")
//    public String getItem()
//    {
//        if (!itemName.isEmpty())
//        {
//                InventoryItem item = new InventoryItem(dbManager.getItem(itemName));
//            if (dbManager.itemExists(itemName))
//            {
//                String[] item = dbManager.getItem(itemName);
//                String cost = '$' + String.format("%6.2f", Integer.parseInt(item[1]) / 100.0);
//                return item[0] + " | Price: " + cost + " | Quantity: " + item[2] + ".";
//            } else
//            {
//                return "Item '" + itemName + "' does not exist.";
//            }
//        } else 
//        {
//            return getInventoryTable();
//        }
//    }
    
    /**
     * Returns a formatted list of all items in the
     * database, including their price and quantity.
     * @return 
     */
    private String getInventoryTable()
    {
//        String inventory = "";
//        for (String name : dbManager.getItemNames())
//        {
//            String[] item = dbManager.getItem(name);
//            String cost = '$' + String.format("%6.2f", Integer.parseInt(item[1]) / 100.0);
//            inventory += item[0] + " | Price: " + cost + " | Quantity: " + item[2] + ".\n";
////                InventoryItem item = new InventoryItem(dbManager.getItem(itemName));
//        }
//        return inventory;
        return dbManager.getInventory().toString();
    }
    
    @GET
    @Path("/get/price")
    @Produces("text/plain")
    public double getItemPrice()
    {
        if (!itemName.isEmpty() && dbManager.itemExists(itemName))
        {
            return dbManager.getItemPrice(itemName) / 100.0;
        } else
        {
            return -1;
        }
    }

//    public String getItemPriceText(String itemName)
//    {
//        if (dbManager.itemExists(itemName))
//        {
//            int price = dbManager.getItemPrice(itemName);
//            return '$' + String.format("%6.2f", price / 100.0);
//        } else
//        {
//            return "Item '" + itemName + "' does not exist.";
//        }
//    }
    
    @GET
    @Path("/get/quantity")
    @Produces("text/plain")
    public int getItemQuantity()
    {
        if (!itemName.isEmpty() && dbManager.itemExists(itemName))
        {
            return dbManager.getItemQuantity(itemName);
        } else
        {
            return -1;
        }
    }

    /**
     * Adds a new item to the database.  Providing price and
     * quantity parameters are optional, and will be set to 0
     * if they do not exist.
     * @return
     */
    @POST
    @Path("/create")
    @Produces("text/plain")
    public String addItem()
    {        
        if (!itemName.isEmpty())
        {
            if (!dbManager.itemExists(itemName))
            {
                int cost = (int) (price * 100);
                dbManager.addItem(itemName, cost, quantity);
                String priceResult = '$' + String.format("%6.2f", cost / 100.0);
                return "Item " + itemName + " (" + priceResult + ") created successfully.";
            } else
            {
                return updateItem();
            }
        } else
        {
            return "Please provide an item name as a parameter.";
        }
    }

    @POST
    @Path("/update")
    @Produces("text/plain")
    private String updateItem()
    {
        int cost = (int) (price * 100);
        return dbManager.updateItem(itemName, cost, quantity);
    }
    
    @POST
    @Path("/add")
    @Produces("text/plain")
    public String addToItem()
    {
        int priceToAdd = (price >= 0) ? (int) (price * 100) : 0;   
        int quantityToAdd = (quantity >= 0) ? quantity : 0;
        return dbManager.addToItem(itemName, priceToAdd, quantityToAdd);     
    }
    
    
    @POST
    @Path("/remove")
    @Produces("text/plain")
    public String removeFromItem()
    {
        int priceToAdd = (price >= 0) ? (int) (price * 100) : 0;   
        int quantityToAdd = (quantity >= 0) ? quantity : 0;
        return dbManager.addToItem(itemName, -priceToAdd, -quantityToAdd);     
    }
    
    public void increaseStockQuantity(String itemName, int numberToAdd)
    {
        if (dbManager.itemExists(itemName))
        {
            dbManager.increaseQuantity(itemName, numberToAdd);
        }
    }

    public void decreaseStockQuantity(String itemName, int numberToRemove)
    {
        if (dbManager.itemExists(itemName))
        {
            dbManager.decreaseQuantity(itemName, numberToRemove);
        }
    }

    public void updateStockQuantity(String itemName, int newQuantity)
    {
        if (dbManager.itemExists(itemName))
        {
            dbManager.updateQuantity(itemName, newQuantity);
        }
    }

    public void removeItem(String itemName)
    {
        if (dbManager.itemExists(itemName))
        {
            dbManager.removeItem(itemName);
        }
    }

    public void updatePrice(String itemName, double newPrice)
    {
        if (dbManager.itemExists(itemName))
        {
            dbManager.updatePrice(itemName, (int) (newPrice * 100));
        }
    }
    
}