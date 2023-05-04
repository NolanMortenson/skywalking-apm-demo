import {
  ActionIcon,
  Badge,
  Box,
  Button,
  Center,
  Group,
  Loader,
  MantineTheme,
  Modal,
  Paper,
  Stack,
  Table,
  Text,
  Title,
  Tooltip,
  UnstyledButton,
} from '@mantine/core';
import { useForceUpdate, useLocalStorage, useMediaQuery } from '@mantine/hooks';
import {
  IconExternalLink,
  IconPencil,
  IconPlus,
  IconTrash,
} from '@tabler/icons';

import { useQuery } from '@tanstack/react-query';
import { useState } from 'react';
import { Link } from 'react-router-dom';
import CreateNewTeam from '../components/modals/CreateNewTeamModal';
import DeleteTeamModal from '../components/modals/DeleteTeamModal';
import EditTeam from '../components/modals/EditTeamModal';
import PageTitle from '../components/PageTitle';
import { QUERY_KEYS } from '../enums/QueryKeys';
import LocalStorageUser from '../models/LocalStorageUser';
import Team from '../models/Team';
import { USER_KEY } from '../services/LoginService';
import { TeamServiceAPI } from '../services/TeamService';
import generateBadgeText from '../utils/generateBadgeText';

const subtleButtonHover = (theme: MantineTheme) => ({
  root: { '&:hover': { backgroundColor: theme.colors.gray[3] } },
});

const ManageTeams = () => {
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const [deleteIsOpen, setDeleteIsOpen] = useState<boolean>(false);
  const [selectedTeam, setSelectedTeam] = useState<Team | undefined>();
  const [currentUser] = useLocalStorage<LocalStorageUser>({ key: USER_KEY });
  const overMediumSize = useMediaQuery('(min-width: 725px)');
  const overSmallSize = useMediaQuery('(min-width: 525px)');
  const tableSpacing = overMediumSize ? 'xl' : 'xs';
  const tablePadding = !overSmallSize
    ? 'xs'
    : !overMediumSize
    ? 'lg'
    : 'default';
  const forceUpdate = useForceUpdate();

  const { data, isLoading, isError, error } = useQuery(
    [QUERY_KEYS.ADMIN_TEAMS, currentUser.id],
    () => TeamServiceAPI.getAdminTeams(currentUser.id),
    { enabled: !!currentUser.id }
  );

  if (isLoading) return <Loader />;

  if (isError) {
    // TODO: Add pretty error notification
    return <code>error: {JSON.stringify(error)}</code>;
  }

  const rows = data.data.map((team) => (
    <tr key={team.id}>
      {/* NAME & MEMBERS */}
      <td>
        <Group noWrap position="apart">
          <Text transform="none">{team.teamName}</Text>
          <Badge style={{ color: 'white', minWidth: overSmallSize ? 75 : 30 }}>
            {generateBadgeText(overSmallSize, team.memberList?.length)}
          </Badge>
        </Group>
      </td>
      {/* PROJECT LINK / CREATE */}
      <td align="right">
        {team.project ? (
          <Button
          aria-label="navigate to project"
            variant="subtle"
            styles={subtleButtonHover}
            radius="sm"
            rightIcon={<IconExternalLink size={16} />}
            component={Link}
            to={'/project-details/' + team.project.id}
          >
            {team.project.projectName}
          </Button>
        ) : (
          <Button
          aria-label="create project"
            variant="subtle"
            styles={subtleButtonHover}
            radius="sm"
            rightIcon={<IconPlus size={16} />}
            component={Link}
            to="/new-project"
            state={{ team: team.id?.toString() }}
          >
            Create project
          </Button>
        )}
      </td>
      {/* EDIT */}
      <td>
        <Tooltip label="Edit team" radius="sm">
          <ActionIcon
            aria-label="edit team"
            radius="sm"
            onClick={() => {
              setSelectedTeam(team);
              setIsOpen(true);
            }}
          >
            <IconPencil size={20} color="black" stroke={1.5} />
          </ActionIcon>
        </Tooltip>
      </td>
      {/* DELETE */}
      <td>
        <Tooltip label="Delete team" radius="sm">
          <ActionIcon
          aria-label="delete team"
            radius="sm"
            onClick={() => {
              setSelectedTeam(team);
              setDeleteIsOpen(true);
            }}
          >
            <IconTrash color="darkRed" size={20} stroke={1.5} />
          </ActionIcon>
        </Tooltip>
      </td>
    </tr>
  ));

  return (
    <>
      <PageTitle title="Manage Teams" />
      <Modal
        centered
        padding="lg"
        opened={isOpen}
        withCloseButton={false}
        onClose={() => {
          setIsOpen(false);
        }}
      >
        {selectedTeam ? (
          <EditTeam
            team={selectedTeam}
            setIsOpen={setIsOpen}
            forceUpdate={forceUpdate}
          />
        ) : (
          <CreateNewTeam setIsOpen={setIsOpen} />
        )}
      </Modal>

      <DeleteTeamModal
        modalOpened={deleteIsOpen}
        setModalOpened={setDeleteIsOpen}
        selectedTeam={selectedTeam}
      />

      <Center style={{ marginTop: 50 }}>
        <Paper p={tablePadding}>
          <Stack spacing="xl">
            <Title order={2} sx={{ color: '#016e6c' }}>
              My Teams
            </Title>
            {data.data.length === 0 && (
              <Center>
                <Text>You do not have any teams yet.</Text>
              </Center>
            )}
            <Table
              highlightOnHover
              withBorder
              fontSize="lg"
              verticalSpacing="sm"
              horizontalSpacing={tableSpacing}
            >
              <tbody>
                {rows}
                <tr>
                  <td
                    colSpan={5}
                    align="center"
                    style={{ cursor: 'pointer' }}
                    onClick={() => {
                      setSelectedTeam(undefined);
                      setIsOpen(true);
                    }}
                  >
                    <Box aria-label="create new team button" component={UnstyledButton}>
                      <Group>
                        <Text weight="bold">Create New Team</Text>
                        <IconPlus size={22} />
                      </Group>
                    </Box>
                  </td>
                </tr>
              </tbody>
            </Table>
          </Stack>
        </Paper>
      </Center>
    </>
  );
};
export default ManageTeams;
