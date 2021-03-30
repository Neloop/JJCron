package cz.polankam.jjcron.remote.manager;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import static org.mockito.Mockito.*;

/**
 *
 * @author Neloop
 */
public class ClientsHolderTest {

    private ClientsHolder holder;
    private ClientAddress firstAddress;
    private ClientAddress secondAddress;
    private ClientWrapper firstClient;
    private ClientWrapper secondClient;

    @Rule
    public TestWatcher watcher = new PrintMethodNameTestWatcher();

    @BeforeClass
    public static void setUpClass() {
        System.out.println(">>> ClientsHolderTest <<<");
    }

    @Before
    public void setUp() throws Exception {
        holder = new ClientsHolder();
        firstAddress = new ClientAddress("//localhost", "first");
        secondAddress = new ClientAddress("//localhost", "second");

        firstClient = mock(ClientWrapper.class);
        secondClient = mock(ClientWrapper.class);

        when(firstClient.getClientAddress()).thenReturn(firstAddress);
        when(secondClient.getClientAddress()).thenReturn(secondAddress);
    }

    @Test
    public void testIsEmpty_Correct() throws ManagerException {
        assertTrue(holder.isEmpty());
        String id = holder.addClient(firstClient);
        assertFalse(holder.isEmpty());
        holder.deleteClient(id);
        assertTrue(holder.isEmpty());
    }

    @Test(expected = ManagerException.class)
    public void testAddClient_NullClient() throws ManagerException {
        holder.addClient(null);
    }

    @Test
    public void testAddClient_TwoSameClientsAdded() throws ManagerException {
        assertEquals(holder.size(), 0);
        assertEquals(holder.clientsObservableList.size(), 0);
        holder.addClient(firstClient);
        assertEquals(holder.size(), 1);
        assertEquals(holder.clientsObservableList.size(), 1);
        holder.addClient(firstClient);
        assertEquals(holder.size(), 1);
        assertEquals(holder.clientsObservableList.size(), 1);
    }

    @Test
    public void testAddClient_Correct() throws ManagerException {
        String first = holder.addClient(firstClient);
        assertEquals(holder.size(), 1);
        assertEquals(holder.clientsObservableList.size(), 1);
        String second = holder.addClient(secondClient);
        assertEquals(holder.size(), 2);
        assertEquals(holder.clientsObservableList.size(), 2);
    }

    @Test
    public void testDeleteClient_NullIdentification() {
        holder.deleteClient(null); // nothing should happen
    }

    @Test
    public void testDeleteClient_Correct() throws ManagerException {
        String first = holder.addClient(firstClient);
        String second = holder.addClient(secondClient);

        holder.deleteClient(second);
        assertEquals(holder.size(), 1);
        assertEquals(holder.clientsObservableList.size(), 1);

        holder.deleteClient(first);
        assertEquals(holder.size(), 0);
        assertEquals(holder.clientsObservableList.size(), 0);
    }

    @Test
    public void testGetClient_NonExistingIdentification()
            throws ManagerException {
        holder.addClient(firstClient);
        holder.addClient(secondClient);

        assertEquals(holder.getClient("non-existing-id"), null);
    }

    @Test
    public void testGetClient_Correct() throws ManagerException {
        String first = holder.addClient(firstClient);
        String second = holder.addClient(secondClient);

        assertEquals(holder.size(), 2);
        assertEquals(holder.getClient(first), firstClient);
        assertEquals(holder.getClient(second), secondClient);
    }
}
