package lets.chatbot.data;

/**
 * Created by shinkook.kim on 2017-06-13.
 */
public class MessageData {
    String url;
    String type;
    String channel;
    String user;
    String text;
    String sourceTeam;
    String team;

    public boolean isType(String type){
        return type.equals(this.type);
    }
}
