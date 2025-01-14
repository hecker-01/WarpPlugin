package net.heckerdev.warpplugin.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.heckerdev.warpplugin.WarpPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static net.heckerdev.warpplugin.WarpPlugin.getInstance;

public class DataSource {

    private static final HikariConfig config = new HikariConfig();
    private static HikariDataSource ds = null;

    private static void startTheDamnThing() {
        config.setDriverClassName("org.mariadb.jdbc.Driver");
        String ip = WarpPlugin.getInstance().getConfig().getString("database.ip");
        String port = WarpPlugin.getInstance().getConfig().getString("database.port");
        String database = WarpPlugin.getInstance().getConfig().getString("database.database-name", "WarpsDatabase");
        String username = WarpPlugin.getInstance().getConfig().getString("database.username");
        String password = WarpPlugin.getInstance().getConfig().getString("database.password");
        config.setJdbcUrl("jdbc:mariadb://" + ip + ":" + port + "/" + database);
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        ds = new HikariDataSource(config);
    }

    private DataSource() {}

    public static Connection getConnection() throws SQLException {
        if (ds == null) {
            startTheDamnThing();
        }
        return ds.getConnection();
    }

    public static void initializeDatabase(){
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS WarpsTable(ID INT AUTO_INCREMENT PRIMARY KEY NOT NULL, WarpName VARCHAR(30), WorldName VARCHAR(255), X DOUBLE, Y DOUBLE, Z DOUBLE, Yaw DOUBLE, Pitch DOUBLE, Hidden BOOLEAN)");
            preparedStatement.executeQuery();

            connection.close();
        } catch (SQLException ex) {
            getInstance().getLogger().warning("Error creating table: " + ex);
        }
    }
}
