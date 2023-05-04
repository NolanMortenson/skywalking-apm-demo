import { Button, NumberInput, Stack, Title } from '@mantine/core';
import { DatePicker } from '@mantine/dates';
import { useInputState, useLocalStorage } from '@mantine/hooks';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import dayjs from 'dayjs';
import { useState } from 'react';
import { CalendarEvent } from 'tabler-icons-react';
import { QUERY_KEYS } from '../enums/QueryKeys';
import LocalStorageUser from '../models/LocalStorageUser';
import { USER_KEY } from '../services/LoginService';
import { ProjectServiceAPI } from '../services/ProjectService';
import CustomTooltip from './CustomTooltip';
import {
  NUM_STORY_DESC,
  START_DATE_DESC,
} from './forms/new-project-form/SmallFormFields';

export interface IProjectParametersProps {
  startDate: string;
  stories: number;
  projectId: number;
}

export default function ProjectParameters({
  startDate,
  stories,
  projectId,
}: IProjectParametersProps) {
  const queryClient = useQueryClient();
  const [currentUser] = useLocalStorage<LocalStorageUser>({ key: USER_KEY });
  const [startDateInput, setStartDateInput] = useInputState<Date | null>(
    new Date(startDate)
  );
  const [storiesInput, setStoriesInput] = useState(stories);
  const [loading, setLoading] = useState<boolean>(false);

  const mutation = useMutation(ProjectServiceAPI.rerunSimulation, {
    onMutate: () => {
      setLoading(true);
    },
    onSuccess: async () => {
      await queryClient.invalidateQueries(
        [QUERY_KEYS.PROJECT_RESULT, projectId.toString()],
        {
          refetchType: 'all',
        }
      );
      await queryClient.invalidateQueries(
        [QUERY_KEYS.PROJECT_DETAILS, projectId.toString()],
        {
          refetchType: 'all',
        }
      );
    },
    onSettled: () => {
      setLoading(false);
    },
  });

  const rerunSimulationHandler = () => {
    const formattedDate = dayjs(startDateInput).format('YYYY-MM-DD');

    mutation.mutate({
      startDate: formattedDate,
      storyCount: storiesInput,
      projectId,
    });
  };

  return (
    <Stack justify="flex-start">
      <Title order={3}>Project Info</Title>

      <CustomTooltip label="Start Date:" description={START_DATE_DESC}>
        <DatePicker
          value={startDateInput}
          onChange={setStartDateInput}
          clearable={false}
          disabled={!currentUser.isAdmin}
          allowFreeInput
          minDate={new Date()}
          firstDayOfWeek="sunday"
          icon={<CalendarEvent />}
        />
      </CustomTooltip>

      <CustomTooltip label="Number of Stories:" description={NUM_STORY_DESC}>
        <NumberInput
          value={storiesInput}
          onChange={(e) => {
            setStoriesInput(e!);
          }}
          disabled={!currentUser.isAdmin}
          size="md"
        />
      </CustomTooltip>

      {currentUser.isAdmin ? (
        <Button
        aria-label="rerun simulation"
          onClick={rerunSimulationHandler}
          disabled={
            stories === storiesInput &&
            new Date(startDate).toDateString() ===
              startDateInput?.toDateString()
          }
          loading={loading}
        >
          Rerun Simulation
        </Button>
      ) : null}
    </Stack>
  );
}
