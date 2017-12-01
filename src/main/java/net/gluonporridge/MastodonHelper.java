package net.gluonporridge;

import com.sys1yagi.mastodon4j.api.entity.*;

import java.util.List;

public class MastodonHelper {
    static void printAccount(Account acc) {
//        id 	The ID of the account 	no
//        username 	The username of the account 	no
//        acct 	Equals username for local users, includes @domain for remote ones 	no
//        display_name 	The account's display name 	no
//        locked 	Boolean for when the account cannot be followed without waiting for approval first 	no
//        created_at 	The time the account was created 	no
//        followers_count 	The number of followers for the account 	no
//        following_count 	The number of accounts the given account is following 	no
//        statuses_count 	The number of statuses the account has made 	no
//        note 	Biography of user 	no
//        url 	URL of the user's profile page (can be remote) 	no
//        avatar 	URL to the avatar image 	no
//        avatar_static 	URL to the avatar static image (gif) 	no
//        header 	URL to the header image 	no
//        header_static 	URL to the header static image (gif) 	no
        StringBuilder sb = new StringBuilder();
        sb.append(acc.getId())
                .append(": ")
                .append(acc.getDisplayName())
                .append(" (")
                .append(acc.getAcct())
                .append(")\n")
                .append("Followers ")
                .append(acc.getFollowersCount())
                .append(" Following ")
                .append(acc.getFollowingCount())
                .append(" Toots ")
                .append(acc.getStatusesCount());

        System.out.println(sb.toString());
    }


    public static void printMentions(Status status) {
        List<Mention> mentions = status.getMentions();
        if (mentions.size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Mentions: ");
            for (Mention m : mentions) {
                sb.append(m.getAcct());
                sb.append(", ");
            }
            sb.delete(sb.length() - 2, sb.length() - 1);
            System.out.println(sb.toString());
        }
    }

    public static void printContent(Status status) {
        String content = status.getContent();
        // convert <br> tag to new line
        String cleaned = String.join("\n", content.split("<br>"));

        // unwrap tags
        cleaned = cleaned.replaceAll("<a href.+?#<span>(.*?)</span></a>", "#$1");
        cleaned = cleaned.replaceAll("#<span.+?<a href.+?>(.*?)</a></span>", "#$1");

        // unwrap users
        cleaned = cleaned.replaceAll("<span.+?<a href.+?@<span>(.*?)</span></a></span>", "@$1");

        // remove junk around links
        cleaned = cleaned.replaceAll("<a href=\"(.+?)\".*?</a>", "$1");

        System.out.println(cleaned);




//        // convert <p> into paragraphs
//        cleaned = cleaned.replaceAll("<p>(.*?)</p>(?!$)", "$1\n\n");
//        cleaned = cleaned.replaceAll("<p>(.*?)</p>", "$1");
//
//        // wrap after 80 char
//        //content = String.join("\n", content.split("(?<=\\G.{80})"));
//
//        System.out.println(cleaned);
    }

    public static void printTags(Status status) {
        List<Tag> tags = status.getTags();
        if (tags.size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Tags: ");
            for (Tag t : tags) {
                sb.append(t.getName());
                sb.append(", ");
            }
            sb.delete(sb.length() - 2, sb.length() - 1);
            System.out.println(sb.toString());
        }
    }

    public static void printAttachments(Status status) {
        List<Attachment> attachments = status.getMediaAttachments();
        if (attachments.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Attachment a : attachments) {
                sb.append("Attachment: ");
                sb.append(a.getUrl());
                sb.append("\n");
            }
            System.out.println(sb.toString());
        }
    }
}
