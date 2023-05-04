import { FileInput, Select, Stack, TextInput } from '@mantine/core';
import { useLocalStorage } from '@mantine/hooks';
import { useQuery } from '@tanstack/react-query';
import { useParams } from 'react-router-dom';
import { Upload } from 'tabler-icons-react';
import { QUERY_KEYS } from '../../../enums/QueryKeys';
import LocalStorageUser from '../../../models/LocalStorageUser';
import { USER_KEY } from '../../../services/LoginService';
import { TeamServiceAPI } from '../../../services/TeamService';
import CustomTooltip from '../../CustomTooltip';
import { useProjectFormContext } from './NewProjectFormContext';

export const PROJECT_NAME_DESC = 'The name of your project must be unique.';
export const SELECTED_TEAM_DESC =
  'The team that the project will be assigned to. A team can only have one project at a time.';
export const CSV_DESC =
  "Select the .csv file which includes the sprint history of your team. Assure that it's formatted with the following columns in order: sprint number, sprint start date, sprint end date, and stories completed.";

export default function TopFormFields() {
  const form = useProjectFormContext();
  const [currentUser] = useLocalStorage<LocalStorageUser>({ key: USER_KEY });
  const { projectId } = useParams();

  const { data, isLoading, isError } = useQuery(
    [QUERY_KEYS.ALL_TEAM_LIST, currentUser.id],
    () => TeamServiceAPI.getAdminTeams(currentUser.id),
    { enabled: !!currentUser.id }
  );

  if (isLoading || isError) {
    return <p>...</p>;
  }

  let allTeams = data.data.filter((teams) => !teams.project);

  const projectlessTeams = allTeams.map((team) => ({
    label: team?.teamName,
    value: team?.id!.toString(),
  }));

  if (projectId) {
    const currentTeam = data.data.find(
      (team) => team.project?.id === +projectId
    );
    projectlessTeams.push({
      label: currentTeam?.teamName!,
      value: currentTeam?.id?.toString()!,
    });
  }

  return (
    <>
      <Stack>
        <CustomTooltip
          label="Project Name"
          description={PROJECT_NAME_DESC}
          withAsterisk
        >
          <TextInput
            placeholder="Sprintify 2.0"
            {...form.getInputProps('projectName')}
          />
        </CustomTooltip>

        {!projectId ? (
          <CustomTooltip
            label="Select Team"
            description={SELECTED_TEAM_DESC}
            withAsterisk
            multiline
            width={200}
          >
            <Select
              data={projectlessTeams}
              placeholder="Pick one"
              {...form.getInputProps('team')}
            />
          </CustomTooltip>
        ) : (
          <CustomTooltip
            label="Select Team"
            description={SELECTED_TEAM_DESC}
            withAsterisk
            multiline
            width={200}
          >
            <Select
              disabled
              data={projectlessTeams}
              placeholder="Pick one"
              {...form.getInputProps('team')}
            />
          </CustomTooltip>
        )}
        {!projectId && (
          <CustomTooltip
            label="Historical Data CSV file"
            description={CSV_DESC}
            withAsterisk
            multiline
            width={200}
          >
            <FileInput
              aria-label="click to choose file"
              accept=".csv"
              icon={<Upload size={14} />}
              placeholder="Click here to choose file"
              {...form.getInputProps('file')}
            />
          </CustomTooltip>
        )}
      </Stack>
    </>
  );
}
