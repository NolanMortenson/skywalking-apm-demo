import { Collapse, NumberInput, SimpleGrid } from '@mantine/core';
import { DatePicker } from '@mantine/dates';
import { CalendarEvent } from 'tabler-icons-react';
import CustomTooltip from '../../CustomTooltip';
import { useProjectFormContext } from './NewProjectFormContext';

export interface IAdvancedDropdownProps {
  opened: boolean;
}

export const SPRINT_DURATION_DESC = 'The number of calendar days in a sprint.';
export const NUM_WORKDAYS_DESC = 'The number of work days in a normal sprint.';
export const NUM_SIMS_DESC =
  'The number of simulations to perform on the historical data. Should be no less than 1000.';
export const FIRST_SPRINT_DATE_DESC =
  'The date of the first sprint represented in the historical data that is used as the basis for the forecast.';
export const FIRST_SPRINT_NUM_DESC =
  'The sprint number of the first sprint represented in the historical data that is used as the basis for the forecast.';
export const SPRINT_HISTORY_DESC =
  'The number of sprints of historical data to evaluate from which a forecast will be calculated. This should be a number, by practice, somewhere between 12-20, as too few sprints and the forecast does not have enough variance and too many sprints may not accurately represent the current state of delivery for the team.';

export default function AdvancedDropdown({ opened }: IAdvancedDropdownProps) {
  const form = useProjectFormContext();

  return (
    <Collapse in={opened}>
      {
        <SimpleGrid cols={2}>
          <CustomTooltip
            label="Sprint Duration"
            description={SPRINT_DURATION_DESC}
          >
            <NumberInput {...form.getInputProps('sprintDuration')} />
          </CustomTooltip>

          <CustomTooltip
            label="Number of Work Days in Sprint"
            description={NUM_WORKDAYS_DESC}
          >
            <NumberInput min={1} {...form.getInputProps('workDaysInSprint')} />
          </CustomTooltip>

          <CustomTooltip
            label="Project Simulations"
            description={NUM_SIMS_DESC}
            multiline
            width={200}
          >
            <NumberInput
              min={1000}
              {...form.getInputProps('projectSimulations')}
            />
          </CustomTooltip>

          <CustomTooltip
            label="Historical Data First Sprint Date"
            description={FIRST_SPRINT_DATE_DESC}
            multiline
            width={200}
          >
            <DatePicker
            aria-label="choose a date"
              icon={<CalendarEvent size={20} />}
              allowFreeInput
              placeholder="Choose a date"
              firstDayOfWeek="sunday"
              {...form.getInputProps('historicalDataStartDate')}
            />
          </CustomTooltip>

          <CustomTooltip
            label="Historical Data First Sprint Number"
            description={FIRST_SPRINT_NUM_DESC}
            multiline
            width={200}
          >
            <NumberInput
              min={1}
              {...form.getInputProps('historicalDataFirstSprintId')}
            />
          </CustomTooltip>

          <CustomTooltip
            label="Sprint History"
            description={SPRINT_HISTORY_DESC}
            multiline
            width={200}
          >
            <NumberInput
              min={12}
              max={20}
              {...form.getInputProps('sprintHistoryToEvaluate')}
            />
          </CustomTooltip>
        </SimpleGrid>
      }
    </Collapse>
  );
}
