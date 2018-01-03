package lets.chatbot.handler;

import lets.chatbot.vo.RequestMessage;
import lets.chatbot.vo.ResponseMessage;

import java.util.HashMap;
import java.util.Map;

public class MessageDispatcher {
    private final Map<String, MessageHandler> handlers = new HashMap();

    public void addHandler(String keyword, MessageHandler messageHandler) throws Exception{
        if (handlers.containsKey(keyword)) {
            throw new Exception("이미 포함된 handler" + keyword);
        }
        System.out.println("[ksk] keyword: " + keyword);
        handlers.put(keyword, messageHandler);

        if(messageHandler ==null) {
            System.out.println("[ksk] 먼지 Handler Null");
        }else{
            System.out.println("[ksk] 먼지 Handler Not Null");
        }
    }

    public ResponseMessage getHandler(RequestMessage requestMessage) {

        String text = requestMessage.getText();
        System.out.println("[ksk] keyword: " + text);
        String[] split = text.split(" ");

        if(handlers == null){
            System.out.println("[ksk] map is null");
        }

        MessageHandler handle = handlers.get(split[0]);

        if (handle == null) {
            System.out.println("[ksk] Null Handle");
            ResponseMessage response = new ResponseMessage();
            response.setText("잘못된 값");
            return response;
        }

        return handle.handle(requestMessage);

    }
}
