import { UseFormReturnType } from '@mantine/form';
import dayjs from 'dayjs';
import { ProjectFormValues } from '../components/forms/new-project-form/NewProjectFormContext';
import Project from '../models/Project';

export default function setProjectFormValues(
  data: Project,
  teamId: number,
  form: UseFormReturnType<ProjectFormValues>
) {
  form.setValues({
    admin: data.admin,
    projectName: data.projectName,
    forecastStartDate: dayjs(data.forecastStartDate).toDate(),
    storyCount: data.storyCount,
    sprintDuration: data.sprintDuration,
    workDaysInSprint: data.sprintDuration - (data.sprintDuration / 7) * 2,
    historicalDataFirstSprintId: data.historicalDataFirstSprintId,
    historicalDataStartDate: data.historicalDataStartDate
      ? dayjs(data.historicalDataStartDate).add(5, 'hours').toDate()
      : null,
    sprintHistoryToEvaluate: data.sprintHistoryToEvaluate,
    projectSimulations: data.projectSimulations,
    isPublic: data.isPublic,
    team: teamId.toString(),
  });
}
