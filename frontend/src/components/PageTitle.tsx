import { Title, Space } from '@mantine/core';

export interface IPageTitleProps {
  title: string;
}

export default function PageTitle(props: IPageTitleProps) {
  return (
    <>
      <Space h={15} />
      <Title order={1}>{props.title}</Title>
      <Space h={15} />
    </>
  );
}
