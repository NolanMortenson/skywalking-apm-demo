import { AppShell, Header, Loader } from '@mantine/core';
import { useLocalStorage } from '@mantine/hooks';
import { useEffect, useState } from 'react';
import { Route, Routes, useLocation, useNavigate } from 'react-router-dom';
import './App.css';
import GuestNavBar from './components/navbar/GuestNavBar';
import UserNavBar from './components/navbar/UserNavBar';
import LocalStorageUser from './models/LocalStorageUser';
import { NotFoundTitle } from './pages/404';
import AdminDashboard from './pages/AdminDashboard';
import CreateNewTeamMember from './pages/CreateTeamMember';
import GuestDashboard from './pages/GuestDashboard';
import KitchenSink from './pages/KitchenSink';
import Login from './pages/Login';
import ManageTeams from './pages/ManageTeams';
import NewProject from './pages/NewProject';
import ProjectDetails from './pages/ProjectDetails';
import {
  axiosClient,
  removeAuthHeaders,
  USER_KEY,
} from './services/LoginService';

function App() {
  const navigate = useNavigate();
  const location = useLocation();
  const [isLoading, setLoading] = useState(true);
  const [currentUser, setCurrentUser] = useLocalStorage<LocalStorageUser>({
    key: USER_KEY,
  });
  const isLoggedIn: boolean = !!currentUser?.id;

  const routes = (
    <Routes>
      <Route path="*" element={<NotFoundTitle />} />
      <Route path="/" element={<AdminDashboard />} />
      <Route path="/login" element={<Login />} />
      <Route path="/manage-teams" element={<ManageTeams />} />
      <Route path="/new-project" element={<NewProject />} />
      <Route path="/edit-project/:projectId" element={<NewProject />} />
      <Route
        path="/project-details/:projectId/guest"
        element={<GuestDashboard />}
      />
      <Route path="/project-details/:projectId" element={<ProjectDetails />} />
      <Route path="/create-team-member" element={<CreateNewTeamMember />} />
      <Route path="/kitchen-sink" element={<KitchenSink />} />
    </Routes>
  );

  useEffect(() => {
    const logout = () => {
      removeAuthHeaders();
      navigate('/login');
    };
    const checkToken = async () => {
      try {
        const isLogin = location.pathname === '/login';
        const isPublic = location.pathname.split('/').pop() === 'guest';

        if (isPublic) return;

        const res = await axiosClient.get('/me');

        if (!isLogin && !isPublic && res.data.id === null) {
          logout();
        }

        setCurrentUser(res.data);
      } catch (err: any) {
        logout();
      } finally {
        setLoading(false);
      }
    };
    checkToken();

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [location.pathname]);

  return !isLoading ? (
    <>
      <AppShell
        padding="md"
        header={
          location.pathname !== '/login' ? (
            <Header height={60} p="xl">
              {!!isLoggedIn ? <UserNavBar /> : <GuestNavBar />}
            </Header>
          ) : undefined
        }
        styles={(theme) => ({
          root: {
            backgroundColor:
              theme.colorScheme === 'light'
                ? theme.colors.lightGray[0]
                : theme.colors.black,
          },
        })}
      >
        <div className="App">{routes}</div>
      </AppShell>
    </>
  ) : (
    <Loader />
  );
}

export default App;
