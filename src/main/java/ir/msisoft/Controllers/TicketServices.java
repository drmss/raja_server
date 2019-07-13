package ir.msisoft.Controllers;

import ir.msisoft.Models.Person;
import ir.msisoft.Models.Ticket;
import ir.msisoft.Repositories.TicketRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class TicketServices {

    @GetMapping(value = "/search")
    @ResponseBody
    public ArrayList<Ticket> searchProjects(@RequestParam(value = "from_id") int from_id, @RequestParam(value = "to_id") int to_id, @RequestParam(value = "date") String date ) {
        return TicketRepository.search(from_id, to_id, date);
    }

    @PostMapping(value = "/order")
    public void order(@RequestParam(value = "data") String data, @RequestParam(value = "go_ticket_id") int go_ticket_id, @RequestParam(value = "back_ticket_id") int back_ticket_id, @RequestAttribute(value = "userId") int userId) {
        ArrayList<Person> persons = new ArrayList<>();
        JSONArray arr = new JSONArray(data);
        for (Object o:arr) {
            persons.add(new Person((JSONObject) o));
        }
        TicketRepository.order(userId, go_ticket_id, persons);
        TicketRepository.order(userId, back_ticket_id, persons);
    }

    @GetMapping(value = "/cancel")
    public void cancel(@RequestParam(value = "user_ticket_id") int user_ticket_id, @RequestAttribute(value = "userId") int userId) {
        TicketRepository.cancelTicket(userId, user_ticket_id);
    }
}
