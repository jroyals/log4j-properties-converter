# Log4J Properties Converter Tool #

I needed a tool to convert our multitude of log4j.properties to log4j.xml files. I could not find any such tools in the public space, so have created one. Free for you to enjoy ;-)

There are two ways this tool can be used.

## Browser Based Tool ##
This tool is exposed as a web application which you may use without downloading anything at all.

Head over to http://log4j-props2xml.appspot.com/ to use the Log4J converter tool right now. (URL updated 31 March 2012).

## Command-line tool ##
If you have a lot of files to convert, a command-line tool may be more appropriate for you.

**Java 1.5 or better** is required to use the command-line tool. If you have the java/bin directory on the system path, or an environment variable JAVA\_HOME set, you are ready to go.

**How to use**
  1. Download the tar.gz or zip distribution. Once you've got it, unzip/tar you some location on your hard drive.
  1. From a command line, run
```
log4j-converter -f <path-to-log4j.properties>
```

If all goes well, a corresponding log4j.xml file will appear.