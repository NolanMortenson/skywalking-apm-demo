import {
  Anchor,
  Box,
  Burger,
  Header,
  MediaQuery,
  Paper,
  Transition,
} from '@mantine/core';
import { useLocalStorage } from '@mantine/hooks';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import logo from '../../assets/sprintify_logo_50h.png';
import LocalStorageUser from '../../models/LocalStorageUser';
import { USER_KEY } from '../../services/LoginService';
import AdminDropdown from './AdminDropdown';
import MobileAdminDropdown from './MobileAdminDropdown';
import MobileUserDropdown from './MobileUserDropdown';
import UserDropdown from './UserDropdown';

const NavBar = () => {
  const navigate = useNavigate();
  const [opened, setOpened] = useState(false);

  const [currentUser] = useLocalStorage<LocalStorageUser>({
    key: USER_KEY,
  });

  const isAdmin: boolean = currentUser.isAdmin;

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
          onClick={() => navigate('/')}
          sx={{ '&:hover': { backgroundColor: '#253746' } }}
        >
          <img src={logo} alt="sprintify logo" width="175rem" height="40rem" />
        </Anchor>
        <MediaQuery smallerThan="sm" styles={{ display: 'none' }}>
          <Box
            sx={(theme) => ({
              display: 'flex',
            })}
          >
            {isAdmin ? <AdminDropdown /> : ''}

            <Anchor
              component="button"
              type="button"
              onClick={() => navigate('/')}
              px={20}
            >
              Dashboard
            </Anchor>
            <UserDropdown currentUser={currentUser} />
          </Box>
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
                {isAdmin ? <MobileAdminDropdown /> : ''}
                <Anchor
                  component="button"
                  type="button"
                  onClick={() => navigate('/')}
                  py={10}
                  sx={{ width: '100%', display: 'flex', paddingLeft: '.5rem' }}
                >
                  Dashboard
                </Anchor>
                <MobileUserDropdown />
              </Paper>
            </MediaQuery>
          )}
        </Transition>
      </Box>
    </Header>
  );
};

export default NavBar;
