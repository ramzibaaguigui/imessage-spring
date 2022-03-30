package jyad.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HelloController {

    @GetMapping("/home")
    public String getHome(Model model) {
        model.addAttribute("data", getData());
        model.addAttribute("data2", getData() + getData());
        model.addAttribute("cars", getCars());
        return "home";
    }

    public String getData() {
        return "This is the data to display";
    }


    public static class Car {
        public String name;
        public int year;
        public Car(String name, int year) {
            this.name = name;
            this.year = year;
        }
    }

    public List<Car> getCars() {
        List<Car> cars = new ArrayList<>();
        cars.add(new Car("BMW", 1998));
        cars.add(new Car("Mercedes", 2015));
        cars.add(new Car("QQ", 2013));
        return cars;
    }

}
