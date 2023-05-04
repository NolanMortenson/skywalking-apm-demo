import {
  Button,
  Box,
  Text,
  Title,
  Paper,
  Alert,
  TextInput,
} from '@mantine/core';
import { Copy } from 'tabler-icons-react';

const KitchenSink = () => {
  return (
    <div>
      <Title order={1} transform="uppercase">
        This is the KitchenSink page
      </Title>
      <Paper shadow="xl" p="md" m="lg">
        Title Components (headings)
        <Box sx={{ display: 'flex', flexDirection: 'column' }}>
          <Title order={1}>
            This is an h1 [order={`{`}1{`}`}]
          </Title>
          <Title order={2}>
            This is an h2 [order={`{`}2{`}`}]
          </Title>
          <Title order={3}>
            This is an h3 [order={`{`}3{`}`}]
          </Title>
          <Title order={4}>
            This is an h4 [order={`{`}4{`}`}]
          </Title>
          <Title order={5}>
            This is an h5 [order={`{`}5{`}`}]
          </Title>
          <Title order={6}>
            This is an h6 [order={`{`}6{`}`}]
          </Title>
        </Box>
      </Paper>
      <Paper  p="md" m="lg">
        Text Components
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'column',
            marginTop: '2rem',
            marginBottom: '2rem',
            height: '10rem',
            justifyContent: 'space-around',
          }}
        >
          <Text size="xs">Extra Small Text [size="xs"]</Text>
          <Text size="sm">Small Text [size="sm"]</Text>
          <Text size="md">Default Text [size="md"]</Text>
          <Text size="lg">Large Text [size="lg"]</Text>
          <Text size="xl">Extra Large Text [size="xl"]</Text>
        </Box>
      </Paper>

      <Box
        sx={{
          display: 'block',
          marginTop: '2rem',
          marginBottom: '2rem',
          height: '10rem',
        }}
      >
        Brand Colors
        <Box
          sx={(theme) => ({
            color: 'white',
            backgroundColor: theme.colors.darkBlue,
            fontSize: theme.fontSizes.lg
          })}
        >
          theme.colors.darkBlue
        </Box>
        <Box
          sx={(theme) => ({
            color: 'white',
            backgroundColor: theme.colors.teal,
            fontSize: theme.fontSizes.lg
          })}
        >
          theme.colors.teal
        </Box>
        <Box
          sx={(theme) => ({
            color: 'white',
            backgroundColor: theme.colors.lightGray,
            fontSize: theme.fontSizes.lg
          })}
        >
          theme.colors.lightGray
        </Box>
      </Box>
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'flex-start',
          height: '8em',
          justifyContent: 'space-between',
          marginBottom: '2rem',
        }}
      >
        <TextInput
          placeholder="Text Input Example"
          label="Text Input Example"
          icon={<Copy />}
        />
      </Box>
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'flex-start',
          height: '20rem',
          justifyContent: 'space-between',
          marginBottom: '2rem',
        }}
      >
        Default Buttons with color prop and color changes on hover
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'space-between',
            height: '10rem',
          }}
        >
          <Button color="darkBlue" leftIcon={<Copy />}>
            darkBlue
          </Button>
          <Button disabled >Disabled</Button>
        </Box>
        Compact Buttons with color prop and color changes on hover
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'space-between',
            height: '8rem',
          }}
        >
          <Button compact color="darkBlue">
            darkBlue
          </Button>
        </Box>
        Fullwidth Buttons with color prop and color changes on hover
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'space-between',
            height: '8rem',
            width: '100%',
          }}
        >
          <Button fullWidth color="darkBlue">
            darkBlue - remember to make button's container width: 100%
          </Button>
        </Box>
      </Box>
      <Box>
        Alert Components with Theme Colors
        <Alert
          radius="xs"
          title="Success!"
          sx={(theme) => ({
            color: theme.colors.success[0],
            backgroundColor: theme.colors.success[1],
          })}
        >
          color: theme.colors.success[0], backgroundColor:
          theme.colors.success[1],
        </Alert>
        <Alert
          radius="xs"
          title="Info!"
          sx={(theme) => ({
            color: theme.colors.info[0],
            backgroundColor: theme.colors.info[1],
          })}
        >
          color: theme.colors.info[0], backgroundColor: theme.colors.info[1],
        </Alert>
        <Alert
          radius="xs"
          title="Warning!"
          sx={(theme) => ({
            color: theme.colors.warning[0],
            backgroundColor: theme.colors.warning[1],
          })}
        >
          color: theme.colors.warning[0], backgroundColor:
          theme.colors.warning[1],
        </Alert>
        <Alert
          title="Oopsies!"
          sx={(theme) => ({
            color: theme.colors.danger[0],
            backgroundColor: theme.colors.danger[1],
          })}
        >
          color: theme.colors.danger[0], backgroundColor: theme.colors.danger[1]
        </Alert>
      </Box>
    </div>
  );
};
export default KitchenSink;
