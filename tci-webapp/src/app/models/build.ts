
export interface Build {
  id: number;
  projectId: number;
  buildStart: number;
  buildEnd: number;
  startBy: string;
  commitHash: string;
  commitMessage: string;
  branch: string;
  error: string;
  status: string;
}
