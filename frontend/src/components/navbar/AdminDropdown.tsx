import { Menu, NavLink } from '@mantine/core';
import { Link } from 'react-router-dom';
import { ChevronDown, FilePlus, UserPlus, Users } from 'tabler-icons-react';

type Props = {};

const AdminDropdown = (props: Props) => {
  return (
    <>
      <Menu shadow="md" width={200} offset={0} position="bottom-start">
        <Menu.Target>
          <NavLink
            label="Admin"
            rightSection={<ChevronDown color="white" size={14} />}
          />
        </Menu.Target>
        <Menu.Dropdown style={{padding: 0}}>
          <Menu.Item
            component={Link}
            to="/create-team-member"
            icon={<UserPlus size={14} />}
          >
            Create Team Member
          </Menu.Item>
          <Menu.Item
            component={Link}
            to="/manage-teams"
            icon={<Users size={14} />}
          >
            Manage Teams
          </Menu.Item>
          <Menu.Item
            component={Link}
            to="/new-project"
            icon={<FilePlus size={14} />}
          >
            New Project
          </Menu.Item>
        </Menu.Dropdown>
      </Menu>
    </>
  );
};
export default AdminDropdown;
