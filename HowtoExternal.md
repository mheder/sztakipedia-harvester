## Introduction ##

Sztakipedia harvester has two main components.
  * a standalone IRC reader that listens on wikipedia channels and downloads the modified articles from the Wikipedia API and posts it (HTTP POST) to a configured server+port.
  * An UIMA Consumer + Aggregate Analysis Engine. The Consumer listens on a HTTP port, waiting for the articles and the the Analysis Engine Processes the articles. However, with the Analysis Engine you have two options
    * Use Sztakipedia's AAE, that builds a Lucene index
    * Use the so-called "External Processor" Chain, in which the articles are pre-processed (links, sentences, categories, etc are recognized)and you provide your own java class that deals with the result - sends it somewhere or stores it your own way.

This Page describes how to get your own External Processor solution running

# System Requirements #
  * You will need a Sun (Oracle) Java 6 JRE
  * approx. 100 MB for the sztakipedia-harvester software
  * storage space for the data. The amount you need largely depends on the following
    * Which Wikipedias you plan to harvest?
    * What do you want to do with the articles you downloaded? For example, if you are harvesting the English Wikipedia, and use the (rather verbose) sztakipedia indexes, you will need more, than 50G for the indexes. However, you can write your own External Processor and do whatever you want with the pre-processed articles.
  * About 4G of RAM and at least 2 cores dedicated for the harvester. Again, largely depends on the mode of usage

# Steps #
## Download Stuff ##
  * ''pedia\_uima\_harvester\_rel-X.tar.gz'' (from this project, see downloads section)
  * ''example.external.proc.tar.gz'' (from this project, see downloads section)
  * ''uima-as-2.3.1-bin.tar.gz'' (http://uima.apache.org/ , Downloads section, look for: UIMA AS Asynchronous Scaleout)


## Writing your External Processor ##
  * extract ''example.external.proc.tar.gz'' which is an eclipse project. It contains one example java file you can customize
  * In order to compile, you will need ''pedia.uima.harvester.jar'' which is in ''pedia\_uima\_harvester\_rel-X.tar.gz'' (other common jars might be necessary). Also, you will need UIMA jars that you can find in ''uima-as-2.3.1-bin.tar.gz'', from which you can create a user library in eclipse if you wish
  * In the example code you will see what kind of annotations you can work with. These are coming from simple pre-processing and the sztakipedia-parser.
  * produce a jar file for your code. It will go into the ''pedia\_uima\_harvester'' directory (see below) so that the starter script puts it on the classpath (also see below)

## Start the UIMA part ##
  * Create a directory where uima and the harvester software will be deployed. In this tutorial this will be ''/tmp/uimahome''
  * extract the contents of ''pedia\_uima\_harvester\_rel-X.tar.gz'' and ''uima-as-2.3.1-bin.tar.gz'' so you will have ''apache-uima-as-2.3.1'' and ''pedia\_uima\_harvester'' directories under uimahome
  * put your jar your jar with your external processor to the ''pedia\_uima\_harvester'' directory
  * export UIMA\_HOME variable
```
username@hostname:/tmp/uimahome$export UIMA_HOME=/tmp/uimahome
```
### Configure your External Processor ###
  * Edit '''descriptors/CPE/HTTPCR\_parser\_wst\_category\_externalConsumer\_CPE.xml''' which is the UIMA descriptor for the Engine we will be using.
  * Locate this element
```
 <casProcessor deployment="integrated" name="ExternalConsumerAE">
```
> This is about your custom External processor. You should change '''externalProcessorClassname''' and '''externalProcessorParams'''
  * additionally, if you look around in this XML you will find the configuration for the consumer queue size, max. request size and the port where the consumer's built-in jetty listens

### Start the UIMA Part ###
```
./run_httpcr_pwstcat_extproc.sh /tmp/harvest 1
```

Where
  * /tmp/harvest is a work directory where UIMA creates logs, etc.
  * 1 is the number of threads this UIMA instance uses

This is what you should see on the console
```
2012-06-29 11:13:08.134:INFO:oejs.Server:jetty-7.6.4.v20120524
2012-06-29 11:13:08.165:INFO:oejsh.ContextHandler:started o.e.j.s.h.ContextHandler{/,file:/tmp/uimahome/pedia_uima_harvester/}
2012-06-29 11:13:08.215:INFO:oejs.AbstractConnector:Started SelectChannelConnector@0.0.0.0:8080
```
The UIMA part is now waiting for the articles.

System.out will go into the log /tmp/harvest/logs/cpe/en/cpe-HTTP-External-29.06.12-1321\_tc1.log

You should see something like this
```
Parsing CPE Descriptor
Instantiating CPE
Init params:
/a/b/c/d
Running CPE
0    [main] INFO  hu.sztaki.pedia.uima.reader.HTTPCollectionReader  - Jetty server is about to start on port: 8080, maxFormSize: 10000000
To abort processing, type "abort" and press enter.
CPM Initialization Complete
```

## Configure the irc reader ##
  * Review and modify '''resources/articlefilter/redirects.list''' which contains how the redirects are called
  * Review and modify '''resources/articlefilter/nonarticle\_titles\_prefixes.list''' that specifies which prefixes are to be filtered out
  * Create a user in the Wikipedia you want to harvest in order to get access to the API
  * Think about a string identifier that is assigned to this IRC bot

## Start the IRC reader ##
```
java -cp pedia.uima.harvester.jar hu.sztaki.pedia.uima.reader.standalone.IRCReader en.wikipedia localhost 8080 enwikipediaorgtest en resources/articlefilter/redirects.list resources/articlefilter/nonarticle_titles_prefixes.list YourWikiUser YourWikiPassword
```
Where
  * en.wikipedia : the name of the wiki IRC channel this bot will be monitoring
  * localhost : server name where the downloaded articles will be sent
  * 8080 : server port
  * en : language tag
  * resources/articlefilter/redirects.list : redirect tag names
  * resources/articlefilter/nonarticle\_titles\_prefixes.list : prefixes to filter out
  * YourWikiUser, YourWikiPassword : wiki user and password

'' Note : you can start multiple IRC readers that push to the same UIMA consumer''

### Monitor what is going on ###

#### IRC reader ####

```
21608 [Thread-57] INFO  BackgroundWorkerThread  - ACCEPTED: Kirk Wong(ID:13955960)
Article Vagbhatananda Gurudevar(Rev:499883711), accepted. Queue util:1 items, 999 remaining.
22035 [Thread-58] INFO  BackgroundWorkerThread  - ACCEPTED: Vagbhatananda Gurudevar(ID:13227099)
22374 [class org.jibble.pircbot.InputThread-Thread] INFO  hu.sztaki.pedia.uima.reader.util.SztakipediaBot  - Articles received:56, sent:40, filtered:15 Articles in progress:1 Curr. running threads:0
22693 [Thread-59] INFO  BackgroundWorkerThread  - FILTERED: Sri Lanka civil war(null)
```
  * Articles can be ACCEPTED or FILTERED
  * ''Queue util'' means queue utilization
  * Articles received: how many articles were designated as modified on the IRC
  * Articles sent: articles sent for processing
  * Articles filtered: articles filtered out
  * Articles in progress: articles that are being donwloaded or are unprocessed otherwise.

#### UIMA Part ####
```
Processed article:3112247, Radical Entertainment, 499893417, enwikipediaorgtest, en
Annotations-> Links:93 categories:9 sentences:61 unique tokens:1928
```

This is the output of the exampe external processor provided in this example when processing an article

##### Errors #####
```
2012-07-02 16:33:39.310:WARN:oejs.AbstractHttpConnection:http://www.baidu.com/
java.lang.NullPointerException
```

This means that your server has a public ip address some web crawler script has found your open port and made a request. Firewalling/nat is required.