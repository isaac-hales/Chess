package chess;

//All the data will be accessible by UserData.userName or UserData.userPassword
/**
 * With this, Java can do the following:
 * A constructor that takes all three parameters
 * Accessor methods userName(), userPassword(), userEmail()
 * equals(), hashCode(), and toString()
 */
public record UserData(
        String username,
        String password,
        String email
)
{}
