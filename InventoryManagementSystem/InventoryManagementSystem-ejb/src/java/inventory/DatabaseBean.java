package inventory;

import java.util.Collection;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;


/**
 * Class to manage SQL queries.
 * Could be singleton.
 * 
 * @author Tomas
 */
@Stateless
@LocalBean
public class DatabaseBean
{
    
    public boolean itemExists(String stockName)
   {  
       //query database with contains
       return false;
   }

   public int getItemValue(String stockName)
   {  
       //query database for item price? or quantity?
       return -1;
   }

   public void addItem(String stockName, int stockValue)
   {  
       //add to database
   }

   public Collection<String> getItemNames()
   {  
       //query db for all items in inventory
       return null;
   }
}
