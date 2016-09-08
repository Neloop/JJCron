package cz.cuni.mff.ms.polankam.jjcron.remote;

import cz.cuni.mff.ms.polankam.jjcron.common.TaskMetadata;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author Neloop
 */
public interface Client {
    public void shutdown() throws RemoteException;
    public boolean isPaused() throws RemoteException;
    public void pause() throws RemoteException;
    public void unpause() throws RemoteException;
    public List<TaskDetail> getTasks() throws RemoteException;
    public void deleteTask(TaskDetail task) throws RemoteException;
    public void addTask(TaskMetadata task) throws RemoteException;
    public void saveToCrontab() throws RemoteException;
}
