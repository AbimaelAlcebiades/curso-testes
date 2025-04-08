package br.com.abimael.cursotestes.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static java.lang.String.join;
import static java.sql.DriverManager.getConnection;
import static org.awaitility.Awaitility.await;

public class DatabaseUtils {

  private static final ObjectMapper objectMapperSnakeCase =
          new ObjectMapper()
                  .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
                  .setPropertyNamingStrategy(SnakeCaseStrategy.INSTANCE)
                  .registerModule(new JavaTimeModule())
                  .disable(WRITE_DATES_AS_TIMESTAMPS);
  private static String jdbcUrl;
  private static String user;
  private static String password;

  public static void setConnectionConfigs(String jdbcUrl, String user, String password) {
    DatabaseUtils.jdbcUrl = jdbcUrl;
    DatabaseUtils.user = user;
    DatabaseUtils.password = password;
  }

  private static Connection buildConnection() throws SQLException {
    return getConnection(jdbcUrl, user, password);
  }

  @SneakyThrows
  public static void cleanDataBase() {
    try {
      truncateAllTables();
      resetAllSequences();
    } catch (Exception e) {
      throw new RuntimeException("Failed to clean database", e);
    }
  }

  @SneakyThrows
  private static void truncateAllTables() {
    try (Connection connection = buildConnection();
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(getQueryAllTables(getSchemas()))) {

      List<String> tableNames = new ArrayList<>();
      while (resultSet.next()) {
        tableNames.add(resultSet.getString(1));
      }

      if (!tableNames.isEmpty()) {
        String truncateQuery =
                "TRUNCATE TABLE " + join(", ", tableNames) + " RESTART IDENTITY CASCADE";
        statement.execute(truncateQuery);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static String getQueryAllTables(List<String> schemas) {
    return "SELECT table_schema || '.' || table_name "
            + "FROM information_schema.tables "
            + "WHERE table_schema IN ('"
            + join("', '", schemas)
            + "') "
            + "AND table_type = 'BASE TABLE'";
  }

  private static List<String> getSchemas() {
    return List.of("cursotestes");
  }

  @SneakyThrows
  private static void resetAllSequences() {
    try (Connection connection = buildConnection();
         Statement statement = connection.createStatement()) {
      for (String sequenceName : getAllSequenceNames(connection)) {
        statement.execute("ALTER SEQUENCE " + sequenceName + " RESTART WITH 1;");
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @SneakyThrows
  public static List<String> getAllSequenceNames(Connection connection) {
    List<String> sequenceNames = new ArrayList<>();
    List<String> schemas = getSchemas();

    String query =
            "SELECT sequence_schema || '.' || sequence_name "
                    + "FROM information_schema.sequences "
                    + "WHERE sequence_schema IN ('"
                    + join("', '", schemas)
                    + "')";

    try (Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(query)) {
      while (resultSet.next()) {
        sequenceNames.add(resultSet.getString(1));
      }
    }
    return sequenceNames;
  }

  @SneakyThrows
  public static <T> List<T> getAllDataFromTable(String schema, String tableName, Class<T> clazz) {

    List<Map<String, Object>> results = new ArrayList<>();
    String query = "SELECT * FROM " + schema + "." + tableName;

    try (Connection connection = buildConnection();
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(query)) {

      ResultSetMetaData metaData = resultSet.getMetaData();
      int columnCount = metaData.getColumnCount();

      while (resultSet.next()) {
        Map<String, Object> row = new HashMap<>();
        for (int i = 1; i <= columnCount; i++) {
          row.put(metaData.getColumnName(i), resultSet.getObject(i));
        }
        results.add(row);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return objectMapperSnakeCase.convertValue(
            results, objectMapperSnakeCase.getTypeFactory().constructCollectionType(List.class, clazz));
  }

  public static <T> void waitUntilRowCount(
          String schema, String table, int expectedNumberOfRows, Class<T> clazz) {
    await()
            .atMost(10, TimeUnit.SECONDS)
            .pollInterval(1, TimeUnit.SECONDS)
            .until(() -> getAllDataFromTable(schema, table, clazz).size() == expectedNumberOfRows);
  }
}
