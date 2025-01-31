package nader.eww.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
	@PrimaryKey
	public String anonymousId;
	public String bio;
	public String name;
	public String dateOfBirth;
	public int verificationStatus;
}