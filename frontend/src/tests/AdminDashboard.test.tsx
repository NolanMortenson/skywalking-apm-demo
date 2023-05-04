import { useLocalStorage } from '@mantine/hooks';
import { useQuery } from '@tanstack/react-query';
import { fireEvent, render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import PageTitle from '../components/PageTitle';
import ProjectGraph from '../components/ProjectGraph';
import { DUMMY_DATA } from '../models/dummydata/ProjectDetailsApiResponse';
import AdminDashboard from '../pages/AdminDashboard';

jest.mock('recharts', () => {
  const OriginalModule = jest.requireActual('recharts');
  return {
    ...OriginalModule,
    ResponsiveContainer: ({ children }: any) => (
      <OriginalModule.ResponsiveContainer width={800} height={800}>
        {children}
      </OriginalModule.ResponsiveContainer>
    ),
  };
});

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
    data: { data: [DUMMY_DATA] },
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

describe('AdminDashboard page component rendering', () => {
  test('renders page title', () => {
    render(<PageTitle title={'Projects'} />);
    const pageTitle = screen.getByText(/projects/i);
    expect(pageTitle).toBeInTheDocument();
  });

  test('renders forecast graph', () => {
    render(<ProjectGraph graphData={DUMMY_DATA.graphData} />);
    expect(screen.getByText('Completion by Sprint %')).toBeInTheDocument();
    expect(screen.getByText('Count %')).toBeInTheDocument();
    expect(
      screen.getByText(/DISTRIBUTION OF TOTAL SPRINTS TO COMPLETE WORK/i)
    ).toBeInTheDocument();
  });

  test('renders details button', () => {
    render(<AdminDashboard />, { wrapper: BrowserRouter });
    const detailsButton = screen.getByText(/DETAILS/i);
    expect(detailsButton).toBeInTheDocument();
  });
});

test('details button redirects to corresponding project details page', () => {
  render(<AdminDashboard />, { wrapper: BrowserRouter });
  const detailsButtonRoute = screen.getByText(/DETAILS/i);
  fireEvent.click(detailsButtonRoute);
  expect(window.location.pathname).toBe(
    '/project-details/' + DUMMY_DATA.projectId
  );
});
