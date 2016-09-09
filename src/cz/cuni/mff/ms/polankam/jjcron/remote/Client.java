package cz.cuni.mff.ms.polankam.jjcron.remote;

import cz.cuni.mff.ms.polankam.jjcron.common.TaskMetadata;
import java.util.List;

/**
 *
 * @author Neloop
 */
public interface Client {
    public void shutdown() throws Exception;
    public boolean isPaused() throws Exception;
    public void pause() throws Exception;
    public void unpause() throws Exception;
    public List<TaskDetail> getTasks() throws Exception;
    public void deleteTask(TaskDetail task) throws Exception;
    public void addTask(TaskMetadata task) throws Exception;
    public void saveToCrontab() throws Exception;
}
