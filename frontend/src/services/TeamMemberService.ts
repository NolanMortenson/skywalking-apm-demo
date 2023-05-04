import TeamMember from '../models/TeamMember';
import { axiosClient } from './LoginService';

const BASE_URL: string = '/users';

interface TeamMemberData {
  data: TeamMember;
}

interface AddToTeam {
  userId: number;
  teamId: number;
}

interface TeamMemberListData {
  data: TeamMember[];
}



export const TeamMemberServiceAPI = {
  getTeamMember: (id: number): Promise<TeamMemberData> =>
    axiosClient.get(`${BASE_URL}/${id}`),

  getAllTeamMembers: (): Promise<TeamMemberListData> =>
    axiosClient.get(`${BASE_URL}`),

  addNewTeamMember: (data: TeamMember) =>
    axiosClient.post(BASE_URL + '/create', data),

  removeTeamFromTeamMember: (id: number): Promise<TeamMemberData> =>
    axiosClient.put(`${BASE_URL}/${id}/remove-team`),

  addTeamToTeamMember: ({
    userId,
    teamId,
  }: AddToTeam): Promise<TeamMemberData> =>
    axiosClient.put(`${BASE_URL}/${userId}/add-team/${teamId}`),

  getTeamMemberByEmail: (email: string): Promise<TeamMemberData> =>
    axiosClient.get(`${BASE_URL}/get/${email}`),
};
