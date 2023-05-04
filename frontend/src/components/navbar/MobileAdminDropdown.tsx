import { Box, NavLink } from '@mantine/core';
import { useNavigate } from 'react-router-dom';
import { ChevronDown, FilePlus, UserPlus, Users } from 'tabler-icons-react';

type Props = {};

const AdminDropdown = (props: Props) => {
  const navigate = useNavigate();
  return (
    <>
      <Box>
        <NavLink
          label="Admin"
          rightSection={<ChevronDown color="white" size={14} />}
        >
          <NavLink
            label="Create Team Member"
            onClick={() => navigate('/create-team-member')}
            icon={<UserPlus size={14} color="white" />}
          />
          <NavLink
            label="Manage Teams"
            onClick={() => navigate('/manage-teams')}
            icon={<Users size={14} color="white" />}
          />
          <NavLink
            label="New Project"
            onClick={() => navigate('/new-project')}
            icon={<FilePlus size={14} color="white" />}
          />
        </NavLink>
      </Box>
    </>
  );
};
export default AdminDropdown;
