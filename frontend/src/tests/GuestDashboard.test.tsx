import { render, screen } from '@testing-library/react';
import PageTitle from '../components/PageTitle';
import ProjectGraph from '../components/ProjectGraph';
import ProjectGrid from '../components/ProjectGrid';
import { DUMMY_DATA } from '../models/dummydata/ProjectDetailsApiResponse';

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

describe('GuestDashboard page component rendering', () => {
  test('renders page title correctly', () => {
    render(
      <PageTitle title={DUMMY_DATA.teamName + ' - ' + DUMMY_DATA.projectName} />
    );
    const pageTitle = screen.getByText('Team Name - Project Name');
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

  test('renders liklihood grid', () => {
    render(<ProjectGrid tableData={DUMMY_DATA.graphData} />);
    expect(screen.getByText(/LIKELIHOOD GRID/i)).toBeInTheDocument();
  });
});
