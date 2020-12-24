import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class MassTestOneFour {
    private int[] source;
    private boolean res;

    public MassTestOneFour(int[] source, boolean res) {
        this.source = source;
        this.res = res;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new int[]{1,1,1,4,4,4,1,1}, true},
                {new int[]{1,1,1,1,1}, false},
                {new int[]{4,4,4,4,4,4}, false},
                {new int[]{1,2,1,1,1,4,2,1}, false},
                {new int[]{1,2,1,1,3,3,3,1}, false}
        });
    }

    @Test
    public void testIsOneFour() {
        Assert.assertEquals(res, Main.isFourAndOne(source));
    }


}
