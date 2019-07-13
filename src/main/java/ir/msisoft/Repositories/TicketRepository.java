package ir.msisoft.Repositories;


import ir.msisoft.Models.*;

import java.sql.*;
import java.util.ArrayList;

public class TicketRepository extends Repository {

    public static boolean init() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS tickets (id INTEGER PRIMARY KEY, from_id INT, to_id INT" +
                    ", price INT, from_time VARCHAR(5), to_time VARCHAR(5), date VARCHAR(10), capacity INT)";
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

    public static Ticket find(int id) {
        String sql = "select * from tickets where id=?";
        try {
            Connection con = DBConnectionPool.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet resultSet = st.executeQuery();
            Ticket ticket = null;
            if (resultSet.next()) {
                ticket = ticketFromSQLResult(resultSet);
            }
            st.close();
            con.close();
            return ticket;
        } catch (Exception e) {
            return null;
        }
    }


    private static Ticket ticketFromSQLResult(ResultSet resultSet) throws SQLException {
        return new Ticket(
                resultSet.getInt("id"),
                CityRepository.find(resultSet.getInt("from_id")),
                resultSet.getString("from_time"),
                CityRepository.find(resultSet.getInt("to_id")),
                resultSet.getString("to_time"),
                resultSet.getInt("price"),
                resultSet.getInt("capacity"),
                resultSet.getString("date")
        );
    }

    private static ArrayList<Ticket> ticketsFromResultSet(ResultSet resultSet) throws SQLException {
        ArrayList<Ticket> tickets = new ArrayList<>();
        while (resultSet.next()) {
            Ticket ticket = ticketFromSQLResult(resultSet);
            tickets.add(ticket);
        }
        return tickets;
    }

    public static ArrayList<Ticket> search(City from, City to, String date, int capacity) {
        String sql = "select * from tickets where from_id = ? and to_id = ? and date = ? and capacity >= ?";
        try {
            Connection con = DBConnectionPool.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, from.getId());
            st.setInt(2, to.getId());
            st.setString(3, date);
            st.setInt(4, capacity);
            ResultSet resultSet = st.executeQuery();
            ArrayList<Ticket> tickets = ticketsFromResultSet(resultSet);
            st.close();
            con.close();
            return tickets;
        } catch (Exception e) {
            return null;
        }
    }

    public static ArrayList<Ticket> search(int from_id, int to_id, String date) {
        String sql = "select * from tickets where from_id = ? and to_id = ? and date = ?";
        try {
            Connection con = DBConnectionPool.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, from_id);
            st.setInt(2, to_id);
            st.setString(3, date);
            ResultSet resultSet = st.executeQuery();
            ArrayList<Ticket> tickets = ticketsFromResultSet(resultSet);
            st.close();
            con.close();
            return tickets;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean add(Ticket ticket) {
        Ticket tmp = find(ticket.getId());
        if (tmp != null) {
            return true;
        }
        String insertTicketQuery = "insert into tickets (from_id, to_id, price, from_time, to_time, date, capacity) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection con = DBConnectionPool.getConnection();
            PreparedStatement st = con.prepareStatement(insertTicketQuery);
            st.setInt(1, ticket.getFrom().getId());
            st.setInt(2, ticket.getTo().getId());
            st.setInt(3, ticket.getPrice());
            st.setString(4, ticket.getFrom_time());
            st.setString(5, ticket.getTo_time());
            st.setString(6, ticket.getDate());
            st.setInt(7, ticket.getCapacity());
            st.executeUpdate();
            st.close();
            con.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public static void seed() {
        City tehran = CityRepository.find(1);
        City mashhad = CityRepository.find(2);
        City shiraz = CityRepository.find(3);
        City esfahaan = CityRepository.find(4);

        // tehran to mashhad
        TicketRepository.add(new Ticket(tehran, "08:00", mashhad, "10:00", 200000, 15, "1398-01-10"));
        TicketRepository.add(new Ticket(tehran, "07:00", mashhad, "09:00", 150000, 3, "1398-01-10"));
        TicketRepository.add(new Ticket(tehran, "06:00", mashhad, "08:00", 90000, 1, "1398-01-10"));

        // mashhad to tehran
        TicketRepository.add(new Ticket(mashhad, "08:00", tehran, "10:00", 200000, 15, "1398-01-20"));
        TicketRepository.add(new Ticket(mashhad, "07:00", tehran, "09:00", 100000, 3, "1398-01-20"));
        TicketRepository.add(new Ticket(mashhad, "06:00", tehran, "08:00", 90000, 1, "1398-01-20"));

        // some other
        TicketRepository.add(new Ticket(shiraz, "06:00", esfahaan, "08:00", 90000, 1, "1398-01-10"));
    }

    public static void order(int user_id, int ticket_id, ArrayList<Person> persons) {
        try {
            String sql = "update tickets set capacity = capacity - ? where id = ?";
            Connection con = DBConnectionPool.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, persons.size());
            st.setInt(2, ticket_id);
            st.executeUpdate();
            st.close();

            String sql3 = "insert into user_tickets (user_id, ticket_id, passenger_count) values (?, ?, ?)";
            st = con.prepareStatement(sql3);
            st.setInt(1, user_id);
            st.setInt(2, ticket_id);
            st.setInt(3, persons.size());
            st.executeUpdate();
            st.close();

            for (Person p: persons) {
                String sql2 = "insert into user_ticket_persons (user_id, ticket_id, first_name, last_name, code_melli, age) values (?, ?, ?, ?, ? ,?)";
                st = con.prepareStatement(sql2);
                st.setInt(1, user_id);
                st.setInt(2, ticket_id);
                st.setString(3, p.getFirst_name());
                st.setString(4, p.getLast_name());
                st.setString(5, p.getCode_melli());
                st.setInt(6, p.getAge());
                st.executeUpdate();
                st.close();
            }

            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static ArrayList<UserTicket> getTicketsOfUser(int user_id) {
        String sql = "select * from user_tickets where user_id=?";
        try {
            Connection con = DBConnectionPool.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, user_id);
            ResultSet resultSet = st.executeQuery();
            ArrayList<UserTicket> arr = new ArrayList<>();
            while (resultSet.next()) {
                Ticket t = TicketRepository.find(resultSet.getInt("ticket_id"));
                arr.add(new UserTicket(
                        resultSet.getInt("id"),
                        t.getFrom(),
                        t.getTo(),
                        t.getDate(),
                        resultSet.getInt("passenger_count"),
                        t
                ));
            }
            st.close();
            con.close();
            return arr;
        } catch (Exception e) {
            return null;
        }
    }

    public static void cancelTicket(int userId, int user_ticket_id) {
        try {
            Connection con = DBConnectionPool.getConnection();

            ArrayList<UserTicket> userTickets = getTicketsOfUser(userId);
            for (UserTicket t: userTickets) {
                if (t.getId() == user_ticket_id) {
                    String sql = "update tickets set capacity = capacity + ? where id=?";
                    PreparedStatement st = con.prepareStatement(sql);
                    st.setInt(1, t.getPassenger_count());
                    st.setInt(2, t.getTicketId());
                    st.executeUpdate();
                    st.close();

                    sql = "delete from user_ticket_persons where user_id=? and ticket_id=?";
                    st = con.prepareStatement(sql);
                    st.setInt(1, userId);
                    st.setInt(2, t.getId());
                    st.executeUpdate();
                    st.close();

                }
            }


            String sql = "delete from user_tickets where id=?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, user_ticket_id);
            st.executeUpdate();
            st.close();



            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
