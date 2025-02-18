# Instagram Feed 📸  

## 📌 Project Overview  
This project is developed as part of the **CmpE 250: Data Structures and Algorithms** course at **Boğaziçi University (Fall 2025)**.  
The task is to develop a **Feed Manager** for the early version of Instagram, handling users, posts, likes, and feed generation efficiently.  

## 📖 Problem Statement  
Kevin Systrom, the founder of Instagram, has hired you to build a system that:  
- **Manages users** (create, follow, unfollow).  
- **Handles posts** (create, like, sort).  
- **Generates a personalized feed** based on user interactions.  
- **Efficiently finds the most liked posts** while keeping track of what users have already seen.  

### 🔍 Key Features:
- **User Management**: Create users, follow/unfollow users.  
- **Post System**: Create, like/unlike, and sort posts.  
- **Feed Generation**: Show top-liked posts from followed users.  
- **Efficient Algorithms**: Optimized feed sorting and retrieval.  

## 📂 Input Files  
The system processes a file containing sequential instructions:  
- `create_user <userId>` → Creates a new user.  
- `follow_user <userId1> <userId2>` → User1 follows User2.  
- `create_post <userId> <postId> <content>` → User creates a post.  
- `toggle_like <userId> <postId>` → User likes/unlikes a post.  
- `generate_feed <userId> <num>` → Generates a sorted feed.  

## 📝 Output Format  
All operations generate logs:  
- `Created user with Id <userId>.`  
- `user1 followed user2.`  
- `user1 liked post1.`  
- `Feed for user1:\nPost ID: post1, Author: user2, Likes: 3`  
- `No more posts available for user1.`  

## 💻 Implementation Details  
- Implemented in **Java**  
- Uses **Priority Queues & Hash Maps** for efficiency  
- Ensures **lexicographical sorting** for equal-like posts  

## 🚀 Running the Project  
Compile and run with:  
```sh
javac *.java
java Main <input_file> <output_file>
