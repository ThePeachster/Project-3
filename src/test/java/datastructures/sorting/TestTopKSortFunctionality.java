package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
* See spec for details on what kinds of tests this class should include.
*/
public class TestTopKSortFunctionality extends BaseTest {
    
    // topKSort throws exception if k < 0
    @Test(timeout=SECOND)
    public void testTopKSortThrowsException() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        try {
            Searcher.topKSort(-1, list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testtopKSortBasic() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        
        IList<Integer> result = Searcher.topKSort(3, list);
        assertEquals(3, result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(i + 17, result.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testZeroK() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(2);
        IList<Integer> result =  Searcher.topKSort(0, list);
        assertTrue(result.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testDataTypes() {
        IList<String> list = new DoubleLinkedList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        IList<String> result =  Searcher.topKSort(2, list);
        assertEquals("c", result.get(0));
        assertEquals("d", result.get(1));
    }
    
    @Test(timeout=SECOND)
    public void testKGreaterThanInput() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        
        IList<Integer> result = Searcher.topKSort(15, list);
        assertEquals(10, result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(i, result.get(i));
        }
    }
}