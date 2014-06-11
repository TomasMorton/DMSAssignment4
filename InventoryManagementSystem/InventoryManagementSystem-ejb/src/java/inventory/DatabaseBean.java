package inventory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Class to manage SQL queries. Could be singleton.
 *
 * @author Tomas
 */
@Singleton
@LocalBean
public class DatabaseBean
{

    //Database connection settings
    private final String dbDriver;
    private final String dbAddress;
    private final String dbName;
    private final String userName;
    private final String password;
    private Connection conn;
    //Prepared statements
    private PreparedStatement stmtGetItem;
    private PreparedStatement stmtGetItemPrice;
    private PreparedStatement stmtGetItemQuantity;
    private PreparedStatement stmtItemList;
    private PreparedStatement stmtAddItem;
    private PreparedStatement stmtRemoveItem;
    private PreparedStatement stmtUpdateQuantity;
    private PreparedStatement stmtUpdatePrice;
    private PreparedStatement stmtAddHistory;

    public DatabaseBean()
    {
        //declare temporary variables due to exceptions and FINAL variables
        Properties properties = new Properties();
        String tempDbDriver = "";
        String tempDbAddress = "";
        String tempDbName = "";
        String tempUserName = "";
        String tempPassword = "";
        try
        {
            properties.loadFromXML(getClass().getResourceAsStream("SQLManagerConfig.xml"));
            tempDbDriver = properties.get("dbDriver").toString();
            tempDbAddress = properties.get("dbAddress").toString();
            tempDbName = properties.get("dbName").toString();
            tempUserName = properties.get("user").toString();
            tempPassword = properties.get("password").toString();
        } catch (IOException ex)
        {
            System.err.println("Unable to connect to database.");
        }
        this.dbDriver = tempDbDriver;
        this.dbAddress = tempDbAddress;
        this.dbName = tempDbName;
        this.userName = tempUserName;
        this.password = tempPassword;
        initialiseSQLConnection();
    }
    
    private void initialiseSQLConnection()
    {
        try
        {
            Class.forName(dbDriver);
            conn = DriverManager.getConnection(dbAddress + dbName + ";create=true;user=" + 
                    userName + ";password=" + password);
            setupTables();
            //Queries
            stmtGetItem = conn.prepareStatement("SELECT * FROM PRODUCT "
                    + "WHERE Product_Name=?");
            stmtGetItemPrice = conn.prepareStatement("SELECT Product_Price FROM PRODUCT "
                    + "WHERE Product_Name=?");
            stmtGetItemQuantity = conn.prepareStatement("SELECT Product_Quantity FROM PRODUCT "
                    + "WHERE Product_Name=?");
            stmtItemList = conn.prepareStatement("SELECT Product_Name FROM PRODUCT");
            //Modifications
            stmtAddItem = conn.prepareStatement("INSERT INTO PRODUCT (Product_Name, "
                    + "Product_Price, Product_Quantity) VALUES (?, ?, ?)");
            stmtRemoveItem = conn.prepareStatement("DELETE FROM PRODUCT WHERE Product_Name=?");
            stmtUpdateQuantity = conn.prepareStatement("UPDATE PRODUCT SET Product_Quantity=? "
                    + "WHERE Product_Name=?");
            stmtUpdatePrice = conn.prepareStatement("UPDATE PRODUCT SET Product_Price=? "
                    + "WHERE Product_Name=?");
            stmtAddHistory = conn.prepareStatement("INSERT INTO HISTORY (Product_Name, "
                    + "Product_Price, Product_Quantity) SELECT Product_Name, "
                    + "Product_Price, Product_Quantity FROM PRODUCT WHERE Product_Name=?");
        } catch (SQLException | ClassNotFoundException ex)
        {
            System.err.println("Failed to initialize database. " + ex);
        }
    }

    private void setupTables()
    {
        try
        {
            setupProductTable();
            setupHistoryTable();
        } catch (SQLException e)
        {
            System.err.println("Failed to create database tables. " + e);
        }
    }

    private void setupProductTable() throws SQLException
    {
        DatabaseMetaData dbm = conn.getMetaData();
        // check if "employee" table is there
        ResultSet rs = dbm.getTables(null, null, "PRODUCT", null);
        if (!rs.next())
        {
            System.out.println("DatabaseBean: Creating Product table.");
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE TABLE PRODUCT (PRODUCT_NAME VARCHAR(100) NOT NULL, PRODUCT_PRICE DECIMAL(5), "
                    + "PRODUCT_QUANTITY DECIMAL(5) DEFAULT 0  NOT NULL, PRIMARY KEY (PRODUCT_NAME))");
        }
        rs.close();
    }

    private void setupHistoryTable() throws SQLException
    {
        DatabaseMetaData dbm = conn.getMetaData();
        // check if "employee" table is there
        ResultSet rs = dbm.getTables(null, null, "HISTORY", null);
        if (!rs.next())
        {
            System.out.println("DatabaseBean: Creating History table.");
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE TABLE HISTORY (PRODUCT_NAME VARCHAR(100) NOT NULL, PRODUCT_PRICE DECIMAL(5),"
                    + " PRODUCT_QUANTITY DECIMAL(5) DEFAULT 0  NOT NULL, HISTORY_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + " NOT NULL, PRIMARY KEY (HISTORY_TIMESTAMP, PRODUCT_NAME), CONSTRAINT fk_history_product_name "
                    + "FOREIGN KEY (Product_Name) REFERENCES PRODUCT(Product_Name) ON DELETE CASCADE)");
        }
        rs.close();
    }

    public void closeSQLConnection()
    {
        try
        {
            stmtAddHistory.close();
            stmtAddItem.close();
            stmtGetItemPrice.close();
            stmtGetItemQuantity.close();
            stmtItemList.close();
            stmtRemoveItem.close();
            stmtUpdatePrice.close();
            stmtUpdateQuantity.close();
            conn.close();
        } catch (SQLException e)
        {
            System.err.println("Failed to close the SQL connection. " + e);
        }
    }

    public boolean itemExists(String itemName)
    {
        boolean exists = false;
        try
        {
            stmtGetItem.setString(1, itemName);
            ResultSet rs = stmtGetItem.executeQuery();
            if (rs.next())
            {
                exists = true;
            }
            rs.close();
        } catch (SQLException ex)
        {
            System.err.println("Failed to check if item exists. " + ex);
        }
        return exists;
    }

    public String[] getItem(String itemName)
    {
        String[] item = new String[3];
        try
        {
            stmtGetItem.setString(1, itemName);
            ResultSet rs = stmtGetItem.executeQuery();
            if (rs.next())
            {
                item[0] = itemName;
                item[1] = Integer.toString(rs.getInt("Product_Price"));
                item[2] = Integer.toString(rs.getInt("Product_Quantity"));
            }
            rs.close();
        } catch (SQLException ex)
        {
            System.err.println("Failed to get item information. " + ex);
        }
        return item;
    }

    public int getItemPrice(String itemName)
    {
        int price = -1;
        try
        {
            stmtGetItemPrice.setString(1, itemName);
            ResultSet rs = stmtGetItemPrice.executeQuery();
            if (rs.next())
            {
                price = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException ex)
        {
            System.err.println("Failed to get item price. " + ex);
        }
        return price;
    }

    public int getItemQuantity(String itemName)
    {
        int quantity = -1;
        try
        {
            stmtGetItemQuantity.setString(1, itemName);
            ResultSet rs = stmtGetItemQuantity.executeQuery();
            if (rs.next())
            {
                quantity = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException ex)
        {
            System.err.println("Failed to get item quantity. " + ex);
        }
        return quantity;
    }

    public void addItem(String itemName, int itemPrice, int quantity)
    {
        try
        {
            stmtAddItem.setString(1, itemName);
            stmtAddItem.setInt(2, itemPrice);
            stmtAddItem.setInt(3, quantity);
            stmtAddItem.executeUpdate();
        } catch (SQLException ex)
        {
            System.err.println("Failed to add new product. " + ex);
        }
    }

    /*
     * I think this method is obselete as the overloading is handled at WSDL level.
     */
    public void addItem(String itemName, int itemPrice)
    {
        addItem(itemName, itemPrice, 0);
    }

    /**
     * Gets an array of names of all items in the database. Used by getInventory
     * currently.
     *
     * @return
     */
    public String[] getItemNames()
    {
        List<String> products = new ArrayList<>();
        try
        {
            ResultSet rs = stmtItemList.executeQuery();
            while (rs.next())
            {
                products.add(rs.getString("Product_Name"));
            }
            rs.close();
        } catch (SQLException ex)
        {
            System.err.println("Failed to get item list. " + ex);
        }
        return products.toArray(new String[0]);
    }

    public void removeItem(String itemName)
    {
        try
        {
            stmtRemoveItem.setString(1, itemName);
            stmtRemoveItem.executeUpdate();
        } catch (SQLException ex)
        {
            System.err.println("Failed to add new product. " + ex);
        }
    }

    /**
     * Adds <code>numberToAdd</code> items to the current quantity of available
     * <code>itemName</code> items.
     * <br>
     *
     * @param itemName
     * @param numberToAdd
     */
    public void increaseQuantity(String itemName, int numberToAdd)
    {
        int currentQuantity = getItemQuantity(itemName);
        int newQuantity = currentQuantity + numberToAdd;
        newQuantity = (newQuantity < 0) ? 0 : newQuantity;
        updateQuantity(itemName, newQuantity);
    }

    public void decreaseQuantity(String itemName, int numberToRemove)
    {
        increaseQuantity(itemName, -numberToRemove);
    }

    /**
     * Creates an entry in the history table and then adjust the quantity of
     * items in stock.
     *
     * @param itemName
     * @param newQuantity
     */
    public void updateQuantity(String itemName, int newQuantity)
    {
        addToHistory(itemName);
        try
        {
            stmtUpdateQuantity.setInt(1, newQuantity);
            stmtUpdateQuantity.setString(2, itemName);
            stmtUpdateQuantity.executeUpdate();
        } catch (SQLException ex)
        {
            System.err.println("Failed to update quantity. " + ex);
        }
    }

    private void addToHistory(String itemName)
    {
        try
        {
            stmtAddHistory.setString(1, itemName);
            stmtAddHistory.executeUpdate();
        } catch (SQLException ex)
        {
            System.err.println("Failed to update quantity. " + ex);
        }
    }

    public void updatePrice(String itemName, int newPrice)
    {
        addToHistory(itemName);
        try
        {
            stmtUpdatePrice.setInt(1, newPrice);
            stmtUpdatePrice.setString(2, itemName);
            stmtUpdatePrice.executeUpdate();
        } catch (SQLException ex)
        {
            System.err.println("Failed to update price. " + ex);
        }
    }

}
