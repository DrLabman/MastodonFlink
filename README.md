# Mastodon + Flink
Test project for using the Mastodon streaming API as a source in Apache Flink.

## Building Fat JAR
You need to have Maven installed or to use an IDE like IntelliJ IDEA

Run the maven _package_ lifecycle with the _build-jar_ profile enabled. 

```bash
mvn package -P build-local-jar
```

This will build a 'Fat JAR' containing all the dependencies which can then be used to test the pipeline.

If you have a Apache Flink cluster to deploy to you can use:

```base
mvn package -P build-jar
```

To create a JAR with only the dependencies required (no Flink dependencies).

## Testing
You need:

* Create a Mastodon account on an instance
* To create a development token (keep this secret)

```bash
java -jar  net.gluonporridge.LiveStream --instance "Name Of Instance" --accessToken "API Access Token Here"
```

This will start up a local Flink instance and run the pipeline.

It will connect to Mastodon and start streaming in real time.

By default the pipeline is set to use the Federated Timeline. New Toots will be shown as they are streamed in.

The output will look like:

'''
=============
2017-11-29T21:31:59.622Z
hackaday (hackaday@apoil.org) [14490]
RT @NeuroTinker: Our @kickstarter campaign is now live! NeuroBytes https://apoil.org/tags/neuron simulators and https://apoil.org/tags/curricula combine https://apoil.org/tags/neuroscience and…  source: https://twitter.com/hackaday/status/935984248430796800
Tags: neuroscience, curricula, neuron 
=============
```