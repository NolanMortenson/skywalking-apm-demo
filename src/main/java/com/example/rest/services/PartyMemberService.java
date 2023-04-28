package com.example.rest.services;

import com.example.rest.entities.PartyMember;

public interface PartyMemberService {

    PartyMember createNewPartyMember(PartyMember newPartyMember);
    String deletePartyMember(Long id);

    PartyMember getPartyMemberById(Long id);

    PartyMember getPartyMemberByUsername(String username);

    PartyMember addToParty(Long partyMemberId, Long partyId);

    PartyMember removeFromParty(Long partyMemberId);

    PartyMember updatePartyMember(PartyMember updatedPartyMember);
}
