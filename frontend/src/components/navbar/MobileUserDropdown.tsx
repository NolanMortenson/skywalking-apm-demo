import { Box, NavLink } from '@mantine/core';
import { useLocalStorage } from '@mantine/hooks';
import { useNavigate } from 'react-router-dom';
import { ChevronDown, Logout } from 'tabler-icons-react';
import LocalStorageUser from '../../models/LocalStorageUser';
import { removeAuthHeaders, USER_KEY } from '../../services/LoginService';

type Props = {};

const MobileUserDropdown = (props: Props) => {
  const [currentUser] = useLocalStorage<LocalStorageUser>({
    key: USER_KEY,
  });

  const navigate = useNavigate();
  return (
    <>
      <Box>
        <NavLink
          noWrap
          mr={20}
          label={`Hi, ${currentUser.firstName}!`}
          rightSection={<ChevronDown color="white" size={14} />}
        >
          {/* <NavLink
            label="Profile"
            onClick={() => navigate('/profile')}
            icon={<User size={14} color="white" />}
          /> */}
          <NavLink
            label="Logout"
            onClick={() => {
              removeAuthHeaders();
              navigate('/login');
            }}
            icon={<Logout size={14} color="white" />}
          />
        </NavLink>
      </Box>
    </>
  );
};
export default MobileUserDropdown;
