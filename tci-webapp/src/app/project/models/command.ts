export interface Command {
  id: number;
  command: string;
  projectId: number;
  commandOrder: number;
  enable: boolean;
  enableLogs: boolean;
}
