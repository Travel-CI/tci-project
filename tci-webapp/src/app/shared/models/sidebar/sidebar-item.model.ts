import { ChildItem } from './child-item.model';

export interface SidebarItem {
  title: string;
  children?: ChildItem[]
}
