package net.gluonporridge.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Mention {

    @Id
    private long id;
    @Column(columnDefinition="TEXT")
    private String acct;
    @Column(columnDefinition="TEXT")
    private String url;
    @Column(columnDefinition="TEXT")
    private String username;

    public Mention() {

    }

    public Mention(com.sys1yagi.mastodon4j.api.entity.Mention mention) {
        acct = mention.getAcct();
        id = mention.getId();
        url = mention.getUrl();
        username = mention.getUsername();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mention mention = (Mention) o;
        return Objects.equals(acct, mention.acct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(acct);
    }

    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.acct = acct;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
