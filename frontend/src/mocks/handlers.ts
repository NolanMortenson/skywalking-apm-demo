import { rest } from 'msw';

export const handlers = [
  rest.get(
    process.env.REACT_APP_API_URL + '/users/admins/:teamId/all-teams',
    (_req, res, ctx) => res(ctx.status(200), ctx.json([]))
  ),
  
  rest.get(
    process.env.REACT_APP_API_URL + '/users',
    (_req, res, ctx) => res(ctx.status(200), ctx.json([]))
  ),
];
