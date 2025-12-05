/**
 *
 * @author Meshal
 *
 * This class serves as a data model for course content.
 * Represents a single lesson entity containing a title and duration.
 */
public class Lesson {
    private String title;
    private int durationMinutes;

    public Lesson(){}
    public Lesson(String title, int durationMinutes) {
        this.title = title;
        this.durationMinutes = durationMinutes;
    }

    ///setters, getters
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getDurationMinutes() {
        return durationMinutes;
    }
    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }


    /**
     * this returns a string representation of the Lesson object.
     * Output example: "Lesson title: Java, Duration in minutes: (120)"
     */
    @Override
    public String toString() {
        return "Lesson title: " + title + ", Duration in minutes: " + "(" + durationMinutes + ")";
    }
}
