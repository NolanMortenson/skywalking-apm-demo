import PageTitle from '../components/PageTitle';
import CreateTeamMemberForm from '../components/forms/CreateTeamMemberForm';

const CreateNewTeamMember = () => {
  return (
    <>
      <PageTitle title={'Create Team Member'} />
      <CreateTeamMemberForm />
    </>
  );
};

export default CreateNewTeamMember;
