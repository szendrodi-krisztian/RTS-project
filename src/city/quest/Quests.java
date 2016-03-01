package city.quest;

/**
 *
 * Static loader for quests. All quests may be in memory or if there are too
 * many use a memorymapped filechannel.
 *
 * @author Krisz
 */
public class Quests {

    private static final Quests instance = new Quests();

    public static Quests getInstance() {
        return instance;
    }

}
