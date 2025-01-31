package nader.eww.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import nader.eww.model.User;

@Dao
public interface UserDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(User user);
	
	@Query("SELECT * FROM users LIMIT 1")
	LiveData<User> getUser();
}