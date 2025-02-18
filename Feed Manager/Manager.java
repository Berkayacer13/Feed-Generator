
public class Manager {
    CustomHashSet<String> allUsersSet; // To store user IDs
    CustomHashMap<String, User> allUsersMap; // To store user details
    CustomHashSet<String> allPostsSet; // To store post IDs
    CustomHashMap<String, Post> allPostsMap; // To store post details

    // Constructor
    public Manager() {
        allUsersSet = new CustomHashSet<>();
        allUsersMap = new CustomHashMap<>();
        allPostsSet = new CustomHashSet<>();
        allPostsMap = new CustomHashMap<>();
    }

    // Create user
    public boolean createUser(String id) {
        if (allUsersSet.contains(id)) return false; // User already exists
        allUsersSet.add(id);
        allUsersMap.put(id, new User(id));
        return true;
    }

    // Follow method
    public boolean followUser(String user1id, String user2id) {
        if (!allUsersSet.contains(user1id) || !allUsersSet.contains(user2id)) {
            return false; // Either user does not exist
        }
        // Check if user is trying to follow themselves
        if (user1id.equals(user2id)) {
            return false; // Cannot follow itself
        }
        User user1 = allUsersMap.get(user1id);
        User user2 = allUsersMap.get(user2id);
        // Add user2 to user1's followed set and user1 to user2's followers set
        if (!user1.followed.contains(user2id)) {
            user1.followed.add(user2id);
            user2.followers.add(user1id);
            return true;
        }
        return false; // Already following
    }
    // Unfollow method
    public boolean unfollowUser(String user1id, String user2id) {
        if (!allUsersSet.contains(user1id) || !allUsersSet.contains(user2id)) {
            return false; // Either user does not exist
        }
        User user1 = allUsersMap.get(user1id);
        User user2 = allUsersMap.get(user2id);

        // Remove user2 from user1's followed set and user1 from user2's followers set
        if (user1.followed.contains(user2id)) {
            user1.followed.remove(user2id);
            user2.followers.remove(user1id);
            return true;
        }
        return false; // Not following
    }

    // Create post
    public boolean createPost(String userId, String postId, String content) {
        if (!allUsersSet.contains(userId) || allPostsSet.contains(postId)) {
            return false; // User doesn't exist or post already exists
        }
        Post newPost = new Post(userId, postId, content);
        allPostsSet.add(postId);
        allPostsMap.put(postId, newPost);
        User author = allUsersMap.get(userId);
        author.ownPosts.add(postId);
        return true; // Post created successfully
    }

    public boolean seePost(String userId, String postId) {
        if (!allUsersSet.contains(userId) || !allPostsSet.contains(postId)) {
            return false;
        }
        User user = allUsersMap.get(userId);
        Post post = allPostsMap.get(postId);
        user.postSeen.add(postId);
        post.whoSee.add(userId);
        return true;
    }

    public boolean seeAllPosts(String viewerId, String viewedId) {
        if (!allUsersSet.contains(viewerId) || !allUsersSet.contains(viewedId)) {
            return false; // Either viewer or viewed user doesn't exist
        }
        User viewed = allUsersMap.get(viewedId);
        // Traverse all posts of the viewed user
        for (String postId : viewed.ownPosts) {
            // Mark the post as seen by the viewer
            seePost(viewerId, postId);
        }
        return true; // Successfully viewed all posts
    }

    public String toggleLike(String userId, String postId) {
        if (!allUsersSet.contains(userId) || !allPostsSet.contains(postId)) {
            return "Some error occurred in toggle_like."; // User or post doesn't exist
        }

        User user = allUsersMap.get(userId);
        Post post = allPostsMap.get(postId);

        // If the user hasn't liked the post, add like
        if (!user.likedPosts.contains(postId)) {
            user.likedPosts.add(postId);
            post.whoLiked.add(userId);
            seePost(userId, postId);  // Liking counts as seeing the post
            return userId + " liked " + postId + ".";
        }

        // If the user has already liked the post, remove like (unlike)
        else {
            user.likedPosts.remove(postId);
            post.whoLiked.remove(userId);
            return userId + " unliked " + postId+".";
        }
    }

    public String generateFeed(String userId, int num) {
        if (!allUsersSet.contains(userId)) {
            return "Some error occurred in generate_feed.";
        }

        CustomHeap feedHeap = new CustomHeap();
        User user = allUsersMap.get(userId);

        // Populate the heap with posts from followed users that have not been seen or liked
        for (String followedId : user.followed) {
            User followedUser = allUsersMap.get(followedId);
            for (String postId : followedUser.ownPosts) {
                // Check if the post is eligible for the feed
                if (!user.likedPosts.contains(postId) && !user.postSeen.contains(postId)) {
                    feedHeap.insert(allPostsMap.get(postId)); // Insert into heap
                }
            }
        }

        StringBuilder feedOutput = new StringBuilder("Feed for " + userId + ":\n");
        int count = 0;

        // Extract the top posts from the heap (most liked first)
        while (!feedHeap.isEmpty() && count < num) {
            Post post = feedHeap.extractMax(); // Get the most liked post
            feedOutput.append("Post ID: ").append(post.postId)
                    .append(", Author: ").append(post.authorId)
                    .append(", Likes: ").append(post.whoLiked.size())
                    .append("\n");
            count++;
        }
        // If fewer posts than requested, add a message
        if (count < num) {
            feedOutput.append("No more posts available for ").append(userId).append(".");
        }
        return feedOutput.toString();
    }


    public String scrollThroughFeed(String userId, int num, int[] likes) {
        if (!allUsersSet.contains(userId)) {
            return "Some error occurred in scroll_through_feed.";
        }

        CustomHeap feedHeap = new CustomHeap();
        User user = allUsersMap.get(userId);
        StringBuilder logOutput = new StringBuilder(userId + " is scrolling through feed:\n");
        int count = 0;

        // Populate the feed with posts from followed users that are not seen or liked
        for (String followedId : user.followed) {
            User followedUser = allUsersMap.get(followedId);
            for (String postId : followedUser.ownPosts) {
                // Check if the post is eligible for the feed
                if (!user.likedPosts.contains(postId) && !user.postSeen.contains(postId)) {
                    feedHeap.insert(allPostsMap.get(postId)); // Insert into feed heap
                }
            }
        }

        // Process posts in the heap
        while (!feedHeap.isEmpty() && count < num) {
            Post post = feedHeap.extractMax(); // Get the most liked post
            user.postSeen.add(post.postId); // Mark as seen

            // Check if the user liked the post
            if (likes[count] == 1) {
                // User liked the post
                user.likedPosts.add(post.postId);
                post.whoLiked.add(userId); // Add to post's whoLiked set
                logOutput.append(userId)
                        .append(" saw ")
                        .append(post.postId)
                        .append(" while scrolling and clicked the like button.\n");
            } else {
                // User did not like the post
                logOutput.append(userId)
                        .append(" saw ")
                        .append(post.postId)
                        .append(" while scrolling.\n");
            }
            count++;
        }

        // If not enough posts to satisfy num, log the message
        if (count < num) {
            if (logOutput.charAt(logOutput.length() - 1) == '\n') {
                logOutput.deleteCharAt(logOutput.length() - 1); // Remove the last newline
            }
            logOutput.append("\nNo more posts in feed.");
        } else {
            logOutput.deleteCharAt(logOutput.length() - 1); // Remove the last newline
        }

        return logOutput.toString();
    }
    public String sortPosts(String userId) {
        // Check if the user exists
        if (!allUsersSet.contains(userId)) {
            return "Some error occurred in sort_posts.";
        }

        User user = allUsersMap.get(userId);

        // Check if the user has any posts
        if (user.ownPosts.size()==0) {
            return "No posts from " + userId + ".";
        }

        // Log the start of sorting
        StringBuilder result = new StringBuilder();
        result.append("Sorting ").append(userId).append("'s posts:\n");

        // Create a CustomHeap for the user's posts
        CustomHeap allPostsHeap = new CustomHeap();

        // Insert all posts of the user into the heap
        for (String postId : user.ownPosts) {
            allPostsHeap.insert(allPostsMap.get(postId));
        }

        // Extract posts from the heap and append to the result
        while (!allPostsHeap.isEmpty()) {
            Post post = allPostsHeap.extractMax();
            result.append(post.postId)
                    .append(", Likes: ")
                    .append(post.whoLiked.size())
                    .append("\n");
        }
        return result.toString().trim();
    }
}