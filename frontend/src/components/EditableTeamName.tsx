import {
  ActionIcon,
  Box,
  Button,
  Group,
  TextInput,
  Title,
} from '@mantine/core';
import { useForm, zodResolver } from '@mantine/form';
import { showNotification, updateNotification } from '@mantine/notifications';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { AxiosError } from 'axios';
import { useState } from 'react';
import { Check, CircleX, DeviceFloppy, Pencil } from 'tabler-icons-react';
import { z } from 'zod';
import { QUERY_KEYS } from '../enums/QueryKeys';
import Team from '../models/Team';
import { TeamServiceAPI } from '../services/TeamService';

export interface Props {
  team?: Team;
}

export default function EditableTeamName({ team }: Props) {
  const [isInputOn, setIsInputOn] = useState<boolean>(false);
  const [currentTeamName, setCurrentTeamName] = useState<String | undefined>(
    team?.teamName
  );

  const queryClient = useQueryClient();

  const mutation = useMutation(TeamServiceAPI.updateTeam, {
    onMutate: () => {
      showNotification({
        id: 'load-data',
        loading: true,
        title: 'Loading your data',
        message: '',
        autoClose: false,
      });
    },
    onSuccess: () => {
      updateNotification({
        id: 'load-data',
        color: 'teal',
        title: 'Success',
        message: 'Team name updated.',
        icon: <Check size={16} />,
        autoClose: 2400,
      });
      queryClient.invalidateQueries([QUERY_KEYS.ADMIN_TEAMS]);
      setIsInputOn(false);
      updateTeamForm.reset();
    },
    onError: (err: AxiosError) => {
      updateNotification({
        id: 'load-data',
        color: 'red',
        title: 'Error',
        // @ts-ignore
        message: err.response.data,
        icon: <CircleX size={16} />,
        autoClose: 2400,
      });
    },
  });

  const zTeam = z.object({
    teamName: z
      .string()
      .min(5, { message: 'Must be a minimum of 5 characters' }),
  });

  const updateTeamForm = useForm({
    validate: zodResolver(zTeam),
    initialValues: {
      teamName: '',
    },
  });

  const submitHandler = ({ teamName }: Team): void => {
    const updatedTeam: Team = {
      id: team?.id,
      teamName: teamName,
      project: team?.project,
      memberList: team?.memberList,
      admin: team?.admin,
    };
    mutation.mutate(updatedTeam);
    setCurrentTeamName(teamName);
  };

  return (
    <>
      {isInputOn ? (
        <Group position="center">
          <form
            onSubmit={updateTeamForm.onSubmit((values) =>
              submitHandler(values)
            )}
          >
            <Box sx={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
              <TextInput
                size="lg"
                name="Team Name"
                placeholder={currentTeamName}
                {...updateTeamForm.getInputProps('teamName')}
              />
              <Button
                aria-label="save new team name"
                type="submit"
                sx={{ justifyContent: 'right' }}
                color={'#253746'}
                variant="white"
                p={1}
              >
                <DeviceFloppy size={30} />
              </Button>
            </Box>
          </form>
        </Group>
      ) : (
        <Group
          position="center"
          sx={{ display: 'flex', gap: '1px', alignItems: 'top' }}
        >
          <Title order={1} align="center">
            {currentTeamName}
          </Title>
          <ActionIcon
            aria-label="edit team name"
            sx={{ paddingBottom: '4px' }}
            onClick={() => {
              setIsInputOn(true);
            }}
          >
            <Pencil size={20} />
          </ActionIcon>
        </Group>
      )}
    </>
  );
}
