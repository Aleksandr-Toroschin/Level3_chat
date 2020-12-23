import org.junit.Test;

public class TestGet {

    @Test(expected = RuntimeException.class)
    public void testException() {
        Main.getArray(new int[]{1,2,3,5,6,7,8});
    }
}
