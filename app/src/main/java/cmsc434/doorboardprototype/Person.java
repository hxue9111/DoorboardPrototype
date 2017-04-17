package cmsc434.doorboardprototype;

/**
 * Created by Huang on 4/1/2017.
 */

public class Person {
    String name;
    String title;
    String hours;
    boolean profile_visible;
    static int default_profile = R.drawable.profile;

    String status;
    String message;
    Person(){}
    Person(String name, String title, String hours, boolean profile_visible) {
        this.name = name;
        this.title = title;
        this.hours = hours;
        this.profile_visible = profile_visible;
    }
}
