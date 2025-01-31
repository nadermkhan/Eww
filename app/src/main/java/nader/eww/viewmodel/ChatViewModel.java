package nader.eww.viewmodel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import nader.eww.api.*;
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
        String anonymousId = repository.getAnonymousId();
        isUserCreated.setValue(anonymousId != null);
    }

    public LiveData<Boolean> isUserCreated() {
        return isUserCreated;
    }

    public void createUser() {
        repository.createUser(new ChatRepository.ApiCallback<User>() {
            @Override
            public void onSuccess(User result) {
                isUserCreated.postValue(true);
                showToast("User created successfully!");
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
        repository.sendMessage(message, new ChatRepository.ApiCallback<Message>() {
            @Override
            public void onSuccess(Message result) {
                showToast("Message sent successfully!");
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
                showToast("Message marked as seen!");
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
                showToast("Reaction sent successfully!");
            }

            @Override
            public void onError(String errorMessage) {
                error.postValue(errorMessage);
            }
        });
    }

    public void reportMessage(String anonymousId, long messageId, String reason) {
        ReactionRequest request = new ReactionRequest();
        request.anonymousId = anonymousId;
        request.messageId = messageId;
        request.reason = reason;

        repository.reportMessage(request, new ChatRepository.ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                showToast("Message reported successfully!");
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
                showToast("Profile updated successfully!");
            }

            @Override
            public void onError(String errorMessage) {
                error.postValue(errorMessage);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
    }
}