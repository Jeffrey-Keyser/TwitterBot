import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.io.*;
import java.nio.charset.Charset;

public class TwitterAutoBot {

    public static void main(String[] args) {

        tweetLines();

    }

    private static void tweetLines() {
        String line;
        try {
            try (
                    // File reading initialization
                    InputStream fis = new FileInputStream("C:\\Users\\JEFFY'S PC\\IdeaProjects\\TwitterAutoBot\\src\\main\\resources\\tweets.txt");
                    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("Cp1252"));
                    BufferedReader br = new BufferedReader(isr);
            ) {
                while ((line = br.readLine()) != null) {    // Read text until there is a blank line
                    sendTweet(line);    // Pass the line to be tweeted
                    System.out.println("Tweeting: " + line + "...");

                    try {
                        // Tweet at a specified interval
                        System.out.println("Sleeping for an hour...");
                        Thread.sleep(3600000); // every hour
                        // Thread.sleep(10000); // every 10 seconds
                        // Thread.sleep(1800000); // every 30 minutes
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void sendTweet(String line) {
        // Use Twitter API functions to send out tweet
        Twitter twitter = TwitterFactory.getSingleton();
        Status status;
        try {
            status = twitter.updateStatus(line);
            System.out.println(status);
        } catch (TwitterException e) {;
            e.printStackTrace();
        }
    }

}