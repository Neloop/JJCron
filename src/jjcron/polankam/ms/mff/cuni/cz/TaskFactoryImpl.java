package jjcron.polankam.ms.mff.cuni.cz;

import java.lang.reflect.Constructor;

/**
 *
 * @author Neloop
 */
public class TaskFactoryImpl implements TaskFactory {

    @Override
    public TaskBase createTask(TaskMetadata taskMeta) throws TaskException {
        TaskBase result = null;

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
                throw new TaskException("Specified class: " + className + " not found");
            }

            // check if loaded class is child of TaskBase
            if (TaskBase.class.isAssignableFrom(clazz)) {
                try {
                    Constructor<?> constructor = clazz.getConstructor(TaskMetadata.class);
                    result = (TaskBase) constructor.newInstance(taskMeta);
                } catch (Exception e) {
                    throw new TaskException("Specified class: " + className + " cannot be instantiated");
                }
            } else {
                throw new TaskException("Specified class: " + className +
                        " does not implement abstract TaskBase class");
            }
        } else {
            result = new CmdTask(taskMeta);
        }

        return result;
    }
}
