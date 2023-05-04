import { Container, Loader, Paper, Text } from '@mantine/core';
import { useMediaQuery } from '@mantine/hooks';
import { useQuery } from '@tanstack/react-query';
import { AxiosError, AxiosResponse } from 'axios';
import { useParams } from 'react-router-dom';
import PageTitle from '../components/PageTitle';
import ProjectGraph from '../components/ProjectGraph';
import ProjectGrid from '../components/ProjectGrid';
import { QUERY_KEYS } from '../enums/QueryKeys';
import ProjectResult from '../models/ProjectResult';
import { ProjectServiceAPI } from '../services/ProjectService';

export interface GraphDataObject {
  name: string;
  percent: number;
  sprints: number;
}

export interface ProjectDetailsAPI {
  project: string;
  team: string;
  daysLeft: number;
  data: GraphDataObject[];
}

// DUMMY_DATA will be replaced with API call
export default function ProjectDetails() {
  const { projectId } = useParams();
  const matches = useMediaQuery('(min-width: 768px)');
  const paperPadding = matches ? 'xl' : 'sm';
  const { data, isLoading, isError, error } = useQuery<
    AxiosResponse<ProjectResult>,
    AxiosError
  >([QUERY_KEYS.PROJECT_RESULT, projectId], () =>
    ProjectServiceAPI.getProjectResult(+projectId!)
  );

  if (isLoading) return <Loader />;

  if (isError) {
    if (error!.response!.status === 403) {
      return (
        <Text transform="none" align="center" size="xl" p={15}>
          You aren't authorized to view this project. Please login or reach out
          to your project admin.
        </Text>
      );
    }
    return <p>Error...</p>;
  }

  const projectDetails = data!.data;

  return (
    <>
      <PageTitle
        title={projectDetails.teamName + ' - ' + projectDetails.projectName}
      />
      <Container style={{ maxWidth: 1050, margin: 'auto' }}>
        <Paper
          p={paperPadding}
          sx={{
            display: 'flex',
            flexDirection: 'column',
            minHeight: 450,
            height: '40vh',
            width: '100%',
            marginBottom: 16,
          }}
        >
          <ProjectGraph graphData={projectDetails.graphData} />
        </Paper>
        <Paper
          p="xl"
          style={{
            minHeight: 450,
            maxWidth: 400,
            minWidth: 200,
            width: '100%',
          }}
        >
          <ProjectGrid tableData={projectDetails.graphData} />
        </Paper>
      </Container>
    </>
  );
}
