package lets.chatbot.handler;

import lets.chatbot.vo.RequestMessage;
import lets.chatbot.vo.ResponseMessage;

public class DustHandler implements MessageHandler {
    public ResponseMessage handle(RequestMessage requestMessage) {

        System.out.println("[ksk] Dust Handler Enter");
        ResponseMessage response = new ResponseMessage();

        response.setText("먼지 상태를 알려드립니다");

        return response;
    }
}
