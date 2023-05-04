import { useLocation } from 'react-router-dom';
import NewOrEditProjectForm from '../components/forms/new-project-form/NewProjectForm';
import PageTitle from '../components/PageTitle';

const NewProjectPage = () => {
  const location = useLocation();
  return (
    <>
      <PageTitle
        title={
          location.pathname === '/new-project' ? 'New Project' : 'Edit Project'
        }
      />
      <NewOrEditProjectForm />
    </>
  );
};
export default NewProjectPage;
