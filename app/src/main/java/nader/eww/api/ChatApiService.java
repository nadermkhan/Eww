package nader.eww.api;

import nader.eww.model.Message;
import nader.eww.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChatApiService {
	@POST("/user")
	Call<User> createUser();
	
	@POST("/user/update")
	Call<Void> updateUserProfile(@Body User user);
	
	@GET("/messages")
	Call<List<Message>> getMessages();
	
	@POST("/message")
	Call<Message> createMessage(@Body Message message);
	
	@GET("/message/{id}/replies")
	Call<List<Message>> getMessageReplies(@Path("id") long messageId);
	
	@POST("/react")
	Call<Void> reactToMessage(@Body ReactionRequest reactionRequest);
	
	@GET("/message/{id}/seen")
	Call<Void> markMessageAsSeen(@Path("id") long messageId);
}