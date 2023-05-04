import { Grid, Paper, Stack } from '@mantine/core';

import { useMediaQuery } from '@mantine/hooks';
import ProjectGraph from '../components/ProjectGraph';
import ProjectGrid from '../components/ProjectGrid';
import ProjectInfo from '../components/ProjectInfo';
import ProjectResult from '../models/ProjectResult';
import ProjectLinksAndPublic from './ProjectLinksAndPublic';

interface Props {
  apiResponse: ProjectResult;
}

export default function ProjectDetailsWrapper({ apiResponse }: Props) {
  const matches = useMediaQuery('(min-width: 768px)');
  const paperPadding = matches ? 'xl' : 'md';

  return (
    <Stack justify="center" style={{ maxWidth: 1050, margin: 'auto' }}>
      <ProjectLinksAndPublic apiResponse={apiResponse} />

      <Grid>
        <Grid.Col sm={12}>
          <Paper
            p={paperPadding}
            style={{
              height: '40vh',
              minHeight: 450,
              width: '100%',
              minWidth: '5rem',
            }}
          >
            <ProjectGraph graphData={apiResponse.graphData} />
          </Paper>
        </Grid.Col>

        <Grid.Col sm={6}>
          <Paper p={paperPadding} style={{ minWidth: '5rem' }}>
            <ProjectGrid tableData={apiResponse.graphData} />
          </Paper>
        </Grid.Col>
        <Grid.Col sm={6}>
          <Paper p={paperPadding} style={{ minWidth: '5rem' }}>
            <ProjectInfo infoData={apiResponse} />
          </Paper>
        </Grid.Col>
      </Grid>
    </Stack>
  );
}
