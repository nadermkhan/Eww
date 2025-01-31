package nader.eww.repository;

import androidx.lifecycle.LiveData;

import nader.eww.api.ChatApiService;
import nader.eww.db.ChatDatabase;
import nader.eww.model.Message;
import nader.eww.model.ReactionRequest;
import nader.eww.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRepository {
	
	private ChatDatabase database;
	private ChatApiService apiService;
	
	public ChatRepository(ChatDatabase database, ChatApiService apiService) {
		this.database = database;
		this.apiService = apiService;
	}
	
	public LiveData<List<Message>> getAllMessages() {
		refreshMessages();
		return database.messageDao().getAllMessages();
	}
	
	private void refreshMessages() {
		apiService.getMessages().enqueue(new Callback<List<Message>>() {
			@Override
			public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
				if (response.isSuccessful() && response.body() != null) {
					new Thread(() -> database.messageDao().insertAll(response.body())).start();
				}
			}
			
			@Override
			public void onFailure(Call<List<Message>> call, Throwable t) {
				// Handle error
			}
		});
	}
	
	public void sendMessage(Message message, ApiCallback<Message> callback) {
		apiService.createMessage(message).enqueue(new Callback<Message>() {
			@Override
			public void onResponse(Call<Message> call, Response<Message> response) {
				if (response.isSuccessful() && response.body() != null) {
					new Thread(() -> database.messageDao().insert(response.body())).start();
					callback.onSuccess(response.body());
					} else {
					callback.onError("Failed to send message");
				}
			}
			
			@Override
			public void onFailure(Call<Message> call, Throwable t) {
				callback.onError(t.getMessage());
			}
		});
	}
	
	public void markMessageAsSeen(long messageId, ApiCallback<Void> callback) {
		apiService.markMessageAsSeen(messageId).enqueue(new Callback<Void>() {
			@Override
			public void onResponse(Call<Void> call, Response<Void> response) {
				if (response.isSuccessful()) {
					callback.onSuccess(null);
					} else {
					callback.onError("Failed to mark message as seen");
				}
			}
			
			@Override
			public void onFailure(Call<Void> call, Throwable t) {
				callback.onError(t.getMessage());
			}
		});
	}
	
	public void reactToMessage(ReactionRequest request, ApiCallback<Void> callback) {
		apiService.reactToMessage(request).enqueue(new Callback<Void>() {
			@Override
			public void onResponse(Call<Void> call, Response<Void> response) {
				if (response.isSuccessful()) {
					callback.onSuccess(null);
					} else {
					callback.onError("Failed to react to message");
				}
			}
			
			@Override
			public void onFailure(Call<Void> call, Throwable t) {
				callback.onError(t.getMessage());
			}
		});
	}
	
	public void updateUserProfile(User user, ApiCallback<Void> callback) {
		apiService.updateUserProfile(user).enqueue(new Callback<Void>() {
			@Override
			public void onResponse(Call<Void> call, Response<Void> response) {
				if (response.isSuccessful()) {
					new Thread(() -> database.userDao().insert(user)).start();
					callback.onSuccess(null);
					} else {
					callback.onError("Failed to update user profile");
				}
			}
			
			@Override
			public void onFailure(Call<Void> call, Throwable t) {
				callback.onError(t.getMessage());
			}
		});
	}
	
	public interface ApiCallback<T> {
		void onSuccess(T result);
		void onError(String errorMessage);
	}
}