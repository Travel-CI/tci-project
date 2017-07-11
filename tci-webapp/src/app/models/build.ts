
export interface Build {
  id: number;
  projectId: number;
  buildStart: number;
  buildEnd: number;
  startBy: string;
  commitHash: string;
  branch: string;
  error: string;
  status: string;
}
