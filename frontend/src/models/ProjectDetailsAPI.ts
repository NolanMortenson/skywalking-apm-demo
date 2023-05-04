import GraphDataObject from './GraphDataObject';

export default interface ProjectDetailsAPI {
  project: string;
  team: string;
  data: GraphDataObject[];
  startDate: string;
  numOfStories: number;
  isPublic: boolean;
}
