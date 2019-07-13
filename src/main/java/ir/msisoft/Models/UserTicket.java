package ir.msisoft.Models;

public class UserTicket {
    private int id;
    private City from;
    private City to;
    private String date;
    private int passenger_count;
    private Ticket ticket;

    public UserTicket(int id, City from, City to, String date, int passenger_count, Ticket ticket) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.date = date;
        this.passenger_count = passenger_count;
        this.ticket = ticket;
    }

    public int getId() {
        return id;
    }

    public City getFrom() {
        return from;
    }

    public City getTo() {
        return to;
    }

    public String getDate() {
        return date;
    }

    public int getPassenger_count() {
        return passenger_count;
    }

    public int getTicketId() {
        return  ticket.getId();
    }
}
