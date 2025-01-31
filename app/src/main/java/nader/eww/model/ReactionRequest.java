package nader.eww.model;

public class ReactionRequest {
    public String anonymousId;
    public long messageId;
    public String reaction;
    public String reason;

    // Default constructor (needed for Retrofit)
    public ReactionRequest() {}

    // Parameterized constructor
    public ReactionRequest(String anonymousId, long messageId, String reaction) {
        this.anonymousId = anonymousId;
        this.messageId = messageId;
        this.reaction = reaction;
        this.reason=null;
    }
    // Constructor with reason
    public ReactionRequest(String anonymousId, long messageId, String reaction, String reason) {
        this.anonymousId = anonymousId;
        this.messageId = messageId;
        this.reaction = reaction;
        this.reason = reason;
    }
}
