package com.example.rest.services;

import com.example.rest.entities.Party;

public interface PartyService {

    Party createNewParty(Party newParty);

    Party getPartyById(Long id);

    Party deleteParty(Long id);

    void updateParty(Party party);
}
