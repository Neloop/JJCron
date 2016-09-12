package cz.cuni.mff.ms.polankam.jjcron.remote;

import cz.cuni.mff.ms.polankam.jjcron.common.TaskMetadata;
import java.rmi.Remote;
import java.util.List;

/**
 *
 * @author Neloop
 */
public interface Client extends Remote {

    /**
     *
     * @throws Exception
     */
    public void shutdown() throws Exception;

    /**
     *
     * @return
     * @throws Exception
     */
    public boolean isPaused() throws Exception;

    /**
     *
     * @throws Exception
     */
    public void pause() throws Exception;

    /**
     *
     * @throws Exception
     */
    public void unpause() throws Exception;

    /**
     *
     * @return
     * @throws Exception
     */
    public List<TaskDetail> getTasks() throws Exception;

    /**
     *
     * @param task
     * @throws Exception
     */
    public void deleteTask(TaskDetail task) throws Exception;

    /**
     *
     * @param task
     * @throws Exception
     */
    public void addTask(TaskMetadata task) throws Exception;

    /**
     * 
     * @throws Exception
     */
    public void saveToCrontab() throws Exception;
}
