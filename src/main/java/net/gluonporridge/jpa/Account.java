package net.gluonporridge.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Represents an account which posted a status.
 */
@Entity
public class Account {

    @Id
    private long id;
    @Column(columnDefinition="TEXT")
    private String acct;
    @Column(columnDefinition="TEXT")
    private String avatar;
    @Column(columnDefinition="TEXT")
    private String createdAt;
    @Column(columnDefinition="TEXT")
    private String displayName;
    private int followersCount;
    private int followingCount;
    private int statusesCount;
    @Column(columnDefinition="TEXT")
    private String header;
    @Column(columnDefinition="TEXT")
    private String note;
    @Column(columnDefinition="TEXT")
    private String url;
    @Column(columnDefinition="TEXT")
    private String username;

    public Account() {
    }

    public Account(com.sys1yagi.mastodon4j.api.entity.Account account) {
        acct = account.getAcct();
        avatar = account.getAvatar();
        createdAt = account.getCreatedAt();
        displayName = account.getDisplayName();
        followersCount = account.getFollowersCount();
        followingCount = account.getFollowingCount();
        id = account.getId();
        statusesCount = account.getStatusesCount();
        header = account.getHeader();
        note = account.getNote();
        url = account.getUrl();
        username = account.getUserName();
    }

    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.acct = acct;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatusesCount() {
        return statusesCount;
    }

    public void setStatusesCount(int statusCount) {
        this.statusesCount = statusCount;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
