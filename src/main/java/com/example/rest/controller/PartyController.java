package com.example.rest.controller;

import com.example.rest.entities.Party;
import com.example.rest.services.PartyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@Component
@RequestMapping("/party")
@CrossOrigin
@AllArgsConstructor
public class PartyController {

    private final PartyService partyService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Party createParty(@RequestBody Party newParty) {return partyService.createNewParty(newParty);}

    @GetMapping("/{id}")
    public Party getParty(@PathVariable Long id){return partyService.getPartyById(id);}

    @DeleteMapping("/{id}/delete")
    public Party deleteParty(@PathVariable Long id){return partyService.deleteParty(id);}

    @PutMapping
    public void updateParty(@RequestBody Party party){partyService.updateParty(party);}
}
