public class CustomHashMap<K, V> {
    // Entry class to hold key-value pairs
    private static class Entry<K, V> {
        K key; // Key
        V value; // Value
        Entry<K, V> next; // To handle collisions (linked list)

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Entry<K, V>[] buckets; // Array of buckets
    private int size; // Number of key-value pairs

    @SuppressWarnings("unchecked")
    public CustomHashMap() {
        this.buckets = new Entry[DEFAULT_CAPACITY];
        this.size = 0;
    }

    // Hash function to get the bucket index for a given key
    private int getBucketIndex(K key) {
        return Math.abs(key.hashCode()) % buckets.length;
    }

    // Put method: Adds a new key-value pair or updates an existing one
    public boolean put(K key, V value) {
        int index = getBucketIndex(key);
        Entry<K, V> head = buckets[index];

        // Check if key already exists
        while (head != null) {
            if (head.key.equals(key)) {
                head.value = value; // Update value
                return false; // Key already existed, no new entry
            }
            head = head.next;
        }

        // Insert new entry at the beginning of the linked list
        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = buckets[index];
        buckets[index] = newEntry;
        size++;

        // Resize if load factor exceeded
        if ((double) size / buckets.length > LOAD_FACTOR) {
            resize();
        }

        return true; // New key-value pair added
    }

    // Remove method: Removes a key-value pair by key
    public boolean remove(K key) {
        int index = getBucketIndex(key);
        Entry<K, V> head = buckets[index];
        Entry<K, V> prev = null;

        while (head != null) {
            if (head.key.equals(key)) {
                if (prev == null) {
                    buckets[index] = head.next; // Head node needs to be removed
                } else {
                    prev.next = head.next; // Bypass the current node
                }
                size--;
                return true; // Key-value pair removed
            }
            prev = head;
            head = head.next;
        }

        return false; // Key not found
    }

    // Get method: Retrieves the value associated with the key
    public V get(K key) {
        int index = getBucketIndex(key);
        Entry<K, V> head = buckets[index];

        while (head != null) {
            if (head.key.equals(key)) {
                return head.value; // Return the value associated with the key
            }
            head = head.next;
        }

        return null; // Key not found
    }

    // Resize method: Doubles the bucket array size and rehashes the entries
    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K, V>[] oldBuckets = buckets; // Save old buckets
        buckets = new Entry[oldBuckets.length * 2]; // Double the size
        size = 0; // Reset size

        for (Entry<K, V> head : oldBuckets) { // Rehash all existing entries
            while (head != null) {
                put(head.key, head.value); // Insert into new bucket array
                head = head.next;
            }
        }
    }

    // Method to get the current size of the map
    public int size() {
        return size;
    }
}
