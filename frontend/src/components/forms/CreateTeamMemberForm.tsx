import {
  Box,
  Button,
  Center,
  Group,
  Modal,
  Paper,
  PasswordInput,
  Space,
  Switch,
  Text,
  TextInput,
} from '@mantine/core';
import { useForm, zodResolver } from '@mantine/form';
import { showNotification, updateNotification } from '@mantine/notifications';
import { useMutation } from '@tanstack/react-query';
import { AxiosError } from 'axios';
import { useState } from 'react';
import { Check, CircleX } from 'tabler-icons-react';
import { z } from 'zod';
import TeamMember from '../../models/TeamMember';
import { TeamMemberServiceAPI } from '../../services/TeamMemberService';

interface FormValues {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  isAdmin: boolean;
}

const CreateNewTeamMemberForm = () => {
  const [opened, setOpened] = useState(false);

  const mutation = useMutation(TeamMemberServiceAPI.addNewTeamMember, {
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
        message: `${
          createTeamMemberForm.values.isAdmin
            ? 'You have created a new Admin'
            : 'You have created a new Team Member'
        }`,
        icon: <Check size={16} />,
        autoClose: 2400,
      });
      createTeamMemberForm.reset();
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
  });

  const zUser = z.object({
    firstName: z.string().min(1, { message: 'Invalid First Name' }),
    lastName: z.string().min(1, { message: 'Invalid Last Name' }),
    email: z.string().email({ message: 'Invalid Email' }),
    password: z
      .string()
      .min(8, { message: 'Password must be a minimum of 8 characters' }),
  });

  const createTeamMemberForm = useForm<FormValues>({
    validate: zodResolver(zUser),
    initialValues: {
      email: '',
      firstName: '',
      lastName: '',
      password: '',
      isAdmin: false,
    },
  });

  const submitHandler = ({
    firstName,
    lastName,
    email,
    password,
    isAdmin,
  }: FormValues): void => {
    const newUser: TeamMember = {
      firstName,
      lastName,
      email,
      password,
      role: `${isAdmin ? 'ADMIN' : 'USER'}`,
    };
    mutation.mutate(newUser);
  };

  return (
    <Center>
      <Paper
        style={{
          minHeight: 350,
          minWidth: '25rem',
        }}
      >
        <form
          onSubmit={createTeamMemberForm.onSubmit((values) =>
            submitHandler(values)
          )}
        >
          <Modal
            centered
            withCloseButton={false}
            opened={opened}
            onClose={() => setOpened(false)}
          >
            <Center>
              <Text>You have selected for this user to be an Admin</Text>
            </Center>
            <Space h={8} />
            <Group position="center">
              <Button
                onClick={() => {
                  createTeamMemberForm.setFieldValue('isAdmin', false);
                  setOpened(false);
                }}
              >
                No
              </Button>
              <Button onClick={() => setOpened(false)}>Yes</Button>
            </Group>
          </Modal>

          <TextInput
            withAsterisk
            label="Email"
            placeholder="example@bah.com"
            {...createTeamMemberForm.getInputProps('email')}
          />
          <Space h={4} />
          <TextInput
            withAsterisk
            label="First Name"
            {...createTeamMemberForm.getInputProps('firstName')}
          />
          <Space h={4} />
          <TextInput
            withAsterisk
            label="Last Name"
            {...createTeamMemberForm.getInputProps('lastName')}
          />
          <Space h={4} />
          <PasswordInput
            withAsterisk
            label="Password"
            {...createTeamMemberForm.getInputProps('password')}
          />
          <Space h={8} />
          <Group spacing="xs">
            <Text weight="bold">Admin Privileges</Text>
            <Switch
              aria-label="admin privileges toggle switch"
              styles={{ track: { width: 40, minWidth: 0 } }}
              {...createTeamMemberForm.getInputProps('isAdmin')}
              onChange={(e) => {
                createTeamMemberForm.setFieldValue(
                  'isAdmin',
                  e.currentTarget.checked
                );
                console.log(e.currentTarget.checked);
                if (e.currentTarget.checked) {
                  setOpened(true);
                }
              }}
              checked={createTeamMemberForm.values.isAdmin}
            />
          </Group>
          <Box sx={{ display: 'flex', justifyContent: 'center' }} mt="md">
            <Button
              aria-label="create team member"
              color="darkBlue"
              type="submit"
            >
              Create New Team Member
            </Button>
          </Box>
        </form>
      </Paper>
    </Center>
  );
};

export default CreateNewTeamMemberForm;
