package nader.eww.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import nader.eww.model.Message;

import java.util.List;

@Dao
public interface MessageDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(Message message);
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insertAll(List<Message> messages);
	
	@Query("SELECT * FROM messages WHERE parentId IS NULL ORDER BY timestamp DESC")
	LiveData<List<Message>> getAllMessages();
	
	@Query("SELECT * FROM messages WHERE parentId = :parentId ORDER BY timestamp ASC")
	LiveData<List<Message>> getMessageReplies(long parentId);
}