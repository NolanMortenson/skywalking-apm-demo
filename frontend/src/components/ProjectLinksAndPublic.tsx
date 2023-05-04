import {
  Button,
  CopyButton,
  Group,
  MantineTheme,
  Switch,
  Text,
  Title,
} from '@mantine/core';
import { useLocalStorage } from '@mantine/hooks';
import { showNotification } from '@mantine/notifications';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { Link, useParams } from 'react-router-dom';
import { Check, CircleX, Copy, ExternalLink } from 'tabler-icons-react';
import { QUERY_KEYS } from '../enums/QueryKeys';
import LocalStorageUser from '../models/LocalStorageUser';
import ProjectResult from '../models/ProjectResult';
import { USER_KEY } from '../services/LoginService';
import { ProjectServiceAPI } from '../services/ProjectService';

export interface IProjectLinksAndPublicProps {
  apiResponse: ProjectResult;
}

export interface PublicParams {
  id: number;
  isPublic: boolean;
}

export default function ProjectLinksAndPublic({
  apiResponse,
}: IProjectLinksAndPublicProps) {
  const [currentUser] = useLocalStorage<LocalStorageUser>({ key: USER_KEY });
  const { projectId } = useParams();
  const queryClient = useQueryClient();
  const isPublic = apiResponse.isPublic;
  const mutation = useMutation(ProjectServiceAPI.updatePublic, {
    onSuccess: () => {
      queryClient.invalidateQueries([QUERY_KEYS.PROJECT_RESULT], {
        refetchType: 'all',
      });
    },
    onError: () => {
      showNotification({
        title: 'Error',
        message: 'Unable to edit visibility of the project.',
        color: 'red',
        icon: <CircleX size={16} />,
      });
    },
  });

  const subtleButtonHover = (theme: MantineTheme) => ({
    root: {
      '&:hover': {
        color: 'white',
        backgroundColor: theme.colors.teal,
      },
    },
  });

  const publicOrPrivate = () => {
    mutation.mutate({
      id: +projectId!,
      isPublic: !isPublic,
    });
  };

  return (
    <Group position="apart">
      <Group>
        <Title order={2}>{apiResponse.projectName}</Title>
        {currentUser.isAdmin && (
          <Button
            aria-label="edit project"
            variant="subtle"
            styles={subtleButtonHover}
            leftIcon={<ExternalLink size={18} />}
            component={Link}
            to={`/edit-project/${apiResponse.projectId}`}
          >
            Edit Project
          </Button>
        )}
      </Group>
      <Group spacing="xs">
        {currentUser.isAdmin && (
          <>
            <Text weight="bold">Make Public</Text>
            <Switch
              aria-label="make project public toggle switch"
              styles={{ track: { width: 40, minWidth: 0 } }}
              checked={apiResponse.isPublic}
              onChange={publicOrPrivate}
            />
          </>
        )}

        <CopyButton
          value={`${process.env.REACT_APP_URL}/project-details/${apiResponse.projectId}/guest`}
        >
          {({ copied, copy }) => (
            <Button
              aria-label="copy url"
              styles={isPublic ? subtleButtonHover : () => ({})}
              compact
              onClick={copy}
              rightIcon={copied ? <Check size={16} /> : <Copy size={16} />}
              disabled={!isPublic}
              variant="subtle"
            >
              {copied ? 'Copied!' : 'Copy URL'}
            </Button>
          )}
        </CopyButton>
      </Group>
    </Group>
  );
}
