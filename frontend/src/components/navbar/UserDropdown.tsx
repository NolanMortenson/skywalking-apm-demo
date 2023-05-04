import { Avatar, Menu, NavLink } from '@mantine/core';
import { Link } from 'react-router-dom';
import { ChevronDown, Logout } from 'tabler-icons-react';
import { removeAuthHeaders } from '../../services/LoginService';

interface Props {
  currentUser: any;
}

const UserDropdown = ({ currentUser }: Props) => {
  const getInitials = () => {
    if (
      currentUser.firstName !== undefined &&
      currentUser.lastName !== undefined
    ) {
      const first = currentUser.firstName.substr(0, 1);
      const last = currentUser.lastName.substr(0, 1);
      return first.concat(last);
    }
  };

  return (
    <>
      <Menu shadow="md" width={131.5} offset={0} position="bottom-start">
        <Menu.Target>
          <NavLink
            noWrap
            mr={20}
            label={`Hi, ${currentUser.firstName}!`}
            rightSection={<ChevronDown color="white" size={14} />}
            icon={
              <Avatar
                radius="xl"
                size={31}
                variant="outline"
                color="white"
                alt={currentUser.firstName + currentUser.lastName}
              >
                {getInitials()}
              </Avatar>
            }
          />
        </Menu.Target>
        <Menu.Dropdown style={{ padding: 0 }}>
          {/* <Menu.Item component={Link} to="/profile" icon={<User size={14} />}>
            Profile
          </Menu.Item> */}
          <Menu.Item
            component={Link}
            to="/login"
            onClick={() => {
              removeAuthHeaders();
            }}
            icon={<Logout size={14} />}
          >
            Logout
          </Menu.Item>
        </Menu.Dropdown>
      </Menu>
    </>
  );
};
export default UserDropdown;
