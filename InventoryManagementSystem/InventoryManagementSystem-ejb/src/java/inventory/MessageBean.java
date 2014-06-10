package inventory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author Tomas
 */
@JMSDestinationDefinition(name = "InventoryQueue", interfaceName = "javax.jms.Queue", resourceAdapter = "jmsra", destinationName = "InventoryQueue")
@MessageDriven(activationConfig =
{
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "InventoryQueue")
})
public class MessageBean implements MessageListener
{

    public MessageBean()
    {
    }

    @Override
    public void onMessage(Message inMessage)
    {
        try
        {
            if (inMessage instanceof TextMessage)
            {
                TextMessage msg = (TextMessage) inMessage;
                System.out.println("MESSAGE BEAN: Message received: "
                        + msg.getText());
            } else
            {
                System.out.println("Message of wrong type: "
                        + inMessage.getClass().getName());
            }
        } catch (JMSException e)
        {
            e.printStackTrace();
//            mdc.setRollbackOnly();
        } catch (Throwable te)
        {
            te.printStackTrace();
        }
    }

}
