import { Grid } from '@mantine/core';
import ProjectResult from '../models/ProjectResult';
import ProjectParameters from './ProjectParameters';
import TeamMemberList from './TeamMemberList';

export interface IProjectInfoProps {
  infoData: ProjectResult;
}

export default function ProjectInfo({ infoData }: IProjectInfoProps) {
  return (
    <>
      <Grid>
        <Grid.Col md={6}>
          <TeamMemberList teamId={infoData.teamId} />
        </Grid.Col>
        <Grid.Col md={6}>
          <ProjectParameters
            startDate={infoData.startDate}
            stories={infoData.numOfStories}
            projectId={infoData.projectId}
          />
        </Grid.Col>
      </Grid>
    </>
  );
}
