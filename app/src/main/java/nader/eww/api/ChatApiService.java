package nader.eww.api;

import nader.eww.model.Message;
import nader.eww.model.User;
import nader.eww.model.ReactionRequest;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChatApiService {
    @POST("/api/user")
    Call<User> createUser();

    @POST("/api/user/update")
    Call<Void> updateUserProfile(@Body User user);

    @GET("/api/messages")
    Call<List<Message>> getMessages();

    @POST("/api/message")
    Call<Message> createMessage(@Body Message message);

    @GET("/api/message/{id}/replies")
    Call<List<Message>> getMessageReplies(@Path("id") long messageId);

    @POST("/api/report")
    Call<Void> reportMessage(@Body ReactionRequest reactionRequest);

    @POST("/api/react")
    Call<Void> reactToMessage(@Body ReactionRequest reactionRequest);

    @GET("/api/message/{id}/seen")
    Call<Void> markMessageAsSeen(@Path("id") long messageId);
}

