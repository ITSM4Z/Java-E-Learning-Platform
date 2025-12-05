/**
 *
 * @author Meshal
*/

/**
 * Course class represents a specific course in the E-Learning Platform.
 *
 * This class serves as a central entity in the system, managing course details,
 * enrolled students, modules, and performance ratings. It implements the
 * {@link Enrollable} interface to handle student registration logic and
 * {@link Rateable} to track course quality.
 */

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class Course implements Enrollable, Rateable, Cloneable, Comparable<Course>{

    /**
     * Constructs a new Course with the specified details.
     *
     * @param courseID    The UNIQUE identifier for the course.
     * @param capacity    The max number of students allowed.
     * @param title       The title of the course.
     * @param price       The cost of enrollment.
     * @param courseLevel The difficulty level (Beginner, Intermediate, Advanced).
     */

    private int courseID;
    private int capacity;
    private String title;
    private double price;
    private double averageRating; //new
    private CourseLevel courseLevel;

    private ArrayList<Module> modules;
    private ArrayList<Double> ratings;
    private ArrayList<Student> enrolledStudents;

    public Course() {}
    public Course(int courseID, int capacity, String title, double price, CourseLevel courseLevel) {

        this.courseID = courseID;
        this.capacity = capacity;
        this.title = title;
        this.price = price;
        this.courseLevel = courseLevel; //new
        this.modules = new ArrayList<>();
        this.ratings = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
    }
    public int getCourseID() {
        return courseID;
    }
    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public CourseLevel getCourseLevel(){
        return courseLevel;
    } //new
    public void setCourseLevel(CourseLevel courseLevel){
        this.courseLevel = courseLevel;
    } //new


    public List<Module> getModules() {
        return Collections.unmodifiableList(modules);
    } //Made it return an unmodifiablelist
    public void setModules(ArrayList<Module> modules) {
        this.modules = modules;
    }

    public List<Double> getRatings() {
        return Collections.unmodifiableList(ratings);
    } //New and made it return an unmodifiablelist
    public void setRatings(ArrayList<Double> ratings) {
        this.ratings = ratings;
    }

    public List<Student> getEnrolledStudents() {
        return Collections.unmodifiableList(enrolledStudents);
    } //Made it return an unmodifiableList
    public void setEnrolledStudents(ArrayList<Student> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public String courseInfo(){
        return String.format("%s (%d) \n%d Students \nProviding %d Modules \nRatings: %.1f \nPrice: %.2f",
                title, courseID, enrolledStudents.size(), modules.size(), averageRating, price);

                 /**
                 * This returns a formatted string summary of the course statistics.
                 * title, ID, enrollment count, module count, rating, and price.
                 *
                 * @param return A multi-line string containing course details.
                 */
    }

    @Override
    public String toString(){
        return String.format("%s (%d)", title, courseID);
    }


    @Override
    public Course clone() throws CloneNotSupportedException {
        Course cloned = (Course) super.clone();
        cloned.enrolledStudents = new ArrayList<>(this.enrolledStudents);
        cloned.modules = new ArrayList<>(this.modules);
        cloned.ratings = new ArrayList<>(this.ratings);
        return cloned;

        /**
         * Creates a deep copy of the Course object.
         *
         * This method ensures that changeable fields like enrolledStudents, modules,
         * and ratings are duplicated so that the clone is independent of the original.
         *
         * @return A new independent Course object.
         * @throws CloneNotSupportedException if the object cannot be cloned.
         */
    }

    @Override
    public boolean equals(Object obj) { //new
        if(obj == null || !(obj instanceof Course)) return false;
        if(this == obj) return true;

        Course course = (Course) obj;
        return this.courseID == course.courseID;
    }

    @Override
    public int hashCode() { //new
        return Objects.hash(courseID);
    }

    @Override
    public int compareTo(Course course) {
        return Integer.compare(this.courseLevel.ordinal(), course.courseLevel.ordinal());

        /**
         * Compares this course with another, (DIFFICULTY BASED).
         *
         * @param course, The other course to compare against.
         * @return A negative integer, zero, or a positive integer as this course's level
         * is less than, equal to, or greater than the specified course's level.
         */
    }

    @Override
    public boolean enroll(Student s) throws AlreadyEnrolledException, CourseFullException {//new
        if (enrolledStudents.contains(s)) {
            throw new AlreadyEnrolledException("Student is already in this course!");
        }
        if (enrolledStudents.size() >= capacity) {
            throw new CourseFullException("Sorry, this course is full!");
        }
        enrolledStudents.add(s);
        Enrollment enrollmentReceipt = new Enrollment(s, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        enrollmentReceipt.printReceipt();
        System.out.println("Student: " + s.getName() + " was Successfully added!");
        return true;

        /**
         * Enrolls a student in this course if and only if capacity allows + they are not already registered.
         *
         * @param s is the Student object attempting to enroll.
         * @return true if the enrollment was successful.
         * @throws AlreadyEnrolledException if the student is already in the enrolled list.
         * @throws CourseFullException if the course has reached its maximum capacity.
         */
    }

    @Override
    public boolean drop(Student s) throws UserNotFoundException { //new
        if (enrolledStudents.contains(s)) {
            enrolledStudents.remove(s);
            System.out.println("Student: " + s.getName() + " was successfully removed.");
            return true;
        } else if (!enrolledStudents.contains(s)) {
            throw new UserNotFoundException("Error: The student is not on the registered list.");
        }
        return false;

        /**
         * Removes (drops) a student from the course.
         *
         * @param s The student to be removed.
         * @return true if the student was successfully removed.
         * @throws UserNotFoundException if the student is NOT currently enrolled in this course.
         */
    }

    @Override
    public void addRating(Double rating) {
        ratings.add(rating);
        calculateAverageRating();

        /**
         * Adds a rating to the course and recalculates the average.
         *
         * @param rating is the rating score (typically 1.0 to 5.0).
         */
    }

    @Override
    public double getAverageRating() {
        return averageRating;
    }

    public void calculateAverageRating(){
        if(ratings.isEmpty()){
            averageRating = 0;
            return;
        }
        double avg = 0.0;
        for(Double rating : ratings){
            avg += rating;
        }
        averageRating = avg/ratings.size();

        /**
         * Recalculates the average rating based on the current list of ratings.
         * Updates the internal averageRating field.
         */
    }

    /**
     * The class below is an inner class representing a receipt for a successful enrollment.
     * This encapsulates the details of a specific registration event.
     */
    public class Enrollment {
        private Student student;
        private String enrollmentDate;

        public Enrollment(Student student, String enrollmentDate) {
            this.student = student;
            this.enrollmentDate = enrollmentDate;
        }

        public void printReceipt() {
            System.out.println("--- Receipt Printing ---");
            System.out.println("Course: " + title);
            System.out.println("Student name: " + student.getName());
            System.out.println("Registering date:  " + enrollmentDate);
            System.out.println("-------------------");
        }
    }
}