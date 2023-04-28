package com.example.rest.services;

import com.example.rest.entities.Party;
import com.example.rest.entities.PartyMember;
import com.example.rest.repos.PartyMemberRepo;
import com.example.rest.repos.PartyRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
//@Component
@AllArgsConstructor
public class PartyMemberServiceImpl implements PartyMemberService{


    private final PartyMemberRepo partyMemberRepo;

    private final PartyRepo partyRepo;
    @Override
    public PartyMember createNewPartyMember(PartyMember newPartyMember) {
        return partyMemberRepo.save(newPartyMember);
    }

    @Override
    public String deletePartyMember(Long id) {
        PartyMember memberToDelete = partyMemberRepo.findById(id).orElse(null);
        partyMemberRepo.deleteById(id);
        return "Successfully deleted " +
                memberToDelete.get_username();
    }

    @Override
    public PartyMember getPartyMemberById(Long id) {
        PartyMember pm = partyMemberRepo.findById(id).orElse(null);
        return pm;
    }

    @Override
    public PartyMember getPartyMemberByUsername(String username) {
        PartyMember pm = getPartyMemberByUsername(username);
        return pm;
    }

    @Override
    public PartyMember addToParty(Long partyMemberId, Long partyId) {
        PartyMember pm = partyMemberRepo.findById(partyMemberId).orElse(null);
        Party party = partyRepo.findById(partyId).orElse(null);

        pm.setParty(party);
        return partyMemberRepo.save(pm);
    }

    @Override
    public PartyMember removeFromParty(Long partyMemberId) {
        PartyMember pm = partyMemberRepo.findById(partyMemberId).orElse(null);
        pm.setParty(null);
        return partyMemberRepo.save(pm);

    }

    @Override
    public PartyMember updatePartyMember(PartyMember updatedPartyMember) {
        PartyMember pmEdit = partyMemberRepo.findById(updatedPartyMember.getId()).orElse(null);

        pmEdit.set_username(updatedPartyMember.get_username());
        pmEdit.set_class(updatedPartyMember.get_class());
        pmEdit.set_health(updatedPartyMember.get_health());
        pmEdit.set_dps(updatedPartyMember.get_dps());

        return partyMemberRepo.save(pmEdit);
    }
}
