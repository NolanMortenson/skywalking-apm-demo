export default interface Project {
  id?: number;
  admin?: Object;
  adminId?: string;
  projectName: string;
  forecastStartDate: Date | null;
  storyCount: number;
  sprintDuration: number;
  workDaysInSprint: number;
  historicalDataStartDate: Date | null;
  historicalDataFirstSprintId: number;
  sprintHistoryToEvaluate: number;
  projectSimulations: number;
  isPublic: boolean;
}
