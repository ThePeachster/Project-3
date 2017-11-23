package datastructures.sorting;

import static org.junit.Assert.assertTrue;
// import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

// import java.util.ArrayList;
// import java.util.List;

import misc.BaseTest;
import datastructures.concrete.ArrayHeap;
// import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import org.junit.Test;

/**
* See spec for details on what kinds of tests this class should include.
*/
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
    
    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
    }
    
    //test remove min basic
    @Test(timeout=SECOND)
    public void testRemoveMinBasic() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(1);
        heap.insert(2);
        assertEquals(1, heap.removeMin());
    }
    
    
    // test remove min empty throws exception
    @Test(timeout=SECOND)
    public void testRemoveMinThrowsException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.removeMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
    }
    
    // test peek min basic and tie
    @Test(timeout=SECOND)
    public void testPeekMinBasic() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(2);
        heap.insert(1);
        heap.insert(3);
        assertEquals(1, heap.peekMin());
    }
    
    // test peek min throws exception
    @Test(timeout=SECOND)
    public void testPeekMinThrowsException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.peekMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
    }
    
    // test insert basic
    @Test(timeout=SECOND)
    public void testInsertBasic() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertEquals(0, heap.size());
        heap.insert(3);
        assertEquals(1, heap.size());
        heap.insert(2);
        assertEquals(2, heap.size());
        heap.insert(1);
        assertEquals(3, heap.size());
    }
    
    // test insert null
    @Test(timeout=SECOND)
    public void testInsertThrowsException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.insert(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }
    
    // test insert tie
    @Test(timeout=SECOND)
    public void testInsertTie() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(1);
        heap.insert(1);
        
        // test insert using removeMin()
        assertEquals(heap.removeMin(), 1);
        assertEquals(heap.removeMin(), 1);
    }
    
    // test isEmpty basic
    @Test(timeout=SECOND)
    public void testIsEmptyBasic() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertTrue(heap.isEmpty());
    }
    
    
    // test isEmpty after inserting and removing
    @Test(timeout=SECOND)
    public void testIsEmptyAfterRemoving() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 1; i <= 10; i++) {
            heap.insert(i);
        }
        for (int i = 1; i <= 10; i++) {
            int removed = heap.removeMin();
            assertEquals(i, removed);
        }
        assertTrue(heap.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testResize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(1);
        assertEquals(heap.size(), 1);
    }
    
    
}