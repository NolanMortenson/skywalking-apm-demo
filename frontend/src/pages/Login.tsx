import {
  Button,
  Center,
  Image,
  Paper,
  PasswordInput,
  Space,
  Stack,
  TextInput,
  Title,
} from '@mantine/core';
import { useForm, zodResolver } from '@mantine/form';
import { useLocalStorage } from '@mantine/hooks';
import { showNotification } from '@mantine/notifications';
import { useMutation } from '@tanstack/react-query';
import { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { LetterX } from 'tabler-icons-react';
import { z } from 'zod';
import logo from '../assets/sprintify_logo_200h.png';
import LocalStorageUser from '../models/LocalStorageUser';
import {
  axiosClient,
  LoginAPI,
  setAuthHeaders,
  USER_KEY,
} from '../services/LoginService';

interface InputValues {
  username: string;
  password: string;
}

const Login = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [, setCurrentUser] = useLocalStorage<LocalStorageUser>({
    key: USER_KEY,
  });

  useEffect(() => {
    const checkToken = async () => {
      try {
        const res = await axiosClient.get('/me');

        if (res.data.id !== null) {
          navigate('/');
        }

        setCurrentUser(res.data);
      } catch (err) {
        if (location.pathname !== '/login') {
          navigate('/login');
        }
        console.log(err);
      }
    };
    checkToken();

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [location]);

  const mutation = useMutation(LoginAPI.login, {
    onSuccess: (response) => {
      setAuthHeaders(response.data.token);
      navigate('/');
    },
    onError: () => {
      showNotification({
        color: 'red',
        title: 'Error',
        message: 'Invalid credentials',
        icon: <LetterX size={16} />,
        autoClose: 2400,
      });
    },
  });

  const zUser = z.object({
    username: z.string().email({ message: 'Invalid Email' }),
    password: z
      .string()
      .min(8, { message: 'Password must be a minimum of 8 characters' }),
  });

  const loginForm = useForm({
    validate: zodResolver(zUser),
    initialValues: {
      username: '',
      password: '',
    },
  });

  const handleSubmit = ({ username, password }: InputValues) => {
    mutation.mutate({ username, password });
  };

  return (
    <Center style={{ height: '100vh' }}>
      <Paper>
        <Center>
          <Image width={326} src={logo} alt="sprintify logo" />
        </Center>
        <Space h={32} />
        <Stack justify="center">
          <Title order={2} align="center">
            Secure Login For
          </Title>
          <Title align="center">Booz Allen Hamilton</Title>

          <form
            onSubmit={loginForm.onSubmit((values) => {
              handleSubmit(values);
            })}
          >
            <Stack>
              <TextInput
                label="Email"
                {...loginForm.getInputProps('username')}
              />
              <PasswordInput
                label="Password"
                {...loginForm.getInputProps('password')}
              />
            </Stack>

            <Space h={40} />
            <Center>
              <Button style={{ width: 130 }} type="submit">
                Login
              </Button>
            </Center>
          </form>
        </Stack>
      </Paper>
    </Center>
  );
};

export default Login;
