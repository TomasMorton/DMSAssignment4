/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author Tomas
 */
@Stateless
@Path("/inventory")
public class InventoryManagerBeanRestful implements InventoryManager
{

    @EJB
    private DatabaseBean dbManager;

    @GET
    @Produces("text/plain")
    @Override
    public String[] getInventory()
    {
        return dbManager.getItemNames();
    }

    @GET
    @Produces("text/plain")
    @Override
    public String getInventoryTable()
    {
        String inventory = "";
        for (String itemName : dbManager.getItemNames())
        {
            int cents = dbManager.getItemPrice(itemName);
            String dollars = '$' + String.format("%6.2f", cents / 100.0);//%(,.2f
            inventory += itemName + " costs " + dollars + ".\n";
        }
        return inventory;
    }

    @GET
    @Path("get/{name}")
    @Produces("text/plain")
    @Override
    public String getItem(@PathParam("name") String itemName)
    {
        if (dbManager.itemExists(itemName))
        {
            String[] item = dbManager.getItem(itemName);
            String price = '$' + String.format("%6.2f", Integer.parseInt(item[1]) / 100.0);
            return item[0] + " | Price: " + price + " | Quantity: " + item[2] + ".";
        } else
        {
            return "Item '" + itemName + "' does not exist.";
        }
    }

    @POST
    @Path("add/{name}/{value}")
    @Produces("text/plain")
    @Override
//    public String putItem(
    /**
     * Adds a new item to the database. Input the price as 13.99, it will then
     * be converted to 1399 and stored.
     */
    public String addItem(@PathParam("name") String itemName,
            @PathParam("value") double itemValue,
            @PathParam("quantity") int quantity)
    {
//        if (dbManager.itemExists(itemName))
//        {
//            int oldValue = dbManager.getItemValue(itemName);
//            dbManager.addItem(itemName, itemValue);
//            return itemName + " value updated from " + oldValue + " to "
//                    + itemValue + ".";
//        } else
//        {
//            dbManager.addItem(itemName, itemValue);
//            return itemName + " added with value " + itemValue + ".";
//        }
        if (!dbManager.itemExists(itemName))
        {
            int price = (int) (itemValue * 100);
            dbManager.addItem(itemName, price);
            String priceResult = '$' + String.format("%6.2f", price / 100.0);
            return "Item " + itemName + " (" + priceResult + ") created successfully.";
        } else
        {
            return "Item already exists.  Please use the update field to change the price.";
        }
    }

    public String addItem(@PathParam("name") String itemName,
            @PathParam("value") double itemValue)
    {
        return addItem(itemName, itemValue, 0);
    }

    @Override
    public double getItemPrice(String itemName)
    {
        if (dbManager.itemExists(itemName))
        {
            return dbManager.getItemPrice(itemName) / 100.0;
        } else
        {
            return -1;
        }
    }

    @Override
    public String getItemPriceText(String itemName)
    {
        if (dbManager.itemExists(itemName))
        {
            int price = dbManager.getItemPrice(itemName);
            return '$' + String.format("%6.2f", price / 100.0);
        } else
        {
            return "Item '" + itemName + "' does not exist.";
        }
    }

    @Override
    public int getItemQuantity(String itemName)
    {
        if (dbManager.itemExists(itemName))
        {
            return dbManager.getItemQuantity(itemName);
        } else
        {
            return -1;
        }
    }

    @Override
    public void increaseStockQuantity(String itemName, int numberToAdd)
    {
        if (dbManager.itemExists(itemName))
        {
            dbManager.increaseQuantity(itemName, numberToAdd);
        }
    }

    @Override
    public void decreaseStockQuantity(String itemName, int numberToRemove)
    {
        if (dbManager.itemExists(itemName))
        {
            dbManager.decreaseQuantity(itemName, numberToRemove);
        }
    }

    @Override
    public void updateStockQuantity(String itemName, int newQuantity)
    {
        if (dbManager.itemExists(itemName))
        {
            dbManager.updateQuantity(itemName, newQuantity);
        }
    }

    @Override
    public void removeItem(String itemName)
    {
        if (dbManager.itemExists(itemName))
        {
            dbManager.removeItem(itemName);
        }
    }

    @Override
    public void updatePrice(String itemName, double newPrice)
    {
        if (dbManager.itemExists(itemName))
        {
            dbManager.updatePrice(itemName, (int) (newPrice * 100));
        }
    }
}
