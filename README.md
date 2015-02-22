### Project Starter using Factorie
This is a bootstrap starter project which other people
may find it useful. It comes with Factorie 1.11 version
of jar and set up to work “out of the box”.

### Why it is needed?
Tooling of Scala is not very good or I cannot seem to 
use `sbt` and want to use Factorie for a number of projects
and found set up of the library(with dependencies and database)
takes more than I’d like. So, I created a starter project and
thought other people could also find it useful.

### How do you get the Factorie jar in the `lib`
I followed the following steps:

#### Get the git repo from Github
- `git clone https://github.com/factorie/factorie.git`
- `cd factorie`

##### Installation via Maven
- `export MAVEN_OPTS="$MAVEN_OPTS -Xmx1g -XX:MaxPermSize=128m"`
- `mvn compile`
- `mvn install`

##### To create a self-contained `jar` file
- `mvn -Dmaven.test.skip=true package -Pjar-with-dependencies` 


