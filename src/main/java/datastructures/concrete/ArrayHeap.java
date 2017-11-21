package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

// import java.util.Arrays;

/**
* See IPriorityQueue for details on what each method must do.
*/
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;
    
    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    
    // Feel free to add more fields and constants.
    private int heapSize;
    
    public ArrayHeap() {
        heapSize = 0;
        heap = makeArrayOfT(1); // should we initialize this to be bigger for less resize?
    }
    
    /**
    * This method will return a new, empty array of the given size
    * that can contain elements of type T.
    *
    * Note that each element in the array will initially be null.
    */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }
    
    @Override
    public T removeMin() {
        if (heapSize == 0) {
            throw new EmptyContainerException();
        }
        T result = heap[0];
        
        // update the heap by percolating down everything
        heapSize--;
        heap[0] = heap[heapSize];
        heap[heapSize] = null;
        percolateDown(0);
        
        return result;
    }
    
    private void percolateDown(int index) {
        int minIndex = index;
        int childIndex;
        for (int i = 1; i <= NUM_CHILDREN; i++) {
            childIndex = NUM_CHILDREN * index + i;
            if (childIndex < heapSize && heap[childIndex].compareTo(heap[minIndex]) < 0) {
                // there is a child smaller than index
                minIndex = childIndex;
            }
        }
        
        // if we found a child smaller than index, swap them, then percolate down
        if (minIndex != index) {
            T temp = heap[index];
            heap[index] = heap[minIndex];
            heap[minIndex] = temp;
            percolateDown(minIndex);
        }
    }
    
    @Override
    public T peekMin() {
        if (heapSize == 0) {
            throw new EmptyContainerException();
        }
        return heap[0];
    }
    
    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        // first check if we need to resize
        if (heapSize == heap.length) {
            T[] resized = makeArrayOfT(heapSize * 2);
            for (int i = 0; i < heapSize; i++) {
                resized[i] = heap[i];
            }
            heap = resized;
            
        }
        
        // add item to the end, then percolate up
        heap[heapSize] = item;
        percolateUp(heapSize);
        
        // recurse through the heap
        // if there is a parent
        // int child = size;
        // T parent = heap[(child - 1)/NUM_CHILDREN];
        // if (heap[parent] > heap[child])
        // T temp = heap[parent]
        // heap[parent] = heap[child]
        // heap[child] = parent
        // update child
        // recurse again
        
        heapSize++;
    }
    
    private void percolateUp(int index) {
        int parentIndex = (index - 1)/NUM_CHILDREN;
        if (heap[parentIndex].compareTo(heap[index]) > 0) {
            // parent greater than child, so swap
            T temp = heap[parentIndex];
            heap[parentIndex] = heap[index];
            heap[index] = temp;
            percolateUp(parentIndex);
        }
        
        
    }
    
    @Override
    public int size() {
        return heapSize;
    }
}
