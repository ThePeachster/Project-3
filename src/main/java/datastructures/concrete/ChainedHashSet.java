package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.ISet;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
* See ISet for more details on what each method is supposed to do.
*/
public class ChainedHashSet<T> implements ISet<T> {
    // This should be the only field you need
    private IDictionary<T, Boolean> map;
    
    public ChainedHashSet() {
        this.map = new ChainedHashDictionary<>();
    }
    
    // If the item already exists in the set, this method does nothing.
    @Override
    public void add(T item) {
        if (!map.containsKey(item)) {
            map.put(item, true);
        }
    }
    
    // @throws NoSuchElementException  if the set does not contain the given item
    @Override
    public void remove(T item) {
        if (!map.containsKey(item)) {
            throw new NoSuchElementException();
        }
        map.remove(item);
    }
    
    @Override
    public boolean contains(T item) {
        return map.containsKey(item);
    }
    
    @Override
    public int size() {
        return map.size();
    }
    
    @Override
    public Iterator<T> iterator() {
        return new SetIterator<>(this.map.iterator());
    }
    
    private static class SetIterator<T> implements Iterator<T> {
        // This should be the only field you need
        private Iterator<KVPair<T, Boolean>> iter;
        
        public SetIterator(Iterator<KVPair<T, Boolean>> iter) {
            this.iter = iter;
        }
        
        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }
        
        @Override
        public T next() {
            return iter.next().getKey();
        }
    }
}
