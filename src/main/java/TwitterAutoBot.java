import twitter4j.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

public class TwitterAutoBot {

    static ResponseList<Status> posterStatus;


    public static void main(String[] args) {


        // INFINTE LOOP
        while (true) {
            searchTwitter("retweet to enter");
        }


      //  tweetLines();     If I want this to be a tweeting bot, call this function.

    }

    private static void searchTwitter(String searchQuery) {

        Twitter twitter = new TwitterFactory().getInstance();

        try {
            {
                Query query = new Query(searchQuery);
                QueryResult result;

                result = twitter.search(query);
                List<Status> tweets = result.getTweets();
                for (Status tweet : tweets) {
                    System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());

                    String originalTweeter = findOriginalTweeter(tweet.getText()); // Returns the original tweeter
                    System.out.println("TIMELINE STARTS");


                    ResponseList<Status> usersTimeline = twitter.getUserTimeline(originalTweeter); // Retrieve list of persons tweets

                    Iterator<Status> itr = usersTimeline.iterator(); // Create an iterator for all their tweets.

                    // Maybe convert to function
                    String theTweetMessage = tweet.getText().substring(returnStartIndexOfTweet(tweet.getText()), returnStartIndexOfTweet(tweet.getText()) + 4);
                    System.out.println(theTweetMessage);
                    Status theStatus = null;
                    long messageID = 0;
                    while (itr.hasNext()) {
                        theStatus = itr.next();
                        String potentialTweetMessage = theStatus.getText().substring(0, 4);
                        System.out.println(potentialTweetMessage);

                        if (potentialTweetMessage.trim().equalsIgnoreCase(theTweetMessage.trim())) {

                            messageID = theStatus.getId(); // Store the messageID
                            break;
                        }


                    }


                    try {
                        // Perform Like, Favorite, Follow
                        twitter.retweetStatus(messageID);
                        twitter.createFriendship(originalTweeter);


                        // Reply to Comment
                        Status status = twitter.showStatus(messageID);
                        Status reply = twitter.updateStatus(new StatusUpdate(" @" + status.getUser().getScreenName() + " @HummaMumma_ ").inReplyToStatusId(status.getId()));


                        System.out.println(messageID);


                        Thread.sleep(600000); // every 10 mins

                 //       twitter.createFavorite(messageID); // Favorite Tweet



                    }
                    catch (TwitterException e)
                    {   }



                }
            }

            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
        catch (InterruptedException e)
        {   }

    }


    private static int returnStartIndexOfTweet(String theTweet)
    {

        int startIndex = 0;
        int lastIndex = 0;

        for (int i = 0; i < theTweet.length()/2 - 3; i++)   // Only using to find first RT
        {
            int j = i + 2;
            if (theTweet.substring(i, j).equals("RT"))
            {
                // We know i's index is equal to the R in "RT @...."

                i = i + 3;
                j = i;

                while (theTweet.charAt(j) != ':')
                    j++;


                System.out.println(theTweet.substring(i,j)); // Prints out the User.

                startIndex = i;
                lastIndex = j;



                break;

            }
        }

        return lastIndex + 2;

    }


    // Find and returns the original tweeter by appending RT and using the @....
    // EX. "RT @Coalition124 . . . "
    // Function returns "@Coalition124"
    private static String findOriginalTweeter(String theTweet)
    {

        int startIndex = 0;
        int lastIndex = 0;

        for (int i = 0; i < theTweet.length()/2 - 3; i++)   // Only using to find first RT
        {
            int j = i + 2;
            if (theTweet.substring(i, j).equals("RT"))
            {
                // We know i's index is equal to the R in "RT @...."

                i = i + 3;
                j = i;

                while (theTweet.charAt(j) != ':')
                    j++;


                System.out.println(theTweet.substring(i,j)); // Prints out the User.

                startIndex = i;
                lastIndex = j;



                break;

            }
        }

        return theTweet.substring(startIndex, lastIndex);

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