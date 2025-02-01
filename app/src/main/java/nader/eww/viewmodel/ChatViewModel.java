package nader.eww.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import nader.eww.api.ChatApiService;
import nader.eww.api.RetrofitClient;
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
    private MutableLiveData<Boolean> isUserCreated = new MutableLiveData<>(false);

    public ChatViewModel(@NonNull Application application) {
        super(application);
        ChatDatabase database = ChatDatabase.getDatabase(application);
        ChatApiService apiService = RetrofitClient.getClient().create(ChatApiService.class);
        repository = new ChatRepository(application, database, apiService);
        messages = repository.getAllMessages();
        checkUserCreated();
    }

    private void checkUserCreated() {
        repository.getUser().observeForever(user -> {
            isUserCreated.setValue(user != null);
        });
    }

    public LiveData<Boolean> isUserCreated() {
        return isUserCreated;
    }

    public void createUser() {
        Log.d("ChatViewModel", "Creating user");
        repository.createUser(new ChatRepository.ApiCallback<User>() {
            @Override
            public void onSuccess(User result) {
                isUserCreated.postValue(true);
            }

            @Override
            public void onError(String errorMessage) {
                error.postValue(errorMessage);
            }
        });
    }

    public LiveData<List<Message>> getMessages() {
        return messages;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void sendMessage(Message message) {
        Log.d("ChatViewModel", "Sending message: " + message.content);
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
        Log.d("ChatViewModel", "Marking message as seen: " + messageId);
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
        Log.d("ChatViewModel", "Reacting to message: " + messageId + " with reaction: " + reaction);
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

    public void reportMessage(String anonymousId, long messageId, String reason) {
        Log.d("ChatViewModel", "Reporting message: " + messageId + " with reason: " + reason);
        ReactionRequest request = new ReactionRequest();
        request.anonymousId = anonymousId;
        request.messageId = messageId;
        request.reason = reason;

        repository.reportMessage(request, new ChatRepository.ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                // Message reported successfully
            }

            @Override
            public void onError(String errorMessage) {
                error.postValue(errorMessage);
            }
        });
    }

    public void updateUserProfile(User user) {
        Log.d("ChatViewModel", "Updating user profile");
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

    public LiveData<User> getUser() {
        return repository.getUser();
    }
}