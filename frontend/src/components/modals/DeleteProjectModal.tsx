import { Button, Group, Stack, Text } from '@mantine/core';
import { showNotification } from '@mantine/notifications';
import { IconCheck, IconCircleX } from '@tabler/icons';
import { useMutation } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { ProjectServiceAPI } from '../../services/ProjectService';

export interface IDeleteProjectModalProps {
  setOpened: (opened: boolean) => void;
  projectName: string;
  projectId: string;
}

export default function DeleteProjectModal({
  setOpened,
  projectName,
  projectId,
}: IDeleteProjectModalProps) {
  // do stuff
  // when something happens
  // setOpened(false)
  const navigate = useNavigate();
  const mutation = useMutation(ProjectServiceAPI.deleteProject, {
    onSuccess: () => {
      setOpened(false);
      navigate('/');
      showNotification({
        title: 'Success',
        message: 'Successfully deleted project',
        icon: <IconCheck size={16} />,
      });
    },
    onError: () => {
      showNotification({
        title: 'Error',
        message: 'Did not successfully delete project',
        icon: <IconCircleX size={16} />,
      });
    },
  });

  return (
    <Stack spacing="xl">
      <Text size="lg">
        Are you sure you want to delete project "{projectName}"?
      </Text>
      <Group position="apart">
        <Button
          aria-label="cancel and close modal"
          variant="outline"
          onClick={() => {
            setOpened(false);
          }}
        >
          Cancel
        </Button>
        <Button
          aria-label="delete project"
          onClick={() => {
            mutation.mutate(projectId);
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
  );
}
