import Project from './Project';
import Team from './Team';

export default interface TeamMember {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  password?: string;
  role?: string;
  lastLogin?: string;
  team?: Team;
  project?: Project;
}
