import { fireEvent, render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import GuestNavBar from '../components/navbar/GuestNavBar';

test('renders logo', () => {
  render(<GuestNavBar />, { wrapper: BrowserRouter });
  const logo = screen.getByRole('img');
  expect(logo).toBeInTheDocument();
});

test('login button links to login page', () => {
  render(<GuestNavBar />, { wrapper: BrowserRouter });
  const loginLink = screen.getByText(/login/i);
  fireEvent.click(loginLink);
  expect(window.location.pathname).toBe('/login');
});

test('logo links to login page', () => {
  render(<GuestNavBar />, { wrapper: BrowserRouter });
  const logoLink = screen.getByRole('img');
  fireEvent.click(logoLink);
  expect(window.location.pathname).toBe('/login');
});
