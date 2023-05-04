import ProjectResult from '../ProjectResult';

export const DUMMY_DATA: ProjectResult = {
  projectId: 1,
  projectName: 'Project Name',
  teamId: 1,
  teamName: 'Team Name',
  startDate: '10/16/2022',
  numOfStories: 65,
  isPublic: false,

  graphData: [
    {
      numberOfSprints: 2,
      count: 9,
      partialSuccessRate: 0.9,
      cumulativeSuccessRate: 0.9,
    },
    {
      numberOfSprints: 3,
      count: 66,
      partialSuccessRate: 6.6,
      cumulativeSuccessRate: 7.5,
    },
    {
      numberOfSprints: 4,
      count: 239,
      partialSuccessRate: 23.9,
      cumulativeSuccessRate: 31.4,
    },
    {
      numberOfSprints: 5,
      count: 305,
      partialSuccessRate: 30.5,
      cumulativeSuccessRate: 61.9,
    },
    {
      numberOfSprints: 6,
      count: 249,
      partialSuccessRate: 24.9,
      cumulativeSuccessRate: 86.800003,
    },
    {
      numberOfSprints: 7,
      count: 113,
      partialSuccessRate: 11.3,
      cumulativeSuccessRate: 98.1,
    },
    {
      numberOfSprints: 8,
      count: 17,
      partialSuccessRate: 1.7,
      cumulativeSuccessRate: 99.8,
    },
    {
      numberOfSprints: 9,
      count: 2,
      partialSuccessRate: 0.2,
      cumulativeSuccessRate: 100,
    },
  ],
};
