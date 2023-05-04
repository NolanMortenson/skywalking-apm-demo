import axios from 'axios';
import TeamMember from '../models/TeamMember';

const BASE_URL: string = '/users';

export const RegisterServiceAPI = {
  register(
    firstName: string,
    lastName: string,
    email: string,
    password: string,
    role: string,
    TeamData: TeamMember
  ) {
    return axios.post(BASE_URL, {
      firstName,
      lastName,
      email,
      password,
      role,
    });
  },
};
