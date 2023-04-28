package com.example.rest.services;

import com.example.rest.entities.Party;
import com.example.rest.entities.PartyMember;
import com.example.rest.repos.PartyMemberRepo;
import com.example.rest.repos.PartyRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@Component
@AllArgsConstructor
public class PartyServiceImpl implements PartyService{

    private final PartyRepo partyRepo;

    private final PartyMemberRepo partyMemberRepo;

    @Override
    public Party createNewParty(Party newParty) {
        return partyRepo.save(newParty);
    }

    @Override
    public Party getPartyById(Long id) {
        return partyRepo.findById(id).orElse(null);
    }

    @Override
    public Party deleteParty(Long id) {
        Party ptd = partyRepo.findById(id).orElse(null);

        List<PartyMember> partyMembers = partyMemberRepo.findPartyMemberByPartyId(id);
        partyMembers.forEach(partyMember -> {
            partyMember.setParty(null);
        });

        partyRepo.deleteById(id);

        return ptd;
    }

    @Override
    public void updateParty(Party party) {
        Party _party = partyRepo.findById(party.getId()).orElse(null);

        _party.set_partyName(party.get_partyName());
        partyRepo.save(_party);
    }
}
