/**
 * Data Access Object (DAO) package.
 * <p>
 * Contains classes responsible for all JDBC interaction with the MySQL database:
 * executing SQL queries/updates and mapping {@code ResultSet} rows to and from
 * domain model objects ({@link com.routeoptimizer.model.Node}, {@link com.routeoptimizer.model.Edge}).
 * This is the only package (besides {@code config}) that should import
 * {@code java.sql.*} classes. No graph or algorithm logic belongs here.
 */
package com.routeoptimizer.dao;