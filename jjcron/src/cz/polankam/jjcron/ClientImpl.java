package cz.polankam.jjcron;

import cz.polankam.jjcron.common.TaskMetadata;
import cz.polankam.jjcron.remote.Client;
import cz.polankam.jjcron.remote.TaskDetail;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of client on JJCron instance based on RMI technology. This
 * class serves also as RMI registry which is created on construction and
 * destructed only after successful call of {@link #shutdown()}. In RMI
 * terminology this is more like server than client.
 *
 * @author Neloop
 */
public class ClientImpl extends UnicastRemoteObject implements Client {

    /**
     * Standard Java logger.
     */
    private static final Logger logger
            = Logger.getLogger(ClientImpl.class.getName());

    /**
     * Command line options given at JJCron construction.
     */
    private final CmdOptions options;
    /**
     * Scheduler and holder for active cron tasks, also handles all operations
     * with them.
     */
    private final TaskScheduler taskScheduler;
    /**
     * RMI registry server.
     */
    private Registry registry;

    /**
     * Classical constructor with all needed values.
     *
     * @param options command line options
     * @param cron task scheduler used in this instance of cron
     * @throws RemoteException in case of any error
     */
    public ClientImpl(CmdOptions options, TaskScheduler cron)
            throws RemoteException {
        this.options = options;
        this.taskScheduler = cron;
    }

    /**
     * Creates RMI registry server and bind this client instance in it.
     *
     * @throws RemoteException in case of any error
     */
    public void startServer() throws RemoteException {
        registry = LocateRegistry.createRegistry(options.rmPort);
        registry.rebind(options.rmName, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown() throws Exception {
        logger.log(Level.INFO, "Shutdown command arrived from manager...");

        try {
            registry.unbind(options.rmName);
            UnicastRemoteObject.unexportObject(this, true);
        } catch (RemoteException | NotBoundException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
            throw ex;
        }

        taskScheduler.exit();
        logger.log(Level.INFO, "Successfully unregistered and unbind,"
                + " JJCron will shutdown.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPaused() throws Exception {
        return !taskScheduler.isRunning();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pause() throws Exception {
        taskScheduler.stop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unpause() throws Exception {
        taskScheduler.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TaskDetail> getTasks() throws Exception {
        return taskScheduler.getTaskDetails();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTask(TaskDetail task) throws Exception {
        taskScheduler.deleteTask(task.id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTask(TaskMetadata task) throws Exception {
        taskScheduler.addTask(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveToCrontab() throws Exception {
        logger.log(Level.INFO, "Save to Crontab was requested from manager.");
        List<TaskMetadata> tasks = taskScheduler.getTaskMetadatas();
        CrontabParser.save(tasks, options.crontab);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reloadCrontab() throws Exception {
        logger.log(Level.INFO, "Reload Crontab was requested from manager.");
        List<TaskMetadata> tasks = CrontabParser.loadFile(options.crontab);
        taskScheduler.reloadTasks(tasks);
    }
}
