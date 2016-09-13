package cz.cuni.mff.ms.polankam.jjcron;

import cz.cuni.mff.ms.polankam.jjcron.common.TaskMetadata;
import cz.cuni.mff.ms.polankam.jjcron.remote.Client;
import cz.cuni.mff.ms.polankam.jjcron.remote.TaskDetail;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Neloop
 */
public class ClientImpl extends UnicastRemoteObject implements Client {

    private static final Logger logger = Logger.getLogger(ClientImpl.class.getName());

    private final CmdOptions options;
    private final TaskScheduler taskScheduler;
    private final Registry registry;

    public ClientImpl(CmdOptions options, TaskScheduler cron) throws RemoteException {
        this.options = options;
        this.taskScheduler = cron;

        registry = LocateRegistry.createRegistry(options.rmPort);
        registry.rebind(options.rmName, this);
    }

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
        logger.log(Level.INFO, "Successfully unregistered and unbind, JJCron will shutdown.");
    }

    @Override
    public boolean isPaused() throws Exception {
        return !taskScheduler.isRunning();
    }

    @Override
    public void pause() throws Exception {
        taskScheduler.stop();
    }

    @Override
    public void unpause() throws Exception {
        taskScheduler.start();
    }

    @Override
    public List<TaskDetail> getTasks() throws Exception {
        return taskScheduler.getTaskDetails();
    }

    @Override
    public void deleteTask(TaskDetail task) throws Exception {
        taskScheduler.deleteTask(task);
    }

    @Override
    public void addTask(TaskMetadata task) throws Exception {
        taskScheduler.addTask(task);
    }

    @Override
    public void saveToCrontab() throws Exception {
        logger.log(Level.INFO, "Save to Crontab was requested from manager.");
        List<TaskMetadata> tasks = taskScheduler.getTaskMetadatas();
        CrontabParser.save(tasks, options.crontab);
    }

    @Override
    public void reloadCrontab() throws Exception {
        logger.log(Level.INFO, "Reload Crontab was requested from manager.");
        List<TaskMetadata> tasks = CrontabParser.loadFile(options.crontab);
        taskScheduler.reloadTasks(tasks);
    }
}
