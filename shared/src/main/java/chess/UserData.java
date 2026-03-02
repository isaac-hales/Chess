package chess;

//All the data will be accessible by UserData.userName or UserData.userPassword
/**
 * With this, Java can do the following:
 * A constructor that takes all three parameters
 * Accessor methods userName(), userPassword(), userEmail()
 * equals(), hashCode(), and toString()
 */
import com.fasterxml.jackson.annotation.JsonProperty;

public record UserData(
        @JsonProperty("username") String userName,
        @JsonProperty("password") String userPassword,
        @JsonProperty("email") String userEmail
) {}
