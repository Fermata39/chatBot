package lets.chatbot.vo;

public class RequestMessage {
    private String type;
    private String url;
    private String channel;
    private String user;
    private String text;
    private String sourceTeam;
    private String team;

    public boolean ofType(String type) {

        if (this.getType().equals(type)) {
            return true;
        } else {
            return false;
        }
    }


    public String getType() {
        return type;
    }

    public String getChannel() {
        return channel;
    }

    public String getUser() {
        return user;
    }


    public String getTeam() {
        return team;
    }

    public String getText() {
        return text;
    }
}
