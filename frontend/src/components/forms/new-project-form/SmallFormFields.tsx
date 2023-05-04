import { Group, NumberInput, Switch } from '@mantine/core';
import { DatePicker } from '@mantine/dates';
import { CalendarEvent } from 'tabler-icons-react';
import CustomTooltip from '../../CustomTooltip';
import { useProjectFormContext } from './NewProjectFormContext';

export const START_DATE_DESC = 'The date from which the forecast will begin.';
export const NUM_STORY_DESC = 'Number of stories to be forecast.';
export const PUBLIC_TOGGLE_DESC = 'Allows the project to be publicly viewable.';

export default function SmallFormFields() {
  const form = useProjectFormContext();

  return (
    <Group style={{ alignItems: 'flex-start' }} position="apart">
      <CustomTooltip
        label="Sprint Start Date"
        description={START_DATE_DESC}
        withAsterisk
      >
        <DatePicker
          withAsterisk
          allowFreeInput
          minDate={new Date()}
          aria-label="choose a date"
          placeholder="Choose a date"
          firstDayOfWeek="sunday"
          icon={<CalendarEvent size={20} />}
          {...form.getInputProps('forecastStartDate')}
        />
      </CustomTooltip>

      <CustomTooltip
        label="Number of Stories"
        description={NUM_STORY_DESC}
        withAsterisk
      >
        <NumberInput
          aria-label="input forvnumber of stories"
          min={1}
          withAsterisk
          {...form.getInputProps('storyCount')}
        />
      </CustomTooltip>

      <CustomTooltip label="Public" description={PUBLIC_TOGGLE_DESC}>
        <Switch
          aria-label={PUBLIC_TOGGLE_DESC}
          {...form.getInputProps('isPublic')}
          checked={form.values.isPublic}
        />
      </CustomTooltip>
    </Group>
  );
}
