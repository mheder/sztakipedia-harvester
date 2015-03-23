usage:
```
java hu.sztaki.pedia.uima.reader.standalone.IRCReader <en.wikipedia> <destinationHostname> <destinationPort> <applicationName> <language> <redirectsFilePath> <nonArticleTitlesFilePath> [<API user>] [<API password>] 
```
where
  * en.wikipedia : the wiki channel name
  * destionationHostname, destinationPort : where the httpcollectionreader is listening
  * applicationName: a string tag you can specify
  * language: for wiki article metadata
  * redirectsFilePath: patterns to recognize redirects
  * nonArticleTitlesFilePath: prefixes to filter out
  * API user, password: a registered wikipedia user's name and password