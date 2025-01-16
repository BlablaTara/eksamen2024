/*package com.example.eksamen2024.initData;

import com.example.eksamen2024.models.Pizza;
import com.example.eksamen2024.repositories.PizzaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitPizza implements CommandLineRunner {

    private final PizzaRepository pizzaRepository;

    public InitPizza(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        createPizza("Margherita", 80);
        createPizza("Pepperoni", 90);
        createPizza("Hawaii", 85);
        createPizza("Vegetar", 95);
        createPizza("BBQ Chicken", 100);

        System.out.println("Pizza data initialized.");
    }

    private void createPizza(String titel, int price) {
        Pizza pizza = new Pizza();
        pizza.setTitel(titel);
        pizza.setPrice(price);
        pizzaRepository.save(pizza);
    }
}

 */
