package cz.polankam.jjcron.remote;

import cz.polankam.jjcron.common.TaskMetadata;
import java.rmi.Remote;
import java.util.List;

/**
 * Used as management point of remote JJCron instance. In RMI speaking this
 * implementation is rather server than client. This interface was designed for
 * RMI, but another connection technology can hopefully be used without much
 * special work.
 *
 * @author Neloop
 */
public interface Client extends Remote {

    /**
     * Whole client execution have to be stopped and client has to be ended
     * properly.
     *
     * @throws Exception in case of any error
     */
    public void shutdown() throws Exception;

    /**
     * Detect if client is paused or not.
     *
     * @return true if client is paused, false otherwise
     * @throws Exception in case of any error
     */
    public boolean isPaused() throws Exception;

    /**
     * Pause execution of client and all of its scheduled tasks.
     *
     * @throws Exception in case of any error
     */
    public void pause() throws Exception;

    /**
     * Should be called after @ref pause() function to start execution again.
     *
     * @throws Exception in case of any error
     */
    public void unpause() throws Exception;

    /**
     * Get currently scheduled tasks from client.
     *
     * @return array of structures which represents task on client
     * @throws Exception in case of any error
     */
    public List<TaskDetail> getTasks() throws Exception;

    /**
     * Stop given task and delete it from client.
     *
     * @param task information about task to be deleted
     * @throws Exception in case of any error
     */
    public void deleteTask(TaskDetail task) throws Exception;

    /**
     * Add given task to client and schedule it for execution.
     *
     * @param task metadata of task which will be created
     * @throws Exception in case of any error
     */
    public void addTask(TaskMetadata task) throws Exception;

    /**
     * Stops all tasks and reload crontab file which was used for initial
     * construction.
     *
     * @throws Exception in case of any error
     */
    public void reloadCrontab() throws Exception;

    /**
     * Gets current loaded and scheduled tasks and saves them into crontab file
     * given at start.
     *
     * @throws Exception in case of any error
     */
    public void saveToCrontab() throws Exception;
}
