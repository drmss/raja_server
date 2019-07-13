package ir.msisoft.Repositories;

import ir.msisoft.Models.City;
import java.sql.*;
import java.util.ArrayList;

public class CityRepository extends Repository {

    public static boolean init() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS cities (id INTEGER PRIMARY KEY, name TEXT)";
            Connection con = DBConnectionPool.getConnection();
            Statement st = con.createStatement();
            st.executeUpdate(sql);
            st.close();
            con.close();
            seed();
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public static City add(String name) {
        City temp = findByName(name);
        if (temp != null)
            return temp;

        String sql = "insert into cities (name) VALUES(?)";
        try {
            Connection con = DBConnectionPool.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, name);
            st.executeUpdate();
            st.close();
            con.close();
            return findByName(name);
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

    private static City findByName(String name) {
        String sql = "select * from cities where name=?";
        try {
            Connection con = DBConnectionPool.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, name);
            ResultSet resultSet = st.executeQuery();
            City city = null;
            if (resultSet.next()) {
                city = new City(resultSet.getInt("id"), resultSet.getString("name"));
            }
            st.close();
            con.close();
            return city;
        } catch (Exception e) {
            return null;
        }
    }

    public static City find(int id) {
        String sql = "select * from cities where id=?";
        try {
            Connection con = DBConnectionPool.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet resultSet = st.executeQuery();
            City city = null;
            if (resultSet.next()) {
                city = new City(resultSet.getInt("id"), resultSet.getString("name"));
            }
            st.close();
            con.close();
            return city;
        } catch (Exception e) {
            return null;
        }
    }

    public static ArrayList<City> all(){
        String sql = "select * from cities";
        try {
            Connection con = DBConnectionPool.getConnection();
            Statement st = con.createStatement();
            ResultSet resultSet = st.executeQuery(sql);
            ArrayList<City> cities = new ArrayList<>();
            while(resultSet.next()){
                cities.add(new City(resultSet.getInt("id"), resultSet.getString("name")));
            }
            st.close();
            con.close();
            return cities;
        } catch (Exception e) {
            return null;
        }
    }

    public static void seed() {
        CityRepository.add("تهران");
        CityRepository.add("مشهد");
        CityRepository.add("شیراز");
        CityRepository.add("اصفهان");
    }
}
