/*
 * This class will be used to connect with Data Base
 */
package logic;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.activation.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author 2dam
 */
public class Pool {

    
    private final String BD = "greetingbd";
    private final String con = "jdbc:mysql://localhost:3306/greetingbd?zeroDateTimeBehavior=convertToNull";
    private final String DBUSER = "root";
    private final String DBPASS = "abcd*1234";
    private final String driver = "com.mysql.jdbc.driver";
    /**
     * Utilizaremos el parametro conexion para abrir o cerrar la conexion
     */
    private Connection conexion;
    private PreparedStatement stmt;
    /**
     * Utilizaremos el parametro pool para poder instanciar la clase cuando 
     * vallamos a conectarnos a la base de datos
     */
    private static Pool pool;
    private BasicDataSource dataSource = null;

    private DataSource getDataSource() {
        if (dataSource == null) {
            dataSource = new BasicDataSource();
            dataSource.setDriverClassName(driver);
            dataSource.setUsername(DBUSER);
            dataSource.setPassword(DBPASS);
            dataSource.setUrl(con);
            //conexiones iniciales, cantidad de conexiones maximas
            dataSource.setMaxIdle(10);
            dataSource.setMaxTotal(10);
            dataSource.setMaxWaitMillis(2000);

        }
        return (DataSource) dataSource;
    }

    /**
     * Creamos un metodo para poder instanciar esta clase
     *
     * @return
     */
    public static Pool getInstance() {
        if (pool == null) {
            pool = new Pool();
            return pool;
        } else {
            return pool;
        }
    }

    /**
     * Metodo para inicializar la coneción con la base de datos
     *
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        try {
            conexion = (Connection) DriverManager.getConnection(this.con, this.DBUSER, this.DBPASS);
        } catch (SQLException e) {
            System.out.println("Error al intentar abrir la BD");
        }
        return conexion;
    }

    /**
     * Metodo para cerrar la conexion a la base de datos
     *
     * @throws SQLException
     */
    public void closeConnection() throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
        if (conexion != null) {
            conexion.close();
        }
    }
}
