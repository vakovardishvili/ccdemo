package com.vako.caucasus.demo.controllers;


import com.vako.caucasus.demo.models.Client;
import com.vako.caucasus.demo.models.Transactions;
import com.vako.caucasus.demo.services.ClientServices;
import com.vako.caucasus.demo.services.TrasnactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@RestController
@Validated
public class ClientController {
    final
    ClientServices clientServices;
    final
    TrasnactionService trasnactionService;

    public ClientController(ClientServices clientServices, TrasnactionService trasnactionService) {
        this.clientServices = clientServices;
        this.trasnactionService = trasnactionService;
    }


    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<String> onValidationError(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({TrasnactionService.NotEnoughMoney.class})
    public ResponseEntity<String> onValidationError(TrasnactionService.NotEnoughMoney e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }


    @GetMapping("/mobile/pay")
    @Validated
    public void payByPhoneNumber(@Pattern(regexp = "[0-9]{9}") String number, @Min(0) @Max(100) BigDecimal amount) throws TrasnactionService.NotEnoughMoney {
                Client client = clientServices.findByPhoneNumber(number).orElseThrow(() -> new IllegalArgumentException("client not found"));
                if(client != null){
                    trasnactionService.make(client, amount, Transactions.TrTypes.PHONE_OUT);
                }
    }

    @GetMapping("/id/pay")
    @Validated
    public void payById(@Pattern(regexp = "[0-9]{11}") String number,
                                 @Pattern(regexp = "[a-zA-Z]+") String firstName,
                                 @Pattern(regexp = "[a-zA-Z]+") String lastName,
                                 @Min(0) @Max(100) BigDecimal amount ) throws TrasnactionService.NotEnoughMoney {
        Client client = clientServices.findByPersonalNumber(number).orElseThrow(() -> new IllegalArgumentException("client not found"));
        if (client != null) {
            trasnactionService.make(client, amount, Transactions.TrTypes.PID_OUT);
        }
    }

    private void addBalance(Long id, BigDecimal amount) throws TrasnactionService.NotEnoughMoney {
            Client client = clientServices.findById(id);
            if(client != null){
                trasnactionService.make(client, amount, Transactions.TrTypes.BALANCE_IN);
            }
    }

}
