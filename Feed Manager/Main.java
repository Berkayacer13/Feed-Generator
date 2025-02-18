import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        // Read input and output file paths from command-line arguments
        String inputFilePath = args[0];
        String outputFilePath = args[1];

        Manager manager = new Manager();
        long startTime = System.currentTimeMillis();

        // File handling with try-with-resources for automatic resource management
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;
            // Read each line (command) from the input file
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String command = parts[0];
                String output = null;

                // Parse and execute each command based on the input format
                switch (command) {
                    case "create_user":
                        if (manager.createUser(parts[1])) {
                            output = "Created user with Id " + parts[1] + ".";
                        } else {
                            output = "Some error occurred in create_user.";
                        }
                        break;

                    case "follow_user":
                        if (manager.followUser(parts[1], parts[2])) {
                            output = parts[1] + " followed " + parts[2] + ".";
                        } else {
                            output = "Some error occurred in follow_user.";
                        }
                        break;

                    case "unfollow_user":
                        if (manager.unfollowUser(parts[1], parts[2])) {
                            output = parts[1] + " unfollowed " + parts[2] + ".";
                        } else {
                            output = "Some error occurred in unfollow_user.";
                        }
                        break;

                    case "create_post":
                        // Expecting format: create_post <userId> <postId> <content>
                        if (manager.createPost(parts[1], parts[2], parts[3])) {
                            output = parts[1] + " created a post with Id " + parts[2] + ".";
                        } else {
                            output = "Some error occurred in create_post.";
                        }
                        break;

                    case "see_post":
                        // Expecting format: see_post <userId> <postId>
                        if (manager.seePost(parts[1], parts[2])) {
                            output = parts[1] + " saw " + parts[2] + ".";
                        } else {
                            output = "Some error occurred in see_post.";
                        }
                        break;

                    case "see_all_posts_from_user":
                        // Expecting format: see_all_posts_from_user <viewerId> <viewedId>
                        if (manager.seeAllPosts(parts[1], parts[2])) {
                            output = parts[1] + " saw all posts of " + parts[2] + ".";
                        } else {
                            output = "Some error occurred in see_all_posts_from_user.";
                        }
                        break;

                    case "toggle_like":
                        // Expecting format: toggle_like <userId> <postId>
                        output = manager.toggleLike(parts[1], parts[2]);
                        break;

                    case "generate_feed":
                        // Expecting format: generate_feed <userId> <num>
                        try {
                            int num = Integer.parseInt(parts[2]);
                            output = manager.generateFeed(parts[1], num);
                        } catch (NumberFormatException e) {
                            output = "Some error occurred in generate_feed.";
                        }
                        break;

                    case "scroll_through_feed":
                        // Expecting format: scroll_through_feed <userId> <num> <like1> <like2> ...
                        try {
                            String userId = parts[1];
                            int num = Integer.parseInt(parts[2]);
                            int[] likesArray = new int[num];
                            for (int i = 0; i < num; i++) {
                                likesArray[i] = Integer.parseInt(parts[3 + i]);
                            }
                            output = manager.scrollThroughFeed(userId, num, likesArray);
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            output = "Some error occurred in scroll_through_feed.";
                        }
                        break;

                    case "sort_posts":
                        // Expecting format: sort_posts <userId>
                        output = manager.sortPosts(parts[1]);
                        break;

                    default:
                        output = "Invalid command: " + command;
                }
                // Write the output to the log file
                if (output != null) {
                    writer.write(output);
                    writer.newLine();
                }
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Execution Time: " + (endTime - startTime) + "ms");
    }
}
