package de.debuglevel.odbjdbc

import mu.KotlinLogging
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {
    Class.forName("de.debuglevel.odbjdbc.OdbDriver")

    val databasePath = File("test.odb").invariantSeparatorsPath

    var connection: Connection? = null
    val database = "jdbc:odb://file=$databasePath"

    val results = mutableListOf<String>()

    try {
        // Create database connection
        logger.debug { "Opening connection to $database..." }
        connection = DriverManager.getConnection(database)

        // Create and execute statement
        val statement = connection.createStatement()
        val resultset = statement.executeQuery("SELECT * FROM \"PUBLIC\".\"Positionen\"")

        while (resultset.next()) {
            logger.debug { "Reading next position from database..." }
            results.add(
                resultset.getString("id") +
                        resultset.getString("positiontitle") +
                        resultset.getString("description")
            )
        }

        resultset.close()
        statement.close()
    } catch (e: SQLException) {
        logger.error { e }
        throw e
    } finally {
        try {
            connection?.close()
        } catch (e: SQLException) {
            logger.error { e }
            throw e
        }
    }

    results.forEach { println(it) }
}