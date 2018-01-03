package lets.chatbot.handler;

import java.util.HashMap;

/**
 * Created by shinkook.kim on 2017-06-13.
 */
public class DispatcherHandler {

    HashMap<String, MessageHandler> handleMap = new HashMap<String, MessageHandler>();

    public void addHandler(String key, MessageHandler handler) {

        if (handleMap.containsKey(key)) {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        handleMap.put("key", handler);
    }
}
