package nader.eww.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import nader.eww.model.Message;
import nader.eww.model.User;

@Database(entities = {User.class, Message.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class ChatDatabase extends RoomDatabase {
	public abstract UserDao userDao();
	public abstract MessageDao messageDao();
	
	private static volatile ChatDatabase INSTANCE;
	
	public static ChatDatabase getDatabase(final Context context) {
		if (INSTANCE == null) {
			synchronized (ChatDatabase.class) {
				if (INSTANCE == null) {
					INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
					ChatDatabase.class, "chat_database")
					.build();
				}
			}
		}
		return INSTANCE;
	}
}