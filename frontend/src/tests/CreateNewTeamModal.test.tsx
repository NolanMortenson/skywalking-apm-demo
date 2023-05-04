import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { render, screen } from '@testing-library/react';
import { ReactNode } from 'react';
import CreateNewTeamModal from '../components/modals/CreateNewTeamModal';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: false,
    },
  },
});
const wrapper = ({ children }: { children: ReactNode }) => (
  <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
);

test('renders title on CreateNewTeamModal component', () => {
  render(<CreateNewTeamModal setIsOpen={() => true} />, {
    wrapper: wrapper,
  });
  const createNewTeamModalTitle = screen.getByText(/create new team/i);
  expect(createNewTeamModalTitle).toBeInTheDocument();
});

test('renders save button on CreateNewTeamModal component', () => {
  render(<CreateNewTeamModal setIsOpen={() => true} />, {
    wrapper: wrapper,
  });
  const SaveButton = screen.getByRole('button', { name: 'Save', hidden: true });
  expect(SaveButton).toBeInTheDocument();
});
