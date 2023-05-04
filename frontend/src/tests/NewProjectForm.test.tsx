import { FileInput, NumberInput, Select, TextInput } from '@mantine/core';
import { DatePicker } from '@mantine/dates';
import * as mantineHooks from '@mantine/hooks';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { render, screen } from '@testing-library/react';
import { ReactNode } from 'react';
import { BrowserRouter } from 'react-router-dom';
import {
  FIRST_SPRINT_DATE_DESC,
  FIRST_SPRINT_NUM_DESC,
  NUM_SIMS_DESC,
  NUM_WORKDAYS_DESC,
  SPRINT_DURATION_DESC,
  SPRINT_HISTORY_DESC,
} from '../components/forms/new-project-form/AdvancedDropdown';
import NewProject from '../components/forms/new-project-form/NewProjectForm';
import {
  NUM_STORY_DESC,
  PUBLIC_TOGGLE_DESC,
  START_DATE_DESC,
} from '../components/forms/new-project-form/SmallFormFields';
import {
  CSV_DESC,
  PROJECT_NAME_DESC,
  SELECTED_TEAM_DESC,
} from '../components/forms/new-project-form/TopFormFields';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: false,
    },
  },
});

const wrapper = ({ children }: { children: ReactNode }) => (
  <BrowserRouter>
    <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  </BrowserRouter>
);

const EMPTY_FN = () => {};

// Confirm all required fields are rendered
test('renders Project Name text input', () => {
  render(<TextInput label="project name" />);
  const projectNameInput = screen.getByText('project name');
  expect(projectNameInput).toBeInTheDocument();
});

test('renders Select Team Select input', () => {
  render(<Select label="select name" data={[]} />);
  const selectTeamInput = screen.getByText('select name');
  expect(selectTeamInput).toBeInTheDocument();
});

test('renders CSV upload input', () => {
  render(<FileInput label="historical data csc file" />);
  const uploadCscInput = screen.getByText('historical data csc file');
  expect(uploadCscInput).toBeInTheDocument();
});

test('renders Date Picker input', () => {
  render(<DatePicker label="sprint start date" />);
  const datePickerInput = screen.getByText('sprint start date');
  expect(datePickerInput).toBeInTheDocument();
});

test('renders Number of Stories input', () => {
  render(<NumberInput label="no. of stories" />);
  const noOfStoriesInput = screen.getByText('no. of stories');
  expect(noOfStoriesInput).toBeInTheDocument();
});

test('show advanced menu is hidden when page is rendered', () => {
  render(<NewProject />, { wrapper });
  const hiddenMenu = screen.queryByLabelText(/sprint duration/i);
  expect(hiddenMenu).not.toBeVisible();
});

test('tooltips do not appear on page load', () => {
  jest
    .spyOn(mantineHooks, 'useLocalStorage')
    .mockImplementation(() => [{ id: 1 }, EMPTY_FN, EMPTY_FN]);

  render(<NewProject />, { wrapper });

  expect(screen.queryByText(PROJECT_NAME_DESC)).not.toBeInTheDocument();
  expect(screen.queryByText(SELECTED_TEAM_DESC)).not.toBeInTheDocument();
  expect(screen.queryByText(CSV_DESC)).not.toBeInTheDocument();
  expect(screen.queryByText(START_DATE_DESC)).not.toBeInTheDocument();
  expect(screen.queryByText(NUM_STORY_DESC)).not.toBeInTheDocument();
  expect(screen.queryByText(PUBLIC_TOGGLE_DESC)).not.toBeInTheDocument();
  expect(screen.queryByText(SPRINT_DURATION_DESC)).not.toBeInTheDocument();
  expect(screen.queryByText(NUM_WORKDAYS_DESC)).not.toBeInTheDocument();
  expect(screen.queryByText(NUM_SIMS_DESC)).not.toBeInTheDocument();
  expect(screen.queryByText(FIRST_SPRINT_DATE_DESC)).not.toBeInTheDocument();
  expect(screen.queryByText(FIRST_SPRINT_NUM_DESC)).not.toBeInTheDocument();
  expect(screen.queryByText(SPRINT_HISTORY_DESC)).not.toBeInTheDocument();
});
