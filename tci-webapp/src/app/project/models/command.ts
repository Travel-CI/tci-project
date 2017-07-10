export interface Command {
  id: number;
  command: string;
  projectId: number;
  commandOrder: number;
  enabled: boolean;
  enableLogs: boolean;
}
