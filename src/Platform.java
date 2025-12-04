import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Mazen
 */

public class Platform {
    private static ArrayList<User> users = new ArrayList<>();
    private static ArrayList<Course> courses = new ArrayList<>();

    public Platform(){
        users.add(new Student(1, "Mazen", "Mazen@GroupWork.com", UserRole.STUDENT));
        users.add(new Student(2, "Meshal", "Meshal@GroupWork.com", UserRole.STUDENT));

        users.add(new Instructor(3, "Rayan", "Rayan@GroupWork.com", UserRole.INSTRUCTOR));

        users.add(new Admin(4, "Osama", "Osama@GroupWork.com", UserRole.ADMIN, this));

        courses.add(new Course(1, 20, "Learn Java fundamentals in two weeks!",
                23.99, CourseLevel.BEGINNER));
        courses.add(new Course(2, 30, "Java OOP from zero to hero in 30 days!",
                43.99, CourseLevel.INTERMEDIATE));
        courses.add(new Course(3, 10, "Take your java skills to the next level in just 20 days!",
                67.99, CourseLevel.ADVANCED));
    }

    public void addUser(User user){ users.add(user); }
    public boolean removeUser(User user){ return users.remove(user); }

    public User searchForUser() throws UserNotFoundException{
        try {
            SystemHelper.Search searcher = new SystemHelper.Search("Enter a user's Name or Id (Enter 0 to go back): ",
                    "Error: You must enter a user's Name or Id.",
                    "Error: You must enter a valid positive Id.");
            SystemHelper.Choice choice = new SystemHelper.Choice("Choose a user or enter another user's name to search again" +
                    " (Enter 0 to go back): ",
                    "Error: You must choose a user.",
                    "Error: You must enter a positive number.",
                    "Error: You must enter a valid user choice.");

            List<User> users = new ArrayList<>(getUsers());
            if(users.isEmpty()){
                System.out.println("There are no users in the system.");
                return null;
            }

            return searcher.searchForUser(users, choice);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        }
    }

    public User findUserById(int id){
        for(User user : users){
            if(user.getUserId() == id){
                return user;
            }
        }
        return null;
    }
    public User findUserByEmail(String email){
        for(User user : users){
            if(user.getEmail().equalsIgnoreCase(email)){
                return user;
            }
        }
        return null;
    }

    public void addCourse(Course course){ courses.add(course); }
    public boolean removeCourse(Course course){ return courses.remove(course); }

    public Course findCourseById(int id){
        for(Course course : courses){
            if(course.getCourseID() == id){
                return course;
            }
        }
        return null;
    }

    public List<User> getUsers() { return Collections.unmodifiableList(users);}
    public List<Course> getCourses() { return Collections.unmodifiableList(courses);}
    public List<Student> getStudentsSortedByGPA(){
        ArrayList<Student> tempStudentList = new ArrayList<>();
        for(User user : users){
            if(user instanceof Student){
                tempStudentList.add((Student) user);
            }
        }
        Collections.sort(tempStudentList);
        return tempStudentList;
    }
    public List<Course> getCoursesSortedByDifficulty(){
        ArrayList<Course> tempCourseList = new ArrayList<>(courses);
        Collections.sort(tempCourseList);
        return tempCourseList;
    }
}