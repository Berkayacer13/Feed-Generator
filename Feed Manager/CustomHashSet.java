import java.util.Iterator;
import java.util.NoSuchElementException;

public class CustomHashSet<E> implements Iterable<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Object[] table;
    private int size;

    public CustomHashSet() {
        this.table = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    private int hashIndex(E element) {
        return (element.hashCode() & 0x7FFFFFFF) % table.length;
    }

    public boolean add(E element) {
        if (element == null) throw new IllegalArgumentException("null value");

        // Resize if load factor exceeded
        if ((double) size / table.length >= LOAD_FACTOR) {
            resize();
        }

        int index = hashIndex(element);
        while (table[index] != null) {
            if (table[index].equals(element)) {
                return false; // Element already exists
            }
            index = (index + 1) % table.length; // Wrap around
        }

        table[index] = element;
        size++;
        return true;
    }

    private void resize() {
        Object[] oldTable = table;
        table = new Object[oldTable.length * 2];
        size = 0;

        for (Object element : oldTable) {
            if (element != null) {
                add((E) element); // Re-add elements
            }
        }
    }

    public boolean remove(E element) {
        if (element == null) return false;

        int index = hashIndex(element);
        while (table[index] != null) {
            if (table[index].equals(element)) {
                table[index] = null; // Mark as deleted
                rehashFrom(index); // Rehash subsequent elements
                size--;
                return true;
            }
            index = (index + 1) % table.length; // Wrap around
        }
        return false;
    }

    private void rehashFrom(int start) {
        int index = (start + 1) % table.length;
        while (table[index] != null) {
            E element = (E) table[index];
            table[index] = null;
            size--; // Temporarily reduce size
            add(element); // Re-add to the set
            index = (index + 1) % table.length; // Wrap around
        }
    }

    public int size() {
        return size;
    }

    public boolean contains(E element) {
        if (element == null) return false;

        int index = hashIndex(element);
        while (table[index] != null) {
            if (table[index].equals(element)) {
                return true;
            }
            index = (index + 1) % table.length; // Wrap around
        }
        return false;
    }

    // Implementation of Iterable<E>
    @Override
    public Iterator<E> iterator() {
        return new CustomHashSetIterator();
    }

    // Inner class for iterator
    private class CustomHashSetIterator implements Iterator<E> {
        private int currentIndex = 0;
        private int elementsReturned = 0;

        @Override
        public boolean hasNext() {
            return elementsReturned < size;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            while (currentIndex < table.length) {
                if (table[currentIndex] != null) {
                    E element = (E) table[currentIndex];
                    currentIndex++;
                    elementsReturned++;
                    return element;
                }
                currentIndex++;
            }
            throw new NoSuchElementException("No more elements");
        }
    }
}
