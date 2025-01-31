package nader.eww.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Map;

@Entity(tableName = "messages")
public class Message {
	@PrimaryKey
	public long id;
	public String content;
	public String timestamp;
	public int seenCount;
	public String anonymousId;
	public long parentId;
	public Map<String, Integer> reactions;
}