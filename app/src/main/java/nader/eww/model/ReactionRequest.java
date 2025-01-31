package nader.eww.model:

public class ReactionRequest {
    public String anonymousId;
    public long messageId;
    public String reaction;

    // Default constructor (needed for Retrofit)
    public ReactionRequest() {}

    // Parameterized constructor
    public ReactionRequest(String anonymousId, long messageId, String reaction) {
        this.anonymousId = anonymousId;
        this.messageId = messageId;
        this.reaction = reaction;
    }
}
