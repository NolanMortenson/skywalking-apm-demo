import { Container, Loader } from '@mantine/core';
import { useQuery } from '@tanstack/react-query';
import { useParams } from 'react-router-dom';
import PageTitle from '../components/PageTitle';
import ProjectDetailsWrapper from '../components/ProjectDetailsWrapper';
import { QUERY_KEYS } from '../enums/QueryKeys';
import { ProjectServiceAPI } from '../services/ProjectService';

export default function ProjectDetails() {
  // This is the projectId from the URL path that will be used to call the API
  const { projectId } = useParams();
  const { data, isLoading, isError, error } = useQuery(
    [QUERY_KEYS.PROJECT_RESULT, projectId],
    () => ProjectServiceAPI.getProjectResult(+projectId!)
  );

  if (isLoading) {
    return <Loader />;
  }

  if (isError) {
    console.log(error);
    return <p>Error</p>;
  }

  return (
    <>
      <Container style={{ maxWidth: 1420 }}>
        <PageTitle title={data.data.teamName} />
        <ProjectDetailsWrapper apiResponse={data.data} />
      </Container>
    </>
  );
}
