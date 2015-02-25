package factorie

import scala.collection.mutable.ArrayBuffer

/*
  DB Object that is implemented as a singleton, which provides
  an interface to communicate to Database and provide the connection
  to other classes/objects
 */

object DB {
  import java.sql.{Connection, DriverManager}

  private var driverLoaded = false
  val DB_TYPE = "postgresql"
  val SERVER_NAME = ""
  val TABLE_NAME = ""
  val USER_NAME = ""
  val PASSWORD = ""

  val dbc: DBConnection = new DBConnection(DB_TYPE, SERVER_NAME, TABLE_NAME, USER_NAME, PASSWORD)

  private def loadDriver: Unit = {
    try {
      Class.forName("com.mysql.jdbc.Driver").newInstance
      driverLoaded = true
    } catch {
      case e: Exception => {
        println("ERROR: Driver not avaiable " + e.getMessage)
        throw e
      }
    }
  }

  /*
    Retrieves connection from MySQL
   */
  def getConnection: Connection = {
    // Only load the driver for the first time
    this.synchronized {
      if(! driverLoaded) loadDriver
    }

    // Get the connection
    try {
      DriverManager.getConnection(dbc.getConnectionString)
    } catch {
      case e: Exception => {
        println("ERROR: No connection!: " + e.getMessage)
        throw e
      }
    }
  }

  /*
  Retrieves one field from MySQL to populate
    a list from that field(as string)
   */
  def getListFromQueryField(sqlQuery: String, field: String) : List[String] = {
    val conn = getConnection
    val statement = conn.createStatement
    val resultSet = statement.executeQuery(sqlQuery)
    var arrayBuffer = new ArrayBuffer[String]
    while ( resultSet.next ) {
      val temp = resultSet.getString(field)
      arrayBuffer += temp
    }
    arrayBuffer.toList
  }

  def main(args: Array[String]) {

    val idInvestorList = getListFromQueryField("", "")
    println(idInvestorList)

    val conn = getConnection
    val statement = conn.createStatement

    val titleContentQuery = ""

    for (idInvestor <- idInvestorList) {
      val resultSet2 = statement.executeQuery(titleContentQuery.format(idInvestor))
      while ( resultSet2.next ) {
        val title = resultSet2.getString("title")
        val content = resultSet2.getString("content")
        println(title + " : " + content)
      }
    }

  }
}