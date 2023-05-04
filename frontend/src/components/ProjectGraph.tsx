import { Center, Stack, Title } from '@mantine/core';
import {
  ComposedChart,
  Bar,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  Legend,
  CartesianGrid,
  ResponsiveContainer,
} from 'recharts';
import GraphDataObject from '../models/GraphDataObject';

interface Props {
  graphData: GraphDataObject[];
}

export default function ProjectGraph({ graphData }: Props) {
  return (
    <Stack style={{ height: '100%', width: '100%' }}>
      <Title order={3} align="center">
        Distribution Of Total Sprints To Complete Work
      </Title>
      <Center style={{ height: '100%', width: '100%' }}>
        <ResponsiveContainer width="95%" height="90%">
          <ComposedChart data={graphData} width={600} height={300}>
            <CartesianGrid vertical={false} strokeDasharray="3" />
            <Bar
              yAxisId="partialSuccessRate"
              dataKey="partialSuccessRate"
              fill="#006E6C"
              barSize={40}
              name="Count %"
            />
            <Line
              yAxisId="cumulativeSuccessRate"
              type="monotone"
              dataKey="cumulativeSuccessRate"
              stroke="#A5A5A5"
              name="Completion by Sprint %"
            />
            <YAxis
              yAxisId="partialSuccessRate"
              tickFormatter={(tick) => `${tick}%`}
            />
            <YAxis
              yAxisId="cumulativeSuccessRate"
              tickFormatter={(tick) => `${tick}%`}
              orientation="right"
            />
            <XAxis dataKey="numberOfSprints" />
            <Tooltip />
            <Legend />
          </ComposedChart>
        </ResponsiveContainer>
      </Center>
    </Stack>
  );
}
