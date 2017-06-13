package lets.chatbot.connector;

import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;

/**
 * Created by shinkook.kim on 2017-06-13.
 */
public class ConnectorChatbot {
    public void connector() throws IOException {
        String tokenValue = "xoxb-196719547462-ciOoqhRm5aq9bZ3zl8iekNkJ";
        NameValuePair formatParam = new BasicNameValuePair("token", tokenValue);

        Response response = Request.Post("https://slack.com/api/rtm.connect")
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .bodyForm(formatParam)
                .execute();

        System.out.println("test");
    }
}

