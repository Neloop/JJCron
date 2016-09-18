package cz.polankam.jjcron.remote;

import cz.polankam.jjcron.common.PrintMethodNameTestWatcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TestWatcher;

/**
 *
 * @author Neloop
 */
public class TaskDetailTest {

    @Rule
    public TestWatcher watcher = new PrintMethodNameTestWatcher();

    @BeforeClass
    public static void setUpClass() {
        System.out.println(">>> TaskDetailTest <<<");
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    public void test_Correct() {
        // TODO:
    }
}
