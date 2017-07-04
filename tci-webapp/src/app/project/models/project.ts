export interface Project {
  name: string;
  description: string;
  enable: boolean;
  repositoryUrl: string;
  branches: [string];
}