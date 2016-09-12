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

    private final int port;
    private final String name;
    private final TaskScheduler taskScheduler;
    private final Registry registry;

    public ClientImpl(int port, String name, TaskScheduler cron) throws RemoteException {
        this.port = port;
        this.name = name;
        this.taskScheduler = cron;

        registry = LocateRegistry.createRegistry(port);
        registry.rebind(name, this);
    }

    @Override
    public void shutdown() throws Exception {
        logger.log(Level.INFO, "Shutdown command arrived from manager...");

        try {
            registry.unbind(name);
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
        return taskScheduler.isPaused();
    }

    @Override
    public void pause() throws Exception {
        taskScheduler.pause();
    }

    @Override
    public void unpause() throws Exception {
        taskScheduler.unpause();
    }

    @Override
    public List<TaskDetail> getTasks() throws Exception {
        return taskScheduler.getTaskDetails();
    }

    @Override
    public void deleteTask(TaskDetail task) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addTask(TaskMetadata task) throws Exception {
        taskScheduler.addTask(task);
    }

    @Override
    public void saveToCrontab() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
