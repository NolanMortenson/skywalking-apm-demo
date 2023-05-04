import { useLocalStorage } from '@mantine/hooks';
import { useQuery } from '@tanstack/react-query';
import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import EditTeamModal from '../components/modals/EditTeamModal';

const mockedUseQuery = useQuery as jest.Mock<any>;
const mockedLocalStorage = useLocalStorage as jest.Mock<any>;

jest.mock('@tanstack/react-query');
jest.mock('@mantine/hooks');

beforeEach(() => {
  window.ResizeObserver = jest.fn().mockImplementation(() => ({
    observe: jest.fn(),
    unobserve: jest.fn(),
    disconnect: jest.fn(),
  }));

  mockedUseQuery.mockImplementation(() => ({
    data: { data: [] },
    isLoading: false,
    isError: false,
    error: {},
  }));

  mockedLocalStorage.mockImplementation(() => [
    {
      id: 1,
      firstName: 'test',
      lastName: 'last',
      email: 'email@email.com',
      isAdmin: true,
    },
  ]);
});

afterEach(() => {
  window.ResizeObserver = ResizeObserver;
  jest.restoreAllMocks();
  jest.clearAllMocks();
});

test('renders close button on EditTeamModal component', () => {
  render(
    <EditTeamModal
      forceUpdate={() => true}
      setIsOpen={(input: boolean) => true}
    />,
    { wrapper: BrowserRouter }
  );
  const closeButton = screen.getByText(/create team member/i);
  expect(closeButton).toBeInTheDocument();
});

test('renders heading on EditTeamModal component', () => {
  render(
    <EditTeamModal
      forceUpdate={() => true}
      setIsOpen={(input: boolean) => true}
    />,
    { wrapper: BrowserRouter }
  );
  const heading = screen.getByText(/There are no team members/i);
  expect(heading).toBeInTheDocument();
});
