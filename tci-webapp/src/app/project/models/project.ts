export interface Project {
  id: number;
  name: string;
  description: string;
  enable: boolean;
  repositoryUrl: string;
  branches: string[];
  userName: string;
  userPassword: string;
  repositoryToken: string;
  dockerfileLocation: string;
  lastStart: Date;
  created: number;
  updated: number;
}
