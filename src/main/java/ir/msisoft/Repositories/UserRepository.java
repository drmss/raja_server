package ir.msisoft.Repositories;

import ir.msisoft.Models.User;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;

public class UserRepository extends Repository {

    public static boolean init() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, name TEXT, username TEXT, password TEXT)";
            Connection con = DBConnectionPool.getConnection();
            Statement st = con.createStatement();
            st.executeUpdate(sql);
            st.close();

            sql = "CREATE TABLE IF NOT EXISTS user_tickets (id INTEGER PRIMARY KEY, user_id INT NOT NULL, ticket_id INT NOT NULL, passenger_count INT)";
            st = con.createStatement();
            st.executeUpdate(sql);
            st.close();

            sql = "CREATE TABLE IF NOT EXISTS user_ticket_persons (id INTEGER PRIMARY KEY, user_id INT, ticket_id INT, first_name TEXT, last_name TEXT, code_melli TEXT, age INT)";
            st = con.createStatement();
            st.executeUpdate(sql);
            st.close();

            con.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }


    public static User find(int id) {
        String sql = "select * from users where id=?";
        try {
            Connection con = DBConnectionPool.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet resultSet = st.executeQuery();
            User user = null;
            if (resultSet.next()) {
                user = createUserFromResultSet(resultSet);
            }
            st.close();
            con.close();
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    public static User findByUsername(String username) {
        String sql = "select * from users where username=?";
        User user = null;
        try {
            Connection con = DBConnectionPool.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, username);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                user = createUserFromResultSet(resultSet);
            }
            st.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    private static User createUserFromResultSet(ResultSet resultSet) {
        User user = null;
        try {
            user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("username"),
                    resultSet.getString("password")
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return user;
    }


    public static boolean isExist(String username) {
        String sql = "select * from users where username = ?";
        boolean result = false;
        try {
            Connection con = DBConnectionPool.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, username);
            ResultSet resultSet = st.executeQuery();
            if (resultSet != null) {
                resultSet.last();
                if (resultSet.getRow() >= 1)
                    result = true;
            }
            st.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public static boolean add(User user) {
        User tmp = find(user.getId());
        if (tmp != null) {
            return true;
        }
        String insertUserQuery = "insert into users (name, username, password) VALUES(?, ?, ?)";
        try {
            Connection con = DBConnectionPool.getConnection();
            PreparedStatement st = con.prepareStatement(insertUserQuery);
            st.setString(1, user.getName());
            st.setString(2, user.getUsername());
            st.setString(3, user.getPassword());
            st.executeUpdate();
            st.close();
            con.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }









//    public static boolean loadSkills(User user, User currentUser) {
//        String sql = "select * from user_skills where user_id=?";
//        try {
//            Connection con = DBConnectionPool.getConnection();
//            PreparedStatement st = con.prepareStatement(sql);
//            st.setString(1, user.getId());
//            ResultSet resultSet = st.executeQuery();
//            while (resultSet.next()) {
//                String endorsedQuery = "select * from endorses where user_id=? and skill=? and endorser_id=?";
//                PreparedStatement st2 = con.prepareStatement(endorsedQuery);
//                st2.setString(1, user.getId());
//                st2.setString(2, resultSet.getString("name"));
//                st2.setString(3, currentUser.getId());
//                ResultSet resultSet2 = st2.executeQuery();
//                boolean endorsed = false;
//                if (resultSet2.next())
//                    endorsed = true;
//                user.getSkills().add(new UserSkill(
//                        resultSet.getString("name"),
//                        resultSet.getInt("point"),
//                        endorsed
//                ));
//                st2.close();
//            }
//            st.close();
//            con.close();
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    public static boolean isEndorsed(User user, String skill, User endorser) {
//        String sql = "select * from endorses where user_id=? and skill=? and endorser_id=?";
//        boolean isEndorsed = false;
//        try {
//            Connection con = DBConnectionPool.getConnection();
//            PreparedStatement st = con.prepareStatement(sql);
//            st.setString(1, user.getId());
//            st.setString(2, skill);
//            st.setString(3, endorser.getId());
//            ResultSet resultSet = st.executeQuery();
//            if (resultSet.next()) {
//                isEndorsed = true;
//            }
//            st.close();
//            con.close();
//            return isEndorsed;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    public static boolean endorse(User user, String skill, User endorser) {
//        try {
//            if (!isEndorsed(user, skill, endorser)) {
//                Connection con = DBConnectionPool.getConnection();
//                PreparedStatement st;
//                String sql = "insert into endorses (user_id, skill, endorser_id) VALUES " +
//                        "(?, ?, ?)";
//                st = con.prepareStatement(sql);
//                st.setString(1, user.getId());
//                st.setString(2, skill);
//                st.setString(3, endorser.getId());
//                st.executeUpdate();
//                st.close();
//                sql = "update user_skills set point = point + 1 where user_id = ? and name = ?";
//                st = con.prepareStatement(sql);
//                st.setString(1, user.getId());
//                st.setString(2, skill);
//                st.executeUpdate();
//                st.close();
//                con.close();
//                return true;
//            }
//            return false;
//        } catch (Exception e) {
//            System.out.println(e.toString());
//            return false;
//        }
//    }
//
//    public static void addSkill(User user, Skill skill) {
//        try {
//            Connection con = DBConnectionPool.getConnection();
//            PreparedStatement st;
//            String sql = "insert into user_skills (user_id, name, point) VALUES " +
//                    "(?, ?, 1)";
//            st = con.prepareStatement(sql);
//            st.setString(1, user.getId());
//            st.setString(2, skill.getName());
//            st.executeUpdate();
//            st.close();
//            con.close();
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//    }
//
//    public static boolean removeSkill(User user, Skill skill) {
//        try {
//            Connection con = DBConnectionPool.getConnection();
//            PreparedStatement st;
//            String sql = "delete from user_skills where user_id=? and name=?";
//            st = con.prepareStatement(sql);
//            st.setString(1, user.getId());
//            st.setString(2, skill.getName());
//            st.executeUpdate();
//            st.close();
//            con.close();
//            return true;
//        } catch (Exception e) {
//            System.out.println(e.toString());
//            return false;
//        }
//    }
}
