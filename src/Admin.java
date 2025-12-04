/**
 *
 * @author Mazen
 */

import java.util.*;

public class Admin extends User implements Cloneable{
    private Platform platform;

    public Admin(int userId, String name, String email, UserRole userRole, Platform platform) {
        super(userId, name, email, userRole);
        this.platform = platform;
    }

    @Override
    public String toString() {
        return String.format("Admin: %s", super.toString());
    }
    @Override
    public Admin clone() throws CloneNotSupportedException {
        return (Admin) super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Admin)) return false;
        if(this == obj) return true;

        Admin admin = (Admin) obj;
        return this.userId == admin.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public void displayDashboard(Platform platform) {
        SystemHelper.Choice choice;

        int option;

        while (true){
            System.out.println();
            System.out.println("------ Admin Dashboard ------");
            System.out.println("1. View All Users \n2. View All Courses \n3. Add New User \n4. Remove User" +
                    "\n5. View Students Sorted by GPA \n6. View Courses Sorted by Difficulty");

            choice = new SystemHelper.Choice("Choose an option (Enter 0 to go back): ");
            option = choice.ChoiceByInt(6, false);

            switch (option){
                case 0: return;
                case 1:
                    List<User> users = platform.getUsers();
                    if(users.isEmpty()){
                        System.out.println("Error: No users found.");
                        break;
                    }
                    System.out.println("Currently there's " + platform.getUsers().size() + " users in the system: ");
                    for(User user : users){
                        System.out.println(user);
                    }
                    break;
                case 2:
                    List<Course> courses = platform.getCourses();
                    if(courses.isEmpty()){
                        System.out.println("Error: No courses found.");
                        break;
                    }
                    System.out.println("Currently there's " + platform.getCourses().size() + " courses in the system: ");
                    for(Course course : courses){
                        System.out.println(course);
                    }
                    break;
                case 3:
                    createUser();
                    break;
                case 4:
                    try {
                        User user = platform.searchForUser();
                        if(user == null) break;

                        if(platform.removeUser(user)){
                            System.out.println(user + " is removed successfully.");
                        }
                        else{
                            System.out.println("CRITICAL: An Unexpected error happened while removing: " + user.name);
                        }

                    } catch (UserNotFoundException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    System.out.println("Students sorted by GPA:");
                    List<Student> sortedUsers = platform.getStudentsSortedByGPA();
                    for(Student student : sortedUsers){
                        System.out.println(student + " GPA: " + student.calculateGPA());
                    }
                    break;
                case 6:
                    System.out.println("Courses sorted by difficulty:");
                    List<Course> sortedCourses = platform.getCoursesSortedByDifficulty();
                    for(Course course : sortedCourses){
                        System.out.println(course + " Difficulty: " + course.getCourseLevel());
                    }
                    break;
                default:
                    break;
            }
        }
    }
    private void createUser(){
        int userId = 9999;
        String userName = "";
        String email = "";
        UserRole userRole = UserRole.STUDENT;

        Random rand = new Random();

        System.out.println("------ User Creator ------ \nUser Roles: \n1. Student \n2.Instructor \n3.Admin");
        SystemHelper.Choice choice = new SystemHelper.Choice("Choose a role for the user (Enter 0 to go back): ");

        int option = choice.ChoiceByInt(3, false);
        switch (option){
            case 0:
                break;
            case 1:
                userRole = UserRole.STUDENT;
                break;
            case 2:
                userRole = UserRole.INSTRUCTOR;
                break;
            case 3:
                userRole = UserRole.ADMIN;
                break;
            default:
                System.out.println("CRITICAL: An unexpected error happened while assigning user role in create user.");
                break;
        }

        Scanner sc = new Scanner(System.in);

        while (true){
            System.out.print("Enter the name of the user (Press 0 to exit or enter to skip): ");

            try {
                userName = sc.nextLine().trim();

                if(userName.isEmpty()){
                    userName = "NewUser" + (platform.getUsers().size() + 1);
                    System.out.println("Skipped name input: Generated default name (" + userName + ").");
                    break;
                }
                int id = Integer.parseInt(userName);
                if(id == 0){
                    break;
                }
                else{
                    System.out.println("Error: The name of the user must not be numbers.");
                }
            } catch (NumberFormatException numberE){
                break;
            }
        }
        while (true){
            System.out.print("Enter the id of the user (Press 0 to exit or enter to skip): ");

            try {
                String userInput = sc.nextLine().trim();

                if(userInput.isEmpty()){
                    do{
                        int randomNum = rand.nextInt(10000);
                        userInput = String.format("%04d", randomNum);
                        userId = Integer.parseInt(userInput);
                    } while(platform.findUserById(Integer.parseInt(userInput)) != null);
                    System.out.println("Skipped id input: Generated random id (" + userId + ").");
                    break;
                }
                int id = Integer.parseInt(userInput);
                if(id == 0){
                    break;
                }
                else if(platform.findUserById(id) != null){
                    System.out.println("Error: The id you entered is already used.");
                    continue;
                }
                else if(id < 0){
                    System.out.println("Error: The id number must be positive.");
                    continue;
                }
                userId = id;
                break;
            } catch (NumberFormatException numberE){
                System.out.println("Error: The id must be in numbers.");
            }
        }
        while (true){
            System.out.print("Enter the email of the user (Press 0 to exit or enter to skip): ");

            try {
                email = sc.nextLine().trim();

                if(email.isEmpty()){
                    email = "none";
                    System.out.println("Skipped email input: No email provided.");
                    break;
                }
                int id = Integer.parseInt(email);
                if(id == 0){
                    break;
                }
                else{
                    System.out.println("Error: The email of the user must not be numbers.");
                }
            } catch (NumberFormatException numberE){
                if(platform.findUserByEmail(email) != null){
                    System.out.println("Error: The email you entered is already used.");
                    continue;
                }
                break;
            }
        }

        User user = null;
        switch (userRole){
            case STUDENT:
                user = new Student(userId, userName, email, userRole);
                break;
            case INSTRUCTOR:
                user = new Instructor(userId, userName, email, userRole);
                break;
            case ADMIN:
                user = new Admin(userId, userName, email, userRole, this.platform);
                break;
            case null, default:
                System.out.println("CRITICAL: An unexpected error happened while creating a new user.");
                break;
        }

        platform.addUser(user);
    }
}
