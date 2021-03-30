package cz.polankam.jjcron.common;

import org.junit.rules.TestWatcher;

/**
 *
 * @author Neloop
 */
public class PrintMethodNameTestWatcher extends TestWatcher {

    @Override
    protected void starting(org.junit.runner.Description description) {
        System.out.println(" " + description.getMethodName());
    }
}
