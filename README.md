Elastic Magpie
======

A basic twitter proxy implementation using scala, twitter4j and elasticsearch. This is just a proof of concept

Add a application.properties file to the classpath with your twitter credentials. You need to authenticate in order to
use the streaming api. You can use the default-application.properties file as an example.

The app uses a simple vagrant box with elasticsearch for data storage.

The app uses the twitter streaming api sample endpoint.