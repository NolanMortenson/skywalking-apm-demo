import {
  Button,
  Center,
  Container,
  createStyles,
  Group,
  Space,
  Text,
  Title,
} from '@mantine/core';
import { Link } from 'react-router-dom';
import { At, Phone } from 'tabler-icons-react';

const useStyles = createStyles((theme) => ({
  root: {
    paddingTop: 80,
    paddingBottom: 80,
  },

  label: {
    textAlign: 'center',
    fontWeight: 900,
    fontSize: 220,
    lineHeight: 1,
    marginBottom: theme.spacing.xl * 1.5,
    color:
      theme.colorScheme === 'dark'
        ? theme.colors.dark[4]
        : theme.colors.gray[2],

    [theme.fn.smallerThan('sm')]: {
      fontSize: 120,
    },
  },

  title: {
    fontFamily: `Greycliff CF, ${theme.fontFamily}`,
    textAlign: 'center',
    fontWeight: 900,
    fontSize: 38,

    [theme.fn.smallerThan('sm')]: {
      fontSize: 32,
    },
  },

  description: {
    maxWidth: 500,
    margin: 'auto',
    marginTop: theme.spacing.xl,
    marginBottom: theme.spacing.xl * 1.5,
  },
}));

export function NotFoundTitle() {
  const { classes } = useStyles();
  const SUPPORT_EMAIL = 'help_desk@example.com';
  const SUPPORT_PHONE = '+1 (888) 123-4567';

  return (
    <Container className={classes.root}>
      <div className={classes.label}>404</div>
      <Title className={classes.title}>You have found a secret place.</Title>
      <Text
        color="dimmed"
        size="lg"
        align="center"
        className={classes.description}
      >
        Unfortunately, this is only a 404 page. You may have mistyped the
        address, or the page has been moved to another URL.
      </Text>

      <Text color="dimmed" size="lg" align="center">
        If you think this is a mistake, please contact Help Desk.
      </Text>

      <Group position="center">
        <Group>
          <Phone />
          <Text>{SUPPORT_PHONE}</Text>
        </Group>
        |
        <Group>
          <At />
          <Text
            transform="lowercase"
            component="a"
            href={'mailto:' + SUPPORT_EMAIL}
          >
            {SUPPORT_EMAIL}
          </Text>
        </Group>
      </Group>
      <Space h="xl" />

      <Center>
        <Button size="md" component={Link} to="/">
          Take me back to home page
        </Button>
      </Center>
    </Container>
  );
}
