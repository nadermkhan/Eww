package nader.eww.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @NonNull
    public String anonymousId;
    public String bio;
    public String name;
    public String dateOfBirth;
    public int verificationStatus;
}

