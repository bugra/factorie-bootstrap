package factorie

/**
 * Created by bugra on 11/13/14.
 *
 * Basic DBConnection which requires ServerName, name of the user
 * and password
 */
case class DBConnection(dbType: String, server: String, table: String, name: String, pw: String) {
  require(dbType != null, "DB Type parameter is null")
  require(server != null, "DB Server parameter is null")
  require(name != null, "DB (user) name parameter is null")
  require(pw != null, "DB pw parameter is null")

  def getConnectionString =
    "jdbc:%s://%s:3306/%s?user=%s&password=%s".format(dbType, server, table, name, pw)
}