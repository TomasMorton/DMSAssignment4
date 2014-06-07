package inventoryclient;

import inventory.InventoryManager;
import javax.ejb.EJB;

/**
 *
 * @author Tomas
 */
public class Client
{

    @EJB(mappedName="ejb/InventoryManager")
    private static InventoryManager manager;

    public Client()
    {
        
    }
    
    public String getInventory()
    {
        return manager.getInventory();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
       Client client = new Client();
       client.getInventory();
    }
}
