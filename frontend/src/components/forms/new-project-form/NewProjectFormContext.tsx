import { createFormContext } from '@mantine/form';

export interface ProjectFormValues {
  admin: object;
  projectName: string;
  forecastStartDate: Date | null;
  storyCount: number;
  sprintDuration: number;
  workDaysInSprint: number;
  historicalDataFirstSprintId: number;
  historicalDataStartDate: Date | null;
  sprintHistoryToEvaluate: number;
  projectSimulations: number;
  isPublic: boolean;
  team: string | null;
  file: File | null;
}

export const [ProjectFormProvider, useProjectFormContext, useProjectForm] =
  createFormContext<ProjectFormValues>();
