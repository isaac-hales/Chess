package chess;

//All the data will be accessible by UserData.userName or UserData.userPassword

import com.google.gson.annotations.SerializedName;

/**
 * With this, Java can do the following:
 * A constructor that takes all three parameters
 * Accessor methods userName(), userPassword(), userEmail()
 * equals(), hashCode(), and toString()
 */
public record UserData(
       @SerializedName("username") String username,
       @SerializedName("password") String password,
       @SerializedName("email") String email
)
{}
