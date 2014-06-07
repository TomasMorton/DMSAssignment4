package inventoryclient;

import javax.xml.ws.WebServiceRef;

/**
 *
 * @author Tomas
 */
public class WebServiceClient
{
    @WebServiceRef(wsdlLocation = "http://localhost:8080/InventoryWebService/InventoryWebService?wsdl")
    private static InventoryWebService_Service service;
    
    public WebServiceClient()
    {
        
    }
    
    public void doTest(String[] args)
    {
        try
        {
            System.out.println("Retrieving the port from the following service: " + service);
            InventoryWebService port = service.getInventoryWebServicePort();
            System.out.println("Invoking the sayHello operation on the port.");

            String name;
            if (args.length > 0)
            {
                name = args[0];
            } else
            {
                name = "No Name";
            }

            String response = port.hello(name);
            System.out.println(response);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        try
        {
            WebServiceClient client = new WebServiceClient();
            client.doTest(args);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
