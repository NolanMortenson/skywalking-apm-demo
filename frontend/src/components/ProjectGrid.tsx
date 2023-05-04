import { Space, Table, Title } from '@mantine/core';
import GraphDataObject from '../models/GraphDataObject';

export interface IProjectGridProps {
  tableData: GraphDataObject[];
}

export default function ProjectGrid({ tableData = [] }: IProjectGridProps) {
  const rows = tableData
    .map((element) => {
      let percent = Math.floor(element.cumulativeSuccessRate);

      // TODO: consider changing these to Mantine alert colors from global styles
      let backgroundColor = '#ec81818a'; // red
      if (percent >= 85) {
        backgroundColor = '#81ec818a'; // green
      } else if (percent >= 50) {
        backgroundColor = '#e3ec818a'; // yellow
      }

      const date = new Date(element.endDate!).toLocaleDateString();

      return (
        <tr key={element.numberOfSprints}>
          <td style={{ backgroundColor }}>{percent}%</td>
          <td style={{ backgroundColor }}>{element.numberOfSprints}</td>
          <td style={{ backgroundColor }}>{date}</td>
        </tr>
      );
    })
    .reverse();

  return (
    <>
      <Title order={3}>Likelihood Grid</Title>
      <Space h={20} />
      <Table highlightOnHover style={{ border: '1.5px solid #DEE2E6' }}>
        <thead>
          <tr>
            <th>Likelihood</th>
            <th>Duration</th>
            <th>Date</th>
          </tr>
        </thead>
        <tbody>{rows}</tbody>
      </Table>
    </>
  );
}
