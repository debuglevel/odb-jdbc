Simple read-only JDBC driver for OpenOffice/LibreOffice Base ODB files.

OpenOffice Base (.odb) files are actually a .zip archive containing a [HSQLDB](http://hsqldb.org/) directory.
This driver extracts the .zip archive into a temporary directory, opens a JDBC connection to the HSQLDB and forwards all requests to the HSQLDB driver.

The driver does not prohibit the user to perform UPDATE/INSERT statements, but any changes will simply get lost.

# Example usage in Kotlin
Although the driver is written in Kotlin, it should work with any JVM language.
```
Class.forName("de.debuglevel.odbjdbc.OdbDriver")
val database = "jdbc:odb://file=test.odb"

val connection = DriverManager.getConnection(database)

val statement = connection.createStatement()
val resultset = statement.executeQuery("SELECT * FROM \"PUBLIC\".\"Positionen\"")

while (resultset.next()) {
    println(
        resultset.getString("id")
    )
}

connection.close()
```
