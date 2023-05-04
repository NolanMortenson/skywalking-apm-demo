import { fireEvent, render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import UserNavBar from '../components/navbar/UserNavBar';
import UserDropdown from '../components/navbar/UserDropdown';

test('renders logo', () => {
  render(<UserNavBar />, { wrapper: BrowserRouter });
  const logo = screen.getByRole('img');
  expect(logo).toBeInTheDocument();
});

test('navbar display username', () => {
  render(<UserDropdown currentUser={{}} />, { wrapper: BrowserRouter });
  const nameDisplay = screen.getByText(/hi/i);
  expect(nameDisplay).toBeInTheDocument();
});

test('dashboard button links to dashboard page', () => {
  render(<UserNavBar />, { wrapper: BrowserRouter });
  const dashboardLink = screen.getByText(/dashboard/i);
  fireEvent.click(dashboardLink);
  expect(window.location.pathname).toBe('/');
});

test('logo links to dashboard page', () => {
  render(<UserNavBar />, { wrapper: BrowserRouter });
  const logoLink = screen.getByRole('img');
  fireEvent.click(logoLink);
  expect(window.location.pathname).toBe('/');
});
