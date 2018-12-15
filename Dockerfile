FROM openjdk:10

RUN mkdir -p /usr/src/MastodonFlink

ADD target/MastodonFlink-1.0-SNAPSHOT.jar /usr/src/MastodonFlink/MastodonFlink.jar

ENTRYPOINT java -cp /usr/src/MastondonFlink/MastodonFlink.jar net.gluonporridge.LiveStream