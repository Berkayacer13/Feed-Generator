public class User {
    public String id;
    public CustomHashSet<String> followers; // to store  follower ids
    public CustomHashSet<String> followed; // to store followed ids
    public CustomHashSet<String> postSeen; // to store post ids that are seen
    public CustomHashSet<String> likedPosts; // to store liked posts ids
    public CustomHashSet<String> ownPosts; // to store user's own post ids

    // Constructor
    public User(String id) {
        this.id = id;
        followers = new CustomHashSet<>();
        followed = new CustomHashSet<>();
        postSeen = new CustomHashSet<>();
        likedPosts = new CustomHashSet<>();
        ownPosts = new CustomHashSet<>();
    }
}
