import {
  Button,
  Container,
  Group,
  Loader,
  Modal,
  Paper,
  Stack,
  Title,
  UnstyledButton,
  useMantineTheme,
} from '@mantine/core';
import { zodResolver } from '@mantine/form';
import { useLocalStorage } from '@mantine/hooks';
import { showNotification, updateNotification } from '@mantine/notifications';
import { IconArrowBackUp } from '@tabler/icons';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { AxiosError, AxiosResponse } from 'axios';
import dayjs from 'dayjs';
import { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Check, ChevronDown, ChevronUp, CircleX } from 'tabler-icons-react';
import { z } from 'zod';
import { QUERY_KEYS } from '../../../enums/QueryKeys';
import LocalStorageUser from '../../../models/LocalStorageUser';

import ProjectResult from '../../../models/ProjectResult';
import { USER_KEY } from '../../../services/LoginService';
import { ProjectServiceAPI } from '../../../services/ProjectService';
import setProjectFormValues from '../../../utils/setFormValues';
import DeleteProjectModal from '../../modals/DeleteProjectModal';
import AdvancedDropdown from './AdvancedDropdown';
import {
  ProjectFormProvider,
  ProjectFormValues,
  useProjectForm,
} from './NewProjectFormContext';
import SmallFormFields from './SmallFormFields';
import TopFormFields from './TopFormFields';

const JSON_TYPE = 'application/json';

const NewProject = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const theme = useMantineTheme();
  const { projectId } = useParams();
  const [currentUser] = useLocalStorage<LocalStorageUser>({ key: USER_KEY });
  const [opened, setOpened] = useState(false);
  const [didFillValues, setDidFillValues] = useState(false);
  const [deleteModalOpened, setDeleteOpened] = useState(false);
  const queryClient = useQueryClient();
  const isExistingProject = !!projectId;

  const initialValues = {
    admin: {
      id: currentUser.id,
      role: 'ADMIN',
    },
    projectName: '',
    forecastStartDate: null,
    storyCount: 1,
    sprintDuration: 14,
    workDaysInSprint: 10,
    historicalDataFirstSprintId: 1,
    historicalDataStartDate: null,
    sprintHistoryToEvaluate: 15,
    projectSimulations: 1000,
    isPublic: false,
    team: null,
    file: null,
  };

  useEffect(() => {
    if (!isExistingProject) {
      projectDetailForm.setValues(initialValues);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [location.pathname]);

  const mutationSideEffect = {
    onMutate: () => {
      showNotification({
        id: 'load-data',
        loading: true,
        title: 'Loading your data...',
        message: '',
        autoClose: false,
      });
    },
    onSuccess: async (response: AxiosResponse<ProjectResult>) => {
      updateNotification({
        id: 'load-data',
        color: 'teal',
        title: 'Success',
        message: 'Project saved!',
        icon: <Check size={16} />,
        autoClose: 2400,
      });

      if (isExistingProject) {
        await ProjectServiceAPI.rerunSimulation({
          projectId: +projectId,
          startDate: dayjs(projectDetailForm.values.forecastStartDate).format(
            'YYYY-MM-DD'
          ),
          storyCount: projectDetailForm.values.storyCount,
        });
      }

      const currProjectId = isExistingProject
        ? projectId
        : response.data.projectId;

      await queryClient.invalidateQueries(
        [QUERY_KEYS.PROJECT_RESULT, currProjectId],
        {
          refetchType: 'all',
        }
      );
      await queryClient.invalidateQueries(
        [QUERY_KEYS.PROJECT_DETAILS, currProjectId],
        {
          refetchType: 'all',
        }
      );

      projectDetailForm.reset();
      navigate('/project-details/' + currProjectId);
    },
    onError: (error: AxiosError<any>) => {
      updateNotification({
        id: 'load-data',
        color: 'red',
        title: 'Error',
        message: error.response?.data,
        icon: <CircleX size={16} />,
        autoClose: 2400,
      });
    },
  };

  const newProjectMutation = useMutation(
    ProjectServiceAPI.createProject,
    mutationSideEffect
  );

  const editProjectMutation = useMutation(
    ProjectServiceAPI.updateProject,
    mutationSideEffect
  );

  const zUser = z.object({
    projectName: z
      .string()
      .min(5, { message: 'Must be 5 or more characters long' }),
    forecastStartDate: z.date({ invalid_type_error: 'Please select a date' }),
    team: z
      .string({ invalid_type_error: 'Please select a team' })
      .min(1, { message: 'Please select a team' }),
    file: isExistingProject
      ? z.any()
      : z.instanceof(File, { message: 'Please select a file' }),
  });

  const projectDetailForm = useProjectForm({
    validate: zodResolver(zUser),
    initialValues,
  });

  const submitHandler = (vals: ProjectFormValues): void => {
    const project: any = {
      admin: {
        id: currentUser.id,
        role: 'ADMIN',
      },
      project_name: vals.projectName,
      forecast_start_date: vals.forecastStartDate,
      story_count: vals.storyCount,
      sprint_duration: vals.sprintDuration,
      work_days_in_sprint: vals.workDaysInSprint,
      historical_data_first_sprint_id: vals.historicalDataFirstSprintId,
      historical_data_start_date: vals.historicalDataStartDate,
      sprint_history_to_evaluate: vals.sprintHistoryToEvaluate,
      project_simulations: vals.projectSimulations,
      is_public: vals.isPublic,
    };

    let data = new FormData();
    data.append('file', vals.file!);
    data.append('team_id', new Blob([vals.team!], { type: JSON_TYPE }));
    data.append(
      'project',
      new Blob([JSON.stringify(project)], { type: JSON_TYPE })
    );

    isExistingProject
      ? editProjectMutation.mutate({ ...project, id: projectId })
      : newProjectMutation.mutate(data);
  };

  const projectDetailsQuery = useQuery(
    [QUERY_KEYS.PROJECT_DETAILS, projectId],
    () => ProjectServiceAPI.getProjectDetails(+projectId!),
    { enabled: isExistingProject }
  );

  const projectResultQuery = useQuery(
    [QUERY_KEYS.PROJECT_RESULT, projectId],
    () => ProjectServiceAPI.getProjectResult(+projectId!),
    { enabled: isExistingProject }
  );

  if (
    (projectDetailsQuery.isLoading && projectDetailsQuery.isFetching) ||
    (projectResultQuery.isLoading && projectResultQuery.isFetching)
  )
    return <Loader />;

  if (projectDetailsQuery.isError || projectResultQuery.isError)
    return <p>ERROR</p>;

  if (projectDetailsQuery.data && projectResultQuery.data && !didFillValues) {
    setProjectFormValues(
      projectDetailsQuery.data.data,
      projectResultQuery.data.data.teamId,
      projectDetailForm
    );
    setDidFillValues(true);
  }

  return (
    <ProjectFormProvider form={projectDetailForm}>
      <Modal
        centered
        withCloseButton={false}
        opened={deleteModalOpened}
        onClose={() => {
          setDeleteOpened(false);
        }}
      >
        <DeleteProjectModal
          setOpened={setDeleteOpened}
          projectName={projectDetailForm.values.projectName}
          projectId={projectId!}
        />
      </Modal>
      <Container size={600.5}>
        {isExistingProject && (
          <UnstyledButton
            aria-label="return to project"
            onClick={() => navigate(-1)}
          >
            <Group>
              <IconArrowBackUp size={20} color={theme.colors.darkBlue[0]} />
              <Title inline order={4}>
                Back to Project
              </Title>
            </Group>
          </UnstyledButton>
        )}
        <Paper shadow="md" p="xl" withBorder mt={15}>
          <form
            onSubmit={projectDetailForm.onSubmit((values) =>
              submitHandler(values)
            )}
          >
            <Stack>
              <TopFormFields />

              <SmallFormFields />

              <UnstyledButton
                aria-label="show or hide advanced settings"
                onClick={() => setOpened((o) => !o)}
              >
                <Group position="center" spacing={0}>
                  <Title order={4}>{opened ? 'Hide' : 'Show'} Advanced</Title>
                  {opened ? <ChevronUp /> : <ChevronDown />}
                </Group>
              </UnstyledButton>

              <AdvancedDropdown opened={opened} />

              <Group>
                {opened && isExistingProject && (
                  <Button
                    aria-label="delete"
                    styles={(theme) => ({
                      root: {
                        backgroundColor: '#8B0000',
                        '&:hover': {
                          backgroundColor: theme.fn.darken('#8B0000', 0.15),
                        },
                      },
                    })}
                    sx={{ width: '40%', margin: 'auto' }}
                    onClick={() => {
                      setDeleteOpened(true);
                    }}
                  >
                    Delete
                  </Button>
                )}
                <Button
                  aria-label="submit"
                  color="darkBlue"
                  type="submit"
                  sx={{ width: '40%', margin: 'auto' }}
                >
                  Submit
                </Button>
              </Group>
            </Stack>
          </form>
        </Paper>
      </Container>
    </ProjectFormProvider>
  );
};
export default NewProject;
