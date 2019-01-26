Simple read only JDBC driver for OpenOffice/LibreOffice Base ODB files.

Works only with underlying HSQLDB, and not with (upcoming) Firebird database.

Driver is written in Kotlin, but should work with any JVM language.

```
Class.forName("de.debuglevel.odbjdbc.OdbDriver")
val database = "jdbc:odb://file=test.odb"

connection = DriverManager.getConnection(database)

val statement = connection.createStatement()
val resultset = statement.executeQuery("SELECT * FROM \"PUBLIC\".\"Positionen\"")

while (resultset.next()) {
    println(
        resultset.getString("id") +
            resultset.getString("positiontitle") +
            resultset.getString("description")
    )
}

connection.close()
```