/**
 *
 * @author Mazen
 */

import java.util.*;

public class Instructor extends User implements Cloneable{
    private ArrayList<Course> teachingCourses;

    public Instructor(int userId, String name, String email, UserRole userRole) {
        super(userId, name, email, userRole);
        teachingCourses = new ArrayList<>();
    }

    public void addTeachingCourse(Course course){
        teachingCourses.add(course);
    }

    @Override
    public String toString() {
        return String.format("Instructor: %s", super.toString());
    }
    @Override
    public Instructor clone() throws CloneNotSupportedException {
        Instructor cloned = (Instructor) super.clone();
        cloned.teachingCourses = new ArrayList<>(this.teachingCourses);
        return cloned;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Instructor)) return false;
        if(this == obj) return true;

        Instructor instructor = (Instructor) obj;
        return this.userId == instructor.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public void displayDashboard(Platform platform) throws UserNotFoundException {
        Scanner sc = new Scanner(System.in);
        SystemHelper.Choice choice;

        int option;

        while (true){
            System.out.println();
            System.out.println("------ Instructor Dashboard ------");
            System.out.println("1. View Teaching Courses \n2. Create New Course");

            choice = new SystemHelper.Choice("Choose an option (Enter 0 to go back): ");
            option = choice.ChoiceByInt(4, false);

            switch (option){
                case 0: return;
                case 1:
                    showTeachingCourses();
                    break;
                case 2:
                    createCourse(platform);
                    break;
                default:
                    System.out.println("Error: You must enter a valid choice number.");
                    break;
            }
        }
    }

    private void createCourse(Platform platform){ //Fixes: Course not added to the teachingCourses list when created,
        // when price, id, capacity is skipped an error appears
        int courseId = 9999;
        int courseCapacity = 0;
        double coursePrice = 0.0;
        String courseTitle = "";
        CourseLevel courseLevel = CourseLevel.BEGINNER;

        Random rand = new Random();

        System.out.println("------ Course Creator ------ \nCourse Levels: \n1. Beginner \n2. Intermediate \n3. Advanced");
        SystemHelper.Choice choice = new SystemHelper.Choice("Choose a level for the course (Enter 0 to go back): ");

        int option = choice.ChoiceByInt(3, false);
        switch (option){
            case 0:
                break;
            case 1:
                courseLevel = CourseLevel.BEGINNER;
                break;
            case 2:
                courseLevel = CourseLevel.INTERMEDIATE;
                break;
            case 3:
                courseLevel = CourseLevel.ADVANCED;
                break;
            default:
                System.out.println("CRITICAL: An unexpected error happened while assigning course level in create course.");
                break;
        }

        Scanner sc = new Scanner(System.in);

        while (true){
            System.out.print("Enter the title of the course (Press 0 to exit or enter to skip): ");

            try {
                courseTitle = sc.nextLine().trim();

                if(courseTitle.isEmpty()){
                    courseTitle = "NewCourse" + Course.courseCount + 1;
                    System.out.println("Skipped title input: Generated default title (" + courseTitle + ").");
                    break;
                }
                int id = Integer.parseInt(courseTitle);
                if(id == 0){
                    break;
                }
                else{
                    System.out.println("Error: The title of the course must not be numbers.");
                }
            } catch (NumberFormatException numberE){
                break;
            }
        }
        while (true){
            System.out.print("Enter the id of the course (Press 0 to exit or enter to skip): ");

            try {
                String userInput = sc.nextLine().trim();

                if(userInput.isEmpty()){
                    do{
                        int randomNum = rand.nextInt(10000);
                        userInput = String.format("%04d", randomNum);
                        courseId = Integer.parseInt(userInput);
                    } while(platform.findCourseById(Integer.parseInt(userInput)) != null);
                    System.out.println("Skipped id input: Generated random id (" + courseId + ").");
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
                courseId = id;
                break;
            } catch (NumberFormatException numberE){
                System.out.println("Error: The id must be in numbers.");
            }
        }
        while (true){
            System.out.print("Enter the capacity of the course (Press 0 to exit or enter to skip): ");

            try {
                String userInput = sc.nextLine().trim();

                if(userInput.isEmpty()){
                    System.out.println("Skipped capacity input: Default capacity is set (" + 15 + ").");
                    break;
                }
                int capacity = Integer.parseInt(userInput);
                if(capacity == 0){
                    break;
                }
                else if(capacity < 0){
                    System.out.println("Error: The capacity must be positive.");
                    continue;
                }
                courseCapacity = capacity;
                break;
            } catch (NumberFormatException numberE){
                System.out.println("Error: The capacity must be in numbers.");
            }
        }
        while (true){
            System.out.print("Enter the price of the course (Press 0 to exit or enter to skip): ");

            try {
                String userInput = sc.nextLine().trim();

                if(userInput.isEmpty()){
                    System.out.println("Skipped price input: Priced the course as free (" + 0.0 + ").");
                    break;
                }
                double price = Double.parseDouble(userInput);
                if(price == 0){
                    break;
                }
                else if(price < 0){
                    System.out.println("Error: The price must be positive.");
                    continue;
                }
                coursePrice = price;
                break;
            } catch (NumberFormatException numberE){
                System.out.println("Error: The price must be in numbers.");
            }
        }

        //If you can, add the ability to confirm creation.
        Course createdCourse = new Course(courseId, courseCapacity, courseTitle, coursePrice, courseLevel);
        platform.addCourse(createdCourse);
        addTeachingCourse(createdCourse);
    }

    public Student showEnrolledStudents(Course course){
        List<Student> studentList = course.getEnrolledStudents();
        if(studentList.isEmpty()){
            System.out.println("There are no students enrolled in this course.");
            return null;
        }
        else{
            for (int i = 0; i < studentList.size(); i++) {
                System.out.println((i+1) + ". " + studentList.get(i));
            }
            SystemHelper.Choice choice = new SystemHelper.Choice("Choose a student to manage (Press 0 to exit): ",
                    "Error: You must choose a student.",
                    "Error: You must enter a positive number.",
                    "Error: You must enter a valid student choice.");

            int option = choice.ChoiceByInt(studentList.size(), false);

            if(option == 0) return null;

            return studentList.get(option - 1);
        }
    }

    public Student findEnrolledStudent(Course course) throws UserNotFoundException {
        try {
            SystemHelper.Search searcher = new SystemHelper.Search("Enter a student's Name or Id (Enter 0 to go back): ",
                    "Error: You must enter a student's Name or Id.",
                    "Error: You must enter a valid positive Id.");
            SystemHelper.Choice choice = new SystemHelper.Choice("Choose a student or enter another student's name to search again" +
                    " (Enter 0 to go back): ",
                    "Error: You must choose a student.",
                    "Error: You must enter a positive number.",
                    "Error: You must enter a valid student choice.");

            List<User> users = new ArrayList<>(course.getEnrolledStudents());
            Student student = (Student) searcher.searchForUser(users, choice, UserRole.STUDENT);
            if(student == null){
                System.out.println("There are no students enrolled in the course.");
                return null;
            }
            return student;
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        }
    }

    public void showTeachingCourses(){
        if(teachingCourses.isEmpty()){
            System.out.println("You are not teaching any course.");
            return;
        }

        for (int i = 0; i < teachingCourses.size(); i++) {
            System.out.println((i+1) + ". " + teachingCourses.get(i));
        }

        SystemHelper.Choice choice = new SystemHelper.Choice("Choose a course to manage (Press 0 to exit): ",
                "Error: You must choose a course.",
                "Error: You must enter a positive number.",
                "Error: You must enter a valid course choice.");

        int option = choice.ChoiceByInt(teachingCourses.size(), false);

        if(option == 0) return;

        Course course = teachingCourses.get(option - 1);

        System.out.println("Managing (" + course.getTitle() + ")");
        System.out.println("1. View Enrolled Students \n2. Search For Enrolled Student");
        option = choice.ChoiceByInt(2, false);

        Student student = null;

        if(option == 0) return;
        else if(option == 1){
            student = showEnrolledStudents(course);
        }
        else if(option == 2){
            try {
                student = findEnrolledStudent(course);
            } catch (UserNotFoundException e){
                System.out.println(e.getMessage());
            }
        }

        if(student == null) return;

        System.out.println("Managing (" + student.getName() + ")");
        System.out.println("1. Assign grade");

        choice = new SystemHelper.Choice("Choose an option (Press 0 to exit): ");

        option = choice.ChoiceByInt(1, false);

        if(option == 0) return;
        else if(option == 1){
            gradeStudent(course, student);
        }
    }

    public void gradeStudent(Course course, Student student){
        Scanner sc = new Scanner(System.in);
        String userInput = "";
        while (true){
            System.out.print("Enter a positive grade to increase or a negative to decrease (Enter 0 to go back): ");
            userInput = sc.nextLine().trim();

            try {
                if(userInput.isEmpty()){
                    System.out.println("Error: You must enter a grade.");
                    continue;
                }

                double grade = Double.parseDouble(userInput);

                if(grade == 0){
                    break;
                }

                boolean success = student.modifyGrade(course, Double.valueOf(grade));

                if(success){
                    break;
                }
            } catch (NumberFormatException e){
                System.out.println("Error: You must enter a number.");
            }
        }
    }
}