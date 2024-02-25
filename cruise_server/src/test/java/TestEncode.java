import org.boketto.cruise_server.network.InboundMessage;
import org.boketto.cruise_server.network.OutboundMessage;

public class TestEncode {

    public static void main(String[] args) throws Exception {
        OutboundMessage outboundMessage = new OutboundMessage();
        byte[] encodeBytes = outboundMessage.encodeMessage();
        System.out.println(InboundMessage.decodeMessage(encodeBytes).getMessageId());
    }

}
