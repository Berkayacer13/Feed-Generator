public class Post {
    public String postId; // id
    public String authorId; // author  id
    public String content;// content od the post
    public CustomHashSet whoLiked; // to store  user ids who liked that post
    public CustomHashSet  whoSee; // to store user ids who saw that post

    // Constructor
    public Post(String authorId,String postId,String content){
        whoLiked = new CustomHashSet<>();
        whoSee = new CustomHashSet<>();
       this.postId = postId;
       this.authorId = authorId;
       this.content = content;
    }
}
