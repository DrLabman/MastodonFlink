package net.gluonporridge.jpa;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Represents the application used to post a status.
 */
@Entity
public class Application {

    @Id
    @Column(columnDefinition="TEXT")
    private String name;
    @Column(columnDefinition="TEXT")
    @Nullable
    private String website;

    public Application() {

    }

    public Application(com.sys1yagi.mastodon4j.api.entity.Application application) {
        this.name = application.getName();
        this.website = application.getWebsite();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Application that = (Application) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(website, that.website);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, website);
    }
}
