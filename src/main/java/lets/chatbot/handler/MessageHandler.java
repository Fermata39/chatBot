package lets.chatbot.handler;

import lets.chatbot.data.*;

/**
 * Created by shinkook.kim on 2017-06-13.
 */
public interface MessageHandler {
    public ResponseMessage handler(MessageData message);
}
