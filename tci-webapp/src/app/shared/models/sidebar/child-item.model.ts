export interface ChildItem {
  label: string;
  path?: string;
  active: boolean;
  description?: string;
  children?: ChildItem[];
}
