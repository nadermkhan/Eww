package nader.eww.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import nader.eww.api.ChatApiService;
import nader.eww.db.ChatDatabase;
import nader.eww.model.Message;
import nader.eww.model.ReactionRequest;
import nader.eww.model.User;
import nader.eww.repository.ChatRepository;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {
	
	private ChatRepository repository;
	private LiveData<List<Message>> messages;
	private MutableLiveData<String> error = new MutableLiveData<>();
	
	public ChatViewModel(@NonNull Application application) {
		super(application);
		ChatDatabase database = ChatDatabase.getDatabase(application);
		ChatApiService apiService = RetrofitClient.getClient("https://mywebsitechat.com").create(ChatApiService.class);
		repository = new ChatRepository(database, apiService);
		messages = repository.getAllMessages();
	}
	
	public LiveData<List<Message>> getMessages() {
		return messages;
	}
	
	public LiveData<String> getError() {
		return error;
	}
	
	public void sendMessage(Message message) {
		repository.sendMessage(message, new ChatRepository.ApiCallback<Message>() {
			@Override
			public void onSuccess(Message result) {
				// Message sent successfully
			}
			
			@Override
			public void onError(String errorMessage) {
				error.postValue(errorMessage);
			}
		});
	}
	
	public void markMessageAsSeen(long messageId) {
		repository.markMessageAsSeen(messageId, new ChatRepository.ApiCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				// Message marked as seen
			}
			
			@Override
			public void onError(String errorMessage) {
				error.postValue(errorMessage);
			}
		});
	}
	
	public void reactToMessage(String anonymousId, long messageId, String reaction) {
		ReactionRequest request = new ReactionRequest();
		request.anonymousId = anonymousId;
		request.messageId = messageId;
		request.reaction = reaction;
		
		repository.reactToMessage(request, new ChatRepository.ApiCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				// Reaction sent successfully
			}
			
			@Override
			public void onError(String errorMessage) {
				error.postValue(errorMessage);
			}
		});
	}
	
	public void updateUserProfile(User user) {
		repository.updateUserProfile(user, new ChatRepository.ApiCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				// Profile updated successfully
			}
			
			@Override
			public void onError(String errorMessage) {
				error.postValue(errorMessage);
			}
		});
	}
}