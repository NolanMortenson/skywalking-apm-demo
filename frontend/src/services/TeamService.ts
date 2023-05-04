import Team from '../models/Team';
import { axiosClient } from './LoginService';

const BASE_URL: string = '/teams';

interface TeamData {
  data: Team;
}

interface TeamListData {
  data: Team[];
}

export const TeamServiceAPI = {
  getTeam: (id: number): Promise<TeamData> =>
    axiosClient.get(`${BASE_URL}/${id}`),

  // TODO: Change Promise<any> return to corresponding TeamDTO once backend implements it
  addNewTeam: (data: Team): Promise<any> => axiosClient.post(BASE_URL, data),

  getAdminTeams: (id: number): Promise<TeamListData> =>
    axiosClient.get(`users/admins/${id}/all-teams`),

  deleteTeam: (id: number): Promise<any> =>
    axiosClient.delete(`teams/${id}/delete`),


  updateTeam: (data: Team): Promise<any> => 
  axiosClient.put(BASE_URL, data)

};