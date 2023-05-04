import Team from '../Team';

export const DUMMY_DATA: Team = {
  id: 1,
  teamName: 'Cool Team',
  memberList: [
    {
      id: 1,
      firstName: 'bob',
      lastName: 'bobberson',
      email: 'billybob@bob.com',
      lastLogin: '01/01/0001',
      team: {
        id: 1,
        teamName: 'Cool Team',
      },
    },
    {
      id: 2,
      firstName: 'Peter',
      lastName: 'Griffen',
      email: 'Griffen@Pet.com',
      lastLogin: '03/11/2001',
      team: {
        id: 1,
        teamName: 'Cool Team',
      },
    },
    {
      id: 3,
      firstName: 'smithy',
      lastName: 'smitherson',
      email: 'smithysmith@smith.com',
      lastLogin: '01/01/0001',
      team: {
        id: 1,
        teamName: 'Cool Team',
      },
    },
  ],
};
