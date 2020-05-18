package com.vako.caucasus.demo.controllers;


import com.vako.caucasus.demo.models.Client;
import com.vako.caucasus.demo.models.Transactions;
import com.vako.caucasus.demo.services.ClientServices;
import com.vako.caucasus.demo.services.TrasnactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.List;

@RestController
@Validated
public class ClientController {
    private final
    ClientServices clientServices;
    private final
    TrasnactionService trasnactionService;



    @RequestMapping("paybyphone")
    public ModelAndView payByPhone () {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("paybyphone");
        return modelAndView;
    }

    @RequestMapping("paybyid")
    public ModelAndView payById () {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("paybyid");
        return modelAndView;
    }
    @RequestMapping("choosebank")
    public ModelAndView chooseBank () {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("choosebank");
        return modelAndView;
    }



    public ClientController(ClientServices clientServices, TrasnactionService trasnactionService) {
        this.clientServices = clientServices;
        this.trasnactionService = trasnactionService;
    }


    @ExceptionHandler({IllegalArgumentException.class})
    public ModelAndView onValidationError(IllegalArgumentException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("success", e.getMessage());
        return modelAndView;
    }


    @ExceptionHandler({ConstraintViolationException.class})
    public ModelAndView onValidationError(ConstraintViolationException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("success", e.getMessage());
        return modelAndView;
    }

    @ExceptionHandler({TrasnactionService.NotEnoughMoney.class})
    public ModelAndView onValidationError(TrasnactionService.NotEnoughMoney e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("success", e.getMessage());
        return modelAndView;
    }



    @GetMapping("/mobile/pay")
    @Validated
    public ModelAndView payByPhoneNumber(@Pattern(regexp = "[0-9]{9}") String number, @Min(1) @Max(100) BigDecimal amount) throws TrasnactionService.NotEnoughMoney {
                Client client = clientServices.findByPhoneNumber(number).orElseThrow(() -> new IllegalArgumentException("client not found"));
        trasnactionService.make(client, amount, Transactions.TrTypes.PHONE_OUT);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("success", "მობილურის ბალანსის შევსება წარმატებით დასრულდა");
        return modelAndView;

    }

    @GetMapping("/id/pay")
    @Validated
    public ModelAndView payById(@Pattern(regexp = "[0-9]{11}") String number,
                                 @Pattern(regexp = "[a-zA-Z]+") String firstName,
                                 @Pattern(regexp = "[a-zA-Z]+") String lastName,
                                 @Min(1) @Max(100) BigDecimal amount ) throws TrasnactionService.NotEnoughMoney {
        Client client = clientServices.findByPersonalNumber(number).orElseThrow(() -> new IllegalArgumentException("client not found"));
        trasnactionService.make(client, amount, Transactions.TrTypes.PID_OUT);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("success", "გადახდა წარმატებით დასრულა");
        return modelAndView;
    }

    public void  addBalance(Long id, BigDecimal amount) throws TrasnactionService.NotEnoughMoney {
            Client client = clientServices.findById(id);
            if(client != null){
                trasnactionService.make(client, amount, Transactions.TrTypes.BALANCE_IN);
            }

    }


    @RequestMapping("/gettransactions/{id}")
    public ModelAndView getTransactions(@PathVariable Long id){
        List<Transactions> list = trasnactionService.findByUserId(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("gettransactions");
        modelAndView.addObject("transactions", list);
        return modelAndView;
    }



}
