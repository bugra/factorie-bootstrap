## Get the git repo from Github
- `git clone https://github.com/factorie/factorie.git`
- `cd factorie`

### Installation via Maven
- `export MAVEN_OPTS="$MAVEN_OPTS -Xmx1g -XX:MaxPermSize=128m"`
- `mvn compile`
- `mvn install`

#### To create a self-contained `jar` file
- `mvn -Dmaven.test.skip=true package -Pjar-with-dependencies` 

Then, using `jar` file in the `target` directory, one may use and import
the factorie package in a maven project as in the following.

