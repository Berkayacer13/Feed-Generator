import java.util.ArrayList;

public class CustomHeap {
    private ArrayList<Post> heap; // ArrayList to hold heap elements

    // Constructor
    public CustomHeap() {
        this.heap = new ArrayList<>();
    }

    // Insert a new post into the heap
    public void insert(Post post) {
        heap.add(post);           // Add the new post at the end
        siftUp(heap.size() - 1);  // Adjust its position
    }

    // Remove and return the max (most liked post)
    public Post extractMax() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }

        Post max = heap.get(0);                     // Root element is the max
        Post last = heap.remove(heap.size() - 1);   // Remove the last element

        if (!heap.isEmpty()) {
            heap.set(0, last); // Move the last element to the root
            siftDown(0);      // Adjust the heap
        }

        return max;
    }

    // Peek at the max element without removing it
    public Post peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }
        return heap.get(0);
    }

    // Check if the heap is empty
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    // Get the size of the heap
    public int getSize() {
        return heap.size();
    }

    // Sift up to maintain heap property
    private void siftUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;

            // Compare with parent
            if (comparePosts(heap.get(index), heap.get(parentIndex)) > 0) {
                swap(index, parentIndex);
                index = parentIndex; // Move up to the parent's index
            } else {
                break;
            }
        }
    }

    // Sift down to maintain heap property
    private void siftDown(int index) {
        int leftChild;
        int rightChild;
        int largest = index;

        while (true) {
            leftChild = 2 * index + 1;
            rightChild = 2 * index + 2;

            // Compare with left child
            if (leftChild < heap.size() &&
                    comparePosts(heap.get(leftChild), heap.get(largest)) > 0) {
                largest = leftChild;
            }

            // Compare with right child
            if (rightChild < heap.size() &&
                    comparePosts(heap.get(rightChild), heap.get(largest)) > 0) {
                largest = rightChild;
            }

            // If the largest is still the current node, stop
            if (largest == index) {
                break;
            }
            swap(index, largest);
            index = largest; // Move to the largest child
        }
    }

    // Swap two elements in the heap
    private void swap(int i, int j) {
        Post temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    // Custom comparator to compare two posts
    private int comparePosts(Post p1, Post p2) {
        // Compare by likes first
        int likesDiff = p1.whoLiked.size() - p2.whoLiked.size();
        if (likesDiff != 0) {
            return likesDiff; // Higher likes come first
        }

        // If likes are equal, compare lexicographically by post ID
        return p1.postId.compareTo(p2.postId); // Higher lexicographical order comes first
    }
}
