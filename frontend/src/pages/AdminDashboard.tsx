import {
  Box,
  Button,
  Center,
  Container,
  Loader,
  Paper,
  Stack,
  Text,
  Title,
} from '@mantine/core';
import { useLocalStorage, useMediaQuery } from '@mantine/hooks';
import { useQuery } from '@tanstack/react-query';
import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import PageTitle from '../components/PageTitle';
import ProjectGraph from '../components/ProjectGraph';
import { QUERY_KEYS } from '../enums/QueryKeys';
import LocalStorageUser from '../models/LocalStorageUser';
import { ProjectServiceAPI } from '../services/ProjectService';
import { TeamMemberServiceAPI } from '../services/TeamMemberService';
import { TeamServiceAPI } from '../services/TeamService';

const AdminDashboard = () => {
  const navigate = useNavigate();
  const [currentUser] = useLocalStorage<LocalStorageUser>({ key: 'user' });
  const currentUserId = currentUser.id;
  const isAdmin = !!currentUser.isAdmin;

  const matches = useMediaQuery('(min-width: 768px)');
  const paperPadding = matches ? 'xl' : 'sm';

  // Admin project list API call -- only called if user is admin
  const adminApi = useQuery(
    [QUERY_KEYS.ADMIN_PROJECTS, currentUserId],
    () => ProjectServiceAPI.getProjectListOfAdmin(currentUserId!),
    { enabled: !!currentUserId && !!isAdmin }
  );

  // Team member info API call -- only called if user is admin
  const teamMemberApi = useQuery(
    [QUERY_KEYS.USER_INFO, currentUserId],
    () => TeamMemberServiceAPI.getTeamMember(currentUserId),
    { enabled: !!currentUserId && !isAdmin }
  );

  const teamApi = useQuery(
    [QUERY_KEYS.ADMIN_TEAMS, currentUserId],
    () => TeamServiceAPI.getAdminTeams(currentUserId!),
    { enabled: !!currentUserId && isAdmin }
  );

  if (
    (adminApi.isLoading && adminApi.isFetching) ||
    (teamMemberApi.isLoading && teamMemberApi.isFetching) ||
    (teamApi.isLoading && teamApi.isFetching)
  ) {
    return <Loader />;
  }

  if (adminApi.isError || teamMemberApi.isError || teamApi.isError) {
    // Keep logs in error handling for now
    console.log(adminApi.error);
    console.log(teamMemberApi.error);
    console.log(teamApi.error);
    return <p>Error</p>;
  }

  // Navigate to project details page if user is linked to project
  if (!isAdmin && teamMemberApi.data?.data.team?.project) {
    navigate('/project-details/' + teamMemberApi.data?.data.team.project!.id);
  }

  const adminProjects = adminApi.data?.data;
  const adminTeams = teamApi.data?.data;

  return (
    <>
      <PageTitle title={'Projects'} />
      <Container style={{ maxWidth: 1050, margin: 'auto' }}>
        {isAdmin &&
          adminProjects!.map((project, i) => (
            <React.Fragment key={i}>
              <Title order={2} mt={20}>
                {project.teamName} - {project.projectName}
              </Title>
              <Paper
                p={paperPadding}
                sx={{
                  display: 'flex',
                  flexDirection: 'column',
                  minHeight: 450,
                  height: '40vh',
                  width: '100%',
                }}
              >
                <ProjectGraph graphData={project.graphData} />
                <Box
                  sx={{
                    display: 'flex',
                    justifyContent: 'flex-end',
                  }}
                >
                  <Button
                    aria-label="details"
                    onClick={() =>
                      navigate('/project-details/' + project.projectId)
                    }
                  >
                    Details
                  </Button>
                </Box>
              </Paper>
            </React.Fragment>
          ))}
        {isAdmin && adminTeams?.length === 0 && (
          <Center>
            <Paper
              style={{
                minHeight: 150,
                minWidth: 400,
                maxWidth: '100%',
              }}
            >
              <Stack align="center">
                <Title order={2} sx={{ textTransform: 'none' }}>
                  You have no teams or projects assigned to you.
                </Title>
                <Title order={4} sx={{ textTransform: 'none' }}>
                  Projects must be assigned to teams. Click below to create
                  teams for your projects.
                </Title>
                <Button  aria-label="create teams" component={Link} to="/manage-teams">
                  Create Teams
                </Button>
              </Stack>
            </Paper>
          </Center>
        )}
        {isAdmin && adminProjects?.length === 0 && adminTeams?.length !== 0 && (
          <Center>
            <Paper
              style={{
                minHeight: 150,
                minWidth: 400,
                maxWidth: '100%',
              }}
            >
              <Stack align="center">
                <Title order={2} sx={{ textTransform: 'none' }}>
                  You have no projects assigned to you.
                </Title>

                <Button aria-label="create new project" component={Link} to="/new-project">
                  Create New Project
                </Button>
              </Stack>
            </Paper>
          </Center>
        )}
        {!isAdmin && (
          <>
            <Center>
              <Paper
                style={{
                  minHeight: 150,
                }}
              >
                <Stack align="center">
                  <Title order={2}>No project assigned.</Title>
                  <Text>
                    Reach out to your admin to have one assigned to you.
                  </Text>
                </Stack>
              </Paper>
            </Center>
          </>
        )}
      </Container>
    </>
  );
};
export default AdminDashboard;
