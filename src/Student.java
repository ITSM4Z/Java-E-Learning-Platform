/**
 *
 * @author Mazen
 */

import java.util.*;

public class Student extends User implements Cloneable, Comparable<Student>{
    private ArrayList<Course> enrolledCourses;
    private HashMap<Course, Double> grades;

    public Student(int userId, String name, String email, UserRole userRole) {
        super(userId, name, email, userRole);
        enrolledCourses = new ArrayList<>();
        grades = new HashMap<>();
    }

    public void setEnrolledCourses(ArrayList<Course> courses){
        enrolledCourses.addAll(courses);
    }

    public void addCourseEnrollment(Course course){
        enrolledCourses.add(course);
        grades.put(course, 0.0);
    }
    public boolean removeCourseEnrollment(Course course) {
        boolean coursesRemoved = enrolledCourses.remove(course);
        boolean gradesRemoved = grades.remove(course) != null;
        return coursesRemoved || gradesRemoved;
    }

    public ArrayList<Course> getEnrolledCourses(){ return enrolledCourses; }

    public boolean modifyGrade(Course course, Double grade){
        double finalGrade;
        Double gradeObj = grades.get(course);
        if(gradeObj == null){
            grades.put(course, Double.valueOf(0.0));
            finalGrade = 0;
        }
        else{
            finalGrade = gradeObj;
        }

        finalGrade += grade;
        if(finalGrade < 0){
            System.out.println("Error: The student's grade must not be negative.");
            return false;
        }
        else if(finalGrade > 100){
            System.out.println("Error: The student's grade must not exceed 100.");
            return false;
        }
        else{
            grades.put(course, finalGrade);
            return true;
        }
    }
    public double getGrade(Course course){ return grades.get(course); }

    public double calculateGPA(){
        if(grades.isEmpty()) return 0.0;
        double totalGrades = 0.0;
        for(Double grade : grades.values()){
            totalGrades += grade;
        }
        return totalGrades / grades.size();
    }

    @Override
    public String toString() {
        return String.format("Student: %s", super.toString());
    }
    @Override
    public Student clone() throws CloneNotSupportedException {
        Student cloned = (Student) super.clone();
        cloned.enrolledCourses = new ArrayList<>(this.enrolledCourses);
        cloned.grades = new HashMap<>(this.grades);
        return cloned;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Student)) return false;
        if(this == obj) return true;

        Student student = (Student) obj;
        return this.userId == student.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public int compareTo(Student student) {
        return Double.compare(this.calculateGPA(), student.calculateGPA());
    }

    @Override
    public void displayDashboard(Platform platform) {
        while (true){
            System.out.println("------ Student Dashboard ------");
            System.out.println("1. View Enrolled Courses \n2. Enroll in New Course \n3. Drop Course \n4. View Grades & GPA" +
                    "\n5. Rate Course");
            Scanner sc = new Scanner(System.in);

            SystemHelper.Choice choice = new SystemHelper.Choice("Choose an option (Press 0 to go back): ");

            int option = choice.ChoiceByInt(4, false);

            switch (option){
                case 0: return;
                case 1:
                    viewEnrolledCourses(sc);
                    break;
                case 2:
                    enrollInNewCourse(platform);
                    break;
                case 3:
                    dropCourse();
                    break;
                case 4:
                    viewGrades();
                    break;
                default:
                    System.out.println("Error: You must enter a valid choice number.");
                    break;
            }
        }
    }

    private void viewGrades(){
        for(Course course : grades.keySet()){
            Double gradeObj = grades.get(course);
            if(gradeObj == null){
                System.out.printf("%s: No grades available for this course.\n", course.getTitle());
            }
            else{
                double grade = gradeObj;
                System.out.printf("%s: %.1f\n", course.getTitle(), grade);
            }
        }
        System.out.println("Your current GPA: " + calculateGPA());
    }

    private void dropCourse() {
        if (enrolledCourses.isEmpty()) {
            System.out.println("No courses to drop.");
            return;
        }

        SystemHelper.Choice choice = new SystemHelper.Choice("Choose course to drop (Press 0 to go back): ");

        while (true) {
            for (int i = 0; i < enrolledCourses.size(); i++) {
                System.out.println((i + 1) + ". " + enrolledCourses.get(i));
            }

            int option = choice.ChoiceByInt(enrolledCourses.size(), false);
            if (option == 0) {
                break;
            }

            Course selected = enrolledCourses.get(option - 1);

            try {
                boolean dropped = selected.drop(this);
                if (dropped) {
                    removeCourseEnrollment(selected);
                    System.out.println("Successfully dropped " + selected.getTitle());
                }
                break;
            } catch (UserNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }


    private void enrollInNewCourse(Platform platform){
        List<Course> availableCourses = platform.getCoursesSortedByDifficulty();

        if(availableCourses.isEmpty()){
            System.out.println("No Courses available for enrollment at the moment.");
            return;
        }

        SystemHelper.Choice choice = new SystemHelper.Choice("Choose a course to enroll in (Press 0 to go back): ");

        while (true){
            for(int i = 0; i < availableCourses.size(); i++){
                System.out.println((i+1) + ". " + availableCourses.get(i).getTitle());
            }
            int option = choice.ChoiceByInt(availableCourses.size(), false);

            if(option == 0){
                break;
            }

            Course selectedCourse = availableCourses.get(option-1);

            try {
                boolean enrolled = selectedCourse.enroll(this);
                if(enrolled){
                    addCourseEnrollment(selectedCourse);
                    System.out.println("Successfully enrolled in " + selectedCourse.getTitle());
                }
                break;
            } catch (AlreadyEnrolledException | CourseFullException e){
                System.out.println(e.getMessage());
            }
        }
    }

    private void viewEnrolledCourses(Scanner sc){
        while (true){
            SystemHelper.Choice choice = new SystemHelper.Choice("Choose a course (Press 0 to go back): ",
                    "Error: You must choose a course.",
                    "Error: You must enter a positive number.",
                    "Error: You must enter a valid course choice.");

            System.out.println();

            if(enrolledCourses.isEmpty()){
                System.out.println("You are not enrolled in any course.");
                return;
            }

            for (int i = 0; i < enrolledCourses.size(); i++) {
                System.out.println((i+1) + ". " + enrolledCourses.get(i));
            }

            int option = choice.ChoiceByInt(enrolledCourses.size(), false);
            if(option == 0) break;

            Course course = enrolledCourses.get(option - 1);

            while (true){
                System.out.println();
                System.out.println(course);
                System.out.println("1. View Grades \n2. Rate the course");
                choice = new SystemHelper.Choice("Choose an option (Enter 0 to go back): ");
                option = choice.ChoiceByInt(2, false);
                if(option == 0){
                    break;
                }
                else if(option == 1){
                    Double gradeObj = grades.get(course);
                    if(gradeObj == null){
                        System.out.println("No grades available for this course.");
                    }
                    else{
                        double grade = gradeObj;
                        System.out.println("Grade for " + course.getTitle() + ": " + grade);
                    }
                    System.out.printf("Your current GPA: %.2f\n", calculateGPA());
                    break;
                }
                else if(option == 2){
                    System.out.println("Enter rating (1-5): ");
                    double rating = sc.nextDouble();

                    if(rating < 1 || rating > 5){
                        System.out.println("Error: Rating must be 1-5.");
                        break;
                    }
                    course.addRating(rating);
                    System.out.printf("Rated %.1f | New course average rating: %.1f \n", rating, course.getAverageRating());
                    break;
                }
            }
        }
    }
}