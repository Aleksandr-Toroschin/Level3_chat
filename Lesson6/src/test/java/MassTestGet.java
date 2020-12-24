import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class MassTestGet {
    private int[] source;
    private int[] end;

    public MassTestGet(int[] source, int[] end) {
        this.source = source;
        this.end = end;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new int[]{1,5,4,3,6,7,1}, new int[]{3,6,7,1}},
                {new int[]{1,2,3,4}, new int[]{}},
//                {new int[]{1,2,3,5,6}, new int[]{}},
                {new int[]{4,4,4,1,1,1}, new int[]{1,1,1}}
        });
    }

    @Test
    public void testGetArray() {
        Assert.assertArrayEquals(end, Main.getArray(source));
    }


}
