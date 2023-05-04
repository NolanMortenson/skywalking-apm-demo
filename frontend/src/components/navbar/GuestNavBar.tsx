import {
  Anchor,
  Box,
  Burger,
  Header,
  MediaQuery,
  NavLink,
  Paper,
  Transition,
} from '@mantine/core';
import { useState } from 'react';

import { useNavigate } from 'react-router-dom';
import { User } from 'tabler-icons-react';
import logo from '../../assets/sprintify_logo_50h.png';

type Props = {};

const GuestNavBar = (props: Props) => {
  const navigate = useNavigate();
  const [opened, setOpened] = useState(false);
  const handleOpen = () => {
    setOpened(true);
  };
  const handleClose = () => {
    setOpened(false);
  };
  return (
    <Header height={60}>
      <Box
        sx={(theme) => ({
          display: 'flex',
          justifyContent: 'space-between',
          backgroundColor: theme.colors.darkBlue,
          height: '100%',
        })}
      >
        <Anchor
          pt={6}
          ml={20}
          component="button"
          style={{ alignSelf: 'center' }}
          type="button"
          onClick={() => navigate('/login')}
          sx={{ '&:hover': { backgroundColor: '#253746' } }}
        >
          <img src={logo} alt="sprintify logo" width="175rem" height="40rem" />
        </Anchor>
        <MediaQuery smallerThan="sm" styles={{ display: 'none' }}>
          <Anchor
            component="button"
            type="button"
            onClick={() => navigate('/login')}
            px={20}
          >
            Login
          </Anchor>
        </MediaQuery>

        <MediaQuery largerThan="sm" styles={{ display: 'none' }}>
          <Burger
            mr={20}
            size="md"
            opened={opened}
            onClick={opened === false ? handleOpen : handleClose}
            color="white"
            sx={{ alignSelf: 'center' }}
          />
        </MediaQuery>
        <Transition
          mounted={opened}
          transition="fade"
          duration={200}
          timingFunction="ease"
        >
          {(styles) => (
            <MediaQuery largerThan="sm" styles={{ display: 'none' }}>
              <Paper
                shadow="md"
                style={{
                  ...styles,
                  position: 'absolute',
                  top: '100%',
                  left: '50%',
                  right: 0,
                  height: 'auto',
                  paddingTop: 10,
                  paddingRight: 0,
                  paddingBottom: 10,
                  paddingLeft: 0,
                  backgroundColor: '#253746',
                  border: 'none',
                }}
              >
                <NavLink
                  label="Login"
                  onClick={() => navigate('/login')}
                  icon={<User size={14} color="white" />}
                />
              </Paper>
            </MediaQuery>
          )}
        </Transition>
      </Box>
    </Header>
  );
};

export default GuestNavBar;
