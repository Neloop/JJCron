package cz.polankam.jjcron.remote.manager;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TestWatcher;

/**
 *
 * @author Neloop
 */
public class ClientAddressTest {

    @Rule
    public TestWatcher watcher = new PrintMethodNameTestWatcher();

    @BeforeClass
    public static void setUpClass() {
        System.out.println(">>> ClientAddressTest <<<");
    }

    @Test
    public void test_Correct() {
        String registry = "registry";
        String client = "client";
        ClientAddress addr = new ClientAddress(registry, client);

        assertEquals(addr.registryAddress, registry);
        assertEquals(addr.clientIdentification, client);
    }

    @Test
    public void testToString_Correct() {
        String registry = "registry";
        String client = "client";
        ClientAddress addr = new ClientAddress(registry, client);

        assertEquals(addr.toString(), registry + "/" + client);
    }
}
