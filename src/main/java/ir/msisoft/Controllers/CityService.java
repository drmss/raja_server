package ir.msisoft.Controllers;

import ir.msisoft.Models.City;
import ir.msisoft.Repositories.CityRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class CityService {

    @GetMapping(value = "/cities")
    @ResponseBody
    public ArrayList<City> getCities() {
        return CityRepository.all();
    }
}
