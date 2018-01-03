package lets.chatbot.vo;

public class ResponseMessage {

    private String channel;
    private String type;
    private String text;

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getChannel() {
        return channel;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }
}
