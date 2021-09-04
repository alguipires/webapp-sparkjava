package model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class SQLiteDatabase {

    private Dao<Car, Integer> dao = null;
    private ConnectionSource conn = null;

    public SQLiteDatabase() {

        try {
            conn = new JdbcConnectionSource("jdbc:sqlite:mydatabase.db");
            TableUtils.createTableIfNotExists(conn, Car.class);
            dao = DaoManager.createDao(conn, Car.class);

            //criação de dados no banco
            /*dao.create( new Car(1, "VW", "Fusca", "Preto") );
            dao.create( new Car(2, "Ford", "F1000", "Vermelho") );
            dao.create( new Car(3, "GM", "Corsa", "Azul") );*/
        }catch (SQLException ex) {
            System.out.println(ex);
        }

    }

    public Dao<Car, Integer> getDao() {
        return dao;
    }
}
