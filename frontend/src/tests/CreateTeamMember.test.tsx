import { render, screen } from '@testing-library/react';
import PageTitle from '../components/PageTitle';
import { Button, Switch } from '@mantine/core';

import { ReactNode } from 'react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import CreateTeamMemberForm from '../components/forms/CreateTeamMemberForm';

test('renders page title component', () => {
  render(<PageTitle title={'CREATE TEAM MEMBER'} />);
  const pageTitle = screen.getByText('CREATE TEAM MEMBER');
  expect(pageTitle).toBeInTheDocument();
});

test('renders submit button', () => {
  render(<Button />);
  const submitButton = screen.getByRole('button');
  expect(submitButton).toBeInTheDocument();
});

test('renders switch', () => {
  render(<Switch />);
  const switchButton = screen.getByRole('checkbox');
  expect(switchButton).toBeInTheDocument();
});

test('renders CreateNewTeamMemberForm component', () => {
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
  render(<CreateTeamMemberForm />, { wrapper: wrapper });
  const createTeamMemberForm = screen.getByText(/email/i);
  expect(createTeamMemberForm).toBeInTheDocument();
});
