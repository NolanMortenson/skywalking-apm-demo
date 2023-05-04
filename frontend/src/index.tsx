import { MantineProvider } from '@mantine/core';
import { NotificationsProvider } from '@mantine/notifications';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import React from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';

import App from './App';
import './index.css';
import theme from './utils/theme';

const rootElement =
  document.getElementById('root') || document.createElement('div');

if (!rootElement) throw new Error('Failed to find the root element');
const root = createRoot(rootElement);

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      refetchOnMount: false,
      refetchOnReconnect: false,
      retry: 1,
      staleTime: 5 * 1000,
    },
  },
});

root.render(
  <React.StrictMode>
    <BrowserRouter>
      <MantineProvider withGlobalStyles theme={theme}>
        <NotificationsProvider>
          <QueryClientProvider client={queryClient}>
            <App />
            {/* <ReactQueryDevtools initialIsOpen={false} /> */}
          </QueryClientProvider>
        </NotificationsProvider>
      </MantineProvider>
    </BrowserRouter>
  </React.StrictMode>
);
