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
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
public class ChatRepository {

    private static final String PREF_NAME = "ChatPrefs";
    private static final String KEY_ANONYMOUS_ID = "anonymous_id";

    private Context context;
    private ChatDatabase database;
    private ChatApiService apiService;
    private SharedPreferences prefs;

    public ChatRepository(Context context, ChatDatabase database, ChatApiService apiService) {
        this.context = context;
        this.database = database;
        this.apiService = apiService;
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public String getAnonymousId() {
        return prefs.getString(KEY_ANONYMOUS_ID, null);
    }

    private void saveAnonymousId(String anonymousId) {
        prefs.edit().putString(KEY_ANONYMOUS_ID, anonymousId).apply();
    }

    public void createUser(ApiCallback<User> callback) {
        apiService.createUser().enqueue(new Callback<ChatApiService.CreateUserResponse>() {
            @Override
            public void onResponse(Call<ChatApiService.CreateUserResponse> call, Response<ChatApiService.CreateUserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String anonymousId = response.body().anonymousId;
                    if (anonymousId != null && !anonymousId.isEmpty()) {
                        saveAnonymousId(anonymousId);
                        User user = new User();
                        user.anonymousId = anonymousId;
                        new Thread(() -> database.userDao().insert(user)).start();
                        Log.d("ChatRepository", "Received anonymousId: " + anonymousId);
                        callback.onSuccess(user);
                    } else {
                        Log.e("ChatRepository", "Error: Anonymous ID is null or empty");
                        callback.onError("Invalid anonymous ID received");
                    }
                } else {
                    Log.e("ChatRepository", "Error: API response unsuccessful");
                    callback.onError("Failed to create user");
                }
            }

            @Override
            public void onFailure(Call<ChatApiService.CreateUserResponse> call, Throwable t) {
                Log.e("ChatRepository", "API Call Failed: " + t.getMessage());
                callback.onError(t.getMessage());
            }
        });
    }

    public LiveData<List<Message>> getAllMessages() {
        refreshMessages();
        return database.messageDao().getAllMessages();
    }

    public LiveData<User> getUser() {
        return database.userDao().getUser();
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
        String anonymousId = getAnonymousId();
        if (anonymousId == null) {
            callback.onError("User not created. Please create a user first.");
            return;
        }
        message.anonymousId = anonymousId;
        Log.d("ChatRepository", "Sending message: " + message.content);
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
        Log.d("ChatRepository", "Marking message as seen: " + messageId);
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
        Log.d("ChatRepository", "Reacting to message: " + request.messageId + " with reaction: " + request.reaction);
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

    public void reportMessage(ReactionRequest request, ApiCallback<Void> callback) {
        apiService.reportMessage(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError("Failed to report message");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void updateUserProfile(User user, ApiCallback<Void> callback) {
        Log.d("ChatRepository", "Updating user profile for: " + user.anonymousId);
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