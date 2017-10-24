package f10.net.androidtoolbox;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

import f10.net.androidtoolbox.collections.SmartList;
import f10.net.androidtoolbox.interfaces.SortDescripor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by fl0 on 24/10/2017.
 */

public class ArrayTest {

    @Test
    public void test_swap() throws Exception {
        // Prepare a list of 100 random integer
        SmartList<Integer> ints = new SmartList<>();
        ints.add(5);
        ints.add(3);

        ints.swap(0, 1);

        Assert.assertTrue(ints.get(0) == 3);
        Assert.assertTrue(ints.get(1) == 5);

    }

    @Test
    public void test_sort() throws Exception {
        // Prepare a list of 100 random integer
        SmartList<Integer> ints = new SmartList<>();
        Random r = new Random();
        for(int i = 0; i < 100; i++)
        {
            ints.add(r.nextInt() + 1);
        }

        // sort
        ints.sort(new SortDescripor<Integer>() {
            @Override
            public boolean isBefore(Integer item1, Integer item2) {
                return item1 < item2;
            }
        });

        // test
        int greater = Integer.MIN_VALUE;
        for(int element : ints)
        {
            assertTrue(element >= greater);
            greater = element;
        }
    }
}
