import { Button, Group, Space, Stack, TextInput, Title } from '@mantine/core';
import { useForm, zodResolver } from '@mantine/form';
import { useInputState, useLocalStorage } from '@mantine/hooks';
import { showNotification, updateNotification } from '@mantine/notifications';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { AxiosError } from 'axios';
import { Check, CircleX } from 'tabler-icons-react';
import { z } from 'zod';
import { QUERY_KEYS } from '../../enums/QueryKeys';
import LocalStorageUser from '../../models/LocalStorageUser';
import Team from '../../models/Team';
import { USER_KEY } from '../../services/LoginService';
import { TeamServiceAPI } from '../../services/TeamService';

interface Props {
  setIsOpen: (input: boolean) => void;
}

const CreateNewTeam = ({ setIsOpen }: Props) => {
  const [teamName, setTeamName] = useInputState('');
  const [currentUser] = useLocalStorage<LocalStorageUser>({ key: USER_KEY });

  const queryClient = useQueryClient();

  const useAddNewTeam = useMutation(TeamServiceAPI.addNewTeam, {
    onMutate: () => {
      showNotification({
        id: 'load-data',
        loading: true,
        title: 'Loading your data...',
        message: '',
        autoClose: false,
      });
    },
    onSuccess: () => {
      updateNotification({
        id: 'load-data',
        color: 'teal',
        title: 'Success',
        message: 'Successfully created team',
        icon: <Check size={16} />,
        autoClose: 2400,
      });
      queryClient.invalidateQueries([QUERY_KEYS.ADMIN_TEAMS]);
      setIsOpen(false);
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

  const zNewTeamNameInput = z.object({
    teamName: z
      .string()
      .min(5, { message: 'Must be a minimum of 5 characters' }),
  });

  const createNewTeamNameInput = useForm({
    validate: zodResolver(zNewTeamNameInput),
    initialValues: {
      teamName: '',
    },
  });

  const onSaveClicked = ({ teamName }: Team): void => {
    const newTeam: Team = {
      teamName,
      admin: { id: currentUser.id, role: 'ADMIN' },
    };
    useAddNewTeam.mutate(newTeam);
  };

  return (
    <>
      <Title order={2} align="center">
        Create New Team
      </Title>
      <Space h={20} />
      <form
        onSubmit={createNewTeamNameInput.onSubmit((values) =>
          onSaveClicked(values)
        )}
      >
        <Stack px={30} pt={0}>
          <TextInput
            aria-label="enter team name"
            placeholder="Enter Team Name"
            value={teamName}
            onChange={setTeamName}
            {...createNewTeamNameInput.getInputProps('teamName')}
          />
        </Stack>
        <Space h={20} />
        <Group position="apart">
          <Button
            aria-label="cancel and close modal"
            variant="outline"
            onClick={() => {
              setIsOpen(false);
            }}
          >
            Cancel
          </Button>
          <Button aria-label="save team" type="submit">
            Save
          </Button>
        </Group>
      </form>
    </>
  );
};
export default CreateNewTeam;
