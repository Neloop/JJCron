package cz.cuni.mff.ms.polankam.jjcron;

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of task creation factory interface.
 * By default it creates {@link CmdTask} or tasks which are loaded
 *   from reflection using name defined in crontab.
 * @author Neloop
 */
public class TaskFactoryImpl implements TaskFactory {

    /**
     * Standard Java logger.
     */
    private static final Logger logger =
            Logger.getLogger(TaskFactoryImpl.class.getName());

    /**
     * From provided {@link TaskMetadata} information create implementation
     *   of abstract {@link Task}.
     * If loaded command string is encapsulated with <code>class</code> tag
     *   then name inside is taken as name of Java class
     *   and is tried to load using reflection and if successful then returned.
     * Classical crontab command without any tags is evaluated
     *   as command line command and {@link CmdTask} is returned.
     * @param taskMeta information about task which should be created
     * @return newly created implementation of {@link Task} abstract.
     * @throws TaskException if task creation failed
     */
    @Override
    public Task createTask(TaskMetadata taskMeta) throws TaskException {
        Task result = null;

        String command = taskMeta.command();
        command = command.trim();
        if (command.startsWith("<class>") && command.endsWith("</class>")) {
            String className = command.substring("<class>".length(),
                    command.length() - "</class>".length());
            className = className.trim();

            // try to load Class object from given name
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                logger.log(Level.SEVERE, "Specified class: {0} not found" +
                        " through reflection", className);
                throw new TaskException("Specified class: " + className +
                        " not found through reflection");
            }

            // check if loaded class is child of Task
            if (!Task.class.isAssignableFrom(clazz)) {
                logger.log(Level.SEVERE, "Specified class: {0} does not" +
                        " implement abstract TaskBase class", className);
                throw new TaskException("Specified class: " + className +
                        " does not implement abstract TaskBase class");
            }


            for (Constructor constr : clazz.getConstructors()) {
                try {
                    if (constr.getParameterCount() == 0) {
                        result = (Task) constr.newInstance();
                        break;
                    } else if (constr.getParameterCount() == 1 &&
                            constr.getParameterTypes()[0] ==
                                    TaskMetadata.class) {
                        result = (Task) constr.newInstance(taskMeta);
                        break;
                    }
                } catch (Exception e) {
                    // nothing to do here... will be handled later
                }
            }

            if (result == null) {
                logger.log(Level.SEVERE, "Specified class: {0}" +
                        " cannot be instantiated", className);
                throw new TaskException("Specified class: " + className +
                        " cannot be instantiated");
            }
        } else {
            result = new CmdTask(taskMeta);
        }

        return result;
    }
}
