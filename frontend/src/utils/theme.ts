import { MantineThemeOverride, TitleStylesParams } from '@mantine/core';

const theme: MantineThemeOverride = {
  colorScheme: 'light',
  primaryColor: 'teal',

  colors: {
    teal: [
      '#016e6c',
      '#016e6c',
      '#016e6c',
      '#016e6c',
      '#016e6c',
      '#016e6c',
      '#016e6c',
      '#016e6c',
      '#016e6c',
      '#016e6c',
    ],
    darkBlue: [
      '#253746',
      '#253746',
      '#253746',
      '#253746',
      '#253746',
      '#253746',
      '#253746',
      '#016e6c',
      '#253746',
      '#253746',
    ],
    lightGray: [
      '#f6f6f6',
      '#D9D9D9',
      '#D9D9D9',
      '#D9D9D9',
      '#D9D9D9',
      '#D9D9D9',
      '#D9D9D9',
      '#D9D9D9',
      '#D9D9D9',
      '#D9D9D9',
    ],
    white: [
      '#ffffff',
      '#ffffff',
      '#ffffff',
      '#ffffff',
      '#ffffff',
      '#ffffff',
      '#ffffff',
      '#ffffff',
      '#ffffff',
      '#ffffff',
    ],

    success: ['#58D68D', '#e6f9ee'],
    info: ['#31d2f2', '#eafafe'],
    warning: ['#ffca2c', '#fffbf0'],
    danger: ['#dc3545', '#fcedee'],
  },

  fontFamily: 'akshar, sans-serif',
  fontSizes: {
    xs: 12,
    sm: 14,
    md: 16,
    lg: 20,
    xl: 24,
  },

  headings: {
    fontFamily: 'akshar, sans-serif',
    fontWeight: 600,
  },

  radius: {
    xs: 0,
    sm: 2.5,
    md: 30,
    lg: 35,
    xl: 50,
  },

  defaultRadius: 'xs',

  components: {
    Anchor: {
      defaultProps: { color: 'white', size: '1.15rem' },
      styles: (theme) => ({
        root: {
          '&:hover': { backgroundColor: theme.colors.teal },
        },
      }),
    },
    NavLink: {
      styles: (theme) => ({
        root: {
          '&:hover': {
            backgroundColor: theme.colors.teal,
            textDecoration: 'underline',
            color: 'white',
          },
        },
        label: { color: 'white', fontSize: '1.15rem' },

        rightSection: { marginLeft: '.15rem' },
      }),
    },
    Menu: {
      styles: (theme) => ({
        item: {
          color: 'white',
          backgroundColor: theme.colors.darkBlue,
          '&[data-hovered]': {
            backgroundColor: theme.colors.teal,
            color: 'white',
          },
        },
      }),
    },
    Button: {
      defaultProps: { radius: 'xl', color: 'darkBlue' },
      styles: (theme) => ({ root: { fontSize: '1rem' } }),
    },
    Title: {
      styles: (theme, params: TitleStylesParams) => ({
        root: {
          color:
            params.element === 'h1' ||
            params.element === 'h3' ||
            params.element === 'h5'
              ? theme.colors.teal
              : theme.colors.darkBlue,
        },
      }),
      defaultProps: { transform: 'capitalize' },
    },
    Text: {
      defaultProps: { color: 'darkBlue' },
    },
    Paper: {
      defaultProps: {
        shadow: 'md',
        withBorder: true,
      },
      styles: (theme) => ({
        root: {
          paddingLeft: '5rem',
          paddingRight: '5rem',
          paddingTop: '2rem',
          paddingBottom: '2rem',
        },
      }),
    },
    TextInput: {
      defaultProps: {
        size: 'md',
        radius: 'sm',
      },
      styles: (theme) => ({
        icon: {
          color: theme.colors.teal,
        },
      }),
    },
    NumberInput: {
      defaultProps: {
        size: 'md',
        radius: 'sm',
      },
      styles: (theme) => ({
        icon: {
          color: theme.colors.teal,
        },
      }),
    },
    Select: {
      defaultProps: {
        size: 'md',
        radius: 'sm',
        active: '',
      },
      styles: (theme) => ({
        icon: {
          color: theme.colors.teal,
        },
        active: {},
      }),
    },
    DatePicker: {
      defaultProps: {
        size: 'md',
        radius: 'sm',
      },
      styles: (theme) => ({
        icon: {
          color: theme.colors.teal,
        },
      }),
    },
    Modal: {
      styles: (theme) => ({
        title: {
          color: theme.colors.teal,
          fontSize: '2.2rem',
          fontWeight: 'bold',
        },
      }),
    },
    FileInput: {
      defaultProps: {
        size: 'md',
        radius: 'sm',
      },
      styles: (theme) => ({
        icon: {
          color: theme.colors.teal,
        },
      }),
    },
    Switch: {
      defaultProps: {
        size: 'md',
        color: 'darkBlue',
      },
      styles: (theme) => ({
        label: {
          color: theme.colors.darkBlue,
          fontWeight: 600,
        },
      }),
    },

    PasswordInput: {
      defaultProps: {
        size: 'md',
        radius: 'sm',
      },
      styles: (theme) => ({
        icon: {
          color: theme.colors.teal,
        },
      }),
    },

    InputWrapper: {
      styles: (theme) => ({
        label: {
          color: theme.colors.darkBlue,
          fontWeight: 600,
        },
      }),
    },
  },
};

export default theme;
