package inventory;

import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
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
@WebService(serviceName = "InventoryWebService")
@Stateless
public class InventoryWebService
{
    public InventoryWebService()
    {
        
    }
    
    /**
     * This is a sample web service operation
     */
    @WebMethod
    public String sayHello(@WebParam(name = "name") String txt)
    {
        return "Hello " + txt + " !";
    }
    
//    @EJB
//    private DatabaseBean dbManager;

    @WebMethod
    public String getInventory()
    {
//        String inventory = "";
//        if (dbManager == null)
//        {
//            return "There are no items.";
//        }
//        for (String itemName : dbManager.getItemNames())
//        {
//            inventory += itemName + " has value "
//                    + dbManager.getItemValue(itemName) + ".\n";
//        }
        return "got inventory?";//inventory;
    }

    @WebMethod
    public String getItem(@WebParam(name = "name") String itemName)
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

    @WebMethod
    public String putItem(@WebParam(name = "name") String itemName,
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
