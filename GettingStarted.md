# System Requirements #
  * You will need a Sun (Oracle) Java 6 JRE
  * approx. 100 MB for the sztakipedia-harvester software
  * storage space for the data. The amount you need largely depends on the following
    * Which Wikipedias you plan to harvest?
    * What do you want to do with the articles you downloaded? For example, if you are harvesting the English Wikipedia, and use the (rather verbose) sztakipedia indexes, you will need more, than 50G for the indexes. However, you can write your own External Processor and do whatever you want with the pre-processed articles.
  * About 4G of RAM and at least 2 cores dedicated for the harvester. Again, largely depends on the mode of usage

# Download and Install #
In this description we distinguish between your developer machine (e.g. your laptop or desktop) and the server that will run the deployed sztakipedia-harvester. These might be the same, of course.

## Things to do on your developer machine ##
  * Download and install eclipse and UIMA
    * Eclipse (we recommend the latest release for Java EE developers)
    * Install the UIMA 2.3.1. plugin following the guide  http://uima.apache.org/d/uimaj-.3.1/overview_and_setup.html#ugr.ovv.eclipse_setup.
    * You will need both UIMA and UIMA AS (async scaleout). From AS the latest version is only 2.3.1. Because of that you can install 2.3.1 from the standard UIMA as well, although there is a 2.4 version. Un-check "Show only the latest versions of available software" (or similar) checkbox on the Eclipse installation page
    * Install SVN
  * Check out the sztakipedia-harvester project
    * for details, see: http://code.google.com/p/sztakipedia-harvester/source/checkout (check out trunk)
  * Modify build.xml dest.root property where you can give where the package is to be created.
  * right-click on build.xml Run As ... ant build
  * run deployAS script. This will upload the software to the server you will be using

# Configure #

  * configure port
  * configure requestSize
  * configure queueSize
  * LocalAbbrev: parameter for the parser
  * ExternalProcessorClassName: the class name of your external processor
  * ExternalProcessorParams: parameters passed to your external processor
  * Configure API user
  * Choose your descriptor
  * Modify filters (optional)
  * Write your own External Processor (optional)

# Start #
  * Starting the UIMA Aggregate Analysis Engine
```
   run_httpcr_pwstcat_extproc.sh (TODO: modifications)
```
  * Starting the IRC bot
```
   java -cp pedia.uima.harvester.jar hu.sztaki.pedia.uima.reader.standalone.IRCReader
```

# Inspect the logs to see if everything is ok #
```
24406 [class org.jibble.pircbot.InputThread-Thread] INFO  hu.sztaki.pedia.uima.reader.util.SztakipediaBot  - Articles received:40, sent:27, filtered:11 Articles in progress:2 Curr. running threads:1
```
# Shutting down #
```
abort
```