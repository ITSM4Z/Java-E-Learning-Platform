/**
 *
 * @author Osama
 */

public abstract class User {
    protected int userId;
    protected String name;
    protected String email;
    protected UserRole userRole;
    public static int usersCount;

    public User(int userId, String name, String email, UserRole userRole) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.userRole = userRole;
        usersCount++;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getUserRole(){
        return userRole;
    }
    public void setUserRole(UserRole userRole){
        this.userRole = userRole;
    }

    public abstract void displayDashboard(Platform platform) throws UserNotFoundException;

    public String toString() {
        return String.format("%s (%d). Email: %s Role: %s", name, userId, email, userRole);
    }
}