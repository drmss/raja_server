package ir.msisoft.Models;

public class Ticket {
    private int id;
    private City from;
    private String from_time;
    private City to;
    private String to_time;
    private int price;
    private int capacity;
    private String date;

    public Ticket(City from, String from_time, City to, String to_time, int price, int capacity, String date) {
        this.from = from;
        this.from_time = from_time;
        this.to = to;
        this.to_time = to_time;
        this.price = price;
        this.capacity = capacity;
        this.date = date;
    }

    public Ticket(int id, City from, String from_time, City to, String to_time, int price, int capacity, String date) {
        this.id = id;
        this.from = from;
        this.from_time = from_time;
        this.to = to;
        this.to_time = to_time;
        this.price = price;
        this.capacity = capacity;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public City getFrom() {
        return from;
    }

    public String getFrom_time() {
        return from_time;
    }

    public City getTo() {
        return to;
    }

    public String getTo_time() {
        return to_time;
    }

    public int getPrice() {
        return price;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getDate() {
        return date;
    }
}
