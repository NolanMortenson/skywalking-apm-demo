import { AxiosResponse } from 'axios';
import { PublicParams } from '../components/ProjectLinksAndPublic';
import Project from '../models/Project';
import ProjectResult from '../models/ProjectResult';
import { axiosClient } from './LoginService';

const BASE_URL: string = '/projects';

interface RerunSimsParams {
  projectId: number;
  startDate: string;
  storyCount: number;
}

export const ProjectServiceAPI = {
  getProjectListOfAdmin: (
    adminId: number
  ): Promise<AxiosResponse<ProjectResult[]>> =>
    axiosClient.get(`${BASE_URL}/admin/${adminId}`),

  getProjectResult: (
    projectId: number
  ): Promise<AxiosResponse<ProjectResult>> =>
    axiosClient.get(`${BASE_URL}/${projectId}`),

  getProjectDetails: (projectId: number): Promise<AxiosResponse<Project>> =>
    axiosClient.get(`${BASE_URL}/${projectId}/details`),

  updatePublic: (data: PublicParams): Promise<ProjectResult> =>
    axiosClient.put(`${BASE_URL}/${data.id}/${data.isPublic}/guest`, data),

  updateProject: (data: Project) => axiosClient.put(BASE_URL, data),

  rerunSimulation: ({
    projectId,
    startDate,
    storyCount,
  }: RerunSimsParams): Promise<AxiosResponse<ProjectResult>> =>
    axiosClient.put(`${BASE_URL}/${projectId}/simulate`, null, {
      params: {
        startDate,
        storyCount,
      },
    }),

  createProject: (data: FormData): Promise<AxiosResponse<ProjectResult>> =>
    axiosClient.post(BASE_URL, data),

  deleteProject: (projectId: string) =>
    axiosClient.delete(`${BASE_URL}/${projectId}/delete`),
};
