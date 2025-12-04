/**
 *
 * @author Mazen
 */

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //Fix the search not showing the instructor
        //Fix the Sort methods not showing and telling what they are actually sorting
        //Fix the second search reverting to the first search when entering 0
        User currentUser = null;
        Platform platform = new Platform();
        List<User> userList = platform.getUsers();

        while (true){
            System.out.println("------ Welcome to the E-Learning Platform Program! ------");
            System.out.println("1. Login as Admin \n2. Login as Instructor \n3. Login as Student");
            SystemHelper.Choice choice = new SystemHelper.Choice("Choose an option (Enter 0 to exit the program): ");
            int option = choice.ChoiceByInt(3, false);
            int userCounter = 0;

            choice = new SystemHelper.Choice("Choose an option (Enter 0 to go back): ");
            switch (option){
                case 0:
                    System.out.println("Thank you for using the E-Learning Platform Program!");
                    return;
                case 1:
                    List<Admin> admins = new ArrayList<>();
                    for (User user : userList) {
                        if (user instanceof Admin) {
                            userCounter++;
                            System.out.println((userCounter) + ". " + user);
                            admins.add((Admin) user);
                        }
                    }
                    option = choice.ChoiceByInt(admins.size(), false);
                    if(option == 0) continue;

                    currentUser = admins.get(option-1);
                    try{
                        currentUser.displayDashboard(platform);
                    } catch (UserNotFoundException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    List<Instructor> instructors = new ArrayList<>();
                    for (User user : userList) {
                        if (user instanceof Instructor) {
                            userCounter++;
                            System.out.println((userCounter) + ". " + user);
                            instructors.add((Instructor) user);
                        }
                    }
                    option = choice.ChoiceByInt(instructors.size(), false);
                    if(option == 0) continue;

                    currentUser = instructors.get(option-1);
                    try{
                        currentUser.displayDashboard(platform);
                    } catch (UserNotFoundException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    List<Student> students = new ArrayList<>();
                    for (User user : userList) {
                        if (user instanceof Student) {
                            userCounter++;
                            System.out.println((userCounter) + ". " + user);
                            students.add((Student) user);
                        }
                    }
                    option = choice.ChoiceByInt(students.size(), false);
                    if(option == 0) continue;

                    currentUser = students.get(option-1);
                    try{
                        currentUser.displayDashboard(platform);
                    } catch (UserNotFoundException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Error");
                    break;
            }
        }
    }
}