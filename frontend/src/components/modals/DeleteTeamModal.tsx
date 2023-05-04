import { Button, Group, Modal, Stack, Text } from '@mantine/core';
import { showNotification } from '@mantine/notifications';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { Check, CircleX } from 'tabler-icons-react';
import { QUERY_KEYS } from '../../enums/QueryKeys';
import Team from '../../models/Team';
import { TeamServiceAPI } from '../../services/TeamService';

export interface IDeleteTeamModalProps {
  modalOpened: boolean;
  setModalOpened: (input: boolean) => void;
  selectedTeam?: Team;
}

export default function DeleteTeamModal({
  setModalOpened,
  modalOpened,
  selectedTeam,
}: IDeleteTeamModalProps) {
  const queryClient = useQueryClient();

  const deleteTeam = useMutation(
    (id: number) => TeamServiceAPI.deleteTeam(id),
    {
      onSuccess: () => {
        queryClient.invalidateQueries([QUERY_KEYS.ADMIN_TEAMS]);
        showNotification({
          title: 'Success',
          message: 'Successfully deleted team',
          icon: <Check size={16} />,
        });
      },
      onError: () => {
        showNotification({
          title: 'Error',
          message: 'Error deleting team',
          color: 'red',
          icon: <CircleX size={16} />,
        });
      },
      onSettled: () => {
        setModalOpened(false);
      },
    }
  );

  return (
    <Modal
      centered
      withCloseButton={false}
      opened={modalOpened}
      onClose={() => {
        setModalOpened(false);
      }}
    >
      <Stack spacing="xl">
        <Text size="lg">
          Are you sure you want to delete team "
          {selectedTeam?.teamName.toUpperCase()}"?
        </Text>
        <Group position="apart">
          <Button
            aria-label="cancel and close modal"
            variant="outline"
            onClick={() => {
              setModalOpened(false);
            }}
          >
            Cancel
          </Button>
          <Button
            aria-label="delete team"
            onClick={() => {
              deleteTeam.mutate(selectedTeam!.id!);
            }}
            styles={(theme) => ({
              root: {
                backgroundColor: '#8B0000',
                '&:hover': {
                  backgroundColor: theme.fn.darken('#8B0000', 0.15),
                },
              },
            })}
          >
            Delete
          </Button>
        </Group>
      </Stack>
    </Modal>
  );
}
