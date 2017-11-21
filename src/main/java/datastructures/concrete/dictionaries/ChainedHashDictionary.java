package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
import java.util.NoSuchElementException;

import java.util.Iterator;

/**
* See the spec and IDictionary for more details on what each method should do
*/
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    
    // You're encouraged to add extra fields (and helper methods) though!
    
    private int size; 		//number of elements in the hash table
    private int numBuckets; //number of buckets for storage in hash table
    
    private double loadFactor = 0.75;
    
    public ChainedHashDictionary() {
        this.chains = makeArrayOfChains(7);
        this.size = 0;
        this.numBuckets = 7;
    }
    
    /**
    * This method will return a new, empty array of the given size
    * that can contain IDictionary<K, V> objects.
    *
    * Note that each element in the array will initially be null.
    */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }
    
    @Override
    public V get(K key) {
        //get the hashcode for the key
        int hashCode;
        if (key == null) {
            hashCode = 0;
        } else {
            hashCode = Math.abs(key.hashCode() % numBuckets);
        }
        //get value
        if (chains[hashCode] == null || !chains[hashCode].containsKey(key)) {
            throw new NoSuchKeyException();
        }
        return chains[hashCode].get(key);
    }
    
    @Override
    public void put(K key, V value) {
        //create a hashcode for that KVPair
        int hashCode;
        if (key == null) {
            hashCode = 0;
        } else {
            hashCode = Math.abs(key.hashCode() % numBuckets);
        }
        //Attempt to insert it into the array using that hashcode
        
        
        //best case: no need to resize or create ArrayDictionary
        //use that ArrayDictionary's put method to store KVPair
        //update size if it's a new key
        
        if (chains[hashCode] == null) {
            chains[hashCode] = new ArrayDictionary<K, V>();
        }
        if (!chains[hashCode].containsKey(key)) {
            size++;
        }
        chains[hashCode].put(key, value);
        
        //Worst Case: Resize
        //check load factor
        if (size >= numBuckets * loadFactor) {
            numBuckets *= 2;
            IDictionary<K, V>[] newChain = makeArrayOfChains(numBuckets);
            //iterate through all array indexes and rehash elements using new array size
            
            
            //first iterate through chains array
            //then each ArrayDictionary
            //for every value, add it to the newChain
            for (int i = 0; i < chains.length; i++) {
                if (chains[i] != null) {
                    for (KVPair<K, V> temp : chains[i]) {
                        int newHashCode = Math.abs(temp.getKey().hashCode() % numBuckets);
                        if (newChain[newHashCode] == null) {
                            newChain[newHashCode] = new ArrayDictionary<K, V>();
                        }
                        newChain[newHashCode].put(temp.getKey(), temp.getValue());
                    }
                }
            }
            chains = newChain;
        }
    }
    
    @Override
    public V remove(K key) {
        //Get hashcode from key
        int hashCode;
        if (key == null) {
            hashCode = 0;
        } else {
            hashCode = Math.abs(key.hashCode() % numBuckets);
        }
        //use the ArrayDictionary remove method to remove the key
        if (chains[hashCode] == null || !chains[hashCode].containsKey(key)) {
            throw new NoSuchKeyException();
        }
        V value = chains[hashCode].remove(key);
        this.size--;
        return value;
    }
    
    @Override
    public boolean containsKey(K key) {
        //Get hashcode from key
        int hashCode;
        if (key == null) {
            hashCode = 0;
        } else {
            hashCode = Math.abs(key.hashCode() % numBuckets);
        }
        //use the ArrayDictionary containsKey() method to check
        return (chains[hashCode] != null && chains[hashCode].containsKey(key));
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }
    
    /**
    * Hints:
    *
    * 1. You should add extra fields to keep track of your iteration
    *    state. You can add as many fields as you want. If it helps,
    *    our reference implementation uses three (including the one we
    *    gave you).
    *
    * 2. Before you try and write code, try designing an algorithm
    *    using pencil and paper and run through a few examples by hand.
    *
    * 3. Think about what exactly your *invariants* are. An *invariant*
    *    is something that must *always* be true once the constructor is
    *    done setting up the class AND must *always* be true both before and
    *    after you call any method in your class.
    *
    *    Once you've decided, write them down in a comment somewhere to
    *    help you remember.
    *
    *    You may also find it useful to write a helper method that checks
    *    your invariants and throws an exception if they're violated.
    *    You can then call this helper method at the start and end of each
    *    method if you're running into issues while debugging.
    *
    *    (Be sure to delete this method once your iterator is fully working.)
    *
    * Implementation restrictions:
    *
    * 1. You **MAY NOT** create any new data structures. Iterators
    *    are meant to be lightweight and so should not be copying
    *    the data contained in your dictionary to some other data
    *    structure.
    *
    * 2. You **MAY** call the `.iterator()` method on each IDictionary
    *    instance inside your 'chains' array, however.
    */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int current;
        private Iterator<KVPair<K, V>> iter;
        
        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            this.current = 0;
            if (this.chains[current] != null) {
                this.iter = this.chains[current].iterator();
            }
        }
        
        @Override
        public boolean hasNext() {
            // If iterator is null or out of results
            if (iter == null || !iter.hasNext()) {
                // If we have nowhere left to go
                if (current + 1 == chains.length) {
                    return false;
                }
                // Move to next chain
                current++;
                
                // If the new chain is filled
                if (chains[current] != null) {
                    // Fill our iterator
                    iter = chains[current].iterator();
                } else {
                    // Otherwise null our iterator
                    iter = null;
                }
                // Run back through and check for a result
                return this.hasNext();
            }
            // We have a result
            return true;
        }
        
        @Override
        public KVPair<K, V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return iter.next();
        }
    }
}