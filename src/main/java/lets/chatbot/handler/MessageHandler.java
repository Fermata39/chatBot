package lets.chatbot.handler;

import lets.chatbot.vo.RequestMessage;
import lets.chatbot.vo.ResponseMessage;

public interface MessageHandler {
    ResponseMessage handle(RequestMessage resp);
}

