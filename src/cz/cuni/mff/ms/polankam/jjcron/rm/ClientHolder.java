package cz.cuni.mff.ms.polankam.jjcron.rm;

/**
 *
 * @author Neloop
 */
public class ClientHolder {
    private final String registryAddress;
    private final String clientIdentification;
    private boolean paused = false;

    public ClientHolder(String addr, String id) {
        registryAddress = addr;
        clientIdentification = id;

        // TODO: actual connection to client should be placed here
    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    public String getClientIdentification() {
        return clientIdentification;
    }

    public boolean isPaused() {
        return paused;
    }

    public void pause() {
        paused = true;
        // TODO: actual pause
    }

    public void unpause() {
        paused = false;
        // TODO: actual unpause
    }
}
