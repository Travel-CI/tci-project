export interface Project {
  id: number;
  name: string;
  description: string;
  enable: boolean;
  repositoryUrl: string;
  branches: [string];
}
