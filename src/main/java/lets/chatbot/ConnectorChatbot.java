package lets.chatbot;

import com.fasterxml.jackson.databind.ObjectMapper;
import lets.chatbot.handler.DispatcherHandler;
import lets.chatbot.handler.dust.DustHandler;
import lets.chatbot.handler.SlackMessageHandler;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.jetty.JettyWebSocketClient;
import org.springframework.web.socket.handler.ExceptionWebSocketHandlerDecorator;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by shinkook_mac on 2017. 6. 11..
 */

public class ConnectorChatbot {

    public static void main(String[] args) throws Exception {

        Response response = null;

        DispatcherHandler dispatcher = new DispatcherHandler();
        dispatcher.addHandler("먼지", new DustHandler());

        String url = getUrl(response);
        System.out.println("[ksk] url: " + url);

        WebSocketConnectionManager connectionManager = getConnectionManager(dispatcher, url);
        connectionManager.start();
    }

    private static WebSocketConnectionManager getConnectionManager(DispatcherHandler dispatcher, String url) {

        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setTrustAll(true);
        org.eclipse.jetty.websocket.client.WebSocketClient webSocketClient = new org.eclipse.jetty.websocket.client.WebSocketClient(
                sslContextFactory);
        WebSocketClient client = new JettyWebSocketClient(webSocketClient);
        WebSocketHandler handler = new SlackMessageHandler(dispatcher);

        return new WebSocketConnectionManager(client, new ExceptionWebSocketHandlerDecorator(handler), url);

    }

    private static String getUrl(Response response) throws IOException {
        NameValuePair formatParam = new BasicNameValuePair("token", tokenValue);

        response = Request.Post("https://slack.com/api/rtm.connect")
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .bodyForm(formatParam)
                .execute();

        ObjectMapper om = new ObjectMapper();
        HashMap returnValue = om.readValue(response.returnContent().asBytes(), HashMap.class);
        return (String) returnValue.get("url");
    }


}
