import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a structural unit (chapter/section) of a Course.
 * A Module acts as a container for multiple Lesson objects.
 * It allows for organizing course content into logical sections.
 *
 * @author Meshal
 */

public class Module {
    private String title;
    private ArrayList<Lesson> lessons;

    public Module(){}
    public Module(String title) {
        this.title = title;
        this.lessons = new ArrayList<>();

        /**
         * Constructs a new Module with a specific title.
         * Initializes an empty list of lessons.
         */
    }

    public void setTitle(String title){ //new
        this.title = title;
    }
    public String getTitle(){ //new
        return title;
    }

    public void addLesson(Lesson lesson){ //made it public
        this.lessons.add(lesson);

        /**
         * Adds a new lesson to this module.
         * @param lesson is The Lesson object to be added.
         */
    }
    public void removeLesson(Lesson lesson){ //made it public
        for(Lesson obj : lessons){
            if(obj.getTitle().equalsIgnoreCase(lesson.getTitle())){
                lessons.remove(obj);

                /**
                 * Removes an existing lesson from this module.
                 * @returns true if the lesson was found and removed, false otherwise.
                 */
            }
        }
    }
    public List<Lesson> getLessonsList(){ //new
       return Collections.unmodifiableList(lessons);
    }
    public void setLessonsList(ArrayList<Lesson> lessons){ //new
        this.lessons = lessons;
    }

    @Override
    public String toString() { //Added more info
        return "module title: " + title + ". " + lessons.size() + " lessons.";

        /**
         * Returns a string representation of the Module and its contents.
         *
         * @return A formatted string with the module title and the number of lessons.
         * Output example: "module title: Java. 6 lessons."
         */
    }
}