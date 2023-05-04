import {
  Box,
  Button,
  Group,
  Select,
  Space,
  Stack,
  Table,
  Text,
} from '@mantine/core';
import { useForm } from '@mantine/form';
import { useLocalStorage } from '@mantine/hooks';
import { showNotification, updateNotification } from '@mantine/notifications';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { AxiosError } from 'axios';
import { forwardRef, useState } from 'react';
import { Link } from 'react-router-dom';
import { Check, CircleX, Plus, Trash } from 'tabler-icons-react';
import { QUERY_KEYS } from '../../enums/QueryKeys';
import LocalStorageUser from '../../models/LocalStorageUser';
import Team from '../../models/Team';
import { USER_KEY } from '../../services/LoginService';
import { TeamMemberServiceAPI } from '../../services/TeamMemberService';
import EditableTeamName from '../EditableTeamName';

interface Props {
  team?: Team;
  setIsOpen: (input: boolean) => void;
  forceUpdate: () => void;
}

interface ItemProps extends React.ComponentPropsWithoutRef<'div'> {
  label: string;
  email: string;
}

const errorSideEffect = (err: AxiosError<any>) => {
  updateNotification({
    id: 'load-data',
    color: 'red',
    title: 'Error',
    message: err.response?.data,
    icon: <CircleX size={16} />,
    autoClose: 2400,
  });
};

const mutateSideEffect = () => {
  showNotification({
    id: 'load-data',
    loading: true,
    title: 'Loading your data',
    message: '',
    autoClose: false,
  });
};

const EditTeam = ({ team, setIsOpen, forceUpdate }: Props) => {
  const [currentUser] = useLocalStorage<LocalStorageUser>({ key: USER_KEY });
  const [selectedTeamMember] = useState<any | null>(null);
  const [teamMemberList, setTeamMemberList] = useState(team?.memberList);

  const { data, isLoading, isError } = useQuery(
    [QUERY_KEYS.ALL_TEAM_MEMBER_LIST, currentUser.id],
    () => TeamMemberServiceAPI.getAllTeamMembers(),
    { enabled: !!currentUser.id }
  );

  const queryClient = useQueryClient();

  const invalidateQueries = () => {
    const invalidations = [
      queryClient.invalidateQueries([QUERY_KEYS.ALL_TEAM_MEMBER_LIST]),
      queryClient.invalidateQueries([QUERY_KEYS.ADMIN_TEAMS]),
      queryClient.invalidateQueries([QUERY_KEYS.TEAM, team?.id], {
        refetchType: 'all',
      }),
    ];
    return Promise.all(invalidations);
  };

  const removeFromTeam = useMutation(
    TeamMemberServiceAPI.removeTeamFromTeamMember,
    {
      onMutate: mutateSideEffect,
      onSuccess: async (TeamApiResponse) => {
        updateNotification({
          id: 'load-data',
          color: 'teal',
          title: 'Success',
          message: 'Successfully deleted from team',
          icon: <Check size={16} />,
          autoClose: 2400,
        });
        setTeamMemberList(
          teamMemberList?.filter(
            (teamMember) => teamMember.id !== TeamApiResponse.data.id
          )
        );
        await invalidateQueries();
      },
      onError: errorSideEffect,
    }
  );

  const useAddMemberToTeam = useMutation(
    TeamMemberServiceAPI.addTeamToTeamMember,
    {
      onMutate: mutateSideEffect,
      onSuccess: async (TeamApiResponse) => {
        updateNotification({
          id: 'load-data',
          color: 'teal',
          title: 'Success',
          message: 'Successfully added team member',
          icon: <Check size={16} />,
          autoClose: 2400,
        });
        console.log(TeamApiResponse.data.id);
        setTeamMemberList([...teamMemberList!, TeamApiResponse.data]);
        form.reset();
        await invalidateQueries();
      },
      onError: errorSideEffect,
    }
  );

  const form = useForm({
    initialValues: {
      currentTeamMemberId: '',
    },
    validate: {
      currentTeamMemberId: (value) =>
        value ? null : 'Please make a selection',
    },
  });

  if (isLoading || isError) {
    return <p>...</p>;
  }

  let allTeamMembers = data.data.filter(
    (teamMember) => teamMember.role !== 'ADMIN'
  );

  let availableTMList = allTeamMembers.filter((tm) => !tm.team);

  const teamMembers = availableTMList.map((teamMember, i) => ({
    label: `${teamMember.firstName} ${teamMember.lastName}`,
    email: teamMember.email,
    value: teamMember.id!.toString(),
  }));

  const SelectItem = forwardRef<HTMLDivElement, ItemProps>(
    ({ label, email, ...others }: ItemProps, ref) => (
      <div ref={ref} {...others}>
        <Text>{label}</Text>
        <Text size="xs">{email}</Text>
      </div>
    )
  );

  const onHandleClicked = ({ ...values }: any): void => {
    useAddMemberToTeam.mutate({
      teamId: team?.id!,
      userId: values.currentTeamMemberId,
    });
  };

  return (
    <>
      <EditableTeamName team={team} />

      <Space h={8} />

      {allTeamMembers.length === 0 ? (
        <>
          <Stack>
            <Text align="center" weight={700} transform="none">
              There are no team members.
            </Text>

            <Button
              aria-label="create team member"
              sx={{ width: '10rem', margin: 'auto' }}
              component={Link}
              to={`/create-new-team-member`}
            >
              Create Team Member
            </Button>
          </Stack>
        </>
      ) : (
        <>
          {teamMemberList?.length === 0 ? (
            <Text weight={700}>Choose a team member from the dropdown:</Text>
          ) : (
            <Table verticalSpacing="xs">
              <thead>
                <tr>
                  <td>
                    <Text weight={700}>Team Members:</Text>
                  </td>
                </tr>
              </thead>
              <tbody>
                {teamMemberList?.map((teamMember, i) => (
                  <tr key={i}>
                    <td>
                      {teamMember.firstName} {teamMember.lastName}
                    </td>
                    <td>({teamMember.email})</td>
                    <td align="right">
                      <Button
                        aria-label="delete team member from team"
                        variant="white"
                        sx={{ color: '#8B0000' }}
                        styles={{ root: { '&:hover': { color: 'red' } } }}
                        onClick={() => removeFromTeam.mutate(teamMember.id!)}
                      >
                        <Trash size={18} cursor="pointer" strokeWidth={1.25} />
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          )}
          <form onSubmit={form.onSubmit((values) => onHandleClicked(values))}>
            <Space h={15} />
            <Group sx={{ gap: '3px' }}>
              <Select
                sx={{ width: '21rem' }}
                data={teamMembers}
                searchable
                clearable
                size="md"
                maxDropdownHeight={280}
                nothingFound="No Available Team Members"
                placeholder="Add a Team Member"
                aria-label="select team member"
                itemComponent={SelectItem}
                value={selectedTeamMember}
                {...form.getInputProps('currentTeamMemberId')}
              />

              <Button
                aria-label="add team member to team"
                variant="white"
                compact
                type="submit"
                styles={{ root: { '&:hover': { color: '#016e6c' } } }}
              >
                <Plus size={20} strokeWidth={3} />
              </Button>
            </Group>
          </form>

          <Space h={20} />
          <Box
            pr={15}
            pb={8}
            sx={{
              display: 'flex',
              justifyContent: 'right',
            }}
          >
            <Button
              aria-label="close modal"
              size="xs"
              onClick={() => {
                setIsOpen(false);
                forceUpdate();
              }}
            >
              Close
            </Button>
          </Box>
        </>
      )}
    </>
  );
};
export default EditTeam;
