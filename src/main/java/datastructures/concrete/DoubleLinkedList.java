// Yasmine Hejazi and Jason Peacher-Ton
// 10/4/17
// CSE 373 Autumn 17

package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
* Note: For more info on the expected behavior of your methods, see
* the source code for IList.
*/
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;
    
    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }
    
    @Override
    public void add(T item) {
        if (size == 0) {
            front = new Node<T>(item);
            back = front;
        } else {
            back.next = new Node<T>(back, item, null);
            back = back.next;
        }
        size++;
    }
    
    @Override
    public T remove() {
        if (size == 0) {
            throw new EmptyContainerException();
        }
        
        Node<T> removed;
        if (size == 1) {
            removed = front;
            front = null;
            back = null;
        } else {
            removed = back;
            back = back.prev;
            back.next = null;
        }
        size--;
        return removed.data;
    }
    
    @Override
    public T get(int index) {
        if (size == 0) {
            throw new EmptyContainerException();
        }
        
        return getNodeAtIndex(index).data;
    }
    
    @Override
    public void set(int index, T item) {
        if (size == 0) {
            // empty case
            throw new EmptyContainerException();
        }
        checkInBounds(index);
        
        if (size == 1) {
            // single element
            front = new Node<T>(item);
            back = front;
        } else if (index == 0) {
            // front case
            front = new Node<T>(null, item, front.next);
            front.next.prev = front;
        } else if (index == size - 1) {
            // back case
            back = new Node<T>(back.prev, item, null);
            back.prev.next = back;
        } else {
            // happy case
            Node<T> old = getNodeAtIndex(index);
            Node<T> setting = new Node<T>(old.prev, item, old.next);
            old.prev.next = setting;
            old.next.prev = setting;
        }
    }
    
    @Override
    public void insert(int index, T item) {
        if (size == 0) {
            // empty case
            if (index != 0) {
                throw new IndexOutOfBoundsException();
            }
            Node<T> node = new Node<T>(item);
            front = node;
            back = node;
        } else if (index == size) {
            // back case
            back.next = new Node<T>(back, item, null);
            back = back.next;
        } else if (index == 0) {
            // front case
            front.prev = new Node<T>(null, item, front);
            front = front.prev;
        } else {
            // happy case
            Node<T> old = getNodeAtIndex(index);
            old.prev.next = new Node<T>(old.prev, item, old);
            old.prev = old.prev.next;
        }
        size++;
    }
    
    @Override
    public T delete(int index) {
        if (size == 0) {
            throw new EmptyContainerException();
        }
        Node<T> deleting = getNodeAtIndex(index);
        if (size == 1) {
            front = null;
            back = null;
        } else {
            if (index == 0) {
                // front case
                deleting.next.prev = null;
                front = deleting.next;
            }
            else if (index < size - 1) {
                // happy case
                deleting.prev.next = deleting.next;
                deleting.next.prev = deleting.prev;
            }
            else {
                // back case
                deleting.prev.next = null;
                back = deleting.prev;
            }
        }
        size--;
        return deleting.data;
    }
    
    @Override
    public int indexOf(T item) {
        Node<T> current = front;
        for (int i = 0; i < size; i++) {
            if (item == null) {
                // checks null case
                if (current.data == null) {
                    return i;
                }
            }
            else if (item.equals(current.data)) {
                return i;
            }
            current = current.next;
        }
        return -1;
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public boolean contains(T other) {
        return indexOf(other) > -1;
    }
    
    // takes in an index as a parameter (exception if invalid index (negative or larger than size))
    // returns the node found in the index
    private Node<T> getNodeAtIndex(int index) {
        Node<T> current = null;
        checkInBounds(index);
        if (index < size / 2) {
            current = front;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = back;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }
    
    // takes in an index, throws exception if index is out of bounds
    private void checkInBounds(int index) {
        if (index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException();
        }
    }
    
    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }
    
    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;
        
        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
        
        public Node(E data) {
            this(null, data, null);
        }
        
        // Feel free to add additional constructors or methods to this class.
    }
    
    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;
        
        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }
        
        /**
        * Returns 'true' if the iterator still has elements to look at;
        * returns 'false' otherwise.
        */
        public boolean hasNext() {
            return (current != null);
        }
        
        /**
        * Returns the next item in the iteration and internally updates the
        * iterator to advance one element forward.
        *
        * @throws NoSuchElementException if we have reached the end of the iteration and
        *         there are no more elements to look at.
        */
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T data =  current.data;
            current = current.next;
            return data;
        }
    }
}