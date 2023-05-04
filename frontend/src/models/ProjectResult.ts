import GraphDataObject from './GraphDataObject';

export default interface ProjectResult {
  projectName: string;
  projectId: number;
  teamName: string;
  teamId: number;
  graphData: GraphDataObject[];
  startDate: string;
  numOfStories: number;
  isPublic: boolean;
}
