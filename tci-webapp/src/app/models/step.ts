
export interface Step {
  id: number;
  command: string;
  commandResult: string;
  status: string;
  stepStart: number;
  stepEnd: number;
  buildId: number;
}
