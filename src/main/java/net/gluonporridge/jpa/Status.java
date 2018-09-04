package net.gluonporridge.jpa;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQuery(name="GetLatest",query="Select s From Status s Order By createdAt asc")
public class Status {

    @Id
    private long id;
    @Column(columnDefinition="TEXT")
    private String uri;
    @Column(columnDefinition="TEXT")
    @Nullable
    private String url;
    @Column(columnDefinition="TEXT")
    private String content;
    @Column(columnDefinition="TEXT")
    private String spoilerText;
    @Column(columnDefinition="TEXT")
    private String visibility;
    @Column(columnDefinition="TEXT")
    private String createdAt;
    private int favouritesCount;
    private int reblogsCount;
    @Nullable
    private Long inReplyToId;
    @Nullable
    private Long inReplyToAccountId;

    @ManyToOne(cascade = {CascadeType.ALL})
    private Account account;
    @ManyToOne(cascade = {CascadeType.ALL})
    private Application application;
    @ManyToOne(cascade = {CascadeType.ALL})
    @Nullable
    private Status reblog;
    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Tag> tags;
    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Mention> mentions;
    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Attachment> attachments;
    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Emoji> emojis;

    public Status() {

    }

    public Status(com.sys1yagi.mastodon4j.api.entity.Status status) {
        id = status.getId();
        url = status.getUrl();
        uri = status.getUri();

        content = status.getContent();
        spoilerText = status.getSpoilerText();
        visibility = status.getVisibility();

        createdAt = status.getCreatedAt();
        favouritesCount = status.getFavouritesCount();
        reblogsCount = status.getReblogsCount();

        inReplyToId = status.getInReplyToId();
        inReplyToAccountId = status.getInReplyToAccountId();

        if (status.getReblog() != null) {
            reblog = new Status(status.getReblog());
        }

        account = new Account(status.getAccount());

        if (status.getApplication() != null) {
            application = new Application(status.getApplication());
        }

        tags = new ArrayList<>();
        for (com.sys1yagi.mastodon4j.api.entity.Tag tag : status.getTags()) {
            tags.add(new Tag(tag));
        }

        mentions = new ArrayList<>();
        for (com.sys1yagi.mastodon4j.api.entity.Mention mention : status.getMentions()) {
            mentions.add(new Mention(mention));
        }

        attachments = new ArrayList<>();
        for (com.sys1yagi.mastodon4j.api.entity.Attachment attachment : status.getMediaAttachments()) {
            attachments.add(new Attachment(attachment));
        }

        emojis = new ArrayList<>();
        for (com.sys1yagi.mastodon4j.api.entity.Emoji emoji : status.getEmojis()) {
            emojis.add(new Emoji(emoji));
        }
    }

    public List<Attachment> getMediaAttachments() {
        return attachments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return Objects.equals(uri, status.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSpoilerText() {
        return spoilerText;
    }

    public void setSpoilerText(String spoilerText) {
        this.spoilerText = spoilerText;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public void setFavouritesCount(int favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

    public int getReblogsCount() {
        return reblogsCount;
    }

    public void setReblogsCount(int reblogsCount) {
        this.reblogsCount = reblogsCount;
    }

    public Long getInReplyToId() {
        return inReplyToId;
    }

    public void setInReplyToId(Long inReplyToId) {
        this.inReplyToId = inReplyToId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Status getReblog() {
        return reblog;
    }

    public void setReblog(Status reblog) {
        this.reblog = reblog;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Mention> getMentions() {
        return mentions;
    }

    public void setMentions(List<Mention> mentions) {
        this.mentions = mentions;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public List<Emoji> getEmojis() {
        return emojis;
    }

    public void setEmojis(List<Emoji> emojis) {
        this.emojis = emojis;
    }
}
