package inventory;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Bean to handle request and response of inventory queries.
 *
 * @author Tomas
 */
@Stateless(mappedName="ejb/InventoryManager")
@Path("/inventory") // url path for StocksResource to handle
public class InventoryManagerBean implements InventoryManager
{

    @EJB
    private DatabaseBean dbManager;

    @GET
    @Produces("text/plain")
    @Override
    public String getInventory()
    {
        String inventory = "";
        if (dbManager == null)
        {
            return "There are no items.";
        }
        for (String itemName : dbManager.getItemNames())
        {
            inventory += itemName + " has value "
                    + dbManager.getItemValue(itemName) + ".\n";
        }
        return inventory;
    }

    @GET
    @Path("{name}")
    @Produces("text/plain")
    @Override
    public String getItem(@PathParam("name") String itemName)
    {
//        if (dbManager.itemExists(itemName))
//        {
//            return itemName + " has value "
//                    + dbManager.getItemValue(itemName) + ".";
//        } else
//        {
            return "No such item.";
//        }
    }

    @POST
    @Path("{name}/{value}")
    @Produces("text/plain")
    @Override
    public String putItem(@PathParam("name") String itemName,
            @PathParam("value") int itemValue)
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
            return itemName + " added with value " + itemValue + ".";
//        }
    }
}
