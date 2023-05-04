import {
  ActionIcon,
  Group,
  MantineSize,
  Stack,
  Text,
  Tooltip,
} from '@mantine/core';
import { QuestionMark } from 'tabler-icons-react';

export interface ICustomTooltipProps {
  label: string;
  size?: MantineSize;
  description: string;
  withAsterisk?: boolean;
  multiline?: boolean;
  width?: number | 'auto' | undefined;
  children: React.ReactNode;
}

export default function CustomTooltip({
  label,
  size,
  description,
  withAsterisk = false,
  multiline = false,
  width = 'auto',
  children,
}: ICustomTooltipProps) {
  return (
    <Stack spacing={0}>
      <Group position="apart">
        <Text weight="bold" size={size} styles={{}}>
          {label}
          <Text color="red" component="span" inherit>
            {withAsterisk ? ' *' : ''}
          </Text>
        </Text>
        <Tooltip
          color="teal"
          withArrow
          label={description}
          width={width}
          multiline={multiline}
          transition="fade"
          transitionDuration={200}
          events={{ hover: true, focus: true, touch: true }}
        >
          <ActionIcon aria-label={label + ' description'} radius="xl" size="xs">
            <QuestionMark />
          </ActionIcon>
        </Tooltip>
      </Group>
      {children}
    </Stack>
  );
}
