import axios from 'axios';
import applyCaseMiddleware from 'axios-case-converter';
import qs from 'qs';

export const USER_KEY = 'user';
export const TOKEN_KEY = 'token';
const BEARER_PREFIX = 'Bearer ';
const LOGIN_PATH = 'http://localhost:8081/login';
const ME_PATH = 'me';
const REFRESH_PATH = 'http://localhost:8081/api/v1/token/refresh';
const LOGOUT_PATH = 'http://localhost:8081/api/v1/token/logout';
const EXPIRED_TOKEN_STATUS = 422;

interface LoginInput {
  username: string;
  password: string;
}

export const axiosClient = applyCaseMiddleware(
  axios.create({
    baseURL: process.env.REACT_APP_API_URL,
    headers: {
      Authorization:
        BEARER_PREFIX +
        (localStorage.getItem(TOKEN_KEY)?.replaceAll('"', '') || ''),
    },
  })
);

axiosClient.interceptors.response.use(
  (response) => response,
  async function (error) {
    const originalRequest = error.config;
    if (
      error.response.status === EXPIRED_TOKEN_STATUS &&
      !originalRequest._retry
    ) {
      originalRequest._retry = true;

      const res = await LoginAPI.refresh();
      const newBearerToken = res.data.token;

      setAuthHeaders(newBearerToken);

      originalRequest.headers.Authorization = BEARER_PREFIX + newBearerToken;

      return axiosClient(originalRequest);
    }
    return Promise.reject(error);
  }
);

export function setAuthHeaders(token: string) {
  localStorage.setItem(TOKEN_KEY, token);
  // @ts-ignore
  axiosClient.defaults.headers.Authorization = BEARER_PREFIX + token;
}

export async function removeAuthHeaders() {
  // @ts-ignore
  delete axiosClient.defaults.headers.Authorization;

  try {
    await LoginAPI.logout();
  } catch (err) {
    console.log(err);
  }

  localStorage.clear();
}

export const LoginAPI = {
  login: ({ username, password }: LoginInput) => {
    return axios.post(LOGIN_PATH, qs.stringify({ username, password }), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      withCredentials: true,
    });
  },

  logout: () => axios.get(LOGOUT_PATH),

  me: (): Promise<any> => axiosClient.get(ME_PATH),

  refresh: (): Promise<any> =>
    axios.get(REFRESH_PATH, { withCredentials: true }),
};
