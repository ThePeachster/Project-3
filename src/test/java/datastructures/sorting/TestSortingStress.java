package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

// import static org.junit.Assert.assertTrue;

/**
* See spec for details on what kinds of tests this class should include.
*/
public class TestSortingStress extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
    
    @Test(timeout=SECOND)
    public void stressTestRemoveMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 1000000; i > 0; i--) {
            heap.insert(i);
        }
        assertEquals(1000000, heap.size());
        for (int i = 0; i < 1000000; i++) {
            heap.removeMin();
        }
        assertEquals(0, heap.size());
    }
    
    
    @Test(timeout=5 * SECOND)
    public void stressTestTopKSort() {
        IList<Integer> list = new DoubleLinkedList<Integer>();
        
        for (int i = 0; i < 1000000; i++) {
            list.add(i);
        }
        list = Searcher.topKSort(999999, list);
    }
    
    @Test(timeout=5 * SECOND)
    public void stressTestTopKSortSmallK() {
        IList<Integer> list = new DoubleLinkedList<Integer>();
        
        for (int i = 0; i < 1000000; i++) {
            list.add(i);
        }
        list = Searcher.topKSort(5, list);
    }
}
