#! /bin/sh
[ ${JAVA_HOME} ] && JAVA=${JAVA_HOME}/bin/java || JAVA=java

# Are we running within Cygwin on some version of Windows?
cygwin=false;
case "`uname -s`" in
    CYGWIN*) cygwin=true ;;
esac

LOG4J_CONVERTER_HOME=`dirname "$0"`

if $cygwin; then
    UNIX_STYLE_HOME=`cygpath "$LOG4J_CONVERTER_HOME"`
else
    UNIX_STYLE_HOME=$LOG4J_CONVERTER_HOME
fi

# Then add all library jars to the classpath.
IFS=""
for a in $UNIX_STYLE_HOME/lib/*; do
    TMP_CP="$TMP_CP":"$a";
done

# Now add the system classpath to the classpath. If running
# Cygwin we also need to change the classpath to Windows format.
if $cygwin; then
    TMP_CP=`cygpath -w -p $TMP_CP`
    TMP_CP=$TMP_CP';'$CLASSPATH
else
    TMP_CP=$TMP_CP:$CLASSPATH
fi

$JAVA -cp $TMP_CP com.fragstealers.log4j.ui.cli.CommandLineUI $1 $2 $3 $4 $5 $6 $7 $8 $9