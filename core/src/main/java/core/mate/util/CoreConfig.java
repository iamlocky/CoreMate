package core.mate.util;

/**
 * @author DrkCore
 * @since 2017-04-03
 */
class CoreConfig extends ConcurrentPrefHelper{

    private static volatile CoreConfig instance = null;

    public static CoreConfig getInstance() {
        if(instance == null){
            synchronized (CoreConfig.class) {
                if (instance == null) {
                    instance = new CoreConfig();
                }
            }
        }
        return instance;
    }

    private static final String PREF_NAME = "CoreConfig";

    private  CoreConfig() {
        super(PREF_NAME);
    }
}
