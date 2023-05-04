import Project from './Project';
import TeamMember from './TeamMember';

export default interface Team {
  id?: number;
  teamName: string;
  project?: Project;
  memberList?: TeamMember[];
  admin?: TeamMember;
}
