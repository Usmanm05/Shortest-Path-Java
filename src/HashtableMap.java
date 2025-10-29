// Name: Usman Mohammed
// Email: umohammed2@wisc.edu

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class HashtableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {
    // Array of linked lists to store pairs
    private LinkedList<Pair>[] newTable = null;
    // The number of pairs
    private int size;


    // Constructor that takes in a capacity value and then initializes the hashtable with that specified capacity
    @SuppressWarnings("unchecked")
    public HashtableMap(int capacity) {
        newTable = (LinkedList<Pair>[]) new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            newTable[i] = new LinkedList<>();
        }
        size = 0;
    }

    // Constructor that takes in no argument and sets the capacity to be 64
    @SuppressWarnings("unchecked")
    public HashtableMap() {
        // Sets the capacity to be 64
        this(64);
    }

    /**
     * Adds a new key,value pair/mapping to this collection.
     * @param key the key of the key,value pair
     * @param value the value that key maps to
     * @throws IllegalArgumentException if key already maps to a value
     * @throws NullPointerException if key is null
     */
    @Override
    public void put(KeyType key, ValueType value) throws IllegalArgumentException {
        // Throws exception in the case that there's no key type to use
        if (key == null) {
            throw new NullPointerException();
        }
        // Calculates the index value based on the key's hashCode value and the length of the newTable
        int index = Math.abs(key.hashCode()) % newTable.length;
        LinkedList<Pair> keyValuePairs = newTable[index];

        //Traverses and checks the keyValuePairs for the pair that contains a duplicate
        for (Pair pair : keyValuePairs) {
            if (pair.key.equals(key)) {
                // If we find the key then we throw exception because we don't want duplicates
                throw new IllegalArgumentException();
            }
        }
        // Pair gets added to the keyValuePairs
        keyValuePairs.add(new Pair(key, value));
        // The amount of elements is incremented
        size++;

        //If the number of elements exceeds 80% of the table length we will resize and rehash
        if (size >= 0.8 * (newTable.length)) {
            // calls our private helper method to resize it
            resizeMethod();
        }
    }

    /**
     * Private helper method that will resize and rehash the table.
     */
    @SuppressWarnings("unchecked")
    private void resizeMethod() {
        // Labels the initial table as the oldTable
        LinkedList<Pair>[] oldTable = newTable;
        // Initializes it again with double the initial length
        newTable = (LinkedList<Pair>[]) new LinkedList[oldTable.length * 2];
        for (int i = 0; i < newTable.length; i++) {
            // Iterates in order to copy over all the elements
            newTable[i] = new LinkedList<>();
        }
        // Resets the size back to 0
        size = 0;

        for (LinkedList<Pair> keyValuePairs : oldTable) {
            for (Pair pair : keyValuePairs) {
                // Rehashes all key-value pairs
                put(pair.key, pair.value);
            }
        }
    }

    /**
     * Checks whether a key maps to a value in this collection.
     * @param key the key to check
     * @return true if the key maps to a value, and false is the
     *         key doesn't map to a value
     */
    @Override
    public boolean containsKey(KeyType key) {
        // If there's no key value then it's not contained in the map
        if (key == null) {
            return false;
        }
        // Initializes index value for the Hashtable
        int index = Math.abs(key.hashCode()) % newTable.length;
        // Makes linked list for hashtable
        LinkedList<Pair> keyValuePairs = newTable[index];

        //Traverses through the keyValuePairs and searches for the key value pair
        for (Pair pair : keyValuePairs) {
            // If key value is found then it's contained in the map
            if (pair.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the specific value that a key maps to.
     * @param key the key to look up
     * @return the value that key maps to
     * @throws NoSuchElementException when key is not stored in this
     *         collection
     */
    @Override
    public ValueType get(KeyType key) throws NoSuchElementException {
        // If key is null exception is thrown
        if (key == null) {
            throw new NoSuchElementException();
        }
        // Finds the index based on hash code and the length of the table created
        int index = Math.abs(key.hashCode()) % newTable.length;
        // Creates new linked list based on the table's index
        LinkedList<Pair> keyValuePairs = newTable[index];

        // Traverses the keyValuePairs and looks for the pair
        for (Pair pair : keyValuePairs) {
            if (pair.key.equals(key)) {
                // If found returns the value of the pair
                return pair.value;
            }
        }
        // Key is not found so exception is thrown
        throw new NoSuchElementException("Key not found within the map");
    }

    /**
     * Remove the mapping for a key from this collection.
     * @param key the key whose mapping to remove
     * @return the value that the removed key mapped to
     * @throws NoSuchElementException when key is not stored in this
     *         collection
     */
    @Override
    public ValueType remove(KeyType key) throws NoSuchElementException {
        // If key is null exception is thrown
        if (key == null) {
            throw new NoSuchElementException();
        }
        // Finds the index based on hash code and the length of the table created
        int index = Math.abs(key.hashCode()) % newTable.length;
        // Creates new linked list based on the table's index
        LinkedList<Pair> keyValuePairs = newTable[index];

        // Traverses the keyValuePairs
        for (Pair pair : keyValuePairs) {
            if (pair.key.equals(key)) {
                // Removes the element
                keyValuePairs.remove(pair);
                // size is decremented
                size--;
                // Returns value of pair
                return pair.value;
            }
        }
        // Exception thrown due to key not being found
        throw new NoSuchElementException("Key not found within the map");
    }

    /**
     * Removes all key,value pairs from this collection.
     */
    @Override
    public void clear() {
        // Traverse through the entire table and remove all the pairs
        for (int i = 0; i < newTable.length; i++) {
            newTable[i].clear();
        }
        // Resets size to 0
        size = 0;
    }

    /**
     * Retrieves the number of keys stored in this collection.
     * @return the number of keys stored in this collection
     */
    @Override
    public int getSize() {
        // Returns size value
        return size;
    }

    /**
     * Retrieves this collection's capacity.
     * @return the size of te underlying array for this collection
     */
    @Override
    public int getCapacity() {
        // Returns the capacity based on the table's length
        return newTable.length;
    }

    /**
     * Retrieves this collection's keys.
     *
     * @return a list of keys in the underlying array for this collection
     */
    public List<KeyType> getKeys() {
        // Creates a linked list to hold the keys from the array
        List<KeyType> keys = new LinkedList<>();

        for (LinkedList<Pair> keyValuePairs : newTable) {
            for (Pair pair : keyValuePairs) {
                // Nested for loop that adds the key from the pair to linked list created
                keys.add(pair.key);
            }
        }
        // Returns list of keys
        return keys;
    }


    // Inner class to store key-value pairs
    protected class Pair {
        // Key of the key-value pair
        public KeyType key;
        // Value of the key-value pair
        public ValueType value;

        // Constructor that will initialize the key and value
        public Pair(KeyType key, ValueType value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * Tests the put and get method to ensure they insert and retrieve
     * the key type correctly.
     */
    @Test
    public void testPuttersandGetters() {
        HashtableMap<String, Integer> map = new HashtableMap<>();
        map.put("seven", 7);
        map.put("twelve", 12);
        assertEquals(7, map.get("seven"));
        assertEquals(12, map.get("twelve"));
    }

    /**
     * Tests if the size method returns the correct size
     */
    @Test
    public void testSizeMethod() {
        HashtableMap<String, Integer> map = new HashtableMap<>();
        map.put("fourteen", 14);
        map.put("three", 3);
        assertEquals(map.getSize(), 2);
    }

    /**
     * Tests if the containsKey method checks if the map contains the
     * KeyType correctly.
     */
    @Test
    public void testContainsKey() {
        HashtableMap<String, Integer> map = new HashtableMap<>();
        map.put("thirteen", 13);
        assertFalse(map.containsKey("five"));
        assertTrue(map.containsKey("thirteen"));
    }

    /**
     * Tests if the remove method removes the KeyType from the map.
     */
    @Test
    public void testRemove() {
        HashtableMap<String, Integer> map = new HashtableMap<>();
        map.put("ninety", 90);
        map.remove("ninety");
        assertFalse(map.containsKey("ninety"));
    }

    /**
     * Tests if the clear method gets rid of all key,value pairs in the map.
     */
    @Test
    public void testClear() {
        HashtableMap<Integer, String> map = new HashtableMap<>(34);
        map.put(70, "seventy");
        map.put(11, "eleven");
        map.clear();
        assertFalse(map.containsKey(70));
        assertFalse(map.containsKey(11));
    }
}