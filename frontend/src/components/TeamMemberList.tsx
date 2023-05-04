import { Avatar, Button, Group, Loader, Stack, Title } from '@mantine/core';
import { useLocalStorage } from '@mantine/hooks';
import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router-dom';
import { QUERY_KEYS } from '../enums/QueryKeys';
import LocalStorageUser from '../models/LocalStorageUser';
import { USER_KEY } from '../services/LoginService';
import { TeamServiceAPI } from '../services/TeamService';

export interface ITeamMemberListProps {
  teamId: number;
}

export default function TeamMemberList({ teamId }: ITeamMemberListProps) {
  const [currentUser] = useLocalStorage<LocalStorageUser>({ key: USER_KEY });

  const { data, isError, isLoading, error } = useQuery(
    [QUERY_KEYS.TEAM, teamId],
    () => TeamServiceAPI.getTeam(teamId)
  );

  if (isLoading) return <Loader />;

  if (isError) return <code>{JSON.stringify(error)}</code>;

  const names = data?.data.memberList!.map((member) => (
    <Group key={member.id}>
      <Avatar radius="xl">{member.firstName![0] + member.lastName![0]}</Avatar>
      <Title order={5}>
        {member.firstName} {member.lastName}
      </Title>
    </Group>
  ));

  return (
    <Stack justify="flex-start">
      <Title order={3}>Team Members</Title>

      <Stack spacing="lg">{names}</Stack>

      {currentUser.isAdmin ? (
        <Button
          aria-label="manage team"
          radius="xl"
          component={Link}
          to="/manage-teams"
        >
          Manage Team
        </Button>
      ) : null}
    </Stack>
  );
}
