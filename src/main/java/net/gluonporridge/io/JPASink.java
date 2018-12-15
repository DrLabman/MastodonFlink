package net.gluonporridge.io;

import net.gluonporridge.MastodonHelper;
import net.gluonporridge.jpa.Account;
import net.gluonporridge.jpa.Status;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

public class JPASink extends RichSinkFunction<Status> {
    private EntityManagerFactory sessionFactory;
    private EntityManager entityManager;

    public JPASink() {
        sessionFactory = Persistence.createEntityManagerFactory("net.gluonporridge.mastodon.jpa");
        entityManager = sessionFactory.createEntityManager();
    }

    @Override
    public void invoke(Status status) {
        System.out.println("=============");
        System.out.println(status.getCreatedAt());
        if (status.getApplication() != null) {
            System.out.println(status.getApplication());
        }
        Account account = status.getAccount();
        MastodonHelper.printAccount(account);

        if (status.getInReplyToId() != null) {
            System.out.println(String.format("In Reply To: %1d", status.getInReplyToId()));
        }

        MastodonHelper.printMentions(status);
        MastodonHelper.printContent(status);
        MastodonHelper.printTags(status);
        MastodonHelper.printAttachments(status);

        System.out.println("-------------");
        System.out.print("Saving...");

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(status);
            entityManager.getTransaction().commit();

            System.out.println("Done");
        } catch (PersistenceException pe) {
            System.err.println(pe.toString());
        } catch (IllegalStateException ese) {
            System.err.println(ese.toString());
        }
    }

    @Override
    public void close() throws Exception {
        super.close();

        entityManager.close();
    }
}

