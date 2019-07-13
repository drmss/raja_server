package ir.msisoft.Controllers;


import ir.msisoft.Models.UserTicket;
import ir.msisoft.Repositories.TicketRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RestController
public class UserServices {

    @GetMapping(value = "/profile")
    public ArrayList<UserTicket> getUserTickets(@RequestAttribute(value = "userId") int userId) {
        return TicketRepository.getTicketsOfUser(userId);
    }
}
