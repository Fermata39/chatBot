package lets.chatbot;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;

/**
 * Created by shinkook_mac on 2017. 6. 11..
 */

@Slf4j
public class connectorChatbot {

    public static void main(String[] args) throws IOException{
        String tokenValue = "";
        NameValuePair formatParam = new BasicNameValuePair("token", tokenValue);

        Response response = Request.Post("https://slack.com/api/rtm.connect")
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .bodyForm(formatParam)
                .execute();

        System.out.println("test: " + response);
    }
}
