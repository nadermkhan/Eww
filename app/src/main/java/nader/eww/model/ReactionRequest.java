package nader.eww.model;

public class ReactionRequest {
    public String anonymousId;
    public long messageId;
    public String reaction;
    public String reason;

    public ReactionRequest() {}

    public ReactionRequest(String anonymousId, long messageId, String reaction) {
        this.anonymousId = anonymousId;
        this.messageId = messageId;
        this.reaction = reaction;
        this.reason = null;
    }

    public ReactionRequest(String anonymousId, long messageId, String reaction, String reason) {
        this.anonymousId = anonymousId;
        this.messageId = messageId;
        this.reaction = reaction;
        this.reason = reason;
    }
}

