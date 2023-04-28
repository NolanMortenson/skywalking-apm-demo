package com.example.rest.controller;

import com.example.rest.entities.PartyMember;
import com.example.rest.services.PartyMemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@Component
@RequestMapping("/partymembers")
@CrossOrigin
@AllArgsConstructor
public class PartyMemberController {

    private final PartyMemberService partyMemberService;

    @GetMapping("/{id}")
    public PartyMember getPartyMemberById(@PathVariable Long id){return partyMemberService.getPartyMemberById(id);}

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public PartyMember createNewPartyMember(PartyMember newPartyMember){return partyMemberService.createNewPartyMember(newPartyMember);}

    @DeleteMapping("/{id}/delete")
    public String deletePartyMember(@PathVariable Long id){return partyMemberService.deletePartyMember(id);}

    @PutMapping("/{partyMemberId}/add-to-party/{partyId}")
    public PartyMember addToParty(@PathVariable Long partyMemberId, @PathVariable Long partyId){return partyMemberService.addToParty(partyMemberId,partyId);}

    @PutMapping("/{partyMemberId}/remove-party")
    public PartyMember removeFromParty(@PathVariable Long partyMemberId){return partyMemberService.removeFromParty(partyMemberId);}

    @PutMapping
    public PartyMember updatePartyMember(PartyMember updatedPartyMember){return partyMemberService.updatePartyMember(updatedPartyMember);}

}
