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
@WebService
@Stateless
public class InventoryWebService
{
    public InventoryWebService()
    {
        
    }
    
    @EJB(mappedName="ejb/InventoryAnalyzer")
    private InventoryAnalyzer analyzer;
    @EJB(mappedName="ejb/InventoryManager")
    private InventoryManager manager;

    @WebMethod
    public String[] getInventory()
    {
        return manager.getInventory();
    }

    @WebMethod
    public String getInventoryTable()
    {
        return manager.getInventoryTable();
    }
    
    @WebMethod
    public String getItem(@WebParam(name = "name") String itemName)
    {
        return manager.getItem(itemName);
    }

    @WebMethod
    public String addItem(@WebParam(name="itemName") String itemName,
            @WebParam(name = "quantity") double itemValue)
    {
        return manager.addItem(itemName, itemValue, 0);
    }
    
    @WebMethod
    public String addItemWithQuantity(@WebParam(name = "itemName") String itemName,
            @WebParam(name="value") double itemValue,
            @WebParam(name="quantity") int quantity)
    {
        return manager.addItem(itemName, itemValue, quantity);
    }
    
    @WebMethod
    public String getItemPriceText(@WebParam(name = "itemName") String itemName)
    {
        return manager.getItemPriceText(itemName);
    }
    
    @WebMethod
    public double getItemPrice(@WebParam(name = "itemName") String itemName)
    {
        return manager.getItemPrice(itemName);
    }
    
    @WebMethod
    public int getItemQuantity(@WebParam(name = "itemName") String itemName)
    {
        return manager.getItemQuantity(itemName);
    }
    
    @WebMethod
    public void increaseStockQuantity(@WebParam(name = "itemName") String itemName,
            @WebParam(name="quantity") int quantityToAdd)
    {
        manager.increaseStockQuantity(itemName, quantityToAdd);
    }
    
    @WebMethod
    public void decreaseStockQuantity(@WebParam(name = "itemName") String itemName,
            @WebParam(name="quantity") int quantityToRemove)
    {
        manager.decreaseStockQuantity(itemName, quantityToRemove);
    }
    
    @WebMethod
    public void updateStockQuantity(@WebParam(name = "itemName") String itemName,
            @WebParam(name="quantity") int newQuantity)
    {
        manager.increaseStockQuantity(itemName, newQuantity);
    }
    
    @WebMethod
    public void removeItem(@WebParam(name = "itemName") String itemName)
    {
        manager.removeItem(itemName);
    }
    
    @WebMethod
    public void updatePrice(@WebParam(name = "itemName") String itemName,
            @WebParam(name="price") double newPrice)
    {
        manager.updatePrice(itemName, newPrice);
    }
}
