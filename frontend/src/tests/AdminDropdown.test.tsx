import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { render, screen } from '@testing-library/react';
import { ReactNode } from 'react';
import AdminDropdown from '../components/navbar/AdminDropdown';

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

test('Admin link exists on navbar', () => {
  render(<AdminDropdown />, { wrapper: wrapper });
  const adminLink = screen.getByText('Admin');
  expect(adminLink).toBeInTheDocument();
});
