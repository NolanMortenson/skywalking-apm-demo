import * as hooks from '@mantine/hooks';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { render, screen } from '@testing-library/react';
import { ReactNode } from 'react';
import { BrowserRouter } from 'react-router-dom';
import PageTitle from '../components/PageTitle';
import ProjectDetailsWrapper from '../components/ProjectDetailsWrapper';
import ProjectGraph from '../components/ProjectGraph';
import ProjectLinksAndPublic from '../components/ProjectLinksAndPublic';
import { DUMMY_DATA } from '../models/dummydata/ProjectDetailsApiResponse';
const { ResizeObserver } = window;

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

beforeEach(() => {
  window.ResizeObserver = jest.fn().mockImplementation(() => ({
    observe: jest.fn(),
    unobserve: jest.fn(),
    disconnect: jest.fn(),
  }));
});

afterEach(() => {
  window.ResizeObserver = ResizeObserver;
  jest.restoreAllMocks();
});

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: false,
    },
  },
});

const EMPTY_FN = () => {};

const wrapper = ({ children }: { children: ReactNode }) => (
  <BrowserRouter>
    <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  </BrowserRouter>
);

test('Renders Graph', () => {
  render(<ProjectGraph graphData={DUMMY_DATA.graphData} />);

  // Popping this call in the event loop makes the test pass
  expect(screen.getByText('Completion by Sprint %')).toBeInTheDocument();
  expect(screen.getByText('Count %')).toBeInTheDocument();
  expect(
    screen.getByText(/DISTRIBUTION OF TOTAL SPRINTS TO COMPLETE WORK/i)
  ).toBeInTheDocument();
});

test("Copy URL Button is disabled when the project isn't public", () => {
  DUMMY_DATA.isPublic = false;
  render(<ProjectLinksAndPublic apiResponse={DUMMY_DATA} />, { wrapper });

  // eslint-disable-next-line testing-library/no-node-access
  expect(screen.getByText('Copy URL').closest('button')).toBeDisabled();
});

test('Copy URL Button is enabled when project is public', () => {
  DUMMY_DATA.isPublic = true;
  render(<ProjectLinksAndPublic apiResponse={DUMMY_DATA} />, { wrapper });

  // eslint-disable-next-line testing-library/no-node-access
  expect(screen.getByText('Copy URL').closest('button')).toBeEnabled();
});

test('Title Shows Correct Team Name', () => {
  render(<PageTitle title={DUMMY_DATA.teamName} />);
  expect(screen.getByText('Team Name')).toBeInTheDocument();
});

test("NonAdmin doesn't see admin buttons", () => {
  jest
    .spyOn(hooks, 'useLocalStorage')
    .mockImplementation(() => [{ isAdmin: false }, EMPTY_FN, EMPTY_FN]);

  render(<ProjectDetailsWrapper apiResponse={DUMMY_DATA} />, { wrapper });
  expect(screen.queryByText(/edit project/i)).not.toBeInTheDocument();
  expect(screen.queryByText(/make public/i)).not.toBeInTheDocument();
});

test('Admin sees admin buttons', () => {
  jest
    .spyOn(hooks, 'useLocalStorage')
    .mockImplementation(() => [{ isAdmin: true }, EMPTY_FN, EMPTY_FN]);

  render(<ProjectDetailsWrapper apiResponse={DUMMY_DATA} />, { wrapper });
  expect(screen.getByText(/edit project/i)).toBeInTheDocument();
  expect(screen.getByText(/make public/i)).toBeInTheDocument();
});
