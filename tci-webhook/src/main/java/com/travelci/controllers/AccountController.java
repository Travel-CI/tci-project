package com.travelci.controllers;


import com.travelci.bitbuckets.Bitbuckets;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Julien on 19/03/2017.
 */
@RestController
@RequestMapping("/webhook")
public class AccountController {



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void AffichageWebhook(@RequestBody Bitbuckets bitbuckets){
        System.out.println(bitbuckets);
    }


}
